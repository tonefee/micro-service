package com.fukun.demo.web.demo5;


import com.fukun.commons.attributes.model.AttributesChange;
import com.fukun.commons.attributes.service.AttributeService;
import com.fukun.commons.web.annotations.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户管理控制器
 *
 * @author tangyifei
 * @since 2019-5-24 16:15:18
 */
@ResponseResult
@RestController("demo4UserAttrController")
@RequestMapping("demo4/users/{userId}/attrs")
public class UserAttrController {

    @Resource
    private AttributeService<String> userAttributeService;

    @PostMapping
    AttributesChange<String> add(@PathVariable("userId") String userId, @RequestBody Map<String, Object> attrMap) {
        return userAttributeService.addAttributes(userId, attrMap);
    }

    @GetMapping
    Map<String, Object> get(@PathVariable("userId") String userId) {
        return userAttributeService.getAttributes(userId);
    }

    @PatchMapping
    AttributesChange<String> patch(@PathVariable("userId") String userId, @RequestBody Map<String, Object> attrMap) {
        return userAttributeService.setAttributes(userId, attrMap);
    }

    @DeleteMapping
    AttributesChange<String> delete(@PathVariable("userId") String userId) {
        return userAttributeService.deleteAttributes(userId);
    }
}
