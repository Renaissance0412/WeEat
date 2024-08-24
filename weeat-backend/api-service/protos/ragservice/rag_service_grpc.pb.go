// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.4.0
// - protoc             v3.19.4
// source: rag_service.proto

package ragservice

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.62.0 or later.
const _ = grpc.SupportPackageIsVersion8

const (
	RAGService_StreamChat_FullMethodName        = "/rag.RAGService/StreamChat"
	RAGService_SyncChat_FullMethodName          = "/rag.RAGService/SyncChat"
	RAGService_CreateUser_FullMethodName        = "/rag.RAGService/CreateUser"
	RAGService_CheckUserValid_FullMethodName    = "/rag.RAGService/CheckUserValid"
	RAGService_CreateChatContext_FullMethodName = "/rag.RAGService/CreateChatContext"
	RAGService_FlushChatHistory_FullMethodName  = "/rag.RAGService/FlushChatHistory"
)

// RAGServiceClient is the client API for RAGService service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
//
// Exporty RAG service for client
type RAGServiceClient interface {
	// Chat with stream response method
	StreamChat(ctx context.Context, in *ChatMessageRequest, opts ...grpc.CallOption) (RAGService_StreamChatClient, error)
	// Chat with sync response method
	SyncChat(ctx context.Context, in *ChatMessageRequest, opts ...grpc.CallOption) (*ChatMessageResponse, error)
	// Create new user
	CreateUser(ctx context.Context, in *CreateUserRequest, opts ...grpc.CallOption) (*CreateUserResponse, error)
	// Check user valid
	CheckUserValid(ctx context.Context, in *CheckUserRequest, opts ...grpc.CallOption) (*CheckUserResponse, error)
	// Create new chat context
	CreateChatContext(ctx context.Context, in *CreateContextRequest, opts ...grpc.CallOption) (*CreateContextResponse, error)
	// Flush chat history to database
	FlushChatHistory(ctx context.Context, in *FlushContextRequest, opts ...grpc.CallOption) (*FlushContextResponse, error)
}

type rAGServiceClient struct {
	cc grpc.ClientConnInterface
}

func NewRAGServiceClient(cc grpc.ClientConnInterface) RAGServiceClient {
	return &rAGServiceClient{cc}
}

func (c *rAGServiceClient) StreamChat(ctx context.Context, in *ChatMessageRequest, opts ...grpc.CallOption) (RAGService_StreamChatClient, error) {
	cOpts := append([]grpc.CallOption{grpc.StaticMethod()}, opts...)
	stream, err := c.cc.NewStream(ctx, &RAGService_ServiceDesc.Streams[0], RAGService_StreamChat_FullMethodName, cOpts...)
	if err != nil {
		return nil, err
	}
	x := &rAGServiceStreamChatClient{ClientStream: stream}
	if err := x.ClientStream.SendMsg(in); err != nil {
		return nil, err
	}
	if err := x.ClientStream.CloseSend(); err != nil {
		return nil, err
	}
	return x, nil
}

type RAGService_StreamChatClient interface {
	Recv() (*ChatMessageResponse, error)
	grpc.ClientStream
}

type rAGServiceStreamChatClient struct {
	grpc.ClientStream
}

func (x *rAGServiceStreamChatClient) Recv() (*ChatMessageResponse, error) {
	m := new(ChatMessageResponse)
	if err := x.ClientStream.RecvMsg(m); err != nil {
		return nil, err
	}
	return m, nil
}

