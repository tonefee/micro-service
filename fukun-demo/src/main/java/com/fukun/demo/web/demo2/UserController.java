package com.fukun.demo.web.demo2;

import com.fukun.commons.model.qo.PageQO;
import com.fukun.commons.model.vo.PageVO;
import com.fukun.commons.web.annotations.ResponseResult;
import com.fukun.demo.service.user.UserService;
import com.fukun.user.model.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Spring Boot项目通用功能之《通用Service第一讲》 DEMO
 *
 * @author tangyifei
 * @since 2019-5-24 15:48:53
 */
@ResponseResult
@RestController("demo2UserController")
@RequestMapping("demo2/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Validated @RequestBody User user) {
        String userId = userService.insert(user);
        if (userId != null) {
            return userService.selectByPk(userId);
        }
        return null;
    }

    @GetMapping
    public PageVO<User> getList(PageQO pageQO, User userQO) {
        pageQO.setCondition(userQO);
        return userService.selectPage(pageQO);
    }

    @GetMapping("/{userId}")
    public User getUserCredential(@PathVariable("userId") String userId) {
        return userService.getUserCredential(userId);
    }

}
