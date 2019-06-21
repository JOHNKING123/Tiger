package com.zhengcq.core.server.aspect;

import com.zhengcq.srv.core.common.entity.JsonResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TestAspect {
    @Pointcut("@annotation(com.zhengcq.core.server.annotation.LogAnnotation)")
	public void logAnnotation() {
	}


    @AfterReturning(pointcut = "logAnnotation()", returning = "returnValue")
	public void doAfter(final JoinPoint joinPoint, final Object returnValue) {
		System.out.println("TestAspect------doafer"+returnValue+"   "+joinPoint.getTarget()+"   "+joinPoint.getSignature().getName());
	}
}
