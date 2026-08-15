package io.github.roger3lee.domain.core.constants;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/18
 **/
public enum Op {
    IN("IN"),
    NOT_IN("NOTIN"),
    LIKE("LIKE"),
    NOT_LIKE("NOT_LIKE"),
    LIKE_LEFT("LIKE_LEFT"),

    LIKE_RIGHT("LIKE_RIGHT"),
    EQ(StringPool.EQUALS),
    NE("<>"),
    GT(StringPool.RIGHT_CHEV),
    GE(">="),
    LT(StringPool.LEFT_CHEV),
    LE("<="),

    ISNULL("ISNULL"),
    NOTNULL("NOTNULL"),

    BETWEEN("BETWEEN"),
    NOT_BETWEEN("NOT_BETWEEN"),
    NOT_LIKE_LEFT("NOT_LIKE_LEFT"),
    NOT_LIKE_RIGHT("NOT_LIKE_RIGHT"),

    /**
     * 自定义操作符，配合 sqlTemplate 使用
     * <p>sqlTemplate 中使用 {0}, {1} 等作为值占位符，列名由框架自动解析</p>
     * <p>示例: sqlTemplate = "ILIKE {0}", value = "%keyword%"</p>
     */
    CUSTOM("CUSTOM"),;

    private final String code;

    Op(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public static Op getOp(String op) {
        for (Op item : Op.values()) {
            if (item.getCode().equals(op)) {
                return item;
            }
        }
        return Op.EQ;
    }
}
