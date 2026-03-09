<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属会话ID" prop="sessionId">
        <el-input
          v-model="queryParams.sessionId"
          placeholder="请输入所属会话ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="角色" prop="role">
        <el-input
          v-model="queryParams.role"
          placeholder="请输入角色"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['masterpicar:message:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['masterpicar:message:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['masterpicar:message:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['masterpicar:message:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="消息ID" align="center" prop="messageId" />
      <el-table-column label="所属会话ID" align="center" prop="sessionId" />
      <el-table-column label="角色" align="center" prop="role" />
      <el-table-column label="对话内容" align="center" prop="content" />
      <el-table-column label="解析后的控制指令JSON" align="center" prop="commandJson" />
      <el-table-column label="chat / command / feedback" align="center" prop="messageType" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['masterpicar:message:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['masterpicar:message:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改对话消息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="所属会话ID" prop="sessionId">
          <el-input v-model="form.sessionId" placeholder="请输入所属会话ID" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-input v-model="form.role" placeholder="请输入角色" />
        </el-form-item>
        <el-form-item label="对话内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="解析后的控制指令JSON" prop="commandJson">
          <el-input v-model="form.commandJson" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-divider content-position="center">机器人设备信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddRobotDevice">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteRobotDevice">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="robotDeviceList" :row-class-name="rowRobotDeviceIndex" @selection-change="handleRobotDeviceSelectionChange" ref="robotDevice">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="设备序列号" prop="deviceSn" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.deviceSn" placeholder="请输入设备序列号" />
            </template>
          </el-table-column>
          <el-table-column label="IP地址" prop="ipAddress" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.ipAddress" placeholder="请输入IP地址" />
            </template>
          </el-table-column>
          <el-table-column label="在线状态" prop="onlineStatus" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.onlineStatus" placeholder="请选择在线状态">
                <el-option label="请选择字典生成" value="" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="最后在线时间" prop="lastOnlineTime" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.lastOnlineTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择最后在线时间" />
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="remark" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" placeholder="请输入备注" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMessage, getMessage, delMessage, addMessage, updateMessage } from "@/api/masterpicar/message"

export default {
  name: "Message",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedRobotDevice: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 对话消息表格数据
      messageList: [],
      // 机器人设备表格数据
      robotDeviceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sessionId: null,
        role: null,
        content: null,
        commandJson: null,
        messageType: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sessionId: [
          { required: true, message: "所属会话ID不能为空", trigger: "blur" }
        ],
        role: [
          { required: true, message: "角色不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "对话内容不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询对话消息列表 */
    getList() {
      this.loading = true
      listMessage(this.queryParams).then(response => {
        this.messageList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        messageId: null,
        sessionId: null,
        role: null,
        content: null,
        commandJson: null,
        messageType: null,
        createTime: null
      }
      this.robotDeviceList = []
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.messageId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加对话消息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const messageId = row.messageId || this.ids
      getMessage(messageId).then(response => {
        this.form = response.data
        this.robotDeviceList = response.data.robotDeviceList
        this.open = true
        this.title = "修改对话消息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.robotDeviceList = this.robotDeviceList
          if (this.form.messageId != null) {
            updateMessage(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMessage(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const messageIds = row.messageId || this.ids
      this.$modal.confirm('是否确认删除对话消息编号为"' + messageIds + '"的数据项？').then(function() {
        return delMessage(messageIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
	/** 机器人设备序号 */
    rowRobotDeviceIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 机器人设备添加按钮操作 */
    handleAddRobotDevice() {
      let obj = {}
      obj.deviceSn = ""
      obj.ipAddress = ""
      obj.onlineStatus = ""
      obj.lastOnlineTime = ""
      obj.remark = ""
      this.robotDeviceList.push(obj)
    },
    /** 机器人设备删除按钮操作 */
    handleDeleteRobotDevice() {
      if (this.checkedRobotDevice.length == 0) {
        this.$modal.msgError("请先选择要删除的机器人设备数据")
      } else {
        const robotDeviceList = this.robotDeviceList
        const checkedRobotDevice = this.checkedRobotDevice
        this.robotDeviceList = robotDeviceList.filter(function(item) {
          return checkedRobotDevice.indexOf(item.index) == -1
        })
      }
    },
    /** 复选框选中数据 */
    handleRobotDeviceSelectionChange(selection) {
      this.checkedRobotDevice = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('masterpicar/message/export', {
        ...this.queryParams
      }, `message_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
