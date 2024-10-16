package com.artframework.domain.core.domain;


import com.artframework.domain.core.constants.SaveState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "_thisDomain")
public class BaseDomain {
    private Boolean changed = false;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private transient Object _thisDomain;

    /**
     * 模型保存(新增或修改)後的相關操作，暫時用來處理複雜模型的ref數據
     */
    public void afterSave(SaveState saveState) {
    }
}
