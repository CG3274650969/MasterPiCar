package com.ruoyi.masterpicar.mapper;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotChatSession;
import com.ruoyi.masterpicar.domain.RobotDevice;

/**
 * 对话会话Mapper接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface RobotChatSessionMapper 
{
    /**
     * 查询对话会话
     * 
     * @param sessionId 对话会话主键
     * @return 对话会话
     */
    public RobotChatSession selectRobotChatSessionBySessionId(Long sessionId);

    /**
     * 查询对话会话列表
     * 
     * @param robotChatSession 对话会话
     * @return 对话会话集合
     */
    public List<RobotChatSession> selectRobotChatSessionList(RobotChatSession robotChatSession);

    /**
     * 新增对话会话
     * 
     * @param robotChatSession 对话会话
     * @return 结果
     */
    public int insertRobotChatSession(RobotChatSession robotChatSession);

    /**
     * 修改对话会话
     * 
     * @param robotChatSession 对话会话
     * @return 结果
     */
    public int updateRobotChatSession(RobotChatSession robotChatSession);

    /**
     * 删除对话会话
     * 
     * @param sessionId 对话会话主键
     * @return 结果
     */
    public int deleteRobotChatSessionBySessionId(Long sessionId);

    /**
     * 批量删除对话会话
     * 
     * @param sessionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotChatSessionBySessionIds(Long[] sessionIds);

    /**
     * 批量删除机器人设备
     * 
     * @param sessionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceNames(Long[] sessionIds);
    
    /**
     * 批量新增机器人设备
     * 
     * @param robotDeviceList 机器人设备列表
     * @return 结果
     */
    public int batchRobotDevice(List<RobotDevice> robotDeviceList);
    

    /**
     * 通过对话会话主键删除机器人设备信息
     * 
     * @param sessionId 对话会话ID
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceName(Long sessionId);
}
