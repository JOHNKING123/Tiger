//package com.zhengcq.core.server.aspect.base;
//
//import com.zhengcq.core.entity.LogTypeEnum;
//import com.zhengcq.core.server.annotation.OperationLogDto;
//import com.zhengcq.core.server.model.dto.LoginAuthDto;
//import com.zhengcq.core.server.utils.WebUtils;
//import com.zhengcq.core.utils.JSonUtils;
//import com.zhengcq.core.server.aspect.tool.OperationLogTool;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//
//import javax.annotation.Resource;
//import java.lang.reflect.Method;
//import java.util.Date;
//
///**
// * The dao Log aspect.
// *
// * @author cludezhu
// */
//@Slf4j
//public class BaseDaoLogAspect {
//
//	@Resource
//	private OperationLogTool logTool;
//
//	protected Object handleAround(final ProceedingJoinPoint joinPoint) throws Throwable {
//		log.info("class_method={}",joinPoint.getSignature().getDeclaringTypeName() + "," + joinPoint.getSignature().getName());
//		Object rst = joinPoint.proceed();
//		this.handleLog(joinPoint);
//		return rst;
//	}
//
//	private void handleLog(final JoinPoint joinPoint) {
//		try {
//			OperationLogDto operationLogDto = new OperationLogDto();
//
////			LoginAuthDto loginUser = RequestUtil.getLoginUser();
//			LoginAuthDto loginUser = WebUtils.getLoginUser();
//			//获取客户端操作系统
//
//			if(joinPoint.getArgs().length > 0){
//				Object arg = joinPoint.getArgs()[0];
//				operationLogDto.setClassName(arg.getClass().getName());
//				operationLogDto.setResponseData(JSonUtils.toJson(arg));
//				try {
//					Method m1 = arg.getClass().getMethod("getId", null);
//					Long id = (Long) m1.invoke(arg);
//					operationLogDto.setEntityId(id);
//				} catch (Exception ex) {
//					// do nothing
//				}
//			}
//
////			operationLogDto.setClassName(joinPoint.getSignature().getDeclaringTypeName());
//			operationLogDto.setMethodName(joinPoint.getSignature().getName());
//			operationLogDto.setGroupId(loginUser.getGroupId());
//			operationLogDto.setGroupName(loginUser.getGroupName());
//			operationLogDto.setCreatedTime(new Date());
//			operationLogDto.setCreator(loginUser.getUserName());
//			operationLogDto.setCreatedBy(loginUser.getUserId());
//			operationLogDto.setUpdator(loginUser.getUserName());
//			operationLogDto.setUpdatedBy(loginUser.getUserId());
//
//			operationLogDto.setLogType(LogTypeEnum.ENTITY_LOG.getType());
//			operationLogDto.setLogName(LogTypeEnum.ENTITY_LOG.getName());
//
//
//
//			logTool.logOperation(operationLogDto);
//
//		} catch (Exception ex) {
//			log.error("获取注解类出现异常={}", ex.getMessage(), ex);
//		}
//	}
//
//
//}
