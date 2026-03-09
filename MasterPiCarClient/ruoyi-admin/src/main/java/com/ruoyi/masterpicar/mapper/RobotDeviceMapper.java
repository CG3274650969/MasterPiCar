package com.ruoyi.masterpicar.mapper;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotDevice;

/**
 * 机器人设备Mapper接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface RobotDeviceMapper 
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
     * 删除机器人设备
     * 
     * @param deviceId 机器人设备主键
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceId(Long deviceId);

    /**
     * 批量删除机器人设备
     * 
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceIds(Long[] deviceIds);

    int updateAllOffline();
}
