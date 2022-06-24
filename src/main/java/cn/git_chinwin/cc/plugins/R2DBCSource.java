package cn.git_chinwin.cc.plugins;

import java.lang.annotation.*;

/**
 * @author chinwin
 * @date 2021/12/30 11:01 下午
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface R2DBCSource {
    String value();
}
