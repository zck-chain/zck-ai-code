package com.zck.aicodemother.core.parser;

import com.zck.aicodemother.exception.BusinessException;
import com.zck.aicodemother.exception.ErrorCode;
import com.zck.aicodemother.model.enums.CodeGenTypeEnum;

import javax.swing.text.html.HTML;

/**
 * 代码解析执行器
 * @author 赵承康
 * @date 2026/1/23
 */
public class CodeParserExecutor {
    private static final HtmlCodeParser htmlCodeParser=new HtmlCodeParser();
    private static final MultiFileCodeParser multiFileCodeParser=new MultiFileCodeParser();
    public static Object execute(String codeContent, CodeGenTypeEnum codeGenType){
        return switch (codeGenType){
            case HTML -> htmlCodeParser.parse(codeContent);
            case MULTI_FILE -> multiFileCodeParser.parse(codeContent);
            default -> {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"代码解析失败!");
            }
        };
    }
}
