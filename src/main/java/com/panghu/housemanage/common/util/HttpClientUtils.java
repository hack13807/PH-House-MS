package com.panghu.housemanage.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP客户端工具类
 * 
 * @author panghu
 */
public class HttpClientUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);
    
    private static final int CONNECT_TIMEOUT = 10000; // 连接超时时间，单位毫秒
    private static final int READ_TIMEOUT = 15000;    // 读取超时时间，单位毫秒
    
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    
    private HttpClientUtils() {
        // 工具类构造方法私有化
    }
    
    /**
     * 发送POST请求，JSON格式数据
     *
     * @param url         请求URL
     * @param requestBody 请求体
     * @param headers     请求头
     * @return 响应结果字符串
     */
    public static String postJson(String url, Object requestBody, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        // 添加自定义请求头
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpHeaders::add);
        }
        
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        
        try {
            ResponseEntity<String> responseEntity = REST_TEMPLATE.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            return responseEntity.getBody();
        } catch (RestClientException e) {
            LOGGER.error("发送POST请求失败，URL: {}, 错误: {}", url, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 调用Coze工作流API
     *
     * @param token      API令牌
     * @param workflowId 工作流ID
     * @param parameters 参数
     * @return API响应结果
     */
    public static String callCozeWorkflow(String token, String workflowId, String appId, Map<String, Object> parameters) {
        String url = "https://api.coze.cn/v1/workflow/run";
        
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Authorization", "Bearer " + token);
        
        Map<String, Object> requestBody = new HashMap<>(2);
        
        // 如果未指定工作流ID，抛出异常
        if (workflowId == null || workflowId.isEmpty()) {
            throw new IllegalArgumentException("工作流ID不能为空");
        }
        
        requestBody.put("workflow_id", workflowId);
        requestBody.put("app_id", appId);
        requestBody.put("parameters", parameters != null ? parameters : new HashMap<>());
        
        LOGGER.info("调用Coze工作流API，URL: {}, 工作流ID: {}", url, workflowId);
        String response = postJson(url, requestBody, headers);
        LOGGER.debug("Coze工作流API响应: {}", response);
        
        return response;
    }
    
    /**
     * 调用Coze固定接口，使用指定的token发送请求
     *
     * @param token API令牌
     * @return API响应结果
     */
    public static String callCozeApi(String token) {
        String url = "https://api.coze.cn/v1/workflow/run";
        
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Authorization", "Bearer " + token);
        
        Map<String, Object> requestBody = new HashMap<>(1);
        requestBody.put("parameters", new HashMap<>());
        
        LOGGER.info("调用Coze API，URL: {}", url);
        String response = postJson(url, requestBody, headers);
        LOGGER.debug("Coze API响应: {}", response);
        
        return response;
    }
} 