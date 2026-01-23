package com.zck.aicodemother.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zck.aicodemother.ai.model.HtmlCodeResult;
import com.zck.aicodemother.ai.model.MultiFileCodeResult;
import com.zck.aicodemother.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author 赵承康
 * @date 2026/1/22
 */
@Deprecated
public class CodeFileSaver {
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";


    /**
     * 保存htmlCodeResult
     * @param htmlCodeResult 
     * @return
     */
    public static File saveHtmlCodeResult(HtmlCodeResult htmlCodeResult){
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
        writeToFile(baseDirPath,"index.html",htmlCodeResult.getHtmlCode());
        return new File(baseDirPath);
    }
    
    public static File saveMultiFileCodeResult(MultiFileCodeResult multiFileCodeResult){
        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        writeToFile(baseDirPath,"index.html",multiFileCodeResult.getHtmlCode());
        writeToFile(baseDirPath,"style.css", multiFileCodeResult.getCssCode());
        writeToFile(baseDirPath,"script.js", multiFileCodeResult.getJsCode());
        return new File(baseDirPath);
    }
    /**
     * 构建唯一目录路径: tmp/code_output/bizType_雪花ID
     *
     * @param bizType
     * @return
     */
    private static String buildUniqueDir(String bizType) {
        String uniquerDirName = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniquerDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 把代码写入文件
     * @param dirPath 文件目录 
     * @param fileName 文件名称
     * @param content 代码
     */
    private static void writeToFile(String dirPath,String fileName,String content){
        String filePath=dirPath+File.separator+fileName;
        FileUtil.writeString(content,filePath, StandardCharsets.UTF_8);
                
    }

}
