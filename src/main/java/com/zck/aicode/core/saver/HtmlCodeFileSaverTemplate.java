package com.zck.aicode.core.saver;

import cn.hutool.core.util.StrUtil;
import com.zck.aicode.ai.model.HtmlCodeResult;
import com.zck.aicode.exception.BusinessException;
import com.zck.aicode.exception.ErrorCode;
import com.zck.aicode.model.enums.CodeGenTypeEnum;

/**
 * 单文件保存器
 * @author 赵承康
 * @date 2026/1/23
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult>{
    @Override
    protected CodeGenTypeEnum getBizType() {
        return CodeGenTypeEnum.HTML;
    }
    @Override
    protected void saveFiles(String baseDirPath, HtmlCodeResult result) {
        writeToFile(baseDirPath, "index.html",result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"HTML代码内容不能为空");
        }
    }
}
