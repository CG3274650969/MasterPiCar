package com.ruoyi.masterpicar.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.utils.SecurityUtils;
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
import com.ruoyi.masterpicar.domain.RobotChatSession;
import com.ruoyi.masterpicar.service.IRobotChatSessionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 对话会话Controller
 * 
 * @author cg
 * @date 2026-01-09
 */
@RestController
@RequestMapping("/masterpicar/session")
public class RobotChatSessionController extends BaseController
{
    @Autowired
    private IRobotChatSessionService robotChatSessionService;

    /**
     * 查询对话会话列表
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:session:list')")
    @GetMapping("/list")
    public TableDataInfo list(RobotChatSession robotChatSession)
    {
        startPage();
        List<RobotChatSession> list = robotChatSessionService.selectRobotChatSessionList(robotChatSession);
        return getDataTable(list);
    }

    /**
     * 导出对话会话列表
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:session:export')")
    @Log(title = "对话会话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RobotChatSession robotChatSession)
    {
        List<RobotChatSession> list = robotChatSessionService.selectRobotChatSessionList(robotChatSession);
        ExcelUtil<RobotChatSession> util = new ExcelUtil<RobotChatSession>(RobotChatSession.class);
        util.exportExcel(response, list, "对话会话数据");
    }

    /**
     * 获取对话会话详细信息
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:session:query')")
    @GetMapping(value = "/{sessionId}")
    public AjaxResult getInfo(@PathVariable("sessionId") Long sessionId)
    {
        return success(robotChatSessionService.selectRobotChatSessionBySessionId(sessionId));
    }

    /**
     * 新增对话会话
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:session:add')")
    @Log(title = "对话会话", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RobotChatSession robotChatSession)
    {
        return toAjax(robotChatSessionService.insertRobotChatSession(robotChatSession));
    }

    /**
     * 修改对话会话
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:session:edit')")
    @Log(title = "对话会话", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RobotChatSession robotChatSession)
    {
        return toAjax(robotChatSessionService.updateRobotChatSession(robotChatSession));
    }

    /**
     * 删除对话会话
     */
    @PreAuthorize("@ss.hasPermi('masterpicar:session:remove')")
    @Log(title = "对话会话", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sessionIds}")
    public AjaxResult remove(@PathVariable Long[] sessionIds)
    {
        return toAjax(robotChatSessionService.deleteRobotChatSessionBySessionIds(sessionIds));
    }

    @Anonymous
    @PostMapping("/start")
    public AjaxResult startSession(@RequestBody RobotChatSession session) {

        session.setUserId(SecurityUtils.getUserId());
        // 如果前端没传 sessionId，创建新会话
        if (session.getSessionId() == null) {
            session.setCreateTime(new Date());
            robotChatSessionService.insertRobotChatSession(session);
        }
        return success(session);
    }



}
