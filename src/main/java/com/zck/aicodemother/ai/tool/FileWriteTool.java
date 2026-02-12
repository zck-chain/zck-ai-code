package com.zck.aicodemother.ai.tool;

import com.zck.aicodemother.constant.AppConstant;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 文件写入工具
 * 支持AI通过工具调用的方式写入文件
 * @author 赵承康
 * @date 2026/2/12
 */
@Slf4j
public class FileWriteTool {
    @Tool("写入文件到指定路径")
    public String writeFile(@P("文件的相对路径")String relativeFilePath, @P("文件内容")String content, @ToolMemoryId Long appId) throws IOException {
        Path path = Paths.get(relativeFilePath);
        if (!path.isAbsolute()) {
           //相对路径处理,创建基于appId的项目目录
           String projectDirName="vue_project_"+appId;
           Path projectRoot = Paths.get(AppConstant.CODE_OUTPUT_ROOT_DIR, projectDirName);
           path=projectRoot.resolve(relativeFilePath);
        }
        //创建父目录（如果不存在）
        try {
            Path parentDir = path.getParent();
            if (parentDir!=null) {
                Files.createDirectories(parentDir);
            }
        }catch (IOException e){
             String errorMessage="文件写入失败:"+path.toAbsolutePath()+"，错误信息:"+e.getMessage();
        }

        //写入文件
        Files.write(path,content.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
        log.info("文件写入成功:{}",path.toAbsolutePath());
        //注意返回相对路径,不能让AI把文件绝对路径返回给用户
        return "文件写入成功:"+relativeFilePath;
    }
}
