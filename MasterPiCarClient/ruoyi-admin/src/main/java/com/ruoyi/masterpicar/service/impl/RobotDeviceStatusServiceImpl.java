package com.ruoyi.masterpicar.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.masterpicar.domain.RobotDevice;
import com.ruoyi.masterpicar.domain.RobotDeviceStatus;
import com.ruoyi.masterpicar.mapper.RobotDeviceStatusMapper;
import com.ruoyi.masterpicar.mapper.RobotDeviceMapper;
import com.ruoyi.masterpicar.service.IRobotDeviceStatusService;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 设备状态Service业务层处理
 */
@Service
public class RobotDeviceStatusServiceImpl implements IRobotDeviceStatusService
{
    @Autowired
    private RobotDeviceStatusMapper robotDeviceStatusMapper;

    @Autowired
    private RobotDeviceMapper robotDeviceMapper;

    /**
     * 实现接口新增的方法：连接并刷新状态
     */
    @Override
    public int connectAndRefreshStatus(Long deviceId, String username, String password) {
        // 1. 获取设备信息以拿到 IP 地址
        RobotDevice device = robotDeviceMapper.selectRobotDeviceByDeviceId(deviceId);
        if (device == null || StringUtils.isEmpty(device.getIpAddress())) {
            throw new RuntimeException("未找到该设备的IP地址配置");
        }

        String host = device.getIpAddress();

        // 2. 执行 SSH 抓取实时数据
        RobotDeviceStatus status = fetchRealStatusFromPi(host, username, password, deviceId);
        if (status == null) {
            throw new RuntimeException("无法连接树莓派 (" + host + ")，请检查网络、用户名或密码");
        }

        // 检查数据库中是否已存在该设备的状态记录
        RobotDeviceStatus existingStatus = robotDeviceStatusMapper.selectRobotDeviceStatusByDeviceId(deviceId);

        if (existingStatus != null) {
            // 如果存在，把新抓取的数据覆盖到旧记录的 ID 上，执行更新
            status.setStatusId(existingStatus.getStatusId());
            return robotDeviceStatusMapper.updateRobotDeviceStatus(status);
        } else {
            // 如果不存在，执行新增
            return robotDeviceStatusMapper.insertRobotDeviceStatus(status);
        }
    }

    /**
     * SSH 抓取核心逻辑
     */
    private RobotDeviceStatus fetchRealStatusFromPi(String host, String user, String password, Long deviceId) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelExec channel = null;

        try {
            session = jsch.getSession(user, host, 22);
            session.setPassword(password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password,keyboard-interactive");
            session.setConfig(config);

            session.connect(10000);

            String cmd = "echo $(top -bn1 | grep 'Cpu(s)' | awk '{print $2}') && " +
                    "echo $(free | grep Mem | awk '{print $3/$2 * 100.0}') && " +
                    "echo $(vcgencmd measure_temp | grep -oE '[0-9.]+' || echo 0) && " +
                    "echo 100.0";

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(cmd);
            InputStream in = channel.getInputStream();
            channel.connect();

            Scanner s = new Scanner(in).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";
            String[] lines = result.split("\n");

            if (lines.length >= 3) {
                RobotDeviceStatus status = new RobotDeviceStatus();
                status.setDeviceId(deviceId);
                status.setCpuUsage(new BigDecimal(lines[0].trim()));
                status.setMemoryUsage(new BigDecimal(lines[1].trim()));
                status.setTemperature(new BigDecimal(lines[2].trim()));
                status.setBatteryLevel(new BigDecimal(lines[3].trim()));
                status.setReportTime(new Date());
                return status;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) channel.disconnect();
            if (session != null) session.disconnect();
        }
        return null;
    }

    // --- 以下为原有生成的 CRUD 方法 ---

    @Override
    public RobotDeviceStatus selectRobotDeviceStatusByStatusId(Long statusId) {
        return robotDeviceStatusMapper.selectRobotDeviceStatusByStatusId(statusId);
    }

    @Override
    public List<RobotDeviceStatus> selectRobotDeviceStatusList(RobotDeviceStatus robotDeviceStatus) {
        return robotDeviceStatusMapper.selectRobotDeviceStatusList(robotDeviceStatus);
    }

    @Transactional
    @Override
    public int insertRobotDeviceStatus(RobotDeviceStatus robotDeviceStatus) {
        int rows = robotDeviceStatusMapper.insertRobotDeviceStatus(robotDeviceStatus);
        insertRobotDevice(robotDeviceStatus);
        return rows;
    }

    @Transactional
    @Override
    public int updateRobotDeviceStatus(RobotDeviceStatus robotDeviceStatus) {
        robotDeviceStatusMapper.deleteRobotDeviceByDeviceName(robotDeviceStatus.getStatusId());
        insertRobotDevice(robotDeviceStatus);
        return robotDeviceStatusMapper.updateRobotDeviceStatus(robotDeviceStatus);
    }

    @Transactional
    @Override
    public int deleteRobotDeviceStatusByStatusIds(Long[] statusIds) {
        robotDeviceStatusMapper.deleteRobotDeviceByDeviceNames(statusIds);
        return robotDeviceStatusMapper.deleteRobotDeviceStatusByStatusIds(statusIds);
    }

    @Transactional
    @Override
    public int deleteRobotDeviceStatusByStatusId(Long statusId) {
        robotDeviceStatusMapper.deleteRobotDeviceByDeviceName(statusId);
        return robotDeviceStatusMapper.deleteRobotDeviceStatusByStatusId(statusId);
    }

    public void insertRobotDevice(RobotDeviceStatus robotDeviceStatus) {
        List<RobotDevice> robotDeviceList = robotDeviceStatus.getRobotDeviceList();
        Long statusId = robotDeviceStatus.getStatusId();
        if (robotDeviceList != null) {
            List<RobotDevice> list = new ArrayList<>();
            for (RobotDevice robotDevice : robotDeviceList) {
                robotDevice.setDeviceName(String.valueOf(statusId));
                list.add(robotDevice);
            }
            if (!list.isEmpty()) {
                robotDeviceStatusMapper.batchRobotDevice(list);
            }
        }
    }
}