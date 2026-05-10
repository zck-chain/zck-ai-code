package com.zck.aicode.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新应用请求对象
 *
 * @author 赵承康
 */
@Data
public class AppUpdateRequest  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 应用id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用封面
     */
    private String cover;

    /**
     * 优先级
     */
    private Integer priority;
}
