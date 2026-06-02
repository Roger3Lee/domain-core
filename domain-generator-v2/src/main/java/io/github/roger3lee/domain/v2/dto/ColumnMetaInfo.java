package io.github.roger3lee.domain.v2.dto;

import lombok.Data;

/**
 * Column DTO for FreeMarker template rendering.
 */
@Data
public class ColumnMetaInfo {
    private String name;
    private String type;
    private String comment;
    private Boolean key = false;
    private Boolean inherit = false;
    private String sourceValue;
}
