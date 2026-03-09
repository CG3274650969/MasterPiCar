package com.ruoyi.masterpicar.service;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotDeviceStatus;

/**
 * 设备状态Service接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface IRobotDeviceStatusService 
{
    /**
     * 查询设备状态
     * 
     * @param statusId 设备状态主键
     * @return 设备状态
     */
    public RobotDeviceStatus selectRobotDeviceStatusByStatusId(Long statusId);

    /**
     * 查询设备状态列表
     * 
     * @param robotDeviceStatus 设备状态
     * @return 设备状态集合
     */
    public List<RobotDeviceStatus> selectRobotDeviceStatusList(RobotDeviceStatus robotDeviceStatus);

    /**
     * 新增设备状态
     * 
     * @param robotDeviceStatus 设备状态
     * @return 结果
     */
    public int insertRobotDeviceStatus(RobotDeviceStatus robotDeviceStatus);

    /**
     * 修改设备状态
     * 
     * @param robotDeviceStatus 设备状态
     * @return 结果
     */
    public int updateRobotDeviceStatus(RobotDeviceStatus robotDeviceStatus);

    /**
     * 批量删除设备状态
     * 
     * @param statusIds 需要删除的设备状态主键集合
     * @return 结果
     */
    public int deleteRobotDeviceStatusByStatusIds(Long[] statusIds);

    /**
     * 删除设备状态信息
     * 
     * @param statusId 设备状态主键
     * @return 结果
     */
    public int deleteRobotDeviceStatusByStatusId(Long statusId);
}
