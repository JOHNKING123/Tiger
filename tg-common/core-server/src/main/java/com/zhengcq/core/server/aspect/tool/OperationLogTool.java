//package com.zhengcq.core.server.aspect.tool;
//
//import com.yoorstore.core.server.annotation.OperationLogDto;
//import com.yoorstore.core.utils.JSonUtils;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * Created by clude on 7/11/18.
// */
//
//@Slf4j
//public class OperationLogTool {
//
////    private final String LOG_OUTPUT_FILE = "file";
////    private final String LOG_OUTPUT_DB = "rpc";
//
////    @Resource
////    private RestTemplate restTemplate;
////
////    @Resource
////    private TaskExecutor taskExecutor;
////
////    @Value("${yst.log.servcie:http://UAC-SERVICE/api/uac/log/saveLog}")
////    private String logRPCUrl;
//
////    @Value("${yst.log.output:file}")
////    private String logOutput;
//
//    public void logOperation(OperationLogDto operationLogDto) {
//        log.info(JSonUtils.toJson(operationLogDto));
////        taskExecutor.execute(() -> this.restTemplate.postForObject(logRPCUrl, operationLogDto, Integer.class));
//    }
//
//
//}
