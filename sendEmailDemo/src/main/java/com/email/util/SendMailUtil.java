package com.email.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;

@Component
public class SendMailUtil {

    @Resource
    private JavaMailSender mailSender;


    private SimpleMailMessage message = new SimpleMailMessage();

    /**
     * 发送一个简单邮件
     * @param from
     * @param to
     * @param subject
     * @param content
     * @return
     */
    public String simpleMail(String from,String to,String subject,String content){
        if (to.isEmpty()){
            return "发送对象不能为空";
        }

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
            return to+"发送成功";
        }catch (Exception e){
            return "发送失败";
        }
    }

    /**
     * 发送多个邮件
     * @param from
     * @param tos
     * @param subject
     * @param content
     * @return
     */
    public String simpleMails(String from, ArrayList<String> tos, String subject, String content){

        if (tos.isEmpty()){
            return "发送对象不能为空";
        }

        for (String s : tos) {

            message.setFrom(from);
            message.setTo(s);
            message.setSubject(subject);
            message.setText(content);
            try {
                mailSender.send(message);
                System.out.println(s + "邮件发送成功");
            } catch (Exception e) {
                return "发送失败";
            }
        }
        return "发送成功";
    }

    /**
     * 发送html页面
     * @param from
     * @param to
     * @param subject
     * @param content
     * @return
     */
    public String sendHtmlMail(String from,String to,String subject,String content){

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            mailSender.send(message);

            return to+"的邮件发送成功";
        }catch (Exception e){
            return "html发送失败";
        }


    }


}
