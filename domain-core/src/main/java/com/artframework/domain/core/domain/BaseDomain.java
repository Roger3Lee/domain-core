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
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseDomain {
    
    /**
     * 是否已更改标志
     * 用于跟踪对象的修改状态
     */
    @ApiModelProperty(value = "是否已更改", hidden = true)
    @JsonIgnore
    private Boolean changed = false;

    /**
     * 当前领域对象的引用
     * 用于内部引用，不参与序列化，防止死循环
     * 
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

    /**
     * 获取当前领域对象的引用
     * 此方法用于内部引用，不参与序列化
     * 
     * @return 当前领域对象引用
     */
    @JsonIgnore
    public Object getThisDomain() {
        return _thisDomain;
    }

    /**
     * 设置当前领域对象的引用
     * 此方法用于内部引用，不参与序列化
     * 
     * @param thisDomain 要设置的领域对象引用
     */
    @JsonIgnore
    public void setThisDomain(Object thisDomain) {
        this._thisDomain = thisDomain;
    }

    /**
     * 检查对象是否已更改
     * 
     * @return true表示已更改，false表示未更改
     */
    public boolean isChanged() {
        return changed != null && changed;
    }

    /**
     * 标记对象为已更改状态
     */
    public void markAsChanged() {
        this.changed = true;
    }

    /**
     * 重置更改状态
     */
    public void resetChanged() {
        this.changed = false;
    }

    /**
     * 深度克隆对象
     * 子类可以重写此方法提供自定义的克隆逻辑
     * 
     * @return 克隆后的对象
     */
    @JsonIgnore
    public BaseDomain clone() {
        try {
            return (BaseDomain) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("克隆失败", e);
        }
    }
}
