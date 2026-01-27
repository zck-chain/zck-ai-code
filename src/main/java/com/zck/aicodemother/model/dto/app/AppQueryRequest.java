package com.zck.aicodemother.model.dto.app;

import com.zck.aicodemother.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询应用请求对象
 *
 * @author 赵承康
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppQueryRequest extends PageRequest {
    /**
     * 应用名称
     */
    private String appName;

    /**
     * 代码生成类型
     */
    private String codeGenType;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序
     */
    private String sortOrder;
}
