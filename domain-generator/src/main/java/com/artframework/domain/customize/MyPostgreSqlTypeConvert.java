package com.artframework.domain.customize;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.select.BranchBuilder;
import com.baomidou.mybatisplus.generator.config.converts.select.Selector;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;

import java.util.regex.Pattern;

import static com.baomidou.mybatisplus.generator.config.rules.DbColumnType.*;
import static com.baomidou.mybatisplus.generator.config.rules.DbColumnType.STRING;

public class MyPostgreSqlTypeConvert implements ITypeConvert {

    public static final PostgreSqlTypeConvert INSTANCE = new PostgreSqlTypeConvert();

    /**
     * @inheritDoc
     */
    @Override
    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
        //System.out.println(fieldType);
        return MyPostgreSqlTypeConvert.use(fieldType)
                .test(containsAny("char", "text", "json", "enum").then(STRING))
                .test(pattern("^numeric\\(\\d+,0\\)$").then(LONG))
                .test(containsAny("bigint").then(LONG))
                .test(contains("int").then(INTEGER))
                .test(containsAny("date", "time").then(t -> toDateType(config, t)))
                .test(contains("bit").then(BOOLEAN))
                .test(containsAny("decimal").then(BIG_DECIMAL))
                .test(contains("bytea").then(BYTE_ARRAY))
                .test(contains("float").then(FLOAT))
                .test(contains("double").then(DOUBLE))
                .test(contains("boolean").then(BOOLEAN))
                .or(STRING);
    }

    /**
     * 转换为日期类型
     *
     * @param config 配置信息
     * @param type   类型
     * @return 返回对应的列类型
     */
    public static IColumnType toDateType(GlobalConfig config, String type) {
        switch (config.getDateType()) {
            case SQL_PACK:
                switch (type) {
                    case "date":
                        return DbColumnType.DATE_SQL;
                    case "time":
                        return DbColumnType.TIME;
                    default:
                        return DbColumnType.TIMESTAMP;
                }
            case TIME_PACK:
                switch (type) {
                    case "time":
                        return DbColumnType.TIME;
                    default:
                        return DbColumnType.DATE;
                }
            default:
                return DbColumnType.DATE;
        }
    }

    /**
     * 使用指定参数构建一个选择器
     *
     * @param param 参数
     * @return 返回选择器
     */
    static Selector<String, IColumnType> use(String param) {
        return new Selector<>(param.toLowerCase());
    }

    /**
     * 这个分支构建器用于构建用于支持 {@link String#contains(CharSequence)} 的分支
     *
     * @param value 分支的值
     * @return 返回分支构建器
     * @see #containsAny(CharSequence...)
     */
    static BranchBuilder<String, IColumnType> contains(CharSequence value) {
        return BranchBuilder.of(s -> s.contains(value));
    }

    /**
     * @see #contains(CharSequence)
     */
    static BranchBuilder<String, IColumnType> containsAny(CharSequence... values) {
        return BranchBuilder.of(s -> {
            for (CharSequence value : values) {
                if (s.contains(value)) return true;
            }
            return false;
        });
    }
    static BranchBuilder<String, IColumnType> pattern(CharSequence value) {
        return BranchBuilder.of(s -> {
            final boolean matches = Pattern.matches(value.toString(), s);
            return matches;
        });
    }
}
