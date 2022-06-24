package cn.git_chinwin.cc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author chinwin
 * @date 2021/12/28 6:58 下午
 */
@ConfigurationProperties
public class DbConfig {
    List<DbGroup> dbGroups;

    public List<DbGroup> getDbGroups() {
        return dbGroups;
    }

    public void setDbGroups(List<DbGroup> dbGroups) {
        this.dbGroups = dbGroups;
    }


}
