package com.fukun.user.service.impl;

import com.fukun.commons.service.impl.BaseSortTreeCrudServiceImpl;
import com.fukun.user.api.OrgService;
import com.fukun.user.model.po.Org;
import org.springframework.stereotype.Service;

/**
 * 组织架构服务实现
 *
 * @author tangyifei
 * @since 2019-5-24 10:16:39
 */
@Service
public class OrgServiceImpl extends BaseSortTreeCrudServiceImpl<Org, Long> implements OrgService {
}
