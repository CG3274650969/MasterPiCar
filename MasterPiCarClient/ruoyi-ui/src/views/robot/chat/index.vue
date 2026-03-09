<template>
  <div class="chat-container">
    <div class="chat-main">

      <!-- 顶部设备选择栏（新增） -->
      <div class="chat-header">
        <el-select
          v-model="currentDeviceId"
          placeholder="选择机械臂小车"
          size="mini"
          class="device-select"
          @change="handleDeviceChange"
        >
          <el-option
            v-for="item in deviceList"
            :key="item.deviceId"
            :label="item.deviceName"
            :value="item.deviceId"
          />
        </el-select>
      </div>

      <!-- 聊天消息列表 -->
      <div class="chat-messages" ref="chatBox">
        <div
          v-for="(msg, index) in messages"
          :key="index"
          :class="['chat-message', msg.role]"
        >
          <div class="bubble">
            <div v-if="msg.role === 'assistant' && msg.isTyping" class="typing-indicator">
              <span></span><span></span><span></span>
            </div>
            <div v-else class="message-content">{{ msg.content }}</div>
            <div class="message-time">{{ msg.time }}</div>
          </div>
        </div>
      </div>

      <!-- 底部输入区（完全不动） -->
      <div class="chat-input">
        <el-select v-model="model" class="model-select" size="mini">
          <el-option label="Spark-X" value="spark-x" />
          <el-option label="GPT-4" value="gpt-4" />
          <el-option label="GPT-3.5" value="gpt-3.5" />
          <el-option label="本地模型" value="local-llm" />
        </el-select>

        <div class="input-wrapper">
          <el-input
            v-model="input"
            type="textarea"
            rows="1"
            placeholder="请输入问题或指令"
            @keyup.enter.native="send"
            class="input-box"
          />
        </div>

        <el-button class="send-btn" @click="send">
          <svg class="send-icon" viewBox="0 0 24 24">
            <path d="M2 21l21-9L2 3v7l15 2-15 2v7z" fill="currentColor"/>
          </svg>
        </el-button>
      </div>
    </div>
  </div>
</template>


<script>
import request from "@/utils/request";

export default {
  name: "RobotChat",
  data() {
    return {
      input: "",
      model: "spark-x",
      messages: [],
      source: null,

      // 🔽 新增
      deviceList: [],
      currentDeviceId: null,
      sessionId: null
    };
  },
  created() {
    this.loadDeviceList();
  },
  methods: {
    /** 获取设备列表 */
    loadDeviceList() {
      request({
        url: "/masterpicar/device/list",
        method: "get"
      }).then(res => {
        this.deviceList = res.rows || [];
      });
    },

    /** 选择设备 → 设置在线 */
    // handleDeviceChange(deviceId) {
    //   request({
    //     url: `/masterpicar/device/online/${deviceId}`,
    //     method: "post"
    //   }).then(() => {
    //     this.$message.success("设备已上线");
    //   });
    // },

    handleDeviceChange(deviceId) {
      request({
        url: `/masterpicar/device/online/${deviceId}`,
        method: "post"
      }).then(() => {
        return request({
          url: "/masterpicar/session/start",
          method: "post",
          data: {
            deviceId,
            model: this.model
          }
        });
      }).then(res => {
        this.sessionId = res.data.sessionId;
        this.$message.success("设备已上线，会话已创建");
      });
    },

    // send() {
    //   if (!this.input) return;
    //
    //   const now = new Date();
    //   const formatTime = `${now.getHours()}:${now.getMinutes().toString().padStart(2, "0")}`;
    //
    //   // 显示用户消息
    //   this.messages.push({
    //     role: "user",
    //     content: this.input,
    //     time: formatTime
    //   });
    //
    //   const userMsg = this.input;
    //   this.input = "";
    //
    //   // 显示助手占位
    //   const assistantMsg = {
    //     role: "assistant",
    //     content: "",
    //     time: formatTime,
    //     isTyping: true
    //   };
    //   this.messages.push(assistantMsg);
    //   this.$nextTick(this.scrollBottom);
    //
    //   if (!this.sessionId) {
    //     this.$message.error("请先选择设备并创建会话！");
    //     return;
    //   }
    //
    //   // 调用后端统一保存消息并获取助手回复
    //   request({
    //     url: "/masterpicar/message/save",
    //     method: "post",
    //     data: {
    //       sessionId: this.sessionId,
    //       role: "user",
    //       content: userMsg
    //     }
    //   }).then(res => {
    //     // 后端返回助手消息
    //     const reply = res.data; // {role: 'assistant', content: '...', sessionId: ...}
    //     assistantMsg.isTyping = false;
    //     assistantMsg.content = reply.content;
    //     this.$nextTick(this.scrollBottom);
    //   }).catch(err => {
    //     assistantMsg.isTyping = false;
    //     assistantMsg.content = "[回复失败]";
    //     this.$nextTick(this.scrollBottom);
    //   });
    // },

    send() {
      if (!this.input || !this.sessionId) return;

      const userContent = this.input;
      const now = new Date();
      const formatTime = `${now.getHours()}:${now.getMinutes().toString().padStart(2, "0")}`;
      this.input = "";

      // 1. 存入用户消息
      request({
        url: "/masterpicar/message/save",
        method: "post",
        data: { sessionId: this.sessionId, role: "user", content: userContent }
      });

      const assistantMsg = { role: "assistant", content: "", isTyping: true, time: formatTime };
      this.messages.push({ role: "user", content: userContent, time: formatTime }, assistantMsg);
      this.$nextTick(this.scrollBottom);

      // 2. 连接 Python
      const url = `http://localhost:8000/chat/stream?prompt=${encodeURIComponent(userContent)}&deviceType=mechanical_arm`;
      const es = new EventSource(url);

      es.onmessage = (e) => {
        assistantMsg.isTyping = false;
        // 实时累加回复内容
        assistantMsg.content += e.data;
        this.$nextTick(this.scrollBottom);
      };

      // 3. 监听结束事件并处理硬件指令
      es.addEventListener('end', (e) => {
        if (e.data === "[DONE]") {
          es.close();

          // --- 关键步骤：提取并执行硬件指令 ---
          const fullContent = assistantMsg.content;
          // 正则匹配 [ACTION_START] ... [ACTION_END]
          const actionRegex = /\[ACTION_START\](.*?)\[ACTION_END\]/s;
          const match = fullContent.match(actionRegex);

          if (match && match[1]) {
            try {
              const commands = JSON.parse(match[1].trim());
              console.log("检测到硬件指令:", commands);

              // a. 将指令发送给若依，由若依通过 MQTT 转给树莓派
              this.executeHardwareCommands(commands);

              // b. 界面优化：从聊天气泡中移除难看的 JSON 源码，只保留自然语言
              assistantMsg.content = fullContent.replace(actionRegex, "").trim();
            } catch (err) {
              console.error("指令解析失败:", err);
            }
          }

          // 4. 将 AI 的最终自然语言回复存入数据库
          request({
            url: "/masterpicar/message/save",
            method: "post",
            data: { sessionId: this.sessionId, role: "assistant", content: assistantMsg.content }
          });
        }
      });

      es.onerror = () => {
        if (assistantMsg.content.length === 0) {
          assistantMsg.isTyping = false;
          assistantMsg.content = "AI 连接异常";
        }
        es.close();
      };
    },

    /**
     * 新增方法：发送指令到若依后端
     */
    executeHardwareCommands(commands) {
      // 这里调用你准备在若依写的控制接口
      request({
        url: "/masterpicar/device/execute",
        method: "post",
        data: {
          deviceId: this.currentDeviceId,
          actions: commands
        }
      }).then(() => {
        this.$message.success("指令已下发至小车");
      });
    },

    scrollBottom() {
      const box = this.$refs.chatBox;
      if (box) box.scrollTop = box.scrollHeight;
    }
  }
};
</script>

