package com.ruoyi.masterpicar.service.impl;

import java.util.Date;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.masterpicar.mapper.RobotDeviceMapper;
import com.ruoyi.masterpicar.domain.RobotDevice;
import com.ruoyi.masterpicar.service.IRobotDeviceService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 机器人设备Service业务层处理
 * 
 * @author cg
 * @date 2026-01-09
 */
@Service
public class RobotDeviceServiceImpl implements IRobotDeviceService 
{
    @Autowired
    private RobotDeviceMapper robotDeviceMapper;

    /**
     * 查询机器人设备
     * 
     * @param deviceId 机器人设备主键
     * @return 机器人设备
     */
    @Override
    public RobotDevice selectRobotDeviceByDeviceId(Long deviceId)
    {
        return robotDeviceMapper.selectRobotDeviceByDeviceId(deviceId);
    }

    /**
     * 查询机器人设备列表
     * 
     * @param robotDevice 机器人设备
     * @return 机器人设备
     */
    @Override
    public List<RobotDevice> selectRobotDeviceList(RobotDevice robotDevice)
    {
        return robotDeviceMapper.selectRobotDeviceList(robotDevice);
    }

    /**
     * 新增机器人设备
     * 
     * @param robotDevice 机器人设备
     * @return 结果
     */
    @Override
    public int insertRobotDevice(RobotDevice robotDevice)
    {
        robotDevice.setCreateTime(DateUtils.getNowDate());
        return robotDeviceMapper.insertRobotDevice(robotDevice);
    }

    /**
     * 修改机器人设备
     * 
     * @param robotDevice 机器人设备
     * @return 结果
     */
    @Override
    public int updateRobotDevice(RobotDevice robotDevice)
    {
        robotDevice.setUpdateTime(DateUtils.getNowDate());
        return robotDeviceMapper.updateRobotDevice(robotDevice);
    }

    /**
     * 批量删除机器人设备
     * 
     * @param deviceIds 需要删除的机器人设备主键
     * @return 结果
     */
    @Override
    public int deleteRobotDeviceByDeviceIds(Long[] deviceIds)
    {
        return robotDeviceMapper.deleteRobotDeviceByDeviceIds(deviceIds);
    }

    /**
     * 删除机器人设备信息
     * 
     * @param deviceId 机器人设备主键
     * @return 结果
     */
    @Override
    public int deleteRobotDeviceByDeviceId(Long deviceId)
    {
        return robotDeviceMapper.deleteRobotDeviceByDeviceId(deviceId);
    }

    @Override
    @Transactional
    public int setDeviceOnline(Long deviceId) {
        robotDeviceMapper.updateAllOffline();

        RobotDevice device = new RobotDevice();
        device.setDeviceId(deviceId);
        device.setOnlineStatus("1");
        device.setLastOnlineTime(new Date());

        return robotDeviceMapper.updateRobotDevice(device);
    }

}
