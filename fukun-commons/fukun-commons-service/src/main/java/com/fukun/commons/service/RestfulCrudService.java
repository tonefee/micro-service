package com.fukun.commons.service;

import com.fukun.commons.model.Model;
import com.fukun.commons.model.qo.PageQO;
import com.fukun.commons.model.vo.PageVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * restful风格的crud服务
 *
 * @author tangyifei
 * @since 2019-5-23 16:03:34 PM
 */
public interface RestfulCrudService<E, PK> {

    /**
     * restful风格的添加操作
     *
     * @param e 待添加的对象
     * @return 添加的对象
     */
    @PostMapping
    E add(@RequestBody E e);

    /**
     * restful风格的删除操作
     *
     * @param id 主键
     * @return 影响的行数
     */
    @DeleteMapping("{id}")
    int deleteById(@PathVariable("id") PK id);

    /**
     * restful风格的部分修改操作
     *
     * @param id     主键
     * @param record 修改的对象
     * @return 修改的对象
     */
    @PatchMapping("{id}")
    E updateByIdSelective(@PathVariable("id") PK id, @RequestBody E record);

    /**
     * restful风格的全部修改操作
     *
     * @param id     主键
     * @param record 修改的对象
     * @return 修改的对象
     */
    @PutMapping("{id}")
    E updateById(@PathVariable("id") PK id, @RequestBody E record);

    /**
     * restful风格的根据主键获取单个对象操作
     *
     * @param id 主键
     * @return 对象的详情
     */
    @GetMapping("{id}")
    E getById(@PathVariable("id") PK id);

    /**
     * restful风格的根据多个主键获取多个对象的操作
     *
     * @param ids 主键列表
     * @return 多个对象
     */
    @PostMapping("_ids")
    List<E> getByIds(@RequestBody Set<PK> ids);

    /**
     * restful风格的分页查询操作
     *
     * @param pageQO
     * @return
     */
    @PostMapping("_search")
    PageVO<E> getPage(@RequestBody PageQO<? extends Model> pageQO);
}
