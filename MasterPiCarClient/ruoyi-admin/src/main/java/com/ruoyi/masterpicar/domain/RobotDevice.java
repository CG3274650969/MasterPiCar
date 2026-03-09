package com.ruoyi.masterpicar.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 机器人设备对象 robot_device
 * 
 * @author cg
 * @date 2026-01-09
 */
public class RobotDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 设备序列号 */
    @Excel(name = "设备序列号")
    private String deviceSn;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddress;

    /** 在线状态（0离线 1在线） */
    @Excel(name = "在线状态", readConverterExp = "0=离线,1=在线")
    private String onlineStatus;

    /** 最后在线时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后在线时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastOnlineTime;

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

    public void setDeviceSn(String deviceSn) 
    {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn() 
    {
        return deviceSn;
    }

    public void setIpAddress(String ipAddress) 
    {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() 
    {
        return ipAddress;
    }

    public void setOnlineStatus(String onlineStatus) 
    {
        this.onlineStatus = onlineStatus;
    }

    public String getOnlineStatus() 
    {
        return onlineStatus;
    }

    public void setLastOnlineTime(Date lastOnlineTime) 
    {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Date getLastOnlineTime() 
    {
        return lastOnlineTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("deviceSn", getDeviceSn())
            .append("ipAddress", getIpAddress())
            .append("onlineStatus", getOnlineStatus())
            .append("lastOnlineTime", getLastOnlineTime())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
