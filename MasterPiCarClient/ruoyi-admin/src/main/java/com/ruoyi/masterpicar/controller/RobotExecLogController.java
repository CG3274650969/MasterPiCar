package com.ruoyi.masterpicar.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.masterpicar.domain.RobotExecLog;
import com.ruoyi.masterpicar.service.IRobotExecLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 执行日志Controller
 * 
 * @author cg
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/masterpicar/log")
public class RobotExecLogController extends BaseController
{
    @Autowired
    private IRobotExecLogService robotExecLogService;

    /**
     * 查询执行日志列表
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(RobotExecLog robotExecLog)
    {
        startPage();
        List<RobotExecLog> list = robotExecLogService.selectRobotExecLogList(robotExecLog);
        return getDataTable(list);
    }

    /**
     * 导出执行日志列表
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:log:export')")
    @Log(title = "执行日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RobotExecLog robotExecLog)
    {
        List<RobotExecLog> list = robotExecLogService.selectRobotExecLogList(robotExecLog);
        ExcelUtil<RobotExecLog> util = new ExcelUtil<RobotExecLog>(RobotExecLog.class);
        util.exportExcel(response, list, "执行日志数据");
    }

    /**
     * 获取执行日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:log:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") Long logId)
    {
        return success(robotExecLogService.selectRobotExecLogByLogId(logId));
    }

    /**
     * 新增执行日志
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:log:add')")
    @Log(title = "执行日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RobotExecLog robotExecLog)
    {
        return toAjax(robotExecLogService.insertRobotExecLog(robotExecLog));
    }

    /**
     * 修改执行日志
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:log:edit')")
    @Log(title = "执行日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RobotExecLog robotExecLog)
    {
        return toAjax(robotExecLogService.updateRobotExecLog(robotExecLog));
    }

    /**
     * 删除执行日志
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:log:remove')")
    @Log(title = "执行日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable Long[] logIds)
    {
        return toAjax(robotExecLogService.deleteRobotExecLogByLogIds(logIds));
    }
}
