package com.ruoyi.masterpicar.service;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotDevice;

/**
 * 机器人设备Service接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface IRobotDeviceService 
{
    /**
     * 查询机器人设备
     * 
     * @param deviceId 机器人设备主键
     * @return 机器人设备
     */
    public RobotDevice selectRobotDeviceByDeviceId(Long deviceId);

    /**
     * 查询机器人设备列表
     * 
     * @param robotDevice 机器人设备
     * @return 机器人设备集合
     */
    public List<RobotDevice> selectRobotDeviceList(RobotDevice robotDevice);

    /**
     * 新增机器人设备
     * 
     * @param robotDevice 机器人设备
     * @return 结果
     */
    public int insertRobotDevice(RobotDevice robotDevice);

    /**
     * 修改机器人设备
     * 
     * @param robotDevice 机器人设备
     * @return 结果
     */
    public int updateRobotDevice(RobotDevice robotDevice);

    /**
     * 批量删除机器人设备
     * 
     * @param deviceIds 需要删除的机器人设备主键集合
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceIds(Long[] deviceIds);

    /**
     * 删除机器人设备信息
     * 
     * @param deviceId 机器人设备主键
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceId(Long deviceId);

    int setDeviceOnline(Long deviceId);
}
