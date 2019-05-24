package com.fukun.user.service.mapper;

import com.fukun.commons.dao.CrudMapper;
import com.fukun.user.model.po.Org;
import org.springframework.stereotype.Repository;

/**
 * 组织架构持久层映射接口
 *
 * @author tangyifei
 * @since 2019-5-24 10:22:38
 */
@Repository
public interface OrgMapper extends CrudMapper<Org> {
}
