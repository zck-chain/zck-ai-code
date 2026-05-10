package com.zck.aicode.core.saver;

import com.zck.aicode.ai.model.MultiFileCodeResult;
import com.zck.aicode.model.enums.CodeGenTypeEnum;

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
