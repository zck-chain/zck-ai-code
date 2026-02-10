package com.zck.aicodemother.service;

import com.mybatisflex.core.service.IService;
import com.zck.aicodemother.model.vo.DiffResult;
import com.zck.aicodemother.model.entity.VersionCode;

import java.util.List;

/**
 * 版本表 服务层。
 *
 * @author 赵承康
 */
public interface VersionCodeService extends IService<VersionCode> {

    VersionCode createVersion(String appId, String codeContent,
                              String commitMessage, long author);

    /**
     * 对比两个版本
     */
    DiffResult compareVersions(String versionId1, String versionId2);

    DiffResult compareWithLatest(String appId, String codeContent);

    List<VersionCode> getVersionList(String appId);

    VersionCode getVersion(String versionId);

    VersionCode rollbackToVersion(String appId, String targetVersionId,
                                  String commitMessage, long author);

    void deleteAppVersions(String appId);
}
