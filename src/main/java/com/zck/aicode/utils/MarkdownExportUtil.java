package com.zck.aicode.utils;


import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Markdown 文件导出工具类
 */
public class MarkdownExportUtil {

    /**
     * 直接将 Markdown 文本导出为文件下载
     *
     * @param response HTTP 响应对象
     * @param markdownContent 拼接好的 Markdown 内容
     * @param fileName       下载的文件名 (无需包含 .md 后缀，工具类会自动补充)
     * @throws IOException IO 异常
     */
    public static void exportMarkdown(HttpServletResponse response, String markdownContent, String fileName) throws IOException {
        if (response == null || markdownContent == null) {
            throw new IllegalArgumentException("Response 和 Markdown 内容不能为空");
        }

        // 1. 确保文件名以 .md 结尾
        String finalFileName = fileName.endsWith(".md") ? fileName : fileName + ".md";

        // 2. 设置响应头
        // 设置内容类型为 text/markdown，字符集为 UTF-8 (非常重要，防止中文乱码)
        response.setContentType("text/markdown;charset=UTF-8");
        // 设置 Content-Disposition 触发浏览器下载，并对文件名进行 URL 编码解决中文乱码问题
        String encodedFileName = URLEncoder.encode(finalFileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
        // 兼容某些浏览器 (如 Firefox) 的特殊写法
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodedFileName);

        // 3. 写入数据流
        // 使用 try-with-resources 自动关闭流，使用 OutputStreamWriter 指定编码
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.write(markdownContent);
            writer.flush();
        }
    }

    /**
     * Markdown 文档构建器 (简化 Markdown 格式的拼接)
     */
    public static class MarkdownBuilder {

        private final StringBuilder content;

        public MarkdownBuilder() {
            this.content = new StringBuilder();
        }

        /** 添加一级标题 */
        public MarkdownBuilder h1(String text) {
            content.append("# ").append(text).append("\n\n");
            return this;
        }

        /** 添加二级标题 */
        public MarkdownBuilder h2(String text) {
            content.append("## ").append(text).append("\n\n");
            return this;
        }

        /** 添加三级标题 */
        public MarkdownBuilder h3(String text) {
            content.append("### ").append(text).append("\n\n");
            return this;
        }

        /** 添加段落文本 */
        public MarkdownBuilder paragraph(String text) {
            content.append(text).append("\n\n");
            return this;
        }

        /** 添加引用块 (> 文本) */
        public MarkdownBuilder blockquote(String text) {
            content.append("> ").append(text).append("\n\n");
            return this;
        }

        /** 添加分割线 (---) */
        public MarkdownBuilder horizontalRule() {
            content.append("---\n\n");
            return this;
        }

        /** 添加换行 */
        public MarkdownBuilder newLine() {
            content.append("\n");
            return this;
        }

        /** 追加原始 Markdown 字符串 (用于直接塞入 AI 返回的 Markdown 内容) */
        public MarkdownBuilder appendRaw(String rawMarkdown) {
            content.append(rawMarkdown).append("\n\n");
            return this;
        }

        /** 构建并返回最终的 Markdown 字符串 */
        public String build() {
            return content.toString();
        }
    }
}
