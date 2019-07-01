package com.fukun.user.client;

import com.fukun.commons.constants.ServerConstants;
import com.fukun.commons.service.RestfulCrudService;
import com.fukun.user.model.po.User;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 暴露的用户相关的restful风格的api
 *
 * @author tangyifei
 * @since 2019-5-24 09:31:13
 */
@FeignClient(value = ServerConstants.USER, path = "users")
public interface UserClient extends RestfulCrudService<User, String> {

}
