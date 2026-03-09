package com.ruoyi.masterpicar.mapper;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotDeviceStatus;
import com.ruoyi.masterpicar.domain.RobotDevice;

/**
 * 设备状态Mapper接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface RobotDeviceStatusMapper 
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
     * 删除设备状态
     * 
     * @param statusId 设备状态主键
     * @return 结果
     */
    public int deleteRobotDeviceStatusByStatusId(Long statusId);

    /**
     * 批量删除设备状态
     * 
     * @param statusIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotDeviceStatusByStatusIds(Long[] statusIds);

    /**
     * 批量删除机器人设备
     * 
     * @param statusIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceNames(Long[] statusIds);
    
    /**
     * 批量新增机器人设备
     * 
     * @param robotDeviceList 机器人设备列表
     * @return 结果
     */
    public int batchRobotDevice(List<RobotDevice> robotDeviceList);
    

    /**
     * 通过设备状态主键删除机器人设备信息
     * 
     * @param statusId 设备状态ID
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceName(Long statusId);
}
