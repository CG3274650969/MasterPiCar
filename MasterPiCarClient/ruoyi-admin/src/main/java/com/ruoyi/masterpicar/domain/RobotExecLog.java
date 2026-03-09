package com.ruoyi.masterpicar.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 执行日志对象 robot_exec_log
 * 
 * @author cg
 * @date 2026-01-09
 */
public class RobotExecLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long logId;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 来源类型（chat / task / manual） */
    @Excel(name = "来源类型", readConverterExp = "c=hat,/=,t=ask,/=,m=anual")
    private String sourceType;

    /** 来源ID */
    @Excel(name = "来源ID")
    private Long sourceId;

    /** 执行结果（0失败 1成功） */
    @Excel(name = "执行结果", readConverterExp = "0=失败,1=成功")
    private String result;

    /** 执行详情 */
    @Excel(name = "执行详情")
    private String detail;

    /** 机器人设备信息 */
    private List<RobotDevice> robotDeviceList;

    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setSourceType(String sourceType) 
    {
        this.sourceType = sourceType;
    }

    public String getSourceType() 
    {
        return sourceType;
    }

    public void setSourceId(Long sourceId) 
    {
        this.sourceId = sourceId;
    }

    public Long getSourceId() 
    {
        return sourceId;
    }

    public void setResult(String result) 
    {
        this.result = result;
    }

    public String getResult() 
    {
        return result;
    }

    public void setDetail(String detail) 
    {
        this.detail = detail;
    }

    public String getDetail() 
    {
        return detail;
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
            .append("logId", getLogId())
            .append("deviceId", getDeviceId())
            .append("sourceType", getSourceType())
            .append("sourceId", getSourceId())
            .append("result", getResult())
            .append("detail", getDetail())
            .append("createTime", getCreateTime())
            .append("robotDeviceList", getRobotDeviceList())
            .toString();
    }
}
