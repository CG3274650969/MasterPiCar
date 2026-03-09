package com.ruoyi.masterpicar.service;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotChatSession;

/**
 * 对话会话Service接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface IRobotChatSessionService 
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
     * 批量删除对话会话
     * 
     * @param sessionIds 需要删除的对话会话主键集合
     * @return 结果
     */
    public int deleteRobotChatSessionBySessionIds(Long[] sessionIds);

    /**
     * 删除对话会话信息
     * 
     * @param sessionId 对话会话主键
     * @return 结果
     */
    public int deleteRobotChatSessionBySessionId(Long sessionId);
}
