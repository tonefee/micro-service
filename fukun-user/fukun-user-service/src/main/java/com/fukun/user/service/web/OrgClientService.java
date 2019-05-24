package com.fukun.user.service.web;

import com.fukun.commons.model.bo.Node;
import com.fukun.commons.service.impl.RestfulCrudServiceImpl;
import com.fukun.user.api.OrgService;
import com.fukun.user.client.OrgClient;
import com.fukun.user.model.po.Org;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织架构服务实现
 *
 * @author tangyifei
 * @since 2019-5-24 10:29:32
 */
@Slf4j
@RestController("orgClientService")
@RequestMapping("/orgs")
public class OrgClientService extends RestfulCrudServiceImpl<Org, Long> implements OrgClient {

    @Autowired
    private OrgService orgService;

    @Override
    public Node<Org> getTreeNode(@PathVariable("treeId") Long treeId) {
        return orgService.selectNodeByParentId(treeId);
    }

}
