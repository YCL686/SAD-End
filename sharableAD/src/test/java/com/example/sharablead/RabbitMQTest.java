package com.example.sharablead;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class RabbitMQTest {

    private static final String TEST_QUEUE = "test_queue";
    private static final String TEST_CHANNEL = "test_channel";
    private static final String TEST_ROUTE_KEY = "test_route_key";

    public static void main(String args[]) throws NoSuchAlgorithmException, KeyManagementException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("***");
        connectionFactory.setPort(5671);
        connectionFactory.setUsername("***");
        connectionFactory.setPassword("***");
        connectionFactory.setHandshakeTimeout(60000);
        connectionFactory.setConnectionTimeout(60000);
        connectionFactory.setVirtualHost("/");
        connectionFactory.useSslProtocol();


        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
//            channel = connection.createChannel();
//            channel.exchangeBind(TEST_CHANNEL, TEST_CHANNEL, TEST_ROUTE_KEY);
//            channel.queueDeclare(TEST_QUEUE, true, false, true, null);
            String message = "test message";
            //channel.basicPublish("", TEST_QUEUE, null, message.getBytes());
            System.out.println("send message:" + message);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}
