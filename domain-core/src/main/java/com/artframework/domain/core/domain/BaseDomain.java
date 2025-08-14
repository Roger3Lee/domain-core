package com.artframework.domain.core.domain;

import cn.hutool.core.annotation.PropIgnore;
import com.artframework.domain.core.constants.SaveState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 基础领域对象
 * 提供领域对象的基本功能和序列化控制
 *
 * @author auto
 * @version v1.0
 */
@Getter
@Setter
@ToString(exclude = "_thisDomain")
@JsonIgnoreProperties(value = {"_thisDomain"}, ignoreUnknown = true)
public class BaseDomain {

    /**
     * 是否已更改标志
     * 用于跟踪对象的修改状态
     */
    @ApiModelProperty(value = "是否已更改", hidden = true)
    private Boolean changed = false;

    /**
     * 当前领域对象的引用
     * 用于内部引用，不参与序列化，防止死循环
     * <p>
     * 注意：此字段使用transient关键字和多个序列化忽略注解
     * 确保在任何序列化场景下都不会被处理
     */
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @PropIgnore
    private transient Object _thisDomain;

    /**
     * 模型保存(新增或修改)后的相关操作
     * 暂时用来处理复杂模型的ref数据
     *
     * @param saveState 保存状态
     */
    public void afterSave(SaveState saveState) {
        // 默认实现为空，子类可以重写此方法
    }

}
