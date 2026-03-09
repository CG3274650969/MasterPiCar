package com.ruoyi.masterpicar.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 对话会话对象 robot_chat_session
 * 
 * @author cg
 * @date 2026-01-09
 */
public class RobotChatSession extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会话ID */
    private Long sessionId;

    /** 用户ID（sys_user） */
    @Excel(name = "用户ID", readConverterExp = "s=ys_user")
    private Long userId;

    /** 绑定设备ID */
    @Excel(name = "绑定设备ID")
    private Long deviceId;

    /** 会话标题 */
    @Excel(name = "会话标题")
    private String sessionTitle;

    /** 状态（0结束 1进行中） */
    @Excel(name = "状态", readConverterExp = "0=结束,1=进行中")
    private String status;

    /** 机器人设备信息 */
    private List<RobotDevice> robotDeviceList;

    public void setSessionId(Long sessionId) 
    {
        this.sessionId = sessionId;
    }

    public Long getSessionId() 
    {
        return sessionId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    public void setSessionTitle(String sessionTitle) 
    {
        this.sessionTitle = sessionTitle;
    }

    public String getSessionTitle() 
    {
        return sessionTitle;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("sessionId", getSessionId())
            .append("userId", getUserId())
            .append("deviceId", getDeviceId())
            .append("sessionTitle", getSessionTitle())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("robotDeviceList", getRobotDeviceList())
            .toString();
    }
}
