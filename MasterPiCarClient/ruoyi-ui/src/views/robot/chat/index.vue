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

      <el-dialog title="设备身份验证" :visible.sync="loginDialogVisible" width="350px" append-to-body>
        <el-form label-width="70px" size="small">
          <el-form-item label="用户名">
            <el-input v-model="loginForm.username" placeholder="请输入用户名 (如: pi)"></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="loginForm.password" type="password" show-password placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item>
            <el-checkbox v-model="loginForm.remember">记住密码</el-checkbox>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="loginDialogVisible = false" size="small">取 消</el-button>
          <el-button type="primary" @click="confirmLogin" size="small" :loading="connecting">连 接</el-button>
        </div>
      </el-dialog>

      <!-- 视频显示区域 -->
      <div class="video-container" v-if="videoActive">
        <img
          :src="videoStreamUrl"
          class="video-stream"
          @load="videoLoading = false"
          @error="onVideoError"
        />
        <div class="video-overlay" v-if="videoLoading">
          <i class="el-icon-loading"></i> 连接中...
        </div>
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
      deviceList: [],
      currentDeviceId: null,
      sessionId: null,

      loginDialogVisible: false,
      connecting: false,
      loginForm: {
        username: 'pi',
        password: '',
        remember: true
      },
      // 存储所有设备的记住密码信息 { deviceId: { username, password } }
      savedCredentials: {},

      // 视频相关
      videoActive: false,
      videoStreamUrl: "",
      videoLoading: false

    };
  },
  created() {
    this.loadDeviceList();
    // 页面加载时读取本地存储的凭证
    const localData = localStorage.getItem('picar_credentials');
    if (localData) {
      this.savedCredentials = JSON.parse(localData);
    }
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

    /** 切换设备逻辑 */
    handleDeviceChange(deviceId) {
      const device = this.deviceList.find(item => item.deviceId === deviceId);
      if (!device) return;

      // 检查该设备是否有保存的凭证
      const saved = this.savedCredentials[deviceId];

      if (device.ipAddress === '10.107.93.135') {
        if (saved) {
          // 如果有保存的凭证，直接执行连接，不弹窗
          console.log("使用保存的凭证自动连接...");
          this.initLocalConnection(deviceId, saved.username, saved.password);
        } else {
          // 否则弹出登录框
          this.loginForm.password = ''; // 清空密码框
          this.loginDialogVisible = true;
        }
      } else {
        this.startNormalSession(deviceId);
      }
    },

    /** 弹窗点击确认连接 */
    confirmLogin() {
      if (!this.loginForm.password) {
        this.$message.warning("请输入密码");
        return;
      }
      this.connecting = true;
      this.initLocalConnection(this.currentDeviceId, this.loginForm.username, this.loginForm.password);
    },

    /** 弹窗点击确认连接 */
    confirmLogin() {
      if (!this.loginForm.password) {
        this.$message.warning("请输入密码");
        return;
      }
      this.connecting = true;
      this.initLocalConnection(this.currentDeviceId, this.loginForm.username, this.loginForm.password);
    },

    /** 执行 SSH 连接 */
    initLocalConnection(deviceId, username, password) {
      request({
        url: "/masterpicar/status/connect-ssh",
        method: "post",
        data: { deviceId, username, password }
      }).then(res => {
        this.$message.success("SSH 连接成功");

        // 处理记住密码逻辑
        if (this.loginForm.remember) {
          this.savedCredentials[deviceId] = { username, password };
          localStorage.setItem('picar_credentials', JSON.stringify(this.savedCredentials));
        } else {
          // 如果没勾选，确保清除旧的
          delete this.savedCredentials[deviceId];
          localStorage.setItem('picar_credentials', JSON.stringify(this.savedCredentials));
        }

        this.loginDialogVisible = false;
        this.startNormalSession(deviceId);
      }).catch(err => {
        this.$message.error("连接失败: " + err.message);
        // 如果连接失败且是自动登录，可能密码改了，清除保存的凭据并弹窗
        if (this.savedCredentials[deviceId]) {
          delete this.savedCredentials[deviceId];
          localStorage.setItem('picar_credentials', JSON.stringify(this.savedCredentials));
          this.loginDialogVisible = true;
        }
      }).finally(() => {
        this.connecting = false;
      });
    },

    startNormalSession(deviceId) {
      request({
        url: `/masterpicar/device/online/${deviceId}`,
        method: "post"
      }).then(() => {
        return request({
          url: "/masterpicar/session/start",
          method: "post",
          data: { deviceId, model: this.model }
        });
      }).then(res => {
        this.sessionId = res.data.sessionId;
        this.$message.success("设备已上线");
      });
    },

    // handleDeviceChange(deviceId) {
    //   const device = this.deviceList.find(item => item.deviceId === deviceId);
    //
    //   if (device && device.ipAddress === '10.107.93.135') { // 这里建议判断具体IP或标识
    //     this.$prompt('请输入 SSH 密码 (用户: pi)', '身份验证', {
    //       confirmButtonText: '连接',
    //       cancelButtonText: '取消',
    //       inputType: 'password'
    //     }).then(({ value }) => {
    //       // 修正：增加 'pi' 作为 username 参数
    //       this.initLocalConnection(deviceId, 'pi', value);
    //     }).catch(() => {
    //       this.$message.info('已取消连接');
    //     });
    //   } else {
    //     this.startNormalSession(deviceId);
    //   }
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

          const fullContent = assistantMsg.content;
          const actionRegex = /\[ACTION_START\](.*?)\[ACTION_END\]/s;
          const match = fullContent.match(actionRegex);

          // --- 核心改进：只有匹配到指令且指令不为空才下发 ---
          if (match && match[1] && match[1].trim() !== "" && match[1].trim() !== "[]") {
            try {
              const commands = JSON.parse(match[1].trim());
              console.log("检测到硬件指令:", commands);

              // 执行下发
              this.executeHardwareCommands(commands);

              // 界面优化：移除 JSON 源码展示
              assistantMsg.content = fullContent.replace(actionRegex, "").trim();
            } catch (err) {
              console.error("指令解析失败:", err);
            }
          } else {
            console.log("纯聊天模式，无需下发硬件指令");
            // 如果 AI 还是吐出了空的 [ACTION_START][ACTION_END]，也顺手清理掉
            assistantMsg.content = fullContent.replace(actionRegex, "").trim();
          }

          // 4. 将 AI 的最终回复（纯文字）存入数据库
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

      // 遍历指令，如果是开启视频，自动更新前端 UI 状态
      commands.forEach(cmd => {
        // 1. 处理开启视频
        if (cmd.action === 'start_video') {
          this.videoActive = true;
          this.videoLoading = true;
          const device = this.deviceList.find(item => item.deviceId === this.currentDeviceId);
          if (device) {
            // 注意：这里改用你 Java 后端写的代理地址，解决跨域并统一管理
            // this.videoStreamUrl = `/dev-api/masterpicar/device/video/stream/${this.currentDeviceId}?t=${Date.now()}`;
            this.videoStreamUrl = `http://${device.ipAddress}:5001/video_feed?t=${Date.now()}`;
          }
        }
        if (cmd.action === 'stop_video') {
          this.videoActive = false;
          this.videoStreamUrl = "";
        }

        // 2. 处理开启 YOLO 识别（电脑识别模式）
        if (cmd.action === 'start_yolo') {
          this.videoActive = true;
          this.videoLoading = true;
          // 【关键修改】：这里填你【运行 yolo_server.py 的电脑 IP】
          // 如果前端和 Python YOLO 在同一台电脑，可以用 localhost，
          // 如果不在同一台，请填写电脑的具体 IP（如 10.107.93.xxx）
          const computerIp = "localhost";
          this.videoStreamUrl = `http://${computerIp}:5001/yolo_feed?t=${Date.now()}`;
        }
        // 3. 处理关闭视频/YOLO
        if (cmd.action === 'stop_video' || cmd.action === 'stop_yolo') {
          this.videoActive = false;
          this.videoStreamUrl = "";
        }
      });

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
    },
    /** 切换视频 */
    toggleVideo() {
      if (this.videoActive) {
        // 关闭视频
        this.videoActive = false;
        this.videoStreamUrl = "";
        // 发送关闭指令
        this.executeHardwareCommands([{type: "function", action: "stop_video"}]);
      } else {
        // 打开视频
        this.videoLoading = true;
        this.videoActive = true;

        // 构建视频流 URL（直接访问树莓派）
        const device = this.deviceList.find(item => item.deviceId === this.currentDeviceId);
        if (device) {
          this.videoStreamUrl = `http://${device.ipAddress}:5000/video_feed?t=${Date.now()}`;
        }

        // 发送开启指令
        this.executeHardwareCommands([{type: "function", action: "start_video"}]);
      }
    },

    /** 视频加载错误处理 */
    onVideoError() {
      this.videoLoading = false;
      this.$message.error("视频流连接失败，请检查摄像头");
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

.video-container {
  position: relative;
  width: 100%;
  max-height: 300px;
  background: #000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.video-stream {
  width: 100%;
  max-height: 300px;
  object-fit: contain;
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  background: rgba(0,0,0,0.5);
}


</style>
