package com.zck.aicodemother.core.saver;

import com.zck.aicodemother.ai.model.MultiFileCodeResult;
import com.zck.aicodemother.model.enums.CodeGenTypeEnum;

/**
 * 多文件代码保存器
 * @author 赵承康
 * @date 2026/1/23
 */
public class MultiFileCodeSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult>{

    @Override
    protected CodeGenTypeEnum getBizType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }
    @Override
    protected void saveFiles(String baseDirPath, MultiFileCodeResult result) {
        writeToFile(baseDirPath,"index.html",result.getHtmlCode());
        writeToFile(baseDirPath,"style.css", result.getCssCode());
        writeToFile(baseDirPath,"script.js", result.getJsCode());
    }
}
