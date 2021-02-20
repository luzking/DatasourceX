package com.dtstack.dtcenter.common.loader.websocket;

import com.dtstack.dtcenter.common.loader.common.exception.AbsErrorPattern;
import com.dtstack.dtcenter.common.loader.common.exception.ConnErrorCode;

import java.util.regex.Pattern;

/**
 *
 * @author ：wangchuan
 * date：Created in 下午1:46 2020/11/6
 * company: www.dtstack.com
 */
public class WebsocketErrorPattern extends AbsErrorPattern {

    private static final Pattern USERNAME_PASSWORD_ERROR = Pattern.compile("(?i)Exception\\s*authenticating");
    static {
        PATTERN_MAP.put(ConnErrorCode.USERNAME_PASSWORD_ERROR.getCode(), USERNAME_PASSWORD_ERROR);
    }
}
