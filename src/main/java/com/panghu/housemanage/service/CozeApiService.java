package com.panghu.housemanage.service;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.panghu.housemanage.common.util.HttpClientUtils;

/**
 * Coze API服务类
 * 
 * @author panghu
 */
@Service
public class CozeApiService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CozeApiService.class);
    
    @Value("${coze.api.token:pat_VEOk6aZf3o1DXCYo0rWklUFvRtLktLXsJLxzB4xrWlTYu1gft3EN8N8SvbEYipw9}")
    private String apiToken;
    
    @Value("${coze.workflow.news.id:7474599559693778982}")
    private String newsWorkflowId;

    @Value("${coze.workflow.news.appid:7474543679249334309}")
    private String appId;

    /**
     * 获取AI新闻数据
     * 
     * @return 新闻数据JSON字符串
     */
    public String fetchNewsData() {
        try {
            LOGGER.info("开始获取AI新闻数据");
            String response = HttpClientUtils.callCozeWorkflow(apiToken, newsWorkflowId, appId, new HashMap<>());
            LOGGER.info("成功获取AI新闻数据");
            return response;
        } catch (Exception e) {
            LOGGER.error("获取AI新闻数据失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取AI新闻数据失败", e);
        }
    }
    
    /**
     * 自定义调用Coze工作流
     * 
     * @param workflowId  工作流ID
     * @param parameters  调用参数
     * @return API响应结果
     */
    public String callWorkflow(String workflowId, String appId, Map<String, Object> parameters) {
        try {
            LOGGER.info("调用Coze工作流，ID: {}", workflowId);
            String response = HttpClientUtils.callCozeWorkflow(apiToken, workflowId, appId, parameters);
            LOGGER.info("成功调用Coze工作流");
            return response;
        } catch (Exception e) {
            LOGGER.error("调用Coze工作流失败: {}", e.getMessage(), e);
            throw new RuntimeException("调用Coze工作流失败", e);
        }
    }
    
    /**
     * 使用cURL命令中的固定URL调用API
     * 
     * @return API响应结果
     */
    public String callDefaultApi() {
        try {
            LOGGER.info("开始调用Coze默认API");
            String response = HttpClientUtils.callCozeApi(apiToken);
            LOGGER.info("成功调用Coze默认API");
            return response;
        } catch (Exception e) {
            LOGGER.error("调用Coze默认API失败: {}", e.getMessage(), e);
            throw new RuntimeException("调用Coze默认API失败", e);
        }
    }
} 