package com.ruoyi.masterpicar.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 指令执行对象 robot_command_exec
 * 
 * @author cg
 * @date 2026-01-09
 */
public class RobotCommandExec extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 指令执行ID */
    private Long execId;

    /** 来源消息ID */
    @Excel(name = "来源消息ID")
    private Long messageId;

    /** 设备ID */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 下发指令JSON */
    @Excel(name = "下发指令JSON")
    private String commandJson;

    /** 执行状态（0待执行 1成功 2失败） */
    @Excel(name = "执行状态", readConverterExp = "0=待执行,1=成功,2=失败")
    private String execStatus;

    /** 设备执行反馈 */
    @Excel(name = "设备执行反馈")
    private String execFeedback;

    /** 执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "执行时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date execTime;

    /** 机器人设备信息 */
    private List<RobotDevice> robotDeviceList;

    public void setExecId(Long execId) 
    {
        this.execId = execId;
    }

    public Long getExecId() 
    {
        return execId;
    }

    public void setMessageId(Long messageId) 
    {
        this.messageId = messageId;
    }

    public Long getMessageId() 
    {
        return messageId;
    }

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setCommandJson(String commandJson) 
    {
        this.commandJson = commandJson;
    }

    public String getCommandJson() 
    {
        return commandJson;
    }

    public void setExecStatus(String execStatus) 
    {
        this.execStatus = execStatus;
    }

    public String getExecStatus() 
    {
        return execStatus;
    }

    public void setExecFeedback(String execFeedback) 
    {
        this.execFeedback = execFeedback;
    }

    public String getExecFeedback() 
    {
        return execFeedback;
    }

    public void setExecTime(Date execTime) 
    {
        this.execTime = execTime;
    }

    public Date getExecTime() 
    {
        return execTime;
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
            .append("execId", getExecId())
            .append("messageId", getMessageId())
            .append("deviceId", getDeviceId())
            .append("commandJson", getCommandJson())
            .append("execStatus", getExecStatus())
            .append("execFeedback", getExecFeedback())
            .append("execTime", getExecTime())
            .append("robotDeviceList", getRobotDeviceList())
            .toString();
    }
}
