package com.zck.aicodemother.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.zck.aicodemother.model.dto.chatHistory.ChatHistoryQueryRequest;
import com.zck.aicodemother.model.entity.App;
import com.zck.aicodemother.model.entity.ChatHistory;
import com.zck.aicodemother.model.entity.User;

import java.time.LocalDateTime;

/**
 * @author 赵承康
 * @date 2026/2/5
 */
public interface ChatHistoryService extends IService<ChatHistory> {
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    boolean deleteByAppId(Long appId);

    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);

    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);
}
