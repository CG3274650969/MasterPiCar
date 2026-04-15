package com.ruoyi.masterpicar.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.client.RestTemplate;

/**
 * 机器人设备 Controller
 *
 * @author cg
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/masterpicar/device")
public class RobotDeviceController extends BaseController {

    // 手动创建 log 对象
    private static final Logger log = LoggerFactory.getLogger(RobotDeviceController.class);

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

    // RobotDeviceController.java
    @GetMapping("/video/stream/{deviceId}")
    @Anonymous // 允许匿名访问，方便调试画面
    public void proxyVideoStream(@PathVariable Long deviceId, HttpServletResponse response) {
        // 1. 获取设备信息（注意若依默认主键通常是 deviceId，确保方法名正确）
        RobotDevice device = robotDeviceService.selectRobotDeviceByDeviceId(deviceId);
        if (device == null || device.getIpAddress() == null) {
            return;
        }

        // 2. 树莓派视频地址
        String videoUrl = "http://" + device.getIpAddress() + ":5001/video_feed";

        try {
            RestTemplate restTemplate = new RestTemplate();

            // 3. 使用 getForEntity 替代 exchange，避免枚举类型转换问题
            ResponseEntity<Resource> videoResponse = restTemplate.getForEntity(videoUrl, Resource.class);

            // 4. 设置响应头为 MJPEG 格式
            response.setContentType("multipart/x-mixed-replace; boundary=frame");

            // 5. 边读边写，实现流式转发
            try (InputStream inputStream = videoResponse.getBody().getInputStream();
                 OutputStream outputStream = response.getOutputStream()) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush(); // 实时推送到浏览器
                }
            }
        } catch (Exception e) {
            log.error("视频流转发异常，请检查树莓派是否在线: {}", e.getMessage());
        }
    }
}
