package com.zck.aicodemother.service.impl;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zck.aicodemother.mapper.VersionCodeMapper;
import com.zck.aicodemother.model.vo.DiffResult;
import com.zck.aicodemother.model.entity.VersionCode;
import com.zck.aicodemother.service.VersionCodeService;
import difflib.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
@Slf4j
public class VersionCodeServiceImpl extends ServiceImpl<VersionCodeMapper, VersionCode> implements VersionCodeService {

    // 存储应用版本
    private final Map<String, List<VersionCode>> appVersionMap = new ConcurrentHashMap<>();
    // 版本号映射
    private final Map<String, VersionCode> versionIdMap = new ConcurrentHashMap<>();
    // 应用当前版本
    private final Map<String, String> appCurrentVersion = new ConcurrentHashMap<>();


    /**
     * 创建新版本
     */
    @Override
    public VersionCode createVersion(String appId, String codeContent,
                                     String commitMessage, long author) {
        List<VersionCode> versions = appVersionMap
                .computeIfAbsent(appId, k -> new ArrayList<>());

        // 获取当前版本
        VersionCode currentVersion = null;
        if (!versions.isEmpty()) {
            currentVersion = versions.get(versions.size() - 1);
        }

        // 生成新版本号
        String newVersion = generateNextVersion(
                currentVersion != null ? currentVersion.getVersion() : null
        );

        // 创建新版本
        VersionCode newVersionCode = VersionCode.builder()
                .appId(Long.valueOf(appId))
                .version(newVersion)
                .codeContent(codeContent)
                .commitMessage(commitMessage)
                .author(author)
                .parentVersionId(currentVersion != null ? currentVersion.getId() : 0L)
                .build();
        this.save(newVersionCode);
        String id = String.valueOf(newVersionCode.getId());
        // 保存版本
        versions.add(newVersionCode);
        versionIdMap.put(id, newVersionCode);
        appCurrentVersion.put(appId, id);
        versions.addLast(newVersionCode);
        appVersionMap.put(appId,versions);
        return newVersionCode;
    }

    /**
     * 对比两个版本
     */
    @Override
    public DiffResult compareVersions(String versionId1, String versionId2) {
        VersionCode v1 = versionIdMap.get(versionId1);
        VersionCode v2 = versionIdMap.get(versionId2);

        if (v1 == null || v2 == null) {
            throw new IllegalArgumentException("版本不存在: " +
                    (v1 == null ? versionId1 : versionId2));
        }

        return compareCodes(v1.getCodeContent(), v2.getCodeContent(),
                v1.getVersion(), v2.getVersion());
    }

    /**
     * 对比最新版本
     */
    @Override
    public DiffResult compareWithLatest(String appId, String codeContent) {
        String currentVersionId = appCurrentVersion.get(appId);
        if (currentVersionId == null) {
            throw new IllegalArgumentException("应用不存在: " + appId);
        }

        VersionCode currentVersion = versionIdMap.get(currentVersionId);
        return compareCodes(currentVersion.getCodeContent(), codeContent,
                currentVersion.getVersion(), "current");
    }

    /**
     * 获取版本列表
     */
    @Override
    public List<VersionCode> getVersionList(String appId) {
        List<VersionCode> versions = appVersionMap.get(appId);
        return versions != null ? new ArrayList<>(versions) : new ArrayList<>();
    }

    /**
     * 获取指定版本
     */
    @Override
    public VersionCode getVersion(String versionId) {
        return versionIdMap.get(versionId);
    }

    /**
     * 回滚到指定版本
     */
    @Override
    public VersionCode rollbackToVersion(String appId, String targetVersionId,
                                         String commitMessage, long author) {
        VersionCode targetVersion = versionIdMap.get(targetVersionId);
        if (targetVersion == null) {
            throw new IllegalArgumentException("目标版本不存在: " + targetVersionId);
        }

        if (!String.valueOf(targetVersion.getAppId()).equals(appId)) {
            throw new IllegalArgumentException("版本不属于此应用");
        }

        // 创建回滚版本
        return createVersion(appId, targetVersion.getCodeContent(),
                "回滚到版本: " + targetVersion.getVersion() +
                        (commitMessage != null ? " - " + commitMessage : ""),
                author);
    }

