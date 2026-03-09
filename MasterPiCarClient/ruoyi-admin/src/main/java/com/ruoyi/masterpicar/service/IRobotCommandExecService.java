package com.ruoyi.masterpicar.service;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotCommandExec;

/**
 * 指令执行Service接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface IRobotCommandExecService 
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
     * 批量删除指令执行
     * 
     * @param execIds 需要删除的指令执行主键集合
     * @return 结果
     */
    public int deleteRobotCommandExecByExecIds(Long[] execIds);

    /**
     * 删除指令执行信息
     * 
     * @param execId 指令执行主键
     * @return 结果
     */
    public int deleteRobotCommandExecByExecId(Long execId);
}
