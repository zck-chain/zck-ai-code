package com.zck.aicodemother.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求类，实现了Serializable接口，用于支持序列化操作
 * 使用@Data注解自动生成getter、setter、toString等方法
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}

