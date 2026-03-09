package com.ruoyi.masterpicar.service;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotChatMessage;

/**
 * 对话消息Service接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface IRobotChatMessageService 
{
    /**
     * 查询对话消息
     * 
     * @param messageId 对话消息主键
     * @return 对话消息
     */
    public RobotChatMessage selectRobotChatMessageByMessageId(Long messageId);

    /**
     * 查询对话消息列表
     * 
     * @param robotChatMessage 对话消息
     * @return 对话消息集合
     */
    public List<RobotChatMessage> selectRobotChatMessageList(RobotChatMessage robotChatMessage);

    /**
     * 新增对话消息
     * 
     * @param robotChatMessage 对话消息
     * @return 结果
     */
    public int insertRobotChatMessage(RobotChatMessage robotChatMessage);

    /**
     * 修改对话消息
     * 
     * @param robotChatMessage 对话消息
     * @return 结果
     */
    public int updateRobotChatMessage(RobotChatMessage robotChatMessage);

    /**
     * 批量删除对话消息
     * 
     * @param messageIds 需要删除的对话消息主键集合
     * @return 结果
     */
    public int deleteRobotChatMessageByMessageIds(Long[] messageIds);

    /**
     * 删除对话消息信息
     * 
     * @param messageId 对话消息主键
     * @return 结果
     */
    public int deleteRobotChatMessageByMessageId(Long messageId);
}
