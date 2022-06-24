package cn.git_chinwin.cc.plugins;

import cn.git_chinwin.cc.config.DbConfig;
import cn.git_chinwin.cc.config.DbGroup;
import cn.git_chinwin.cc.config.DbProp;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

/**
 * @author chinwin
 * @date 2021/12/28 7:08 下午
 */
public class CCConnectionFactory {

    private final Logger logger = LoggerFactory.getLogger(CCConnectionFactory.class);
    private final static String EMPTY_KEY = "NOT_FOUND_EMPTY_KEY";

    private final Map<String, Map<String, ConnectionFactory>> connectionFactoriesMap;
    private final Map<String, ConnectionFactory> connectionFactoriesCache;
    private final Random random;

    public CCConnectionFactory(DbConfig config) {
        this.connectionFactoriesMap = new HashMap<>();
        this.connectionFactoriesCache = new HashMap<>();
        this.random = new Random();

        // 去掉配置中相同name的group
        Map<String, DbGroup> dbGroupDistinct = new HashMap<>();
        for (DbGroup g : config.getDbGroups()) {
            dbGroupDistinct.putIfAbsent(g.getName(), g);
        }

        // set
        setSessionFactory(dbGroupDistinct);

        // 检查是否存在defaultConnectionFactory, 不存在，则将配置的最后一个当作default
        if (!this.connectionFactoriesMap.containsKey(DbGroup.defaultGroup)) {
            DbGroup dbGroup = config.getDbGroups().get(config.getDbGroups().size() - 1);
            // 创建一个新的，不要修改原有的dbGroup
            DbGroup defaultGroup = new DbGroup(DbGroup.defaultGroup, dbGroup.getList());
            setSessionFactory(defaultGroup);
        }
    }

    private void setSessionFactory(Map<String, DbGroup> dbGroups) {
        for (DbGroup dbGroup : dbGroups.values()) {
            setSessionFactory(dbGroup);
        }
    }

    private void setSessionFactory(DbGroup dbGroup) {
        Map<String, ConnectionFactory> factoryMap = initSqlSessionFactory(dbGroup);
        // 构建 main map
        // 结构（两层）： {"master": {"master-0":{}, "master-1":{}}, "slave": {"slave-0":{}, "slave-1":{}} ...}
        this.connectionFactoriesMap.put(dbGroup.getName(), factoryMap);
        // 构建 cache map
        // 结构（平铺）： {"master-0":{}, "master-1":{}, "slave-0":{}, "slave-1":{} ...}
        this.connectionFactoriesCache.putAll(factoryMap);
    }

    private Map<String, ConnectionFactory> initSqlSessionFactory(DbGroup g) {
        if (null == g.getList() || g.getList().isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, ConnectionFactory> m = new HashMap<>();
        for (int i = 0; i < g.getList().size(); i++) {
            DbProp p = g.getList().get(i);
            m.put(generateKey(g.getName(), i), createSessionFactory(p));
        }
        return m;
    }

    private ConnectionFactory createSessionFactory(DbProp config) {
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(DRIVER, config.getDriver())
                .option(HOST, config.getHost())
                .option(PORT, config.getPort())
                .option(USER, config.getUser())
                .option(PASSWORD, config.getPass())
                .option(DATABASE, config.getDatabase())
                .option(CONNECT_TIMEOUT, Duration.ofSeconds(5))
                .build();
        ConnectionFactory connectionFactory = ConnectionFactories.get(options);
        return this.connectionPool(connectionFactory, config);
    }

    private ConnectionFactory connectionPool(ConnectionFactory connectionFactory, DbProp config) {
        if (null == config.getPool()) {
            return connectionFactory;
        } else if (connectionFactory instanceof ConnectionPool) {
            return connectionFactory;
        } else {
            ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(connectionFactory)
                    .initialSize(config.getPool().getInitialSize())
                    .maxSize(config.getPool().getMaxSize())
                    .maxIdleTime(config.getPool().getMaxIdleTime())
                    .validationQuery(config.getPool().getValidationQuery())
                    .build();
            return new ConnectionPool(configuration);
        }
    }

    public Map<String, ConnectionFactory> getConnectionFactories() {
        return this.connectionFactoriesCache;
    }


    public ConnectionFactory getDefaultConnectionFactory() {
        return this.connectionFactoriesCache.get(getDefaultConnectionFactoryKey());
    }

    public ConnectionFactory getConnectionFactory(String group) {
        return this.connectionFactoriesCache.get(getConnectionFactoryKey(group));
    }

    public String getDefaultConnectionFactoryKey() {
        return getConnectionFactoryKey(DbGroup.defaultGroup);
    }

    public String getConnectionFactoryKey(String group) {
        if (group == null || !this.connectionFactoriesMap.containsKey(group)) {
            logger.error("Key was not be fond! Key: {}" , group);
            group = DbGroup.defaultGroup;
        }
        Map<String, ConnectionFactory> connectionFactories = this.connectionFactoriesMap.get(group);
        if (CollectionUtils.isEmpty(connectionFactories)) {
            logger.error("The HashMap corresponding to the key is empty! Key: {}" , group);
            return EMPTY_KEY;
        }
        return generateKey(group, this.random.nextInt(connectionFactories.size()));
    }

    private String generateKey(String group, int index) {
        return String.format("%s-%d", group, index);
    }

}
