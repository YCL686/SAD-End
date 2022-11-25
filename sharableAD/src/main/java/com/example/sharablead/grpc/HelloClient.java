//package com.example.sharablead.grpc;
//
//import com.example.sharablead.config.GrpcClientConfig;
//import com.example.sharablead.Hello;
//import com.example.sharablead.grpc.stub.SayHelloServiceGrpc;
//import com.google.protobuf.ByteString;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.UnsupportedEncodingException;
//import java.nio.charset.Charset;
//
///**
// * Hello gRPC服务 - 客户端
// *
// * @author inncore
// * @date 2022-02-06 13:45
// */
//@Component
//public class HelloClient {
//
//    @Autowired
//    private GrpcClientConfig config;
//
//
//    public void say() {
//        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(config.getIp(), config.getPort())
//                .usePlaintext()
//                .build();
//        Hello.SayHelloRequest req=Hello.SayHelloRequest.newBuilder().setName(ByteString.copyFrom("测试", Charset.forName("utf-8"))).build();
//
//        Hello.SayHelloResponse result= SayHelloServiceGrpc.newBlockingStub(managedChannel).sayHello(req);
//        try {
//            System.out.println(result.getResult().toString("utf-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//}
//
