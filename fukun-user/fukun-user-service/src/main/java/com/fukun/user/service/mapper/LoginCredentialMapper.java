package com.fukun.user.service.mapper;

import com.fukun.commons.dao.CrudMapper;
import com.fukun.user.model.po.LoginCredential;
import org.springframework.stereotype.Repository;

/**
 * 登录凭证持久层映射接口
 *
 * @author tangyifei
 * @since 2019-5-24 10:20:01
 */
@Repository
public interface LoginCredentialMapper extends CrudMapper<LoginCredential> {
}
