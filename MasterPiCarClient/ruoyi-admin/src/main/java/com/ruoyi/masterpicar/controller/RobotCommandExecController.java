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
import com.ruoyi.masterpicar.domain.RobotCommandExec;
import com.ruoyi.masterpicar.service.IRobotCommandExecService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 指令执行Controller
 * 
 * @author cg
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/masterpicar/exec")
public class RobotCommandExecController extends BaseController
{
    @Autowired
    private IRobotCommandExecService robotCommandExecService;

    /**
     * 查询指令执行列表
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:exec:list')")
    @GetMapping("/list")
    public TableDataInfo list(RobotCommandExec robotCommandExec)
    {
        startPage();
        List<RobotCommandExec> list = robotCommandExecService.selectRobotCommandExecList(robotCommandExec);
        return getDataTable(list);
    }

    /**
     * 导出指令执行列表
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:exec:export')")
    @Log(title = "指令执行", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RobotCommandExec robotCommandExec)
    {
        List<RobotCommandExec> list = robotCommandExecService.selectRobotCommandExecList(robotCommandExec);
        ExcelUtil<RobotCommandExec> util = new ExcelUtil<RobotCommandExec>(RobotCommandExec.class);
        util.exportExcel(response, list, "指令执行数据");
    }

    /**
     * 获取指令执行详细信息
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:exec:query')")
    @GetMapping(value = "/{execId}")
    public AjaxResult getInfo(@PathVariable("execId") Long execId)
    {
        return success(robotCommandExecService.selectRobotCommandExecByExecId(execId));
    }

    /**
     * 新增指令执行
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:exec:add')")
    @Log(title = "指令执行", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RobotCommandExec robotCommandExec)
    {
        return toAjax(robotCommandExecService.insertRobotCommandExec(robotCommandExec));
    }

    /**
     * 修改指令执行
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:exec:edit')")
    @Log(title = "指令执行", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RobotCommandExec robotCommandExec)
    {
        return toAjax(robotCommandExecService.updateRobotCommandExec(robotCommandExec));
    }

    /**
     * 删除指令执行
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:exec:remove')")
    @Log(title = "指令执行", businessType = BusinessType.DELETE)
	@DeleteMapping("/{execIds}")
    public AjaxResult remove(@PathVariable Long[] execIds)
    {
        return toAjax(robotCommandExecService.deleteRobotCommandExecByExecIds(execIds));
    }
}
