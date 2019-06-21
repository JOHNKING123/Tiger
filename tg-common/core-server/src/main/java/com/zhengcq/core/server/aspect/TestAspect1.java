package com.zhengcq.core.server.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TestAspect1 {
    @Pointcut("@annotation(com.zhengcq.core.server.annotation.LogAnnotation)")
	public void logAnnotation() {
	}


    @Before(value = "@annotation(com.zhengcq.core.server.annotation.LogAnnotation)")
	public void doBefore(final JoinPoint joinPoint) {
		System.out.println("TestAspect1------doBefore"+"   "+joinPoint.getTarget()+"   "+joinPoint.getSignature().getName());
	}

	@Before(value = "@annotation(com.zhengcq.core.server.annotation.LogAnnotation)")
	public void doBefore1(final JoinPoint joinPoint) {
		System.out.println("TestAspect1------doBefore1"+"   "+joinPoint.getTarget()+"   "+joinPoint.getSignature().getName());
	}

	@Before(value = "@annotation(com.zhengcq.core.server.annotation.LogAnnotation)")
	public void doBefore2(final JoinPoint joinPoint) {
		System.out.println("TestAspect1------doBefore2"+"   "+joinPoint.getTarget()+"   "+joinPoint.getSignature().getName());
	}
}
