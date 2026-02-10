package com.zck.aicodemother.model.vo;
import lombok.Data;
import java.util.List;
/**
 * @author 赵承康
 * @date 2026/2/10
 */

@Data
public class DiffResult {
    private String fromVersion;    // 源版本
    private String toVersion;      // 目标版本
    private List<CodeChange> changes;  // 变更列表
    private DiffStatistics statistics; // 统计信息

    @Data
    public static class CodeChange {
        private ChangeType type;           // 变更类型
        private int originalLineNum;       // 原始行号
        private int revisedLineNum;        // 新行号
        private List<String> originalLines; // 原始代码行
        private List<String> revisedLines;  // 新代码行
        private String changeContext;     // 变更上下文
    }

    @Data
    public static class DiffStatistics {
        private int addedLines;      // 新增行数
        private int deletedLines;    // 删除行数
        private int modifiedLines;   // 修改行数
        private int totalChanges;    // 总变更数
        private double similarity;   // 相似度百分比
    }

    public enum ChangeType {
        ADD,        // 新增
        DELETE,     // 删除
        MODIFY,     // 修改
        EQUAL       // 无变化
    }
}
