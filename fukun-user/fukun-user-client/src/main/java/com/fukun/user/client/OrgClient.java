package com.fukun.user.client;

import com.fukun.commons.constants.ServerConstants;
import com.fukun.commons.model.bo.Node;
import com.fukun.commons.service.RestfulCrudService;
import com.fukun.user.model.po.Org;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 暴露的组织架构相关的restful风格的api
 *
 * @author tangyifei
 * @since 2019-5-24 09:30:38
 */
@FeignClient(value = ServerConstants.USER, path = "orgs")
public interface OrgClient extends RestfulCrudService<Org, Long> {

    /**
     * 根据树的主键获取相关的架构树
     *
     * @param treeId 树主键
     * @return 组织架构的相关的节点对象
     */
    @GetMapping("tree/{treeId}")
    Node<Org> getTreeNode(@PathVariable("treeId") Long treeId);

}
