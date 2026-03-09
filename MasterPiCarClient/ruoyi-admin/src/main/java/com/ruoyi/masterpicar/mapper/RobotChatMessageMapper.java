package com.ruoyi.masterpicar.mapper;

import java.util.List;
import com.ruoyi.masterpicar.domain.RobotChatMessage;
import com.ruoyi.masterpicar.domain.RobotDevice;

/**
 * 对话消息Mapper接口
 * 
 * @author cg
 * @date 2026-01-09
 */
public interface RobotChatMessageMapper 
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
     * 删除对话消息
     * 
     * @param messageId 对话消息主键
     * @return 结果
     */
    public int deleteRobotChatMessageByMessageId(Long messageId);

    /**
     * 批量删除对话消息
     * 
     * @param messageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotChatMessageByMessageIds(Long[] messageIds);

    /**
     * 批量删除机器人设备
     * 
     * @param messageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceNames(Long[] messageIds);
    
    /**
     * 批量新增机器人设备
     * 
     * @param robotDeviceList 机器人设备列表
     * @return 结果
     */
    public int batchRobotDevice(List<RobotDevice> robotDeviceList);
    

    /**
     * 通过对话消息主键删除机器人设备信息
     * 
     * @param messageId 对话消息ID
     * @return 结果
     */
    public int deleteRobotDeviceByDeviceName(Long messageId);
}
