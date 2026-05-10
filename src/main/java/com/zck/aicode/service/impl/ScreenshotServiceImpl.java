package com.zck.aicode.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.zck.aicode.exception.ErrorCode;
import com.zck.aicode.exception.ThrowUtils;
import com.zck.aicode.manager.CosManager;
import com.zck.aicode.service.ScreenshotService;
import com.zck.aicode.utils.WebScreenshotUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author 赵承康
 * @date 2026/3/9
 */
@Service
@Slf4j
public class ScreenshotServiceImpl implements ScreenshotService {
    @Resource
    private CosManager cosManager;

    @Override
    public String generateAndUploadScreenshot(String webUrl){
        //校验参数
        ThrowUtils.throwIf(StrUtil.isBlank(webUrl), ErrorCode.PARAMS_ERROR, "url不能为空");
        log.info("开始生成截图，url:{}", webUrl);
        //1.生成本地截图
        String localScreenshotPath = WebScreenshotUtils.saveWebPageScreenShot(webUrl);
        ThrowUtils.throwIf(StrUtil.isBlank(localScreenshotPath),ErrorCode.OPERATION_ERROR,"本地截图失败");
        try {
            //2.上传到对象存储
            String cosURL = uploadScreenshotToCos(localScreenshotPath);
            ThrowUtils.throwIf(StrUtil.isBlank(cosURL),ErrorCode.OPERATION_ERROR,"截图上传对象存储失败");
            log.info("截图上传对象存储成功，cosURL:{}", cosURL);
            return cosURL;
        }finally {
            //3.删除本地截图
            cleanupLocalFile(localScreenshotPath);
        }
    }

    private void cleanupLocalFile(String localScreenshotPath) {
        File localFile = new File(localScreenshotPath);
        if (localFile.exists()) {
            File parentFile = localFile.getParentFile();
            FileUtil.del(parentFile);
            log.info("本地截图文件已清除: {}",localScreenshotPath);
        }
    }

    /**
     * 上传截图到对象存储
     * @param localScreenshotPath
     * @return
     */
    private String uploadScreenshotToCos(String localScreenshotPath) {
        if (StrUtil.isBlank(localScreenshotPath)) {
            return null;
        }
        File screenshotFile = new File(localScreenshotPath);
        if (!screenshotFile.exists()) {
            log.error("截图文件不存在:{}",localScreenshotPath);
            return null;
        }
        //生成cos对象键
        String fileName=UUID.randomUUID().toString().substring(0, 8) + "_compressed.jpg";
        String cosKey=generateScreenshotKey(fileName);
        return cosManager.uploadFile(cosKey, screenshotFile);
    }

    /**
     * 生成截图对象存储键
     * @param fileName
     * @return
     */
    private String generateScreenshotKey(String fileName) {
        String dataPath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("/screenshot/%s/%s", dataPath, fileName);
    }


}
