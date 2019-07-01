package com.fukun.user.client;

import com.fukun.commons.constants.ServerConstants;
import com.fukun.commons.service.RestfulCrudService;
import com.fukun.user.model.po.LoginCredential;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 暴露的用户登录验证相关的restful风格的api
 *
 * @author tangyifei
 * @since 2019-5-24 09:28:58
 */
@FeignClient(value = ServerConstants.USER, path = "login-credentials")
public interface LoginCredentialClient extends RestfulCrudService<LoginCredential, Long> {

    /**
     * 获取单个登录凭证相关的信息
     *
     * @param account 账号
     * @param type    类型
     * @return 登录凭证
     */
    @GetMapping("_by-account-type")
    LoginCredential getLoginCredential(@RequestParam("account") String account, @RequestParam("type") String type);

    /**
     * 获取多个登录凭证信息列表
     *
     * @param account 账号
     * @param type    类型
     * @return 登录凭证信息列表
     */
    @GetMapping("_by-account-types")
    List<LoginCredential> getLoginCredentialList(@RequestParam("account") String account, @RequestParam("type") List<String> type);

}
