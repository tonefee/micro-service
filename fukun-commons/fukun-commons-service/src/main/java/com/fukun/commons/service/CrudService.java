package com.fukun.commons.service;

/**
 * 通用服务类
 *
 * @author tangyifei
 * @since 2019-5-23 15:55:42 PM
 */
public interface CrudService<E, PK> extends
        InsertService<E, PK>,
        UpdateService<E, PK>,
        DeleteService<PK>,
        SelectService<E, PK> {
}
