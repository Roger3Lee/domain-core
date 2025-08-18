package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 统一API响应
 * 
 * @author auto
 * @version v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "统一API响应")
public class ApiResponse<T> {

  @ApiModelProperty(value = "响应码，0表示成功，非0表示失败")
  private String code;

  @ApiModelProperty(value = "响应消息")
  private String message;

  @ApiModelProperty(value = "响应数据")
  private T data;

  @ApiModelProperty(value = "是否成功")
  private Boolean success;

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>("0", "操作成功", data, true);
  }

  public static <T> ApiResponse<T> success() {
    return new ApiResponse<>("0", "操作成功", null, true);
  }

  public static <T> ApiResponse<T> fail(String message) {
    return new ApiResponse<>("500", message, null, false);
  }

  public static <T> ApiResponse<T> fail(String code, String message) {
    return new ApiResponse<>(code, message, null, false);
  }
}