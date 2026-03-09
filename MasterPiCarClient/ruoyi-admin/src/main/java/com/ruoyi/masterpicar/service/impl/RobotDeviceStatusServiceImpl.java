package com.ruoyi.masterpicar.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.masterpicar.domain.RobotDevice;
import com.ruoyi.masterpicar.mapper.RobotDeviceStatusMapper;
import com.ruoyi.masterpicar.domain.RobotDeviceStatus;
import com.ruoyi.masterpicar.service.IRobotDeviceStatusService;

/**
 * 设备状态Service业务层处理
 * 
 * @author cg
 * @date 2026-01-09
 */
@Service
public class RobotDeviceStatusServiceImpl implements IRobotDeviceStatusService 
{
    @Autowired
    private RobotDeviceStatusMapper robotDeviceStatusMapper;

    /**
     * 查询设备状态
     * 
     * @param statusId 设备状态主键
     * @return 设备状态
     */
    @Override
    public RobotDeviceStatus selectRobotDeviceStatusByStatusId(Long statusId)
    {
        return robotDeviceStatusMapper.selectRobotDeviceStatusByStatusId(statusId);
    }

    /**
     * 查询设备状态列表
     * 
     * @param robotDeviceStatus 设备状态
     * @return 设备状态
     */
    @Override
    public List<RobotDeviceStatus> selectRobotDeviceStatusList(RobotDeviceStatus robotDeviceStatus)
    {
        return robotDeviceStatusMapper.selectRobotDeviceStatusList(robotDeviceStatus);
    }

    /**
     * 新增设备状态
     * 
     * @param robotDeviceStatus 设备状态
     * @return 结果
     */
    @Transactional
    @Override
    public int insertRobotDeviceStatus(RobotDeviceStatus robotDeviceStatus)
    {
        int rows = robotDeviceStatusMapper.insertRobotDeviceStatus(robotDeviceStatus);
        insertRobotDevice(robotDeviceStatus);
        return rows;
    }

    /**
     * 修改设备状态
     * 
     * @param robotDeviceStatus 设备状态
     * @return 结果
     */
    @Transactional
    @Override
    public int updateRobotDeviceStatus(RobotDeviceStatus robotDeviceStatus)
    {
        robotDeviceStatusMapper.deleteRobotDeviceByDeviceName(robotDeviceStatus.getStatusId());
        insertRobotDevice(robotDeviceStatus);
        return robotDeviceStatusMapper.updateRobotDeviceStatus(robotDeviceStatus);
    }

    /**
     * 批量删除设备状态
     * 
     * @param statusIds 需要删除的设备状态主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRobotDeviceStatusByStatusIds(Long[] statusIds)
    {
        robotDeviceStatusMapper.deleteRobotDeviceByDeviceNames(statusIds);
        return robotDeviceStatusMapper.deleteRobotDeviceStatusByStatusIds(statusIds);
    }

    /**
     * 删除设备状态信息
     * 
     * @param statusId 设备状态主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteRobotDeviceStatusByStatusId(Long statusId)
    {
        robotDeviceStatusMapper.deleteRobotDeviceByDeviceName(statusId);
        return robotDeviceStatusMapper.deleteRobotDeviceStatusByStatusId(statusId);
    }

    /**
     * 新增机器人设备信息
     * 
     * @param robotDeviceStatus 设备状态对象
     */
    public void insertRobotDevice(RobotDeviceStatus robotDeviceStatus)
    {
        List<RobotDevice> robotDeviceList = robotDeviceStatus.getRobotDeviceList();
        Long statusId = robotDeviceStatus.getStatusId();
        if (StringUtils.isNotNull(robotDeviceList))
        {
            List<RobotDevice> list = new ArrayList<RobotDevice>();
            for (RobotDevice robotDevice : robotDeviceList)
            {
//                robotDevice.setDeviceName(statusId);
                robotDevice.setDeviceName(String.valueOf(statusId));

                list.add(robotDevice);
            }
            if (list.size() > 0)
            {
                robotDeviceStatusMapper.batchRobotDevice(list);
            }
        }
    }
}
