package com.fukun.demo.service.user;

import com.fukun.commons.service.CrudService;
import com.fukun.user.model.po.User;

/**
 * 用户服务接口
 *
 * @author tangyifei
 * @since 2019-5-24 15:32:20
 */
public interface UserService extends CrudService<User, String> {

    User getUserCredential(String id);

}
