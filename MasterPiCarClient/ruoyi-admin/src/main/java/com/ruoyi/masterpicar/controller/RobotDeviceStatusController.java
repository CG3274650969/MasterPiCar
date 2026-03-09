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
import com.ruoyi.masterpicar.domain.RobotDeviceStatus;
import com.ruoyi.masterpicar.service.IRobotDeviceStatusService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备状态Controller
 * 
 * @author cg
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/masterpicar/status")
public class RobotDeviceStatusController extends BaseController
{
    @Autowired
    private IRobotDeviceStatusService robotDeviceStatusService;

    /**
     * 查询设备状态列表
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:status:list')")
    @GetMapping("/list")
    public TableDataInfo list(RobotDeviceStatus robotDeviceStatus)
    {
        startPage();
        List<RobotDeviceStatus> list = robotDeviceStatusService.selectRobotDeviceStatusList(robotDeviceStatus);
        return getDataTable(list);
    }

    /**
     * 导出设备状态列表
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:status:export')")
    @Log(title = "设备状态", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RobotDeviceStatus robotDeviceStatus)
    {
        List<RobotDeviceStatus> list = robotDeviceStatusService.selectRobotDeviceStatusList(robotDeviceStatus);
        ExcelUtil<RobotDeviceStatus> util = new ExcelUtil<RobotDeviceStatus>(RobotDeviceStatus.class);
        util.exportExcel(response, list, "设备状态数据");
    }

    /**
     * 获取设备状态详细信息
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:status:query')")
    @GetMapping(value = "/{statusId}")
    public AjaxResult getInfo(@PathVariable("statusId") Long statusId)
    {
        return success(robotDeviceStatusService.selectRobotDeviceStatusByStatusId(statusId));
    }

    /**
     * 新增设备状态
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:status:add')")
    @Log(title = "设备状态", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RobotDeviceStatus robotDeviceStatus)
    {
        return toAjax(robotDeviceStatusService.insertRobotDeviceStatus(robotDeviceStatus));
    }

    /**
     * 修改设备状态
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:status:edit')")
    @Log(title = "设备状态", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RobotDeviceStatus robotDeviceStatus)
    {
        return toAjax(robotDeviceStatusService.updateRobotDeviceStatus(robotDeviceStatus));
    }

    /**
     * 删除设备状态
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:status:remove')")
    @Log(title = "设备状态", businessType = BusinessType.DELETE)
	@DeleteMapping("/{statusIds}")
    public AjaxResult remove(@PathVariable Long[] statusIds)
    {
        return toAjax(robotDeviceStatusService.deleteRobotDeviceStatusByStatusIds(statusIds));
    }
}
