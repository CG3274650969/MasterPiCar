package com.ruoyi.masterpicar.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.masterpicar.domain.RobotDevice;
import com.ruoyi.masterpicar.mapper.RobotChatMessageMapper;
import com.ruoyi.masterpicar.domain.RobotChatMessage;
import com.ruoyi.masterpicar.service.IRobotChatMessageService;

/**
 * 对话消息Service业务层处理
 *
 * @author cg
 * @date 2026-01-09
 */
@Service
public class RobotChatMessageServiceImpl implements IRobotChatMessageService
{
    @Autowired
    private RobotChatMessageMapper robotChatMessageMapper;

    /**
     * 查询对话消息
     *
     * @param messageId 对话消息主键
     * @return 对话消息
     */
    @Override
    public RobotChatMessage selectRobotChatMessageByMessageId(Long messageId)
    {
        return robotChatMessageMapper.selectRobotChatMessageByMessageId(messageId);
    }

    /**
     * 查询对话消息列表
     *
     * @param robotChatMessage 对话消息
     * @return 对话消息
     */
    @Override
    public List<RobotChatMessage> selectRobotChatMessageList(RobotChatMessage robotChatMessage)
    {
        return robotChatMessageMapper.selectRobotChatMessageList(robotChatMessage);
    }

    /**
     * 新增对话消息
     *
     * @param robotChatMessage 对话消息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertRobotChatMessage(RobotChatMessage robotChatMessage)
    {
        if (robotChatMessage.getContent() == null) {
            robotChatMessage.setContent(""); // 或者你想要的默认文本
        }

        robotChatMessage.setCreateTime(DateUtils.getNowDate());
        int rows = robotChatMessageMapper.insertRobotChatMessage(robotChatMessage);
        insertRobotDevice(robotChatMessage);
        return rows;
    }

    /**
     * 修改对话消息
     *
     * @param robotChatMessage 对话消息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateRobotChatMessage(RobotChatMessage robotChatMessage)
    {
        robotChatMessageMapper.deleteRobotDeviceByDeviceName(robotChatMessage.getMessageId());
        insertRobotDevice(robotChatMessage);
        return robotChatMessageMapper.updateRobotChatMessage(robotChatMessage);
    }

    /**
     * 批量删除对话消息
     *
     * @param messageIds 需要删除的对话消息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRobotChatMessageByMessageIds(Long[] messageIds)
    {
        robotChatMessageMapper.deleteRobotDeviceByDeviceNames(messageIds);
        return robotChatMessageMapper.deleteRobotChatMessageByMessageIds(messageIds);
    }

    /**
     * 删除对话消息信息
     *
     * @param messageId 对话消息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRobotChatMessageByMessageId(Long messageId)
    {
        robotChatMessageMapper.deleteRobotDeviceByDeviceName(messageId);
        return robotChatMessageMapper.deleteRobotChatMessageByMessageId(messageId);
    }

    /**
     * 新增机器人设备信息
     *
     * @param robotChatMessage 对话消息对象
     */
    public void insertRobotDevice(RobotChatMessage robotChatMessage)
    {
        List<RobotDevice> robotDeviceList = robotChatMessage.getRobotDeviceList();
        Long messageId = robotChatMessage.getMessageId();
        if (StringUtils.isNotNull(robotDeviceList))
        {
            List<RobotDevice> list = new ArrayList<RobotDevice>();
            for (RobotDevice robotDevice : robotDeviceList)
            {
//                robotDevice.setDeviceName(messageId);
                robotDevice.setDeviceName(String.valueOf(messageId));

                list.add(robotDevice);
            }
            if (list.size() > 0)
            {
                robotChatMessageMapper.batchRobotDevice(list);
            }
        }
    }
}
