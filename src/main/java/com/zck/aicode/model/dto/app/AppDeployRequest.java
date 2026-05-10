package com.zck.aicode.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 赵承康
 * @date 2026/1/28
 */
@Data
public class AppDeployRequest implements Serializable {
    /*
    * 应用id
    * */
    private Long appId;

    private static final Long serialVersionUID = 1L;
}
