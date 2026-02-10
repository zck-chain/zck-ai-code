package com.zck.aicodemother.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 版本表 实体类。
 *
 * @author 赵承康
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("version_codes")
public class VersionCode implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private long id;

    /**
     * 应用Id
     */
    @Column("appId")
    private long appId;

    /**
     * 版本
     */
    @Column("version")
    private String version;

    /**
     * 提交说明
     */
    @Column("commitMessage")
    private String commitMessage;

    /**
     * 作者
     */
    @Column("author")
    private long author;

    /**
     * 代码内容
     */
    @Column("codeContent")
    private String codeContent;

    /**
     * 父版本
     */
    @Column("parentVersionId")
    private long parentVersionId;

    /**
     * 文件格式
     */
    @Column("fileStructure")
    private String fileStructure;

    /**
     * 创建时间
     */
    @Column("creatorTime")
    private LocalDateTime creatorTime;

    /**
     * 更新时间
     */
    @Column("updateTime")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @Column(value = "isDelete", isLogicDelete = true)
    private Integer isDelete;

}
