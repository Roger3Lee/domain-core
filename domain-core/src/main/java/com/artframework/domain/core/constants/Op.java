package com.artframework.domain.core.constants;

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
    NOT_LIKE("NOTLIKE"),
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

    /**
     * 不過濾任何數據
     */
    NIL("NIL"),;

    private String code;

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
