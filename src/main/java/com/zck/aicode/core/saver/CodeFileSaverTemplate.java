package com.zck.aicode.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.zck.aicode.constant.AppConstant;
import com.zck.aicode.exception.BusinessException;
import com.zck.aicode.exception.ErrorCode;
import com.zck.aicode.model.enums.CodeGenTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 抽象代码文件保存器-模板方法模式
 * @author 赵承康
 * @date 2026/1/23
 */
@Slf4j
public abstract class CodeFileSaverTemplate<T> {
    private static final String FILE_SAVE_ROOT_DIR = AppConstant.CODE_OUTPUT_ROOT_DIR;
    public final File saveCode(T result,Long appId){
        //1.验证输入
        validateInput(result);
        //2.构建文件目录
        String baseDirPath = buildUniqueDir(appId);
        //3.保存文件
        saveFiles(baseDirPath,result);
        //4.返回文件对象
        log.info("文件目录是{}",baseDirPath);
        return new File(baseDirPath);
    }

    protected abstract void saveFiles(String baseDirPath, T result) ;

    protected void validateInput(T result) {
        if (result==null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"保存文件失败,解析出来代码是null");
        }
    }

    /**
     * 构建唯一目录路径: tmp/code_output/bizType_雪花ID
     *
     * @return
     */
    protected   String buildUniqueDir(Long appId) {
        if (appId==null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"appId不能为空");

        //1.获取业务类型
        }
        String bizType = getBizType().getValue();
        String uniquerDirName = StrUtil.format("{}_{}", bizType,appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniquerDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    protected abstract CodeGenTypeEnum getBizType();

    /**
     * 把代码写入文件
     * @param dirPath 文件目录
     * @param fileName 文件名称
     * @param content 代码
     */
    protected final void writeToFile(String dirPath,String fileName,String content){
        String filePath=dirPath+File.separator+fileName;
        FileUtil.writeString(content,filePath, StandardCharsets.UTF_8);
    }

}
