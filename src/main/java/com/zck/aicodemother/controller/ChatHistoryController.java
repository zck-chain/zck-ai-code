package com.zck.aicodemother.controller;


import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.zck.aicodemother.annotation.AuthCheck;
import com.zck.aicodemother.common.BaseResponse;
import com.zck.aicodemother.common.ResultUtils;
import com.zck.aicodemother.constant.UserConstant;
import com.zck.aicodemother.exception.ErrorCode;
import com.zck.aicodemother.exception.ThrowUtils;
import com.zck.aicodemother.model.dto.chatHistory.ChatHistoryQueryRequest;
import com.zck.aicodemother.model.entity.ChatHistory;
import com.zck.aicodemother.model.entity.User;
import com.zck.aicodemother.service.ChatHistoryService;
import com.zck.aicodemother.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

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


}
