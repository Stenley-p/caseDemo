package com.yyy.yyyasm_es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {

    @Bean
    public RestClient getClient(){
        RestClientBuilder clientBuilder = RestClient.builder(new HttpHost("localhost",9200,"http"));
        return clientBuilder.build();


        /*//clientBuilder.set();设置连接参数
        //设置请求头 每个请求都会带上这个请求头
        Header[] defaultHeaders= {new BasicHeader("header","value")};
        clientBuilder.setDefaultHeaders(defaultHeaders);

        //设置超时时间
        clientBuilder.setMaxRetryTimeoutMillis(60000);

        //设置节点失败监听器
        clientBuilder.setFailureListener(new RestClient.FailureListener(){

            @Override
            public void onFailure(Node node){
                super.onFailure(node);
                System.out.println(node.getName()+"==节点失败了");
            }
        });

        //设置http异步请求es的线程数量
        clientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                return httpAsyncClientBuilder.setDefaultIOReactorConfig(
                        IOReactorConfig.custom().setIoThreadCount(1).build()
                );
            }
        });

        //设置es安全认证
        *//*
        *默认异步机制实现抢先认证，这个功能也可以禁用，这意味着每个请求都将在没有授权标头的情况下发送，然后查看它是否被接受，
        *并且在收到HTTP 401响应后，它再使用基本认证头重新发送完全相同的请求，这个可能是基于安全、性能的考虑
        * *//*
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("user","password"));
        clientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                // 禁用抢先认证的方式
                httpAsyncClientBuilder.disableAuthCaching();

                return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        });

        //配置通宵加密 有多种方式：setSSLContext、setSSLSessionStrategy和setConnectionManager(它们的重要性逐渐递增)
        KeyStore truststore = KeyStore.getInstance("jks");
        try (InputStream is = Files.newInputStream(keyStorePath)) {
            truststore.load(is, keyStorePass.toCharArray());
        }
        SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(truststore, null);
        final SSLContext sslContext = sslBuilder.build();
        clientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder.setSSLContext(sslContext);
            }
        });*/



    }

}
