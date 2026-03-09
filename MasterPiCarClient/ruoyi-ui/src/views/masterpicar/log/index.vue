<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备ID" prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入设备ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来源ID" prop="sourceId">
        <el-input
          v-model="queryParams.sourceId"
          placeholder="请输入来源ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执行结果" prop="result">
        <el-input
          v-model="queryParams.result"
          placeholder="请输入执行结果"
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
          v-hasPermi="['masterpicar:log:add']"
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
          v-hasPermi="['masterpicar:log:edit']"
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
          v-hasPermi="['masterpicar:log:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['masterpicar:log:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日志ID" align="center" prop="logId" />
      <el-table-column label="设备ID" align="center" prop="deviceId" />
      <el-table-column label="来源类型" align="center" prop="sourceType" />
      <el-table-column label="来源ID" align="center" prop="sourceId" />
      <el-table-column label="执行结果" align="center" prop="result" />
      <el-table-column label="执行详情" align="center" prop="detail" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['masterpicar:log:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['masterpicar:log:remove']"
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

    <!-- 添加或修改执行日志对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入设备ID" />
        </el-form-item>
        <el-form-item label="来源ID" prop="sourceId">
          <el-input v-model="form.sourceId" placeholder="请输入来源ID" />
        </el-form-item>
        <el-form-item label="执行结果" prop="result">
          <el-input v-model="form.result" placeholder="请输入执行结果" />
        </el-form-item>
        <el-form-item label="执行详情" prop="detail">
          <el-input v-model="form.detail" type="textarea" placeholder="请输入内容" />
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
import { listLog, getLog, delLog, addLog, updateLog } from "@/api/masterpicar/log"

export default {
  name: "Log",
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
      // 执行日志表格数据
      logList: [],
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
        deviceId: null,
        sourceType: null,
        sourceId: null,
        result: null,
        detail: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询执行日志列表 */
    getList() {
      this.loading = true
      listLog(this.queryParams).then(response => {
        this.logList = response.rows
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
        logId: null,
        deviceId: null,
        sourceType: null,
        sourceId: null,
        result: null,
        detail: null,
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
      this.ids = selection.map(item => item.logId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加执行日志"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const logId = row.logId || this.ids
      getLog(logId).then(response => {
        this.form = response.data
        this.robotDeviceList = response.data.robotDeviceList
        this.open = true
        this.title = "修改执行日志"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.robotDeviceList = this.robotDeviceList
          if (this.form.logId != null) {
            updateLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLog(this.form).then(response => {
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
      const logIds = row.logId || this.ids
      this.$modal.confirm('是否确认删除执行日志编号为"' + logIds + '"的数据项？').then(function() {
        return delLog(logIds)
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
      this.download('masterpicar/log/export', {
        ...this.queryParams
      }, `log_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
