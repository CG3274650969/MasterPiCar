package com.ruoyi.masterpicar.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 对话消息对象 robot_chat_message
 * 
 * @author cg
 * @date 2026-01-09
 */
public class RobotChatMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long messageId;

    /** 所属会话ID */
    @Excel(name = "所属会话ID")
    private Long sessionId;

    /** 角色（user / assistant / system） */
    @Excel(name = "角色", readConverterExp = "u=ser,/=,a=ssistant,/=,s=ystem")
    private String role;

    /** 对话内容 */
    @Excel(name = "对话内容")
    private String content;

    /** 解析后的控制指令JSON */
    @Excel(name = "解析后的控制指令JSON")
    private String commandJson;

    /** chat / command / feedback */
    @Excel(name = "chat / command / feedback")
    private String messageType;

    /** 机器人设备信息 */
    private List<RobotDevice> robotDeviceList;

    public void setMessageId(Long messageId) 
    {
        this.messageId = messageId;
    }

    public Long getMessageId() 
    {
        return messageId;
    }

    public void setSessionId(Long sessionId) 
    {
        this.sessionId = sessionId;
    }

    public Long getSessionId() 
    {
        return sessionId;
    }

    public void setRole(String role) 
    {
        this.role = role;
    }

    public String getRole() 
    {
        return role;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setCommandJson(String commandJson) 
    {
        this.commandJson = commandJson;
    }

    public String getCommandJson() 
    {
        return commandJson;
    }

    public void setMessageType(String messageType) 
    {
        this.messageType = messageType;
    }

    public String getMessageType() 
    {
        return messageType;
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
            .append("messageId", getMessageId())
            .append("sessionId", getSessionId())
            .append("role", getRole())
            .append("content", getContent())
            .append("commandJson", getCommandJson())
            .append("messageType", getMessageType())
            .append("createTime", getCreateTime())
            .append("robotDeviceList", getRobotDeviceList())
            .toString();
    }
}
