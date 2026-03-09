package com.ruoyi.masterpicar.mapper;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotCommandExec;
import com.ruoyi.masterpicar.domain.RobotDevice;

/**
 * 指令执行Mapper接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface RobotCommandExecMapper 
{
    /**
     * 查询指令执行
     * 
     * @param execId 指令执行主键
     * @return 指令执行
     */
    public RobotCommandExec selectRobotCommandExecByExecId(Long execId);

    /**
     * 查询指令执行列表
     * 
     * @param robotCommandExec 指令执行
     * @return 指令执行集合
     */
    public List<RobotCommandExec> selectRobotCommandExecList(RobotCommandExec robotCommandExec);

    /**
     * 新增指令执行
     * 
     * @param robotCommandExec 指令执行
     * @return 结果
     */
    public int insertRobotCommandExec(RobotCommandExec robotCommandExec);

    /**
     * 修改指令执行
     * 
     * @param robotCommandExec 指令执行
     * @return 结果
     */
    public int updateRobotCommandExec(RobotCommandExec robotCommandExec);

    /**
     * 删除指令执行
     * 
     * @param execId 指令执行主键
     * @return 结果
     */
    public int deleteRobotCommandExecByExecId(Long execId);

    /**
     * 批量删除指令执行
     * 
     * @param execIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotCommandExecByExecIds(Long[] execIds);

    /**
     * 批量删除机器人设备
     * 
     * @param execIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceNames(Long[] execIds);
    
    /**
     * 批量新增机器人设备
     * 
     * @param robotDeviceList 机器人设备列表
     * @return 结果
     */
    public int batchRobotDevice(List<RobotDevice> robotDeviceList);
    

    /**
     * 通过指令执行主键删除机器人设备信息
     * 
     * @param execId 指令执行ID
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceName(Long execId);
}
