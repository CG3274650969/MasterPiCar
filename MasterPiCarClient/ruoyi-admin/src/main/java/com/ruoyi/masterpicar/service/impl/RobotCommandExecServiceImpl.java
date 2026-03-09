package com.ruoyi.masterpicar.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.masterpicar.domain.RobotDevice;
import com.ruoyi.masterpicar.mapper.RobotCommandExecMapper;
import com.ruoyi.masterpicar.domain.RobotCommandExec;
import com.ruoyi.masterpicar.service.IRobotCommandExecService;

/**
 * 指令执行Service业务层处理
 * 
 * @author cg
 * @date 2026-01-09
 */
@Service
public class RobotCommandExecServiceImpl implements IRobotCommandExecService 
{
    @Autowired
    private RobotCommandExecMapper robotCommandExecMapper;

    /**
     * 查询指令执行
     * 
     * @param execId 指令执行主键
     * @return 指令执行
     */
    @Override
    public RobotCommandExec selectRobotCommandExecByExecId(Long execId)
    {
        return robotCommandExecMapper.selectRobotCommandExecByExecId(execId);
    }

    /**
     * 查询指令执行列表
     * 
     * @param robotCommandExec 指令执行
     * @return 指令执行
     */
    @Override
    public List<RobotCommandExec> selectRobotCommandExecList(RobotCommandExec robotCommandExec)
    {
        return robotCommandExecMapper.selectRobotCommandExecList(robotCommandExec);
    }

    /**
     * 新增指令执行
     * 
     * @param robotCommandExec 指令执行
     * @return 结果
     */
    @Transactional
    @Override
    public int insertRobotCommandExec(RobotCommandExec robotCommandExec)
    {
        int rows = robotCommandExecMapper.insertRobotCommandExec(robotCommandExec);
        insertRobotDevice(robotCommandExec);
        return rows;
    }

    /**
     * 修改指令执行
     * 
     * @param robotCommandExec 指令执行
     * @return 结果
     */
    @Transactional
    @Override
    public int updateRobotCommandExec(RobotCommandExec robotCommandExec)
    {
        robotCommandExecMapper.deleteRobotDeviceByDeviceName(robotCommandExec.getExecId());
        insertRobotDevice(robotCommandExec);
        return robotCommandExecMapper.updateRobotCommandExec(robotCommandExec);
    }

    /**
     * 批量删除指令执行
     * 
     * @param execIds 需要删除的指令执行主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRobotCommandExecByExecIds(Long[] execIds)
    {
        robotCommandExecMapper.deleteRobotDeviceByDeviceNames(execIds);
        return robotCommandExecMapper.deleteRobotCommandExecByExecIds(execIds);
    }

    /**
     * 删除指令执行信息
     * 
     * @param execId 指令执行主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRobotCommandExecByExecId(Long execId)
    {
        robotCommandExecMapper.deleteRobotDeviceByDeviceName(execId);
        return robotCommandExecMapper.deleteRobotCommandExecByExecId(execId);
    }

    /**
     * 新增机器人设备信息
     * 
     * @param robotCommandExec 指令执行对象
     */
    public void insertRobotDevice(RobotCommandExec robotCommandExec)
    {
        List<RobotDevice> robotDeviceList = robotCommandExec.getRobotDeviceList();
        Long execId = robotCommandExec.getExecId();
        if (StringUtils.isNotNull(robotDeviceList))
        {
            List<RobotDevice> list = new ArrayList<RobotDevice>();
            for (RobotDevice robotDevice : robotDeviceList)
            {
//                robotDevice.setDeviceName(execId);
                robotDevice.setDeviceName(String.valueOf(execId));

                list.add(robotDevice);
            }
            if (list.size() > 0)
            {
                robotCommandExecMapper.batchRobotDevice(list);
            }
        }
    }
}
