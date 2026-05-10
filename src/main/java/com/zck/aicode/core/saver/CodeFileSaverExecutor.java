package com.zck.aicode.core.saver;

import com.zck.aicode.ai.model.HtmlCodeResult;
import com.zck.aicode.ai.model.MultiFileCodeResult;
import com.zck.aicode.exception.BusinessException;
import com.zck.aicode.exception.ErrorCode;
import com.zck.aicode.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * 代码文件保存执行器
 * @author 赵承康
 * @date 2026/1/23
 */
public class CodeFileSaverExecutor {
    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaverTemplate = new HtmlCodeFileSaverTemplate();
    private static final MultiFileCodeSaverTemplate multiFileCodeSaverTemplate = new MultiFileCodeSaverTemplate();

    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenTypeEnum,Long appId) {
        return switch (codeGenTypeEnum) {
            case HTML -> htmlCodeFileSaverTemplate.saveCode((HtmlCodeResult) codeResult,appId);
            case MULTI_FILE -> multiFileCodeSaverTemplate.saveCode((MultiFileCodeResult) codeResult,appId);
            default -> {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的代码生成类型");
            }
        };
    }

}