    /**
     * 对比代码生成差异结果
     */
    private DiffResult compareCodes(String oldCode, String newCode,
                                    String fromVersion, String toVersion) {
        List<String> oldLines = Arrays.asList(oldCode.split("\n", -1));
        List<String> newLines = Arrays.asList(newCode.split("\n", -1));

        // 计算差异
        Patch<String> patch = DiffUtils.diff(oldLines, newLines);
        List<Delta<String>> deltas = patch.getDeltas();

        // 转换为自定义格式
        List<DiffResult.CodeChange> changes = new ArrayList<>();
        DiffResult.DiffStatistics stats = new DiffResult.DiffStatistics();

        for (Delta<String> delta : deltas) {
            DiffResult.CodeChange change = new DiffResult.CodeChange();

            // 设置变更类型
            if (delta.getType() == Delta.TYPE.INSERT) {
                change.setType(DiffResult.ChangeType.ADD);
                stats.setAddedLines(stats.getAddedLines() + delta.getRevised().size());
            } else if (delta.getType() == Delta.TYPE.DELETE) {
                change.setType(DiffResult.ChangeType.DELETE);
                stats.setDeletedLines(stats.getDeletedLines() + delta.getOriginal().size());
            } else if (delta.getType() == Delta.TYPE.CHANGE) {
                change.setType(DiffResult.ChangeType.MODIFY);
                stats.setModifiedLines(stats.getModifiedLines() +
                        Math.max(delta.getOriginal().size(), delta.getRevised().size()));
            }

            // 设置行号
            change.setOriginalLineNum(delta.getOriginal().getPosition() + 1);
            change.setRevisedLineNum(delta.getRevised().getPosition() + 1);

            // 设置代码行
            change.setOriginalLines(new ArrayList<>(delta.getOriginal().getLines()));
            change.setRevisedLines(new ArrayList<>(delta.getRevised().getLines()));

            // 添加上下文
            change.setChangeContext(getChangeContext(oldLines, newLines, delta));

            changes.add(change);
        }

        // 计算统计
        stats.setTotalChanges(changes.size());
        stats.setSimilarity(calculateSimilarity(oldCode, newCode));

        // 构建结果
        DiffResult result = new DiffResult();
        result.setFromVersion(fromVersion);
        result.setToVersion(toVersion);
        result.setChanges(changes);
        result.setStatistics(stats);

        return result;
    }

    /**
     * 获取变更上下文
     */
    private String getChangeContext(List<String> oldLines, List<String> newLines,
                                    Delta<String> delta) {
        int contextLines = 2;  // 上下文行数

        int startOld = Math.max(0, delta.getOriginal().getPosition() - contextLines);
        int endOld = Math.min(oldLines.size(),
                delta.getOriginal().getPosition() + delta.getOriginal().size() + contextLines);

        int startNew = Math.max(0, delta.getRevised().getPosition() - contextLines);
        int endNew = Math.min(newLines.size(),
                delta.getRevised().getPosition() + delta.getRevised().size() + contextLines);

        String context = "上下文变更:\n" +
                "原始代码行 " + (startOld + 1) + "-" + endOld +
                "\n" +
                "新代码行 " + (startNew + 1) + "-" + endNew;

        return context;
    }

    /**
     * 计算代码相似度
     */
    private double calculateSimilarity(String code1, String code2) {
        if (code1.isEmpty() && code2.isEmpty()) return 1.0;
        if (code1.isEmpty() || code2.isEmpty()) return 0.0;

        List<String> lines1 = Arrays.asList(code1.split("\n"));
        List<String> lines2 = Arrays.asList(code2.split("\n"));

        // 使用最长公共子序列计算相似度
        int lcs = longestCommonSubsequence(lines1, lines2);
        int maxLength = Math.max(lines1.size(), lines2.size());

        return (double) lcs / maxLength;
    }

    /**
     * 最长公共子序列算法
     */
    private int longestCommonSubsequence(List<String> list1, List<String> list2) {
        int m = list1.size();
        int n = list2.size();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (list1.get(i - 1).equals(list2.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[m][n];
    }

    /**
     * 生成下一个版本号
     */
    private String generateNextVersion(String currentVersion) {
        if (currentVersion == null) {
            return "v1.0.0";
        }

        // 匹配 v1.2.3 格式
        Pattern pattern = Pattern.compile("v(\\d+)\\.(\\d+)\\.(\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(currentVersion);

        if (matcher.find()) {
            int major = Integer.parseInt(matcher.group(1));
            int minor = Integer.parseInt(matcher.group(2));
            int patch = Integer.parseInt(matcher.group(3)) + 1;

            return String.format("v%d.%d.%d", major, minor, patch);
        }

        throw new IllegalArgumentException("无效的版本格式: " + currentVersion);
    }

    /**
     * 删除应用的所有版本
     */
    @Override
    public void deleteAppVersions(String appId) {
        List<VersionCode> versions = appVersionMap.remove(appId);
        if (versions != null) {
            for (VersionCode version : versions) {
                versionIdMap.remove(version.getId());
            }
        }
        appCurrentVersion.remove(appId);
    }
}