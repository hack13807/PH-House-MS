package com.panghu.housemanage.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panghu.housemanage.common.util.PHResp;
import com.panghu.housemanage.service.CozeApiService;

/**
 * Coze API控制器
 * 
 * @author panghu
 */
@RestController
@RequestMapping("/api/proxy")
public class CozeApiController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CozeApiController.class);
    
    @Autowired
    private CozeApiService cozeApiService;
    
    /**
     * 代理调用Coze API
     * 
     * @param requestBody 包含workflow_id和parameters的请求体
     * @return Coze API的响应结果
     */
    @PostMapping("/coze")
    public ResponseEntity<String> proxyCozeApi(@RequestBody Map<String, Object> requestBody) {
        LOGGER.info("接收到Coze API代理请求");
        try {
            // 从请求中提取工作流ID
            String workflowId = (String) requestBody.get("workflow_id");
            String appId = (String) requestBody.get("app_id");

            // 如果没有提供工作流ID，使用默认API调用
            if (workflowId == null || workflowId.isEmpty()) {
                LOGGER.info("未提供工作流ID，使用默认API调用");
                String response = cozeApiService.callDefaultApi();
                return ResponseEntity.ok(response);
            }
            
            // 提取调用参数
            @SuppressWarnings("unchecked")
            Map<String, Object> parameters = (Map<String, Object>) requestBody.get("parameters");
            if (parameters == null) {
                parameters = new HashMap<>();
            }
            
            // 调用指定工作流
            String response = cozeApiService.callWorkflow(workflowId, appId, parameters);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error("代理调用Coze API失败: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("{\"error\":\"调用Coze API失败: " + e.getMessage() + "\"}");
        }
    }
    
    /**
     * 获取AI新闻数据
     * 
     * @return 包含新闻数据的响应
     */
    @PostMapping("/news")
    public PHResp getNewsData() {
        LOGGER.info("接收到获取AI新闻数据请求");
        try {
            String newsData = cozeApiService.fetchNewsData();
            return PHResp.success("获取新闻数据成功", newsData);
        } catch (Exception e) {
            LOGGER.error("获取AI新闻数据失败: {}", e.getMessage(), e);
            return PHResp.error("获取新闻数据失败: " + e.getMessage());
        }
    }
} 