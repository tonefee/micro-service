package com.fukun.user.service.web;

import com.fukun.commons.exceptions.MethodNotAllowException;
import com.fukun.commons.service.impl.RestfulCrudServiceImpl;
import com.fukun.user.client.UserClient;
import com.fukun.user.model.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户服务实现
 *
 * @author tangyifei
 * @since 2019-5-24 10:30:48
 */
@Slf4j
@RestController("userClientService")
@RequestMapping("/users")
public class UserClientService extends RestfulCrudServiceImpl<User, String> implements UserClient {

    @Override
    public int deleteById(@PathVariable("id") String id) {
        throw new MethodNotAllowException();
    }

}
