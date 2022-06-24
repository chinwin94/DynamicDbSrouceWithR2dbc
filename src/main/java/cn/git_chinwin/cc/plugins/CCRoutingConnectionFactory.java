package cn.git_chinwin.cc.plugins;

import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author chinwin
 * @date 2021/12/30 5:39 下午
 */
public class CCRoutingConnectionFactory extends AbstractRoutingConnectionFactory {

    private final static Logger logger = LoggerFactory.getLogger(CCRoutingConnectionFactory.class);
    private final static String DB_KEY = "app_r2dbc_key";
    private final String defaultConnectionFactoryKey;
    private static CCConnectionFactory ccConnectionFactory;

    public CCRoutingConnectionFactory(CCConnectionFactory ccConnectionFactory) {
        CCRoutingConnectionFactory.ccConnectionFactory = ccConnectionFactory;
        Map<String, ConnectionFactory> connectionFactories = ccConnectionFactory.getConnectionFactories();
        this.defaultConnectionFactoryKey = ccConnectionFactory.getDefaultConnectionFactoryKey();
        // set target Maps
        setTargetConnectionFactories(connectionFactories);
        // set default factory
        setDefaultTargetConnectionFactory(connectionFactories.get(this.defaultConnectionFactoryKey));
    }

    public static <T> Mono<T> putR2dbcSource(Mono<T> mono, String group) {
        return mono.contextWrite(ctx -> ctx.put(DB_KEY, ccConnectionFactory.getConnectionFactoryKey(group)));
    }

    public static <T> Flux<T> putR2dbcSource(Flux<T> flux, String group) {
        return flux.contextWrite(ctx -> ctx.put(DB_KEY, ccConnectionFactory.getConnectionFactoryKey(group)));
    }

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return Mono.deferContextual(Mono::just).map(ctx -> {
            if (ctx.hasKey(DB_KEY)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("determine datasource: " + ctx.get(DB_KEY));
                }
                return ctx.get(DB_KEY);
            } else {
                return this.defaultConnectionFactoryKey;
            }
        });
//        return Mono.subscriberContext().handle((ctx, sink) -> {
//            if (ctx.hasKey(DB_KEY)) {
//                if (logger.isDebugEnabled()) {
//                    logger.debug("determine datasource: " + ctx.get(DB_KEY));
//                }
//                sink.next(ctx.get(DB_KEY));
//            } else {
//                if (logger.isDebugEnabled()) {
//                    logger.debug("can not determine, get default datasource: " + this.defaultConnectionFactoryKey);
//                }
//                sink.next(this.defaultConnectionFactoryKey);
//            }
//        });
    }
}
