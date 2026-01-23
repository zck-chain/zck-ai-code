package com.zck.aicodemother.core.saver;

import com.zck.aicodemother.ai.model.HtmlCodeResult;
import com.zck.aicodemother.ai.model.MultiFileCodeResult;
import com.zck.aicodemother.exception.BusinessException;
import com.zck.aicodemother.exception.ErrorCode;
import com.zck.aicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * 代码文件保存执行器
 * @author 赵承康
 * @date 2026/1/23
 */
public class CodeFileSaverExecutor {
    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaverTemplate = new HtmlCodeFileSaverTemplate();
    private static final MultiFileCodeSaverTemplate multiFileCodeSaverTemplate = new MultiFileCodeSaverTemplate();

    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenTypeEnum) {
        return switch (codeGenTypeEnum) {
            case HTML -> htmlCodeFileSaverTemplate.saveCode((HtmlCodeResult) codeResult);
            case MULTI_FILE -> multiFileCodeSaverTemplate.saveCode((MultiFileCodeResult) codeResult);
            default -> {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的代码生成类型");
            }
        };
    }

}
