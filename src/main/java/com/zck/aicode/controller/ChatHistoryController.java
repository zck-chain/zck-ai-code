package com.zck.aicode.controller;


import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.zck.aicode.annotation.AuthCheck;
import com.zck.aicode.common.BaseResponse;
import com.zck.aicode.common.ResultUtils;
import com.zck.aicode.constant.UserConstant;
import com.zck.aicode.exception.ErrorCode;
import com.zck.aicode.exception.ThrowUtils;
import com.zck.aicode.model.dto.chatHistory.ChatHistoryQueryRequest;
import com.zck.aicode.model.entity.ChatHistory;
import com.zck.aicode.model.entity.User;
import com.zck.aicode.model.enums.ChatHistoryMessageTypeEnum;
import com.zck.aicode.service.ChatHistoryService;
import com.zck.aicode.service.UserService;
import com.zck.aicode.utils.MarkdownExportUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 对话历史 控制层。
 *
 * @author 赵承康
 */
@RestController
@RequestMapping("/chatHistory")
@Tag(name = "ChatHistoryController", description = "提供对话历史查询、管理等功能")
public class ChatHistoryController {

    @Resource
    private ChatHistoryService chatHistoryService;
    @Resource
    private UserService userService;

    /**
     * 分页查询某个应用的对话历史（游标查询）
     *
     * @param appId          应用ID
     * @param pageSize       页面大小
     * @param lastCreateTime 最后一条记录的创建时间
     * @param request        请求
     * @return 对话历史分页
     */
    @GetMapping("/app/{appId}")
    @Operation(summary = "查询应用对话历史", description = "分页查询某个应用的对话历史，支持游标查询")
    public BaseResponse<Page<ChatHistory>> listAppChatHistory(@Parameter(description = "应用ID", required = true) @PathVariable Long appId,
                                                              @Parameter(description = "页面大小", required = false) @RequestParam(defaultValue = "10") int pageSize,
                                                              @Parameter(description = "最后一条记录的创建时间", required = false) @RequestParam(required = false) LocalDateTime lastCreateTime,
                                                              HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Page<ChatHistory> result = chatHistoryService.listAppChatHistoryByPage(appId, pageSize, lastCreateTime, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 管理员分页查询所有对话历史
     *
     * @param chatHistoryQueryRequest 查询请求
     * @return 对话历史分页
     */
    @PostMapping("/admin/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Operation(summary = "管理员查询所有对话历史", description = "管理员分页查询所有对话历史记录")
    public BaseResponse<Page<ChatHistory>> listAllChatHistoryByPageForAdmin(@RequestBody ChatHistoryQueryRequest chatHistoryQueryRequest) {
        ThrowUtils.throwIf(chatHistoryQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long pageNum = chatHistoryQueryRequest.getPageNum();
        long pageSize = chatHistoryQueryRequest.getPageSize();
        // 查询数据
        QueryWrapper queryWrapper = chatHistoryService.getQueryWrapper(chatHistoryQueryRequest);
        Page<ChatHistory> result = chatHistoryService.page(Page.of(pageNum, pageSize), queryWrapper);
        return ResultUtils.success(result);
    }

    @PostMapping("/export/markdown")
    @Operation(summary = "导出选中的对话记录为Markdown")
    public void exportChatHistoryToMarkdown(
            @Parameter(description = "选中的对话记录ID列表", required = true) @RequestBody List<Long> ids,
            HttpServletResponse response) throws IOException {

        // 1. 参数校验
        ThrowUtils.throwIf(ids == null || ids.isEmpty(), ErrorCode.PARAMS_ERROR, "请至少选择一条记录");

        // 2. 批量查询记录
        List<ChatHistory> historyList = chatHistoryService.listByIds(ids);
        ThrowUtils.throwIf(historyList == null || historyList.isEmpty(), ErrorCode.NOT_FOUND_ERROR, "未找到对话记录");

        // 3. 构建 Markdown
        MarkdownExportUtil.MarkdownBuilder builder = new MarkdownExportUtil.MarkdownBuilder();
        builder.h1("对话记录导出").horizontalRule();

        for (ChatHistory history : historyList) {
            String messageType = history.getMessageType();

            // 核心点：结合你提供的枚举 ChatHistoryMessageTypeEnum 判断角色
            if (ChatHistoryMessageTypeEnum.USER.getValue().equals(messageType)) {
                builder.h3("🧑 用户");
            } else if (ChatHistoryMessageTypeEnum.AI.getValue().equals(messageType)) {
                builder.h3("🤖 AI 助手");
            } else {
                builder.h3("系统");
            }

            builder.appendRaw(history.getMessage()).horizontalRule();
        }

        // 4. 执行导出下载
        MarkdownExportUtil.exportMarkdown(response, builder.build(), "对话记录_选中导出");
    }

}
