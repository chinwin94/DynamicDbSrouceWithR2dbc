package cn.git_chinwin.cc.config;

import java.time.Duration;

/**
 * @author chinwin
 * @date 2021/12/30 8:57 下午
 */
public class DbPool {
    private int initialSize;
    private int maxSize;
    private Duration maxIdleTime;
    private String validationQuery = "SELECT 1";

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(Duration maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }
}
