//package com.example.sharablead.grpc.stub;
//
//import static io.grpc.MethodDescriptor.generateFullMethodName;
//
///**
// */
//@javax.annotation.Generated(
//    value = "by gRPC proto compiler (version 1.44.0)",
//    comments = "Source: hello.proto")
//@io.grpc.stub.annotations.GrpcGenerated
//public final class SayHelloServiceGrpc {
//
//  private SayHelloServiceGrpc() {}
//
//  public static final String SERVICE_NAME = "proto.SayHelloService";
//
//  // Static method descriptors that strictly reflect the proto.
//  private static volatile io.grpc.MethodDescriptor<com.example.sharablead.Hello.SayHelloRequest,
//      com.example.sharablead.Hello.SayHelloResponse> getSayHelloMethod;
//
//  @io.grpc.stub.annotations.RpcMethod(
//      fullMethodName = SERVICE_NAME + '/' + "SayHello",
//      requestType = com.example.sharablead.Hello.SayHelloRequest.class,
//      responseType = com.example.sharablead.Hello.SayHelloResponse.class,
//      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
//  public static io.grpc.MethodDescriptor<com.example.sharablead.Hello.SayHelloRequest,
//      com.example.sharablead.Hello.SayHelloResponse> getSayHelloMethod() {
//    io.grpc.MethodDescriptor<com.example.sharablead.Hello.SayHelloRequest, com.example.sharablead.Hello.SayHelloResponse> getSayHelloMethod;
//    if ((getSayHelloMethod = SayHelloServiceGrpc.getSayHelloMethod) == null) {
//      synchronized (SayHelloServiceGrpc.class) {
//        if ((getSayHelloMethod = SayHelloServiceGrpc.getSayHelloMethod) == null) {
//          SayHelloServiceGrpc.getSayHelloMethod = getSayHelloMethod =
//              io.grpc.MethodDescriptor.<com.example.sharablead.Hello.SayHelloRequest, com.example.sharablead.Hello.SayHelloResponse>newBuilder()
//              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
//              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SayHello"))
//              .setSampledToLocalTracing(true)
//              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
//                  com.example.sharablead.Hello.SayHelloRequest.getDefaultInstance()))
//              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
//                  com.example.sharablead.Hello.SayHelloResponse.getDefaultInstance()))
//              .setSchemaDescriptor(new SayHelloServiceMethodDescriptorSupplier("SayHello"))
//              .build();
//        }
//      }
//    }
//    return getSayHelloMethod;
//  }
//
//  /**
//   * Creates a new async stub that supports all call types for the service
//   */
//  public static SayHelloServiceStub newStub(io.grpc.Channel channel) {
//    io.grpc.stub.AbstractStub.StubFactory<SayHelloServiceStub> factory =
//      new io.grpc.stub.AbstractStub.StubFactory<SayHelloServiceStub>() {
//        @Override
//        public SayHelloServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
//          return new SayHelloServiceStub(channel, callOptions);
//        }
//      };
//    return SayHelloServiceStub.newStub(factory, channel);
//  }
//
//  /**
//   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
//   */
//  public static SayHelloServiceBlockingStub newBlockingStub(
//      io.grpc.Channel channel) {
//    io.grpc.stub.AbstractStub.StubFactory<SayHelloServiceBlockingStub> factory =
//      new io.grpc.stub.AbstractStub.StubFactory<SayHelloServiceBlockingStub>() {
//        @Override
//        public SayHelloServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
//          return new SayHelloServiceBlockingStub(channel, callOptions);
//        }
//      };
//    return SayHelloServiceBlockingStub.newStub(factory, channel);
//  }
//
//  /**
//   * Creates a new ListenableFuture-style stub that supports unary calls on the service
//   */
//  public static SayHelloServiceFutureStub newFutureStub(
//      io.grpc.Channel channel) {
//    io.grpc.stub.AbstractStub.StubFactory<SayHelloServiceFutureStub> factory =
//      new io.grpc.stub.AbstractStub.StubFactory<SayHelloServiceFutureStub>() {
//        @Override
//        public SayHelloServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
//          return new SayHelloServiceFutureStub(channel, callOptions);
//        }
//      };
//    return SayHelloServiceFutureStub.newStub(factory, channel);
//  }
//
//  /**
//   */
//  public static abstract class SayHelloServiceImplBase implements io.grpc.BindableService {
//
//    /**
//     */
//    public void sayHello(com.example.sharablead.Hello.SayHelloRequest request,
//        io.grpc.stub.StreamObserver<com.example.sharablead.Hello.SayHelloResponse> responseObserver) {
//      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSayHelloMethod(), responseObserver);
//    }
//
//    @Override public final io.grpc.ServerServiceDefinition bindService() {
//      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
//          .addMethod(
//            getSayHelloMethod(),
//            io.grpc.stub.ServerCalls.asyncUnaryCall(
//              new MethodHandlers<
//                com.example.sharablead.Hello.SayHelloRequest,
//                com.example.sharablead.Hello.SayHelloResponse>(
//                  this, METHODID_SAY_HELLO)))
//          .build();
//    }
//  }
//
//  /**
//   */
//  public static final class SayHelloServiceStub extends io.grpc.stub.AbstractAsyncStub<SayHelloServiceStub> {
//    private SayHelloServiceStub(
//        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
//      super(channel, callOptions);
//    }
//
//    @Override
//    protected SayHelloServiceStub build(
//        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
//      return new SayHelloServiceStub(channel, callOptions);
//    }
//
//    /**
//     */
//    public void sayHello(com.example.sharablead.Hello.SayHelloRequest request,
//        io.grpc.stub.StreamObserver<com.example.sharablead.Hello.SayHelloResponse> responseObserver) {
//      io.grpc.stub.ClientCalls.asyncUnaryCall(
//          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request, responseObserver);
//    }
//  }
//
//  /**
//   */
//  public static final class SayHelloServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<SayHelloServiceBlockingStub> {
//    private SayHelloServiceBlockingStub(
//        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
//      super(channel, callOptions);
//    }
//
//    @Override
//    protected SayHelloServiceBlockingStub build(
//        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
//      return new SayHelloServiceBlockingStub(channel, callOptions);
//    }
//
//    /**
//     */
//    public com.example.sharablead.Hello.SayHelloResponse sayHello(com.example.sharablead.Hello.SayHelloRequest request) {
//      return io.grpc.stub.ClientCalls.blockingUnaryCall(
//          getChannel(), getSayHelloMethod(), getCallOptions(), request);
//    }
//  }
//
//  /**
//   */
//  public static final class SayHelloServiceFutureStub extends io.grpc.stub.AbstractFutureStub<SayHelloServiceFutureStub> {
//    private SayHelloServiceFutureStub(
//        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
//      super(channel, callOptions);
//    }
//
//    @Override
//    protected SayHelloServiceFutureStub build(
//        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
//      return new SayHelloServiceFutureStub(channel, callOptions);
//    }
//
//    /**
//     */
//    public com.google.common.util.concurrent.ListenableFuture<com.example.sharablead.Hello.SayHelloResponse> sayHello(
//        com.example.sharablead.Hello.SayHelloRequest request) {
//      return io.grpc.stub.ClientCalls.futureUnaryCall(
//          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request);
//    }
//  }
//
//  private static final int METHODID_SAY_HELLO = 0;
//
//  private static final class MethodHandlers<Req, Resp> implements
//      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
//      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
//      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
//      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
//    private final SayHelloServiceImplBase serviceImpl;
//    private final int methodId;
//
//    MethodHandlers(SayHelloServiceImplBase serviceImpl, int methodId) {
//      this.serviceImpl = serviceImpl;
//      this.methodId = methodId;
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
//      switch (methodId) {
//        case METHODID_SAY_HELLO:
//          serviceImpl.sayHello((com.example.sharablead.Hello.SayHelloRequest) request,
//              (io.grpc.stub.StreamObserver<com.example.sharablead.Hello.SayHelloResponse>) responseObserver);
//          break;
//        default:
//          throw new AssertionError();
//      }
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public io.grpc.stub.StreamObserver<Req> invoke(
//        io.grpc.stub.StreamObserver<Resp> responseObserver) {
//      switch (methodId) {
//        default:
//          throw new AssertionError();
//      }
//    }
//  }
//
//  private static abstract class SayHelloServiceBaseDescriptorSupplier
//      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
//    SayHelloServiceBaseDescriptorSupplier() {}
//
//    @Override
//    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
//      return com.example.sharablead.Hello.getDescriptor();
//    }
//
//    @Override
//    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
//      return getFileDescriptor().findServiceByName("SayHelloService");
//    }
//  }
//
//  private static final class SayHelloServiceFileDescriptorSupplier
//      extends SayHelloServiceBaseDescriptorSupplier {
//    SayHelloServiceFileDescriptorSupplier() {}
//  }
//
//  private static final class SayHelloServiceMethodDescriptorSupplier
//      extends SayHelloServiceBaseDescriptorSupplier
//      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
//    private final String methodName;
//
//    SayHelloServiceMethodDescriptorSupplier(String methodName) {
//      this.methodName = methodName;
//    }
//
//    @Override
//    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
//      return getServiceDescriptor().findMethodByName(methodName);
//    }
//  }
//
//  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;
//
//  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
//    io.grpc.ServiceDescriptor result = serviceDescriptor;
//    if (result == null) {
//      synchronized (SayHelloServiceGrpc.class) {
//        result = serviceDescriptor;
//        if (result == null) {
//          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
//              .setSchemaDescriptor(new SayHelloServiceFileDescriptorSupplier())
//              .addMethod(getSayHelloMethod())
//              .build();
//        }
//      }
//    }
//    return result;
//  }
//}