<style scoped>
/* 聊天整体布局 */
.chat-container {
  height: calc(100vh - 84px);
  display: flex;
}

.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 消息列表 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 消息气泡 */
.chat-message {
  max-width: 70%;
}

.chat-message.user {
  align-self: flex-end;
}

.chat-message.assistant {
  align-self: flex-start;
}

.bubble {
  padding: 10px 14px;
  border-radius: 12px;
  word-break: break-word;
  position: relative;
}

.user .bubble {
  background-color: #409eff;
  color: #fff;
  border-bottom-right-radius: 0;
}

.assistant .bubble {
  background-color: #f1f1f1;
  color: #333;
  border-bottom-left-radius: 0;
}

/* 三点动画取代文字 */
.typing-indicator {
  display: flex;
  gap: 4px;
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  background-color: #999;
  border-radius: 50%;
  display: inline-block;
  animation: typing 1s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

.message-time {
  font-size: 13px;
  color: #999;
  text-align: right;
  margin-top: 4px;
}

/* ================================================ */
/* 底部输入区 */
.chat-input {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 14px;
  border-top: 1px solid #e6e6e6;
  background-color: #fff;
}

.model-select {
  width: 120px;
}

.model-select .el-input__inner {
  border-radius: 18px;
  height: 36px;
  font-size: 13px;
  padding: 0 12px;
  border: 1px solid #dcdfe6;
  box-sizing: border-box;
  transition: all 0.2s;
}

.model-select .el-input__inner:focus {
  border-color: #409eff;
}

.input-wrapper {
  flex: 1;
  height: 36px;
  border-radius: 18px;
  background-color: #fafafa;
  display: flex;
  align-items: center;
  padding: 0 12px;
  position: relative;
}

.input-box {
  flex: 1;
  height: 36px;
  border: none !important;
  background-color: transparent !important;
  padding: 0;
  font-size: 14px;
  outline: none;
  resize: none;
  display: flex;
  align-items: center;
  line-height: 36px;
}

/* 发送按钮 */
.send-btn {
  width: 36px;
  height: 36px;
  min-width: 36px;
  padding: 0;
  border-radius: 50%;
  background-color: #409eff;
  color: #fff;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.send-btn:hover {
  background-color: #66b1ff;
}

.send-btn:active {
  background-color: #337ecc;
  transform: scale(0.95);
}

.send-icon {
  width: 18px;
  height: 18px;
  transition: transform 0.3s ease;
}

/* 滚动条美化 */
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 3px;
}

.chat-header {
  padding: 8px 14px;
  border-bottom: 1px solid #eee;
  background-color: #fff;
}

.device-select {
  width: 200px;
}

</style>
