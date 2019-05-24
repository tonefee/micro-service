package com.fukun.user.model.po;

import com.fukun.commons.annotations.EnumValue;
import com.fukun.commons.model.po.BaseSortTreePO;
import com.fukun.commons.validator.CreateGroup;
import com.fukun.commons.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 组织架构持久层对象
 *
 * @author tangyifei
 * @since 2019-5-24 09:40:18
 */
@ApiModel("组织架构持久层对象")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Org extends BaseSortTreePO<Long> {

    private static final long serialVersionUID = 2623905517895913619L;

    @ApiModelProperty(value = "主键")
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @ApiModelProperty(value = "组织架构名称")
    @NotBlank(groups = CreateGroup.class)
    @Length(min = 1, max = 64, groups = {CreateGroup.class, UpdateGroup.class})
    private String name;

    @ApiModelProperty(value = "类型")
    @NotBlank(groups = CreateGroup.class)
    @EnumValue(enumClass = TypeEnum.class, enumMethod = "isValidName", groups = {CreateGroup.class, UpdateGroup.class})
    private String type;

    /**
     * 组织架构类型枚举
     */
    public enum TypeEnum {
        /**
         * 公司
         */
        COMPANY,
        /**
         * 部门
         */
        DEPARTMENT,
        /**
         * 组别
         */
        GROUP;

        public static boolean isValidName(String name) {
            for(TypeEnum typeEnum : TypeEnum.values()) {
                if (typeEnum.name().equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

}
