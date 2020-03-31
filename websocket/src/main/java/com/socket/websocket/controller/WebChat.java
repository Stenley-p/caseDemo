package com.socket.websocket.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
/**
 * websocket
 * @author CHHUANG
 */
@ServerEndpoint("/webSocket/{senderId}") // 该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。类似Servlet的注解mapping。无需在web.xml中配置。
@RestController
public class WebChat {
    // 用来存放每个客户端对应的ChatAnnotation对象，实现服务端与单一客户端通信的话，使用Map来存放，其中Key可以为用户标识，hashtable比hashmap线程安全
    private static Map<String, WebChat> webSocketMap = new ConcurrentHashMap<>();
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    public String senderId;
    //private String socketId;
    /**
     * 连接建立成功调用的方法
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam(value="senderId") String senderId, Session session) {
        this.session = session;
        this.senderId = senderId;
        //this.socketId = socketId;
        webSocketMap.put(senderId,this);//加入map中
        System.out.println(senderId+"连接加入！当前在线人数为"+getOnlineCount());
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam(value="senderId") String senderId) {
        webSocketMap.remove(senderId);
        System.out.println(senderId+"关闭连接！当前在线人数为" + getOnlineCount());
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(@PathParam(value="senderId") String senderId, String message) {

        System.out.println("收到"+senderId+":"+message);
        // 群发消息
        try {
            Set<Map.Entry<String, WebChat>> entries = webSocketMap.entrySet();
            for (Map.Entry<String, WebChat> entry : entries) {
                if(!entry.getKey().equals(senderId)){//将消息转发到其他非自身客户端
                    entry.getValue().sendMessage(message);

                }
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(@PathParam(value="userId") String userId, Session session, Throwable error) {
        System.out.println(userId+"发生错误");
        error.printStackTrace();
    }

    /**
     * 发送消息
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 指定消息发送
     * @param message
     * @param senderId
     * @param ToSenderId
     * @param socketId
     */
    public void sendMessage(String message,String senderId,String ToSenderId,String socketId){

    }

    /**
     * 发送文件
     * @throws IOException
     */
    public void sendFile(File file)throws IOException{
        this.session.getAsyncRemote().sendObject(file);
    }

    public static synchronized int getOnlineCount() {
        return webSocketMap.size();
    }

}
