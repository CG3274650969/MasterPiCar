package com.ruoyi.masterpicar.controller;

import java.net.URLEncoder;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.framework.config.MqttConfig;
import com.ruoyi.masterpicar.domain.RobotControlDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.masterpicar.domain.RobotChatMessage;
import com.ruoyi.masterpicar.service.IRobotChatMessageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.client.RestTemplate;

/**
 * 对话消息Controller
 * 
 * @author cg
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/masterpicar/message")
public class RobotChatMessageController extends BaseController {
    @Autowired
    private IRobotChatMessageService robotChatMessageService;

    /**
     * 查询对话消息列表
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(RobotChatMessage robotChatMessage) {
        startPage();
        List<RobotChatMessage> list = robotChatMessageService.selectRobotChatMessageList(robotChatMessage);
        return getDataTable(list);
    }

    /**
     * 导出对话消息列表
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:message:export')")
    @Log(title = "对话消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RobotChatMessage robotChatMessage) {
        List<RobotChatMessage> list = robotChatMessageService.selectRobotChatMessageList(robotChatMessage);
        ExcelUtil<RobotChatMessage> util = new ExcelUtil<RobotChatMessage>(RobotChatMessage.class);
        util.exportExcel(response, list, "对话消息数据");
    }

    /**
     * 获取对话消息详细信息
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:message:query')")
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable("messageId") Long messageId) {
        return success(robotChatMessageService.selectRobotChatMessageByMessageId(messageId));
    }

    /**
     * 新增对话消息
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:message:add')")
    @Log(title = "对话消息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RobotChatMessage robotChatMessage) {
        return toAjax(robotChatMessageService.insertRobotChatMessage(robotChatMessage));
    }

    /**
     * 修改对话消息
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:message:edit')")
    @Log(title = "对话消息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RobotChatMessage robotChatMessage) {
        return toAjax(robotChatMessageService.updateRobotChatMessage(robotChatMessage));
    }

    /**
     * 删除对话消息
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:message:remove')")
    @Log(title = "对话消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable Long[] messageIds) {
        return toAjax(robotChatMessageService.deleteRobotChatMessageByMessageIds(messageIds));
    }

//    @Anonymous
//    @PostMapping("/save")
//    public AjaxResult saveMessage(@RequestBody RobotChatMessage message) {
//        return toAjax(robotChatMessageService.insertRobotChatMessage(message));
//    }

    @Autowired
    private RestTemplate restTemplate; // 在配置类中需要定义 Bean

    private String fetchModelReply(String prompt) {
        try {
            String url = "http://localhost:8000/chat/stream?prompt=" + URLEncoder.encode(prompt, "UTF-8");
            // 直接调用接口，获取返回内容
            String response = restTemplate.getForObject(url, String.class);

            if (response == null || response.isEmpty()) {
                return "[模型未返回]";
            }

            // SSE 流返回的是多行 data: xxx，你可以简单处理：
            StringBuilder sb = new StringBuilder();
            String[] lines = response.split("\n");
            for (String line : lines) {
                if (line.startsWith("data:")) {
                    String data = line.substring(5).trim();
                    if (!"[DONE]".equals(data)) sb.append(data);
                }
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "[模型调用失败]";
        }
    }



//    @Anonymous
//    @PostMapping("/save")
//    public AjaxResult saveMessage(@RequestBody RobotChatMessage message) {
//        // 1️⃣ 确保必要字段不为空
//        if (message.getContent() == null) {
//            message.setContent("");
//        }
//        if (message.getRole() == null || message.getRole().isEmpty()) {
//            message.setRole("user"); // 默认 user
//        }
//        if (message.getSessionId() == null) {
//            message.setSessionId(System.currentTimeMillis()); // 临时生成 sessionId
//        }
////        if (message.getUserId() == null) {
////            message.setUserId(1L); // 用户ID，可根据实际情况
////        }
//
//        // 2️⃣ 保存用户消息或系统消息
//        robotChatMessageService.insertRobotChatMessage(message);
//        // 2️⃣ 如果是用户消息，调用模型生成回复并存数据库
//        if ("user".equals(message.getRole())) {
//            String modelReply = fetchModelReply(message.getContent());
//            RobotChatMessage reply = new RobotChatMessage();
//            reply.setSessionId(message.getSessionId());
//            reply.setRole("assistant");
//            reply.setContent(modelReply != null ? modelReply : "[模型未返回]");
//            robotChatMessageService.insertRobotChatMessage(reply);
//            return AjaxResult.success(reply); // 返回给前端
//        }
//
//        // 4️⃣ 否则直接返回前端保存的消息
//        return AjaxResult.success(message);
//    }
    @Anonymous
    @PostMapping("/save")
    public AjaxResult saveMessage(@RequestBody RobotChatMessage message) {
        // 1. 验证
        if (message.getSessionId() == null) {
            return error("会话ID不能为空");
        }

        // 2. 纯粹的保存逻辑
        // 不管是前端传来的用户话，还是前端传来的 AI 话，都直接入库
        int rows = robotChatMessageService.insertRobotChatMessage(message);

        // 3. 直接返回结果，不要再在这里掉 Python 了
        return toAjax(rows);
    }

    @RestController
    @RequestMapping("/masterpicar/device")
    public static class RobotDeviceController extends BaseController {

        @Autowired
        private MqttConfig.MqttGateway mqttGateway;

        @Anonymous // 开发阶段建议先开启匿名，生产环境用 @PreAuthorize
        @PostMapping("/execute")
        public AjaxResult execute(@RequestBody RobotControlDto controlDto) {
            try {
                // 1. 将指令列表转为 JSON 字符串
                String payload = JSON.toJSONString(controlDto.getActions());

                // 2. 定义主题，例如：picar/control/1
                String topic = "picar/control/" + controlDto.getDeviceId();

                // 3. 通过 MQTT 发送给树莓派
                mqttGateway.sendToMqtt(payload, topic);

                return success("指令已下发至 MQTT Topic: " + topic);
            } catch (Exception e) {
                return error("指令下发失败: " + e.getMessage());
            }
        }
    }
}
