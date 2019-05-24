package com.fukun.demo.mapper;

import com.fukun.commons.dao.CrudMapper;
import com.fukun.user.model.po.User;
import org.springframework.stereotype.Repository;

/**
 * 用户模块基本的CRUD操作Mapper
 *
 * @author tangyifei
 * @since 2019-5-24 15:10:13
 */
@Repository
public interface UserMapper extends CrudMapper<User> {
}
