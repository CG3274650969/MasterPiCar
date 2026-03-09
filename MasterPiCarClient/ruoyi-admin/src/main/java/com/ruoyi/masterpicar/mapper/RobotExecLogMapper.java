package com.ruoyi.masterpicar.mapper;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotExecLog;
import com.ruoyi.masterpicar.domain.RobotDevice;

/**
 * 执行日志Mapper接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface RobotExecLogMapper 
{
    /**
     * 查询执行日志
     * 
     * @param logId 执行日志主键
     * @return 执行日志
     */
    public RobotExecLog selectRobotExecLogByLogId(Long logId);

    /**
     * 查询执行日志列表
     * 
     * @param robotExecLog 执行日志
     * @return 执行日志集合
     */
    public List<RobotExecLog> selectRobotExecLogList(RobotExecLog robotExecLog);

    /**
     * 新增执行日志
     * 
     * @param robotExecLog 执行日志
     * @return 结果
     */
    public int insertRobotExecLog(RobotExecLog robotExecLog);

    /**
     * 修改执行日志
     * 
     * @param robotExecLog 执行日志
     * @return 结果
     */
    public int updateRobotExecLog(RobotExecLog robotExecLog);

    /**
     * 删除执行日志
     * 
     * @param logId 执行日志主键
     * @return 结果
     */
    public int deleteRobotExecLogByLogId(Long logId);

    /**
     * 批量删除执行日志
     * 
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotExecLogByLogIds(Long[] logIds);

    /**
     * 批量删除机器人设备
     * 
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceNames(Long[] logIds);
    
    /**
     * 批量新增机器人设备
     * 
     * @param robotDeviceList 机器人设备列表
     * @return 结果
     */
    public int batchRobotDevice(List<RobotDevice> robotDeviceList);
    

    /**
     * 通过执行日志主键删除机器人设备信息
     * 
     * @param logId 执行日志ID
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceName(Long logId);
}
