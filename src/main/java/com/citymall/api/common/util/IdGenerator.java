package com.citymall.api.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @author cqkir
 */
public class IdGenerator {

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake();

    public static String nextIdStr() {
        return String.valueOf(SNOWFLAKE.nextId());
    }

    public static Long nextId() {
        return SNOWFLAKE.nextId();
    }
}