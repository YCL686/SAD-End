//// Generated by the protocol buffer compiler.  DO NOT EDIT!
//// source: hello.proto
//
//package com.example.sharablead.grpc.stub;
//
//public final class Hello {
//  private Hello() {}
//  public static void registerAllExtensions(
//      com.google.protobuf.ExtensionRegistryLite registry) {
//  }
//
//  public static void registerAllExtensions(
//      com.google.protobuf.ExtensionRegistry registry) {
//    registerAllExtensions(
//        (com.google.protobuf.ExtensionRegistryLite) registry);
//  }
//  public interface SayHelloRequestOrBuilder extends
//      // @@protoc_insertion_point(interface_extends:proto.SayHelloRequest)
//      com.google.protobuf.MessageOrBuilder {
//
//    /**
//     * <code>bytes name = 1;</code>
//     * @return The name.
//     */
//    com.google.protobuf.ByteString getName();
//  }
//  /**
//   * Protobuf type {@code proto.SayHelloRequest}
//   */
//  public static final class SayHelloRequest extends
//      com.google.protobuf.GeneratedMessageV3 implements
//      // @@protoc_insertion_point(message_implements:proto.SayHelloRequest)
//      SayHelloRequestOrBuilder {
//  private static final long serialVersionUID = 0L;
//    // Use SayHelloRequest.newBuilder() to construct.
//    private SayHelloRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
//      super(builder);
//    }
//    private SayHelloRequest() {
//      name_ = com.google.protobuf.ByteString.EMPTY;
//    }
//
//    @Override
//    @SuppressWarnings({"unused"})
//    protected Object newInstance(
//        UnusedPrivateParameter unused) {
//      return new SayHelloRequest();
//    }
//
//    @Override
//    public final com.google.protobuf.UnknownFieldSet
//    getUnknownFields() {
//      return this.unknownFields;
//    }
//    private SayHelloRequest(
//        com.google.protobuf.CodedInputStream input,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      this();
//      if (extensionRegistry == null) {
//        throw new NullPointerException();
//      }
//      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
//          com.google.protobuf.UnknownFieldSet.newBuilder();
//      try {
//        boolean done = false;
//        while (!done) {
//          int tag = input.readTag();
//          switch (tag) {
//            case 0:
//              done = true;
//              break;
//            case 10: {
//
//              name_ = input.readBytes();
//              break;
//            }
//            default: {
//              if (!parseUnknownField(
//                  input, unknownFields, extensionRegistry, tag)) {
//                done = true;
//              }
//              break;
//            }
//          }
//        }
//      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
//        throw e.setUnfinishedMessage(this);
//      } catch (java.io.IOException e) {
//        throw new com.google.protobuf.InvalidProtocolBufferException(
//            e).setUnfinishedMessage(this);
//      } finally {
//        this.unknownFields = unknownFields.build();
//        makeExtensionsImmutable();
//      }
//    }
//    public static final com.google.protobuf.Descriptors.Descriptor
//        getDescriptor() {
//      return Hello.internal_static_proto_SayHelloRequest_descriptor;
//    }
//
//    @Override
//    protected FieldAccessorTable
//        internalGetFieldAccessorTable() {
//      return Hello.internal_static_proto_SayHelloRequest_fieldAccessorTable
//          .ensureFieldAccessorsInitialized(
//              Hello.SayHelloRequest.class, Hello.SayHelloRequest.Builder.class);
//    }
//
//    public static final int NAME_FIELD_NUMBER = 1;
//    private com.google.protobuf.ByteString name_;
//    /**
//     * <code>bytes name = 1;</code>
//     * @return The name.
//     */
//    @Override
//    public com.google.protobuf.ByteString getName() {
//      return name_;
//    }
//
//    private byte memoizedIsInitialized = -1;
//    @Override
//    public final boolean isInitialized() {
//      byte isInitialized = memoizedIsInitialized;
//      if (isInitialized == 1) return true;
//      if (isInitialized == 0) return false;
//
//      memoizedIsInitialized = 1;
//      return true;
//    }
//
//    @Override
//    public void writeTo(com.google.protobuf.CodedOutputStream output)
//                        throws java.io.IOException {
//      if (!name_.isEmpty()) {
//        output.writeBytes(1, name_);
//      }
//      unknownFields.writeTo(output);
//    }
//
//    @Override
//    public int getSerializedSize() {
//      int size = memoizedSize;
//      if (size != -1) return size;
//
//      size = 0;
//      if (!name_.isEmpty()) {
//        size += com.google.protobuf.CodedOutputStream
//          .computeBytesSize(1, name_);
//      }
//      size += unknownFields.getSerializedSize();
//      memoizedSize = size;
//      return size;
//    }
//
//    @Override
//    public boolean equals(final Object obj) {
//      if (obj == this) {
//       return true;
//      }
//      if (!(obj instanceof Hello.SayHelloRequest)) {
//        return super.equals(obj);
//      }
//      Hello.SayHelloRequest other = (Hello.SayHelloRequest) obj;
//
//      if (!getName()
//          .equals(other.getName())) return false;
//      if (!unknownFields.equals(other.unknownFields)) return false;
//      return true;
//    }
//
//    @Override
//    public int hashCode() {
//      if (memoizedHashCode != 0) {
//        return memoizedHashCode;
//      }
//      int hash = 41;
//      hash = (19 * hash) + getDescriptor().hashCode();
//      hash = (37 * hash) + NAME_FIELD_NUMBER;
//      hash = (53 * hash) + getName().hashCode();
//      hash = (29 * hash) + unknownFields.hashCode();
//      memoizedHashCode = hash;
//      return hash;
//    }
//
//    public static Hello.SayHelloRequest parseFrom(
//        java.nio.ByteBuffer data)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data);
//    }
//    public static Hello.SayHelloRequest parseFrom(
//        java.nio.ByteBuffer data,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data, extensionRegistry);
//    }
//    public static Hello.SayHelloRequest parseFrom(
//        com.google.protobuf.ByteString data)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data);
//    }
//    public static Hello.SayHelloRequest parseFrom(
//        com.google.protobuf.ByteString data,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data, extensionRegistry);
//    }
//    public static Hello.SayHelloRequest parseFrom(byte[] data)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data);
//    }
//    public static Hello.SayHelloRequest parseFrom(
//        byte[] data,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data, extensionRegistry);
//    }
//    public static Hello.SayHelloRequest parseFrom(java.io.InputStream input)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseWithIOException(PARSER, input);
//    }
//    public static Hello.SayHelloRequest parseFrom(
//        java.io.InputStream input,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseWithIOException(PARSER, input, extensionRegistry);
//    }
//    public static Hello.SayHelloRequest parseDelimitedFrom(java.io.InputStream input)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseDelimitedWithIOException(PARSER, input);
//    }
//    public static Hello.SayHelloRequest parseDelimitedFrom(
//        java.io.InputStream input,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
//    }
//    public static Hello.SayHelloRequest parseFrom(
//        com.google.protobuf.CodedInputStream input)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseWithIOException(PARSER, input);
//    }
//    public static Hello.SayHelloRequest parseFrom(
//        com.google.protobuf.CodedInputStream input,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseWithIOException(PARSER, input, extensionRegistry);
//    }
//
//    @Override
//    public Builder newBuilderForType() { return newBuilder(); }
//    public static Builder newBuilder() {
//      return DEFAULT_INSTANCE.toBuilder();
//    }
//    public static Builder newBuilder(Hello.SayHelloRequest prototype) {
//      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
//    }
//    @Override
//    public Builder toBuilder() {
//      return this == DEFAULT_INSTANCE
//          ? new Builder() : new Builder().mergeFrom(this);
//    }
//
//    @Override
//    protected Builder newBuilderForType(
//        BuilderParent parent) {
//      Builder builder = new Builder(parent);
//      return builder;
//    }
//    /**
//     * Protobuf type {@code proto.SayHelloRequest}
//     */
//    public static final class Builder extends
//        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
//        // @@protoc_insertion_point(builder_implements:proto.SayHelloRequest)
//        Hello.SayHelloRequestOrBuilder {
//      public static final com.google.protobuf.Descriptors.Descriptor
//          getDescriptor() {
//        return Hello.internal_static_proto_SayHelloRequest_descriptor;
//      }
//
//      @Override
//      protected FieldAccessorTable
//          internalGetFieldAccessorTable() {
//        return Hello.internal_static_proto_SayHelloRequest_fieldAccessorTable
//            .ensureFieldAccessorsInitialized(
//                Hello.SayHelloRequest.class, Hello.SayHelloRequest.Builder.class);
//      }
//
//      // Construct using com.example.sharablead.Hello.SayHelloRequest.newBuilder()
//      private Builder() {
//        maybeForceBuilderInitialization();
//      }
//
//      private Builder(
//          BuilderParent parent) {
//        super(parent);
//        maybeForceBuilderInitialization();
//      }
//      private void maybeForceBuilderInitialization() {
//        if (com.google.protobuf.GeneratedMessageV3
//                .alwaysUseFieldBuilders) {
//        }
//      }
//      @Override
//      public Builder clear() {
//        super.clear();
//        name_ = com.google.protobuf.ByteString.EMPTY;
//
//        return this;
//      }
//
//      @Override
//      public com.google.protobuf.Descriptors.Descriptor
//          getDescriptorForType() {
//        return Hello.internal_static_proto_SayHelloRequest_descriptor;
//      }
//
//      @Override
//      public Hello.SayHelloRequest getDefaultInstanceForType() {
//        return Hello.SayHelloRequest.getDefaultInstance();
//      }
//
//      @Override
//      public Hello.SayHelloRequest build() {
//        Hello.SayHelloRequest result = buildPartial();
//        if (!result.isInitialized()) {
//          throw newUninitializedMessageException(result);
//        }
//        return result;
//      }
//
//      @Override
//      public Hello.SayHelloRequest buildPartial() {
//        Hello.SayHelloRequest result = new Hello.SayHelloRequest(this);
//        result.name_ = name_;
//        onBuilt();
//        return result;
//      }
//
//      @Override
//      public Builder clone() {
//        return super.clone();
//      }
//      @Override
//      public Builder setField(
//          com.google.protobuf.Descriptors.FieldDescriptor field,
//          Object value) {
//        return super.setField(field, value);
//      }
//      @Override
//      public Builder clearField(
//          com.google.protobuf.Descriptors.FieldDescriptor field) {
//        return super.clearField(field);
//      }
//      @Override
//      public Builder clearOneof(
//          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
//        return super.clearOneof(oneof);
//      }
//      @Override
//      public Builder setRepeatedField(
//          com.google.protobuf.Descriptors.FieldDescriptor field,
//          int index, Object value) {
//        return super.setRepeatedField(field, index, value);
//      }
//      @Override
//      public Builder addRepeatedField(
//          com.google.protobuf.Descriptors.FieldDescriptor field,
//          Object value) {
//        return super.addRepeatedField(field, value);
//      }
//      @Override
//      public Builder mergeFrom(com.google.protobuf.Message other) {
//        if (other instanceof Hello.SayHelloRequest) {
//          return mergeFrom((Hello.SayHelloRequest)other);
//        } else {
//          super.mergeFrom(other);
//          return this;
//        }
//      }
//
//      public Builder mergeFrom(Hello.SayHelloRequest other) {
//        if (other == Hello.SayHelloRequest.getDefaultInstance()) return this;
//        if (other.getName() != com.google.protobuf.ByteString.EMPTY) {
//          setName(other.getName());
//        }
//        this.mergeUnknownFields(other.unknownFields);
//        onChanged();
//        return this;
//      }
//
//      @Override
//      public final boolean isInitialized() {
//        return true;
//      }
//
//      @Override
//      public Builder mergeFrom(
//          com.google.protobuf.CodedInputStream input,
//          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//          throws java.io.IOException {
//        Hello.SayHelloRequest parsedMessage = null;
//        try {
//          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
//        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
//          parsedMessage = (Hello.SayHelloRequest) e.getUnfinishedMessage();
//          throw e.unwrapIOException();
//        } finally {
//          if (parsedMessage != null) {
//            mergeFrom(parsedMessage);
//          }
//        }
//        return this;
//      }
//
//      private com.google.protobuf.ByteString name_ = com.google.protobuf.ByteString.EMPTY;
//      /**
//       * <code>bytes name = 1;</code>
//       * @return The name.
//       */
//      @Override
//      public com.google.protobuf.ByteString getName() {
//        return name_;
//      }
//      /**
//       * <code>bytes name = 1;</code>
//       * @param value The name to set.
//       * @return This builder for chaining.
//       */
//      public Builder setName(com.google.protobuf.ByteString value) {
//        if (value == null) {
//    throw new NullPointerException();
//  }
//
//        name_ = value;
//        onChanged();
//        return this;
//      }
//      /**
//       * <code>bytes name = 1;</code>
//       * @return This builder for chaining.
//       */
//      public Builder clearName() {
//
//        name_ = getDefaultInstance().getName();
//        onChanged();
//        return this;
//      }
//      @Override
//      public final Builder setUnknownFields(
//          final com.google.protobuf.UnknownFieldSet unknownFields) {
//        return super.setUnknownFields(unknownFields);
//      }
//
//      @Override
//      public final Builder mergeUnknownFields(
//          final com.google.protobuf.UnknownFieldSet unknownFields) {
//        return super.mergeUnknownFields(unknownFields);
//      }
//
//
//      // @@protoc_insertion_point(builder_scope:proto.SayHelloRequest)
//    }
//
//    // @@protoc_insertion_point(class_scope:proto.SayHelloRequest)
//    private static final Hello.SayHelloRequest DEFAULT_INSTANCE;
//    static {
//      DEFAULT_INSTANCE = new Hello.SayHelloRequest();
//    }
//
//    public static Hello.SayHelloRequest getDefaultInstance() {
//      return DEFAULT_INSTANCE;
//    }
//
//    private static final com.google.protobuf.Parser<SayHelloRequest>
//        PARSER = new com.google.protobuf.AbstractParser<SayHelloRequest>() {
//      @Override
//      public SayHelloRequest parsePartialFrom(
//          com.google.protobuf.CodedInputStream input,
//          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//          throws com.google.protobuf.InvalidProtocolBufferException {
//        return new SayHelloRequest(input, extensionRegistry);
//      }
//    };
//
//    public static com.google.protobuf.Parser<SayHelloRequest> parser() {
//      return PARSER;
//    }
//
//    @Override
//    public com.google.protobuf.Parser<SayHelloRequest> getParserForType() {
//      return PARSER;
//    }
//
//    @Override
//    public Hello.SayHelloRequest getDefaultInstanceForType() {
//      return DEFAULT_INSTANCE;
//    }
//
//  }
//
//  public interface SayHelloResponseOrBuilder extends
//      // @@protoc_insertion_point(interface_extends:proto.SayHelloResponse)
//      com.google.protobuf.MessageOrBuilder {
//
//    /**
//     * <code>bytes result = 1;</code>
//     * @return The result.
//     */
//    com.google.protobuf.ByteString getResult();
//  }
//  /**
//   * Protobuf type {@code proto.SayHelloResponse}
//   */
//  public static final class SayHelloResponse extends
//      com.google.protobuf.GeneratedMessageV3 implements
//      // @@protoc_insertion_point(message_implements:proto.SayHelloResponse)
//      SayHelloResponseOrBuilder {
//  private static final long serialVersionUID = 0L;
//    // Use SayHelloResponse.newBuilder() to construct.
//    private SayHelloResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
//      super(builder);
//    }
//    private SayHelloResponse() {
//      result_ = com.google.protobuf.ByteString.EMPTY;
//    }
//
//    @Override
//    @SuppressWarnings({"unused"})
//    protected Object newInstance(
//        UnusedPrivateParameter unused) {
//      return new SayHelloResponse();
//    }
//
//    @Override
//    public final com.google.protobuf.UnknownFieldSet
//    getUnknownFields() {
//      return this.unknownFields;
//    }
//    private SayHelloResponse(
//        com.google.protobuf.CodedInputStream input,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      this();
//      if (extensionRegistry == null) {
//        throw new NullPointerException();
//      }
//      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
//          com.google.protobuf.UnknownFieldSet.newBuilder();
//      try {
//        boolean done = false;
//        while (!done) {
//          int tag = input.readTag();
//          switch (tag) {
//            case 0:
//              done = true;
//              break;
//            case 10: {
//
//              result_ = input.readBytes();
//              break;
//            }
//            default: {
//              if (!parseUnknownField(
//                  input, unknownFields, extensionRegistry, tag)) {
//                done = true;
//              }
//              break;
//            }
//          }
//        }
//      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
//        throw e.setUnfinishedMessage(this);
//      } catch (java.io.IOException e) {
//        throw new com.google.protobuf.InvalidProtocolBufferException(
//            e).setUnfinishedMessage(this);
//      } finally {
//        this.unknownFields = unknownFields.build();
//        makeExtensionsImmutable();
//      }
//    }
//    public static final com.google.protobuf.Descriptors.Descriptor
//        getDescriptor() {
//      return Hello.internal_static_proto_SayHelloResponse_descriptor;
//    }
//
//    @Override
//    protected FieldAccessorTable
//        internalGetFieldAccessorTable() {
//      return Hello.internal_static_proto_SayHelloResponse_fieldAccessorTable
//          .ensureFieldAccessorsInitialized(
//              Hello.SayHelloResponse.class, Hello.SayHelloResponse.Builder.class);
//    }
//
//    public static final int RESULT_FIELD_NUMBER = 1;
//    private com.google.protobuf.ByteString result_;
//    /**
//     * <code>bytes result = 1;</code>
//     * @return The result.
//     */
//    @Override
//    public com.google.protobuf.ByteString getResult() {
//      return result_;
//    }
//
//    private byte memoizedIsInitialized = -1;
//    @Override
//    public final boolean isInitialized() {
//      byte isInitialized = memoizedIsInitialized;
//      if (isInitialized == 1) return true;
//      if (isInitialized == 0) return false;
//
//      memoizedIsInitialized = 1;
//      return true;
//    }
//
//    @Override
//    public void writeTo(com.google.protobuf.CodedOutputStream output)
//                        throws java.io.IOException {
//      if (!result_.isEmpty()) {
//        output.writeBytes(1, result_);
//      }
//      unknownFields.writeTo(output);
//    }
//
//    @Override
//    public int getSerializedSize() {
//      int size = memoizedSize;
//      if (size != -1) return size;
//
//      size = 0;
//      if (!result_.isEmpty()) {
//        size += com.google.protobuf.CodedOutputStream
//          .computeBytesSize(1, result_);
//      }
//      size += unknownFields.getSerializedSize();
//      memoizedSize = size;
//      return size;
//    }
//
//    @Override
//    public boolean equals(final Object obj) {
//      if (obj == this) {
//       return true;
//      }
//      if (!(obj instanceof Hello.SayHelloResponse)) {
//        return super.equals(obj);
//      }
//      Hello.SayHelloResponse other = (Hello.SayHelloResponse) obj;
//
//      if (!getResult()
//          .equals(other.getResult())) return false;
//      if (!unknownFields.equals(other.unknownFields)) return false;
//      return true;
//    }
//
//    @Override
//    public int hashCode() {
//      if (memoizedHashCode != 0) {
//        return memoizedHashCode;
//      }
//      int hash = 41;
//      hash = (19 * hash) + getDescriptor().hashCode();
//      hash = (37 * hash) + RESULT_FIELD_NUMBER;
//      hash = (53 * hash) + getResult().hashCode();
//      hash = (29 * hash) + unknownFields.hashCode();
//      memoizedHashCode = hash;
//      return hash;
//    }
//
//    public static Hello.SayHelloResponse parseFrom(
//        java.nio.ByteBuffer data)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data);
//    }
//    public static Hello.SayHelloResponse parseFrom(
//        java.nio.ByteBuffer data,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data, extensionRegistry);
//    }
//    public static Hello.SayHelloResponse parseFrom(
//        com.google.protobuf.ByteString data)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data);
//    }
//    public static Hello.SayHelloResponse parseFrom(
//        com.google.protobuf.ByteString data,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data, extensionRegistry);
//    }
//    public static Hello.SayHelloResponse parseFrom(byte[] data)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data);
//    }
//    public static Hello.SayHelloResponse parseFrom(
//        byte[] data,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws com.google.protobuf.InvalidProtocolBufferException {
//      return PARSER.parseFrom(data, extensionRegistry);
//    }
//    public static Hello.SayHelloResponse parseFrom(java.io.InputStream input)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseWithIOException(PARSER, input);
//    }
//    public static Hello.SayHelloResponse parseFrom(
//        java.io.InputStream input,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseWithIOException(PARSER, input, extensionRegistry);
//    }
//    public static Hello.SayHelloResponse parseDelimitedFrom(java.io.InputStream input)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseDelimitedWithIOException(PARSER, input);
//    }
//    public static Hello.SayHelloResponse parseDelimitedFrom(
//        java.io.InputStream input,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
//    }
//    public static Hello.SayHelloResponse parseFrom(
//        com.google.protobuf.CodedInputStream input)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseWithIOException(PARSER, input);
//    }
//    public static Hello.SayHelloResponse parseFrom(
//        com.google.protobuf.CodedInputStream input,
//        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//        throws java.io.IOException {
//      return com.google.protobuf.GeneratedMessageV3
//          .parseWithIOException(PARSER, input, extensionRegistry);
//    }
//
//    @Override
//    public Builder newBuilderForType() { return newBuilder(); }
//    public static Builder newBuilder() {
//      return DEFAULT_INSTANCE.toBuilder();
//    }
//    public static Builder newBuilder(Hello.SayHelloResponse prototype) {
//      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
//    }
//    @Override
//    public Builder toBuilder() {
//      return this == DEFAULT_INSTANCE
//          ? new Builder() : new Builder().mergeFrom(this);
//    }
//
//    @Override
//    protected Builder newBuilderForType(
//        BuilderParent parent) {
//      Builder builder = new Builder(parent);
//      return builder;
//    }
//    /**
//     * Protobuf type {@code proto.SayHelloResponse}
//     */
//    public static final class Builder extends
//        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
//        // @@protoc_insertion_point(builder_implements:proto.SayHelloResponse)
//        Hello.SayHelloResponseOrBuilder {
//      public static final com.google.protobuf.Descriptors.Descriptor
//          getDescriptor() {
//        return Hello.internal_static_proto_SayHelloResponse_descriptor;
//      }
//
//      @Override
//      protected FieldAccessorTable
//          internalGetFieldAccessorTable() {
//        return Hello.internal_static_proto_SayHelloResponse_fieldAccessorTable
//            .ensureFieldAccessorsInitialized(
//                Hello.SayHelloResponse.class, Hello.SayHelloResponse.Builder.class);
//      }
//
//      // Construct using com.example.sharablead.Hello.SayHelloResponse.newBuilder()
//      private Builder() {
//        maybeForceBuilderInitialization();
//      }
//
//      private Builder(
//          BuilderParent parent) {
//        super(parent);
//        maybeForceBuilderInitialization();
//      }
//      private void maybeForceBuilderInitialization() {
//        if (com.google.protobuf.GeneratedMessageV3
//                .alwaysUseFieldBuilders) {
//        }
//      }
//      @Override
//      public Builder clear() {
//        super.clear();
//        result_ = com.google.protobuf.ByteString.EMPTY;
//
//        return this;
//      }
//
//      @Override
//      public com.google.protobuf.Descriptors.Descriptor
//          getDescriptorForType() {
//        return Hello.internal_static_proto_SayHelloResponse_descriptor;
//      }
//
//      @Override
//      public Hello.SayHelloResponse getDefaultInstanceForType() {
//        return Hello.SayHelloResponse.getDefaultInstance();
//      }
//
//      @Override
//      public Hello.SayHelloResponse build() {
//        Hello.SayHelloResponse result = buildPartial();
//        if (!result.isInitialized()) {
//          throw newUninitializedMessageException(result);
//        }
//        return result;
//      }
//
//      @Override
//      public Hello.SayHelloResponse buildPartial() {
//        Hello.SayHelloResponse result = new Hello.SayHelloResponse(this);
//        result.result_ = result_;
//        onBuilt();
//        return result;
//      }
//
//      @Override
//      public Builder clone() {
//        return super.clone();
//      }
//      @Override
//      public Builder setField(
//          com.google.protobuf.Descriptors.FieldDescriptor field,
//          Object value) {
//        return super.setField(field, value);
//      }
//      @Override
//      public Builder clearField(
//          com.google.protobuf.Descriptors.FieldDescriptor field) {
//        return super.clearField(field);
//      }
//      @Override
//      public Builder clearOneof(
//          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
//        return super.clearOneof(oneof);
//      }
//      @Override
//      public Builder setRepeatedField(
//          com.google.protobuf.Descriptors.FieldDescriptor field,
//          int index, Object value) {
//        return super.setRepeatedField(field, index, value);
//      }
//      @Override
//      public Builder addRepeatedField(
//          com.google.protobuf.Descriptors.FieldDescriptor field,
//          Object value) {
//        return super.addRepeatedField(field, value);
//      }
//      @Override
//      public Builder mergeFrom(com.google.protobuf.Message other) {
//        if (other instanceof Hello.SayHelloResponse) {
//          return mergeFrom((Hello.SayHelloResponse)other);
//        } else {
//          super.mergeFrom(other);
//          return this;
//        }
//      }
//
//      public Builder mergeFrom(Hello.SayHelloResponse other) {
//        if (other == Hello.SayHelloResponse.getDefaultInstance()) return this;
//        if (other.getResult() != com.google.protobuf.ByteString.EMPTY) {
//          setResult(other.getResult());
//        }
//        this.mergeUnknownFields(other.unknownFields);
//        onChanged();
//        return this;
//      }
//
//      @Override
//      public final boolean isInitialized() {
//        return true;
//      }
//
//      @Override
//      public Builder mergeFrom(
//          com.google.protobuf.CodedInputStream input,
//          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//          throws java.io.IOException {
//        Hello.SayHelloResponse parsedMessage = null;
//        try {
//          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
//        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
//          parsedMessage = (Hello.SayHelloResponse) e.getUnfinishedMessage();
//          throw e.unwrapIOException();
//        } finally {
//          if (parsedMessage != null) {
//            mergeFrom(parsedMessage);
//          }
//        }
//        return this;
//      }
//
//      private com.google.protobuf.ByteString result_ = com.google.protobuf.ByteString.EMPTY;
//      /**
//       * <code>bytes result = 1;</code>
//       * @return The result.
//       */
//      @Override
//      public com.google.protobuf.ByteString getResult() {
//        return result_;
//      }
//      /**
//       * <code>bytes result = 1;</code>
//       * @param value The result to set.
//       * @return This builder for chaining.
//       */
//      public Builder setResult(com.google.protobuf.ByteString value) {
//        if (value == null) {
//    throw new NullPointerException();
//  }
//
//        result_ = value;
//        onChanged();
//        return this;
//      }
//      /**
//       * <code>bytes result = 1;</code>
//       * @return This builder for chaining.
//       */
//      public Builder clearResult() {
//
//        result_ = getDefaultInstance().getResult();
//        onChanged();
//        return this;
//      }
//      @Override
//      public final Builder setUnknownFields(
//          final com.google.protobuf.UnknownFieldSet unknownFields) {
//        return super.setUnknownFields(unknownFields);
//      }
//
//      @Override
//      public final Builder mergeUnknownFields(
//          final com.google.protobuf.UnknownFieldSet unknownFields) {
//        return super.mergeUnknownFields(unknownFields);
//      }
//
//
//      // @@protoc_insertion_point(builder_scope:proto.SayHelloResponse)
//    }
//
//    // @@protoc_insertion_point(class_scope:proto.SayHelloResponse)
//    private static final Hello.SayHelloResponse DEFAULT_INSTANCE;
//    static {
//      DEFAULT_INSTANCE = new Hello.SayHelloResponse();
//    }
//
//    public static Hello.SayHelloResponse getDefaultInstance() {
//      return DEFAULT_INSTANCE;
//    }
//
//    private static final com.google.protobuf.Parser<SayHelloResponse>
//        PARSER = new com.google.protobuf.AbstractParser<SayHelloResponse>() {
//      @Override
//      public SayHelloResponse parsePartialFrom(
//          com.google.protobuf.CodedInputStream input,
//          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
//          throws com.google.protobuf.InvalidProtocolBufferException {
//        return new SayHelloResponse(input, extensionRegistry);
//      }
//    };
//
//    public static com.google.protobuf.Parser<SayHelloResponse> parser() {
//      return PARSER;
//    }
//
//    @Override
//    public com.google.protobuf.Parser<SayHelloResponse> getParserForType() {
//      return PARSER;
//    }
//
//    @Override
//    public Hello.SayHelloResponse getDefaultInstanceForType() {
//      return DEFAULT_INSTANCE;
//    }
//
//  }
//
//  private static final com.google.protobuf.Descriptors.Descriptor
//    internal_static_proto_SayHelloRequest_descriptor;
//  private static final
//    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
//      internal_static_proto_SayHelloRequest_fieldAccessorTable;
//  private static final com.google.protobuf.Descriptors.Descriptor
//    internal_static_proto_SayHelloResponse_descriptor;
//  private static final
//    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
//      internal_static_proto_SayHelloResponse_fieldAccessorTable;
//
//  public static com.google.protobuf.Descriptors.FileDescriptor
//      getDescriptor() {
//    return descriptor;
//  }
//  private static  com.google.protobuf.Descriptors.FileDescriptor
//      descriptor;
//  static {
//    String[] descriptorData = {
//      "\n\013hello.proto\022\005proto\"\037\n\017SayHelloRequest\022" +
//      "\014\n\004name\030\001 \001(\014\"\"\n\020SayHelloResponse\022\016\n\006res" +
//      "ult\030\001 \001(\0142N\n\017SayHelloService\022;\n\010SayHello" +
//      "\022\026.proto.SayHelloRequest\032\027.proto.SayHell" +
//      "oResponseB\032\n\026com.example.sharableadP\000b\006p" +
//      "roto3"
//    };
//    descriptor = com.google.protobuf.Descriptors.FileDescriptor
//      .internalBuildGeneratedFileFrom(descriptorData,
//        new com.google.protobuf.Descriptors.FileDescriptor[] {
//        });
//    internal_static_proto_SayHelloRequest_descriptor =
//      getDescriptor().getMessageTypes().get(0);
//    internal_static_proto_SayHelloRequest_fieldAccessorTable = new
//      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
//        internal_static_proto_SayHelloRequest_descriptor,
//        new String[] { "Name", });
//    internal_static_proto_SayHelloResponse_descriptor =
//      getDescriptor().getMessageTypes().get(1);
//    internal_static_proto_SayHelloResponse_fieldAccessorTable = new
//      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
//        internal_static_proto_SayHelloResponse_descriptor,
//        new String[] { "Result", });
//  }
//
//  // @@protoc_insertion_point(outer_class_scope)
//}
