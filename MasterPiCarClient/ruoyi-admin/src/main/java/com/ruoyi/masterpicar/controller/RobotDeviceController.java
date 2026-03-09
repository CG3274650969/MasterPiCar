package com.ruoyi.masterpicar.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.masterpicar.domain.RobotDevice;
import com.ruoyi.masterpicar.service.IRobotDeviceService;

/**
 * 机器人设备 Controller
 *
 * @author cg
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/masterpicar/device")
public class RobotDeviceController extends BaseController {

    @Autowired
    private IRobotDeviceService robotDeviceService;

    /**
     * 查询机器人设备列表
     */
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(RobotDevice robotDevice) {
        startPage();
        List<RobotDevice> list = robotDeviceService.selectRobotDeviceList(robotDevice);
        return getDataTable(list);
    }

    /**
     * 导出机器人设备列表
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:device:export')")
    @Log(title = "机器人设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RobotDevice robotDevice) {
        List<RobotDevice> list = robotDeviceService.selectRobotDeviceList(robotDevice);
        ExcelUtil<RobotDevice> util = new ExcelUtil<>(RobotDevice.class);
        util.exportExcel(response, list, "机器人设备数据");
    }

    /**
     * 获取机器人设备详细信息
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:device:query')")
    @GetMapping("/{deviceId}")
    public AjaxResult getInfo(@PathVariable Long deviceId) {
        return success(robotDeviceService.selectRobotDeviceByDeviceId(deviceId));
    }

    /**
     * 新增机器人设备
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:device:add')")
    @Log(title = "机器人设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RobotDevice robotDevice) {
        return toAjax(robotDeviceService.insertRobotDevice(robotDevice));
    }

    /**
     * 修改机器人设备
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:device:edit')")
    @Log(title = "机器人设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RobotDevice robotDevice) {
        return toAjax(robotDeviceService.updateRobotDevice(robotDevice));
    }

    /**
     * 删除机器人设备
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:device:remove')")
    @Log(title = "机器人设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds) {
        return toAjax(robotDeviceService.deleteRobotDeviceByDeviceIds(deviceIds));
    }

    /**
     * 设置设备在线（只允许一台在线）
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:device:edit')")
    @PostMapping("/online/{deviceId}")
    public AjaxResult online(@PathVariable Long deviceId) {
        return toAjax(robotDeviceService.setDeviceOnline(deviceId));
    }
}
