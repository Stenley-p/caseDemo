package com.yyy.yyyasm_es.controller;

import com.yyy.yyyasm_es.entity.Book;
import org.apache.http.HttpStatus;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/rest/book")
public class BookController {

    @Resource
    private RestClient client;

    @RequestMapping(value = "/go",method = RequestMethod.GET)
    public String go(){
        return "go"+HttpStatus.SC_OK;
    }

    //同步执行http请求
    @RequestMapping(value = "/es",method = RequestMethod.GET)
    public ResponseEntity<String> getEsInfo() throws IOException {
        Request request= new Request("GET","/");
        Response response = client.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        //return new ResponseEntity<>(responseBody, HttpStatus.SC_OK);
        return null;
    }

    //异步执行http请求
    @RequestMapping(value = "/es/asyn",method = RequestMethod.GET)
    public ResponseEntity<String> asyn(){
        Request request = new Request("GET","/");
        client.performRequestAsync(request, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                System.out.println("异步执行http请求并成功");
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("异步执行http请求并失败");
            }

        });
           return null;
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ResponseEntity<String> add(@RequestBody Book book) throws IOException {
        Request request = new Request("POST",new StringBuffer("/book_index/_doc/").append(book.getBookId()).toString());

        request.addParameter("pretty","true");

        JSONObject jsonObject = new JSONObject(book);
        System.out.println(jsonObject.toString());

        //设置请求体并指定ContentType  如果不指定默认为APPLICATION_JSON
        request.setEntity(new NStringEntity(jsonObject.toString()));

        //发送http请求
        Response response = client.performRequest(request);

        //获取响应体,
        String responseBody = EntityUtils.toString(response.getEntity());
        //return new ResponseEntity<>(responseBody,HttpStatus.SC_OK);
        return null;
    }

    //根据id获取es对象
    @RequestMapping(value = "/{id}",method =  RequestMethod.GET)
    public String getBookById(@PathVariable("id") String id){

        Request request = new Request("GET",new StringBuffer("/book_index/_doc/").append(id).toString());

        //添加json返回优化
        request.addParameter("pretty","true");
        Response response = null;
        String responseBody = null;
        try {
            response=client.performRequest(request);
            responseBody = EntityUtils.toString(response.getEntity());

        }catch (IOException e){
            return "zhao budao id";
        }
        System.out.println(responseBody);


        return responseBody;

    }

    //根据id更新book
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public ResponseEntity<String> updateBook(@PathVariable("id") String id,@RequestBody Book book) throws IOException {
        Request request = new Request("POST",new StringBuffer("/book_index/_doc/").append(id).toString());

        request.addParameter("pretty","true");

        //将数据丢进去，这里一定要外包一层“doc”，否则内部不能识别
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("doc",new JSONObject(book));
        request.setEntity(new NStringEntity(jsonObject.toString()));

        Response response = client.performRequest(request);

        String responseBody = EntityUtils.toString(response.getEntity());

        //return new ResponseEntity<>(responseBody,HttpStatus.SC_OK);
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteById(@PathVariable("id") String id) throws IOException {
        Request request = new Request("DELETE", new StringBuilder("/book_index/_doc/").append(id).toString());
        request.addParameter("pretty", "true");
        // 执行HTTP请求
        Response response = client.performRequest(request);
        // 获取结果
        String responseBody = EntityUtils.toString(response.getEntity());

        //return new ResponseEntity<>(responseBody, HttpStatus.SC_OK);
        return null;
    }


}
