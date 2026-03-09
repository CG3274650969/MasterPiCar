package com.ruoyi.masterpicar.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备状态对象 robot_device_status
 * 
 * @author cg
 * @date 2026-01-09
 */
public class RobotDeviceStatus extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 状态ID */
    private Long statusId;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** CPU使用率 */
    @Excel(name = "CPU使用率")
    private BigDecimal cpuUsage;

    /** 内存使用率 */
    @Excel(name = "内存使用率")
    private BigDecimal memoryUsage;

    /** 电量百分比 */
    @Excel(name = "电量百分比")
    private BigDecimal batteryLevel;

    /** 设备温度 */
    @Excel(name = "设备温度")
    private BigDecimal temperature;

    /** 报告时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报告时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportTime;

    /** 机器人设备信息 */
    private List<RobotDevice> robotDeviceList;

    public void setStatusId(Long statusId) 
    {
        this.statusId = statusId;
    }

    public Long getStatusId() 
    {
        return statusId;
    }

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setCpuUsage(BigDecimal cpuUsage) 
    {
        this.cpuUsage = cpuUsage;
    }

    public BigDecimal getCpuUsage() 
    {
        return cpuUsage;
    }

    public void setMemoryUsage(BigDecimal memoryUsage) 
    {
        this.memoryUsage = memoryUsage;
    }

    public BigDecimal getMemoryUsage() 
    {
        return memoryUsage;
    }

    public void setBatteryLevel(BigDecimal batteryLevel) 
    {
        this.batteryLevel = batteryLevel;
    }

    public BigDecimal getBatteryLevel() 
    {
        return batteryLevel;
    }

    public void setTemperature(BigDecimal temperature) 
    {
        this.temperature = temperature;
    }

    public BigDecimal getTemperature() 
    {
        return temperature;
    }

    public void setReportTime(Date reportTime) 
    {
        this.reportTime = reportTime;
    }

    public Date getReportTime() 
    {
        return reportTime;
    }

    public List<RobotDevice> getRobotDeviceList()
    {
        return robotDeviceList;
    }

    public void setRobotDeviceList(List<RobotDevice> robotDeviceList)
    {
        this.robotDeviceList = robotDeviceList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("statusId", getStatusId())
            .append("deviceId", getDeviceId())
            .append("cpuUsage", getCpuUsage())
            .append("memoryUsage", getMemoryUsage())
            .append("batteryLevel", getBatteryLevel())
            .append("temperature", getTemperature())
            .append("reportTime", getReportTime())
            .append("robotDeviceList", getRobotDeviceList())
            .toString();
    }
}
