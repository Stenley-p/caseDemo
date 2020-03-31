package com.yyyasm.asm_apply.uitl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HttpClientUtil {

    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    private static SSLConnectionSocketFactory sslConnectionSocketFactory = null;
    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;

    static {
        try {
            //管理https链接上下文
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder().loadTrustMaterial(null, (x509Certificates, s) -> true);
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContextBuilder.build(),
                    new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"},
                    null,
                    NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslConnectionSocketFactory)
                    .build();
            //连接池
            poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
            //连接数
            poolingHttpClientConnectionManager.setMaxTotal(200);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            log.error("httpClientUtil pool连接池初始化发生错误"+ e);
        }
    }

    public  CloseableHttpClient getHttpClient(){
        return HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setConnectionManagerShared(true)//连接管理器是否可以共享
                .build();
    }



    /** post请求*/
    public String doPostRequest(String url, Map<String,String> header, Map<String,String> params, HttpEntity httpEntity){
        String resultStr="";
        if (StringUtils.isEmpty(url)){
            return resultStr;
        }
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;

        try {
            httpClient= getHttpClient();
            HttpPost httpPost = new HttpPost(url);
            //请求头
            if (MapUtils.isNotEmpty(header)){
                for (Map.Entry<String,String> entry: header.entrySet()) {
                    httpPost.setHeader(entry.getKey(),entry.getValue());
                }
            }

            //请求参数信息
            if (MapUtils.isNotEmpty(params)){
                List<NameValuePair> list = new ArrayList<>();
                for (Map.Entry<String,String> entry:params.entrySet()) {
                    list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
                }
                //list有时候需要 json.toJSONString(list)
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, Consts.UTF_8);
                httpPost.setEntity(urlEncodedFormEntity);
            }

            //实体设置
            if (httpEntity!= null){
                httpPost.setEntity(httpEntity);
            }

            //发起请求
            httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode== HttpStatus.SC_OK){
                resultStr = EntityUtils.toString(httpResponse.getEntity(), Consts.UTF_8);
                log.info("请求成功  请求地址:" + url + "数据内容" + resultStr);
            }else {
                StringBuilder stringBuffer = new StringBuilder();
                HeaderIterator headerIterator = httpResponse.headerIterator();
                while (headerIterator.hasNext()){
                    stringBuffer.append("\t").append(headerIterator.next());
                }
                log.error("请求异常 请求地址:"+url+"响应状态:"+httpResponse.getStatusLine().getStatusCode()+"返回结果:"+stringBuffer);
            }
        }catch (Exception e){
            log.error("请求失败:"+e);
        }finally {
            //关闭资源
            closeConnection(httpClient, httpResponse);
        }
        return resultStr;
    }

    /**get请求*/
    public  String doGetRequest(String url,Map<String,String> header,Map<String,String> params){
        String resultStr="";
        if (StringUtils.isEmpty(url)){
            return resultStr;
        }
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        
        try {
            httpClient = getHttpClient();
            //请求参数
            if (MapUtils.isNotEmpty(params)){
                url=url+buidUrl(params);
            }
            HttpGet httpGet = new HttpGet(url);
            RequestConfig build = RequestConfig.custom().setConnectTimeout(5000)//连接超时
                    .setConnectionRequestTimeout(5000)//请求超时
                    .setSocketTimeout(5000)//套接字连接超时
                    .setRedirectsEnabled(true)//重定向
                    .build();
            httpGet.setConfig(build);
            //设置请求头
            if (MapUtils.isNotEmpty(header)){
                for (Map.Entry<String,String> entry:header.entrySet()) {
                    httpGet.setHeader(entry.getKey(),entry.getValue());
                }
            }

            //发起请求
            httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode== HttpStatus.SC_OK) {
                resultStr = EntityUtils.toString(httpResponse.getEntity(), Consts.UTF_8);
                log.info("请求成功  请求地址:" + url + "数据内容" + resultStr);
            }else {
                StringBuilder stringBuffer = new StringBuilder();
                HeaderIterator headerIterator = httpResponse.headerIterator();
                while (headerIterator.hasNext()){
                    stringBuffer.append("\t").append(headerIterator.next());
                }
                log.error("请求异常 请求地址:"+url+"响应状态:"+httpResponse.getStatusLine().getStatusCode()+"返回结果:"+stringBuffer);
            }
        }catch (Exception e){
            log.error("请求失败:"+e);
        }finally {
            closeConnection(httpClient,httpResponse);
        }
        return resultStr;

    }


    /**参数url拼接*/
    private static String buidUrl(Map<String, String> params) {
        if (MapUtils.isEmpty(params)){
            return "";
        }

        StringBuilder stringBuffer = new StringBuilder("?");
        for (Map.Entry<String,String> entry: params.entrySet()) {
            stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String result = stringBuffer.toString();
        if (result.length() > 0){
            result=result.substring(0,result.length()-1);//去掉结尾的&连接符
        }
        return result;
    }

    /** 关闭资源*/
    private static void closeConnection(CloseableHttpClient httpClient, CloseableHttpResponse httpResponse) {
        if (httpResponse!=null){
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (httpClient!=null){
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
