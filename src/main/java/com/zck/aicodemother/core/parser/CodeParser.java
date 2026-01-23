package com.zck.aicodemother.core.parser;

/**
 * 代码解析器
 * @author 赵承康
 * @date 2026/1/23
 */
public interface CodeParser<T> {
    /**
     * 解析代码
     * @param codeContent 代码
     * @return 解析结果
     */
    T parse(String codeContent);

}
