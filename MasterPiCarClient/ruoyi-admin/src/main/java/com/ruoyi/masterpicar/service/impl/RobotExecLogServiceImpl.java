package com.ruoyi.masterpicar.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.masterpicar.domain.RobotDevice;
import com.ruoyi.masterpicar.mapper.RobotExecLogMapper;
import com.ruoyi.masterpicar.domain.RobotExecLog;
import com.ruoyi.masterpicar.service.IRobotExecLogService;

/**
 * 执行日志Service业务层处理
 * 
 * @author cg
 * @date 2026-01-09
 */
@Service
public class RobotExecLogServiceImpl implements IRobotExecLogService 
{
    @Autowired
    private RobotExecLogMapper robotExecLogMapper;

    /**
     * 查询执行日志
     * 
     * @param logId 执行日志主键
     * @return 执行日志
     */
    @Override
    public RobotExecLog selectRobotExecLogByLogId(Long logId)
    {
        return robotExecLogMapper.selectRobotExecLogByLogId(logId);
    }

    /**
     * 查询执行日志列表
     * 
     * @param robotExecLog 执行日志
     * @return 执行日志
     */
    @Override
    public List<RobotExecLog> selectRobotExecLogList(RobotExecLog robotExecLog)
    {
        return robotExecLogMapper.selectRobotExecLogList(robotExecLog);
    }

    /**
     * 新增执行日志
     * 
     * @param robotExecLog 执行日志
     * @return 结果
     */
    @Transactional
    @Override
    public int insertRobotExecLog(RobotExecLog robotExecLog)
    {
        robotExecLog.setCreateTime(DateUtils.getNowDate());
        int rows = robotExecLogMapper.insertRobotExecLog(robotExecLog);
        insertRobotDevice(robotExecLog);
        return rows;
    }

    /**
     * 修改执行日志
     * 
     * @param robotExecLog 执行日志
     * @return 结果
     */
    @Transactional
    @Override
    public int updateRobotExecLog(RobotExecLog robotExecLog)
    {
        robotExecLogMapper.deleteRobotDeviceByDeviceName(robotExecLog.getLogId());
        insertRobotDevice(robotExecLog);
        return robotExecLogMapper.updateRobotExecLog(robotExecLog);
    }

    /**
     * 批量删除执行日志
     * 
     * @param logIds 需要删除的执行日志主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRobotExecLogByLogIds(Long[] logIds)
    {
        robotExecLogMapper.deleteRobotDeviceByDeviceNames(logIds);
        return robotExecLogMapper.deleteRobotExecLogByLogIds(logIds);
    }

    /**
     * 删除执行日志信息
     * 
     * @param logId 执行日志主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRobotExecLogByLogId(Long logId)
    {
        robotExecLogMapper.deleteRobotDeviceByDeviceName(logId);
        return robotExecLogMapper.deleteRobotExecLogByLogId(logId);
    }

    /**
     * 新增机器人设备信息
     * 
     * @param robotExecLog 执行日志对象
     */
    public void insertRobotDevice(RobotExecLog robotExecLog)
    {
        List<RobotDevice> robotDeviceList = robotExecLog.getRobotDeviceList();
        Long logId = robotExecLog.getLogId();
        if (StringUtils.isNotNull(robotDeviceList))
        {
            List<RobotDevice> list = new ArrayList<RobotDevice>();
            for (RobotDevice robotDevice : robotDeviceList)
            {
//                robotDevice.setDeviceName(logId);
                robotDevice.setDeviceName(String.valueOf(logId));

                list.add(robotDevice);
            }
            if (list.size() > 0)
            {
                robotExecLogMapper.batchRobotDevice(list);
            }
        }
    }
}
