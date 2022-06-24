package cn.git_chinwin.cc.config;

import cn.git_chinwin.cc.plugins.CCConnectionFactory;
import cn.git_chinwin.cc.plugins.CCRoutingConnectionFactory;
import cn.git_chinwin.cc.plugins.R2DBCSourceAOP;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chinwin
 * @date 2021/12/28 8:14 下午
 */
@Configuration
class SessionFactoryInit {

    @Bean
    public DbConfig dbConfig() {
        return new DbConfig();
    }

    @Bean
    public CCConnectionFactory ccConnectionFactory() {
        return new CCConnectionFactory(dbConfig());
    }

    @Bean
    public ConnectionFactory connectionFactory(CCConnectionFactory ccConnectionFactory) {
        return new CCRoutingConnectionFactory(ccConnectionFactory);
    }

    @Bean
    public R2DBCSourceAOP r2DBCSourceAOP(){
        return new R2DBCSourceAOP();
    }
}
