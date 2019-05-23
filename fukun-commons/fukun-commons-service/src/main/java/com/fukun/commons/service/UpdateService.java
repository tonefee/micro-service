package com.fukun.commons.service;

/**
 * 基础更新服务
 *
 * @author tangyifei
 * @since 2019-5-23 15:57:43 PM
 */
public interface UpdateService<E, PK> {

    /**
     * 修改记录信息
     *
     * @param pk     主键
     * @param record 要修改的对象
     * @return 影响记录数
     */
    int updateByPk(PK pk, E record);

    /**
     * 修改记录信息
     *
     * @param pk     主键
     * @param record 要修改的对象
     * @return 影响记录数
     */
    int updateByPkSelective(PK pk, E record);

    /**
     * 保存或修改
     *
     * @param record 要修改的数据
     * @return 影响记录数
     */
    PK saveOrUpdate(E record);

}
