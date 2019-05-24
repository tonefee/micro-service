package com.fukun.demo.web.demo1;

import com.fukun.commons.model.qo.PageQO;
import com.fukun.commons.model.vo.PageVO;
import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.demo.mapper.UserMapper;
import com.fukun.user.model.po.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户管理控制器
 *
 * @author tangyifei
 * @since 2019-5-24 15:38:14
 */
@ResponseResult
@RestController("demo1UserController")
@RequestMapping("demo1/users")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Validated @RequestBody User user) {
        Date currentDate = new Date();
        user.setCreateTime(currentDate);
        user.setUpdateTime(currentDate);
        userMapper.insert(user);
        return userMapper.selectByPrimaryKey(user.getId());
    }

    @GetMapping
    public PageVO<User> getList(PageQO pageQO) {
        Page<User> page = PageHelper.startPage(pageQO.getPageNum(), pageQO.getPageSize(), pageQO.getOrderBy());
        userMapper.selectAll();
        return PageVO.build(page);
    }
}
