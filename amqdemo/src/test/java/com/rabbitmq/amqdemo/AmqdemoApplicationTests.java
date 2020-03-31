package com.rabbitmq.amqdemo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
class AmqdemoApplicationTests {

    @Resource
    private AmqpTemplate amqpTemplate;

    @Test
    void contextLoads() {
       amqpTemplate.convertAndSend("hello","aqp测试2");
    }



}
