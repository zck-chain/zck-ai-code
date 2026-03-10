package com.zck.aicodemother.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author 赵承康
 * @date 2026/3/10
 */
public interface ProjectDownloadService {
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);
}
