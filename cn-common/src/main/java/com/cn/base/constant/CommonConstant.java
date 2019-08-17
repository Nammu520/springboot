package com.cn.base.constant;

/**
 * 通用常量池
 */
public class CommonConstant {

    public static final String TICKET = "ticket";

    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     *
     */
    public static class CacheTime {
        // 3小时(单位秒)
        public static final int ADMIN_TICKET = 10800;

        // 30天(单位秒)
        public static final int TICKET = 2592000;

        // 5分钟(单位分钟)
        public static final int VERIFY_CODE_TIMEOUT = 5;

        // 5分钟(单位秒)
        public static final int VERIFY_CODE_TIMEOUT_SECONDS = 300;
        // 1分钟(单位秒)
        public static final long SMS_FREQUENCY = 60;

        // 2小时(单位秒)
        public static final int VUS_CACHE_TIME = 7200;

        // 30s
        public static final int LOCATION_CACHE_TIME = 30;
        // 24小时
        public static final int BRAND_CACHE_TIME = 86400;

        public static final long USER_COMMUNITY_TIMEOUT = 300;

        public static final long COMMUNITY_TOP_TIMEOUT = 60;
    }
}
