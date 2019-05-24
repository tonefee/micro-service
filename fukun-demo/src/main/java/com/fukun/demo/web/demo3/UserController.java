package com.fukun.demo.web.demo3;

import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.user.client.UserClient;
import com.fukun.user.model.po.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制器
 *
 * @author tangyifei
 * @since 2019-5-24 16:12:18
 */
@Api(value = "用户管理", description = "用户管理")
@ResponseResult
@RestController("demo3UserController")
@RequestMapping("demo3/users")
public class UserController {

    @Resource
    private UserClient userClient;

    @ApiOperation("添加用户")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Validated @RequestBody User user) {
        return userClient.add(user);
    }

    /**
     * 通常系统都是不允许删除用户的（删除也是逻辑删除），当你的userClient使用了RestfulCrudService时，如果不想支持该方法，
     * 就覆盖它，并抛出MethodNotAllowException
     */
    @ApiOperation("删除用户")
    @DeleteMapping("{id}")
    public void addUser(@PathVariable("id") String id) {
        userClient.deleteById(id);
    }
}
