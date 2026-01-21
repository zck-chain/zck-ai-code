package com.zck.aicodemother.common;

import lombok.Data;

/**
 * 分页请求参数类
 * 用于封装前端传递的分页查询条件，包括当前页码、每页大小、排序字段和排序顺序
 */
@Data  // 使用Lombok自动生成getter、setter等方法
public class PageRequest {

    /**
     * 当前页号
     */
    private int pageNum = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}

