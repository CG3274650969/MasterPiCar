package com.ruoyi.masterpicar.domain;

import java.util.List;
import java.util.Map;

public class RobotControlDto {
    private Long deviceId;
    private List<Map<String, Object>> actions; // 对应前端的 commands 数组

    // Getter and Setter ...

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public List<Map<String, Object>> getActions() {
        return actions;
    }

    public void setActions(List<Map<String, Object>> actions) {
        this.actions = actions;
    }
}
