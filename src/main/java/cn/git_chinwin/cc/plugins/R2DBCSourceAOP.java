package cn.git_chinwin.cc.plugins;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * @author chinwin
 * @date 2021/12/31 2:14 下午
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class R2DBCSourceAOP {

    @Pointcut(value = "@annotation(cn.git_chinwin.cc.plugins.R2DBCSource)")
    public void point() {
    }

    @Around(value = "point()")
    public Object dynamicSelectSource(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        R2DBCSource r2DBCSource = method.getAnnotation(R2DBCSource.class);
        if (method.getReturnType() == Mono.class) {
            return CCRoutingConnectionFactory.putR2dbcSource((Mono<?>) pjp.proceed(), r2DBCSource.value());
        } else if (method.getReturnType() == Flux.class) {
            return CCRoutingConnectionFactory.putR2dbcSource((Flux<?>) pjp.proceed(), r2DBCSource.value());
        } else {
            throw new RuntimeException("R2DBCSource just provide Mono or Flux return DAO");
        }
    }
}
