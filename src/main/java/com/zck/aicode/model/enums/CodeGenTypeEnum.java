package com.zck.aicode.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * @author 赵承康
 * @date 2026/1/22
 */
@Getter
public enum CodeGenTypeEnum {
    HTML("原生HTML模式","HTML"),
    MULTI_FILE("原生多文件模式","MULTI_FILE"),
    VUE_PROJECT("Vue工程模式","VUE_PROJECT");

    private final String text;
    private final String value;

    CodeGenTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static CodeGenTypeEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (CodeGenTypeEnum codeGenTypeEnum : CodeGenTypeEnum.values()) {
            if (codeGenTypeEnum.getValue().equals(value)) {
                return codeGenTypeEnum;
            }
        }
        return null;
    }
}
