package com.fukun.demo.web.demo4;


import com.fukun.commons.model.bo.Node;
import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.user.client.OrgClient;
import com.fukun.user.model.po.Org;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户管理控制器
 *
 * @author tangyifei
 * @since 2019-5-24 16:13:44
 */
@ResponseResult
@RestController("demo4OrgController")
@RequestMapping("demo4/orgs")
public class OrgController {

    @Resource
    private OrgClient orgClient;

    @PostMapping
    Org add(@RequestBody Org org) {
        Org dbOrg = orgClient.add(org);
        return dbOrg;
    }

    @PatchMapping
    Org patch(@PathVariable("id") Long id, @RequestBody Org org) {
        Org dbOrg = orgClient.updateByIdSelective(id, org);
        return dbOrg;
    }

    @GetMapping("tree/{treeId}")
    Node<Org> getTreeNode(@PathVariable("treeId") Long treeId) {
        Node<Org> tree = orgClient.getTreeNode(treeId);
        return tree;
    }

}
