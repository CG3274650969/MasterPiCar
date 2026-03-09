package com.ruoyi.masterpicar.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.masterpicar.domain.RobotDevice;
import com.ruoyi.masterpicar.mapper.RobotChatSessionMapper;
import com.ruoyi.masterpicar.domain.RobotChatSession;
import com.ruoyi.masterpicar.service.IRobotChatSessionService;

/**
 * 对话会话Service业务层处理
 * 
 * @author cg
 * @date 2026-01-09
 */
@Service
public class RobotChatSessionServiceImpl implements IRobotChatSessionService 
{
    @Autowired
    private RobotChatSessionMapper robotChatSessionMapper;

    /**
     * 查询对话会话
     * 
     * @param sessionId 对话会话主键
     * @return 对话会话
     */
    @Override
    public RobotChatSession selectRobotChatSessionBySessionId(Long sessionId)
    {
        return robotChatSessionMapper.selectRobotChatSessionBySessionId(sessionId);
    }

    /**
     * 查询对话会话列表
     * 
     * @param robotChatSession 对话会话
     * @return 对话会话
     */
    @Override
    public List<RobotChatSession> selectRobotChatSessionList(RobotChatSession robotChatSession)
    {
        return robotChatSessionMapper.selectRobotChatSessionList(robotChatSession);
    }

    /**
     * 新增对话会话
     * 
     * @param robotChatSession 对话会话
     * @return 结果
     */
    @Transactional
    @Override
    public int insertRobotChatSession(RobotChatSession robotChatSession)
    {
        robotChatSession.setCreateTime(DateUtils.getNowDate());
        int rows = robotChatSessionMapper.insertRobotChatSession(robotChatSession);
        insertRobotDevice(robotChatSession);
        return rows;
    }

    /**
     * 修改对话会话
     * 
     * @param robotChatSession 对话会话
     * @return 结果
     */
    @Transactional
    @Override
    public int updateRobotChatSession(RobotChatSession robotChatSession)
    {
        robotChatSessionMapper.deleteRobotDeviceByDeviceName(robotChatSession.getSessionId());
        insertRobotDevice(robotChatSession);
        return robotChatSessionMapper.updateRobotChatSession(robotChatSession);
    }

    /**
     * 批量删除对话会话
     * 
     * @param sessionIds 需要删除的对话会话主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRobotChatSessionBySessionIds(Long[] sessionIds)
    {
        robotChatSessionMapper.deleteRobotDeviceByDeviceNames(sessionIds);
        return robotChatSessionMapper.deleteRobotChatSessionBySessionIds(sessionIds);
    }

    /**
     * 删除对话会话信息
     * 
     * @param sessionId 对话会话主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRobotChatSessionBySessionId(Long sessionId)
    {
        robotChatSessionMapper.deleteRobotDeviceByDeviceName(sessionId);
        return robotChatSessionMapper.deleteRobotChatSessionBySessionId(sessionId);
    }

    /**
     * 新增机器人设备信息
     * 
     * @param robotChatSession 对话会话对象
     */
    public void insertRobotDevice(RobotChatSession robotChatSession)
    {
        List<RobotDevice> robotDeviceList = robotChatSession.getRobotDeviceList();
        Long sessionId = robotChatSession.getSessionId();
        if (StringUtils.isNotNull(robotDeviceList))
        {
            List<RobotDevice> list = new ArrayList<RobotDevice>();
            for (RobotDevice robotDevice : robotDeviceList)
            {
//                robotDevice.setDeviceName(sessionId);
                robotDevice.setDeviceName(String.valueOf(sessionId));

                list.add(robotDevice);
            }
            if (list.size() > 0)
            {
                robotChatSessionMapper.batchRobotDevice(list);
            }
        }
    }
}
