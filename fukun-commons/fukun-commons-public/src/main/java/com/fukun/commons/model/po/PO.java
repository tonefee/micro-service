package com.fukun.commons.model.po;


import com.fukun.commons.model.Model;

import java.util.Date;

/**
 * 生成相关的数据库的值，比如生成主键、创建时间、修改时间
 *
 * @param <PK>
 * @author tangyifei
 * @since 2019-5-23 11:30:11 AM
 */
public interface PO<PK> extends Model {

    /**
     * 获取主键
     *
     * @return 数据类型
     */
    PK getId();

    /**
     * 设置主键
     *
     * @param id 主键
     */
    void setId(PK id);

    /**
     * 获取创建时间
     *
     * @return 时间类型
     */
    Date getCreateTime();

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    void setCreateTime(Date createTime);

    /**
     * 获取更新时间
     *
     * @return 时间类型
     */
    Date getUpdateTime();

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    void setUpdateTime(Date updateTime);
}
