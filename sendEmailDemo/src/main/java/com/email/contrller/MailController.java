package com.email.contrller;

import com.email.util.SendMailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;

@RestController
public class MailController {


    @Value("${spring.mail.username}")
    private String mailName;

    @Resource
    private SendMailUtil sendMailUtil;

    @PostMapping("/sendMail")
    public String sendMails(){
        ArrayList<String> to = new ArrayList<>();
        to.add("675894391@qq.com");
        to.add("wsfmlhaha@qq.com");
        to.add("wyf200407@163.com");

        String title = "这个邮件很重要";
        String content = "程序自动发送邮件测试";

        return sendMailUtil.simpleMails(mailName, to, title, content);

    }
}