func (c *rAGServiceClient) SyncChat(ctx context.Context, in *ChatMessageRequest, opts ...grpc.CallOption) (*ChatMessageResponse, error) {
	cOpts := append([]grpc.CallOption{grpc.StaticMethod()}, opts...)
	out := new(ChatMessageResponse)
	err := c.cc.Invoke(ctx, RAGService_SyncChat_FullMethodName, in, out, cOpts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *rAGServiceClient) CreateUser(ctx context.Context, in *CreateUserRequest, opts ...grpc.CallOption) (*CreateUserResponse, error) {
	cOpts := append([]grpc.CallOption{grpc.StaticMethod()}, opts...)
	out := new(CreateUserResponse)
	err := c.cc.Invoke(ctx, RAGService_CreateUser_FullMethodName, in, out, cOpts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *rAGServiceClient) CheckUserValid(ctx context.Context, in *CheckUserRequest, opts ...grpc.CallOption) (*CheckUserResponse, error) {
	cOpts := append([]grpc.CallOption{grpc.StaticMethod()}, opts...)
	out := new(CheckUserResponse)
	err := c.cc.Invoke(ctx, RAGService_CheckUserValid_FullMethodName, in, out, cOpts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *rAGServiceClient) CreateChatContext(ctx context.Context, in *CreateContextRequest, opts ...grpc.CallOption) (*CreateContextResponse, error) {
	cOpts := append([]grpc.CallOption{grpc.StaticMethod()}, opts...)
	out := new(CreateContextResponse)
	err := c.cc.Invoke(ctx, RAGService_CreateChatContext_FullMethodName, in, out, cOpts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *rAGServiceClient) FlushChatHistory(ctx context.Context, in *FlushContextRequest, opts ...grpc.CallOption) (*FlushContextResponse, error) {
	cOpts := append([]grpc.CallOption{grpc.StaticMethod()}, opts...)
	out := new(FlushContextResponse)
	err := c.cc.Invoke(ctx, RAGService_FlushChatHistory_FullMethodName, in, out, cOpts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// RAGServiceServer is the server API for RAGService service.
// All implementations must embed UnimplementedRAGServiceServer
// for forward compatibility
//
// Exporty RAG service for client
type RAGServiceServer interface {
	// Chat with stream response method
	StreamChat(*ChatMessageRequest, RAGService_StreamChatServer) error
	// Chat with sync response method
	SyncChat(context.Context, *ChatMessageRequest) (*ChatMessageResponse, error)
	// Create new user
	CreateUser(context.Context, *CreateUserRequest) (*CreateUserResponse, error)
	// Check user valid
	CheckUserValid(context.Context, *CheckUserRequest) (*CheckUserResponse, error)
	// Create new chat context
	CreateChatContext(context.Context, *CreateContextRequest) (*CreateContextResponse, error)
	// Flush chat history to database
	FlushChatHistory(context.Context, *FlushContextRequest) (*FlushContextResponse, error)
	mustEmbedUnimplementedRAGServiceServer()
}

// UnimplementedRAGServiceServer must be embedded to have forward compatible implementations.
type UnimplementedRAGServiceServer struct {
}

func (UnimplementedRAGServiceServer) StreamChat(*ChatMessageRequest, RAGService_StreamChatServer) error {
	return status.Errorf(codes.Unimplemented, "method StreamChat not implemented")
}
func (UnimplementedRAGServiceServer) SyncChat(context.Context, *ChatMessageRequest) (*ChatMessageResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method SyncChat not implemented")
}
func (UnimplementedRAGServiceServer) CreateUser(context.Context, *CreateUserRequest) (*CreateUserResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method CreateUser not implemented")
}
func (UnimplementedRAGServiceServer) CheckUserValid(context.Context, *CheckUserRequest) (*CheckUserResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method CheckUserValid not implemented")
}
func (UnimplementedRAGServiceServer) CreateChatContext(context.Context, *CreateContextRequest) (*CreateContextResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method CreateChatContext not implemented")
}
func (UnimplementedRAGServiceServer) FlushChatHistory(context.Context, *FlushContextRequest) (*FlushContextResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method FlushChatHistory not implemented")
}
func (UnimplementedRAGServiceServer) mustEmbedUnimplementedRAGServiceServer() {}

// UnsafeRAGServiceServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to RAGServiceServer will
// result in compilation errors.
type UnsafeRAGServiceServer interface {
	mustEmbedUnimplementedRAGServiceServer()
}

func RegisterRAGServiceServer(s grpc.ServiceRegistrar, srv RAGServiceServer) {
	s.RegisterService(&RAGService_ServiceDesc, srv)
}

func _RAGService_StreamChat_Handler(srv interface{}, stream grpc.ServerStream) error {
	m := new(ChatMessageRequest)
	if err := stream.RecvMsg(m); err != nil {
		return err
	}
	return srv.(RAGServiceServer).StreamChat(m, &rAGServiceStreamChatServer{ServerStream: stream})
}

type RAGService_StreamChatServer interface {
	Send(*ChatMessageResponse) error
	grpc.ServerStream
}

type rAGServiceStreamChatServer struct {
	grpc.ServerStream
}

func (x *rAGServiceStreamChatServer) Send(m *ChatMessageResponse) error {
	return x.ServerStream.SendMsg(m)
}

func _RAGService_SyncChat_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(ChatMessageRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(RAGServiceServer).SyncChat(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: RAGService_SyncChat_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(RAGServiceServer).SyncChat(ctx, req.(*ChatMessageRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _RAGService_CreateUser_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(CreateUserRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(RAGServiceServer).CreateUser(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: RAGService_CreateUser_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(RAGServiceServer).CreateUser(ctx, req.(*CreateUserRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _RAGService_CheckUserValid_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(CheckUserRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(RAGServiceServer).CheckUserValid(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: RAGService_CheckUserValid_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(RAGServiceServer).CheckUserValid(ctx, req.(*CheckUserRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _RAGService_CreateChatContext_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(CreateContextRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(RAGServiceServer).CreateChatContext(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: RAGService_CreateChatContext_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(RAGServiceServer).CreateChatContext(ctx, req.(*CreateContextRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _RAGService_FlushChatHistory_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(FlushContextRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(RAGServiceServer).FlushChatHistory(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: RAGService_FlushChatHistory_FullMethodName,
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(RAGServiceServer).FlushChatHistory(ctx, req.(*FlushContextRequest))
	}
	return interceptor(ctx, in, info, handler)
}

// RAGService_ServiceDesc is the grpc.ServiceDesc for RAGService service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var RAGService_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "rag.RAGService",
	HandlerType: (*RAGServiceServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "SyncChat",
			Handler:    _RAGService_SyncChat_Handler,
		},
		{
			MethodName: "CreateUser",
			Handler:    _RAGService_CreateUser_Handler,
		},
		{
			MethodName: "CheckUserValid",
			Handler:    _RAGService_CheckUserValid_Handler,
		},
		{
			MethodName: "CreateChatContext",
			Handler:    _RAGService_CreateChatContext_Handler,
		},
		{
			MethodName: "FlushChatHistory",
			Handler:    _RAGService_FlushChatHistory_Handler,
		},
	},
	Streams: []grpc.StreamDesc{
		{
			StreamName:    "StreamChat",
			Handler:       _RAGService_StreamChat_Handler,
			ServerStreams: true,
		},
	},
	Metadata: "rag_service.proto",
}