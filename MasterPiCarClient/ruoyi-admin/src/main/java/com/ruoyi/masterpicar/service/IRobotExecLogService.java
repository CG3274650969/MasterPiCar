package com.ruoyi.masterpicar.service;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotExecLog;

/**
 * 执行日志Service接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface IRobotExecLogService 
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
     * 批量删除执行日志
     * 
     * @param logIds 需要删除的执行日志主键集合
     * @return 结果
     */
    public int deleteRobotExecLogByLogIds(Long[] logIds);

    /**
     * 删除执行日志信息
     * 
     * @param logId 执行日志主键
     * @return 结果
     */
    public int deleteRobotExecLogByLogId(Long logId);
}
