// Code generated by protoc-gen-go. DO NOT EDIT.
// versions:
// 	protoc-gen-go v1.34.2
// 	protoc        v3.19.4
// source: recognition_service.proto

package recognitionservice

import (
	protoreflect "google.golang.org/protobuf/reflect/protoreflect"
	protoimpl "google.golang.org/protobuf/runtime/protoimpl"
	reflect "reflect"
	sync "sync"
)

const (
	// Verify that this generated code is sufficiently up-to-date.
	_ = protoimpl.EnforceVersion(20 - protoimpl.MinVersion)
	// Verify that runtime/protoimpl is sufficiently up-to-date.
	_ = protoimpl.EnforceVersion(protoimpl.MaxVersion - 20)
)

type RecognitionRequest struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Image []byte `protobuf:"bytes,1,opt,name=image,proto3" json:"image,omitempty"`
}

func (x *RecognitionRequest) Reset() {
	*x = RecognitionRequest{}
	if protoimpl.UnsafeEnabled {
		mi := &file_recognition_service_proto_msgTypes[0]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *RecognitionRequest) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*RecognitionRequest) ProtoMessage() {}

func (x *RecognitionRequest) ProtoReflect() protoreflect.Message {
	mi := &file_recognition_service_proto_msgTypes[0]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use RecognitionRequest.ProtoReflect.Descriptor instead.
func (*RecognitionRequest) Descriptor() ([]byte, []int) {
	return file_recognition_service_proto_rawDescGZIP(), []int{0}
}

func (x *RecognitionRequest) GetImage() []byte {
	if x != nil {
		return x.Image
	}
	return nil
}

type RecognitionResponseItem struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Name       string  `protobuf:"bytes,1,opt,name=name,proto3" json:"name,omitempty"`
	Confidence float32 `protobuf:"fixed32,2,opt,name=confidence,proto3" json:"confidence,omitempty"`
}

func (x *RecognitionResponseItem) Reset() {
	*x = RecognitionResponseItem{}
	if protoimpl.UnsafeEnabled {
		mi := &file_recognition_service_proto_msgTypes[1]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *RecognitionResponseItem) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*RecognitionResponseItem) ProtoMessage() {}

func (x *RecognitionResponseItem) ProtoReflect() protoreflect.Message {
	mi := &file_recognition_service_proto_msgTypes[1]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use RecognitionResponseItem.ProtoReflect.Descriptor instead.
func (*RecognitionResponseItem) Descriptor() ([]byte, []int) {
	return file_recognition_service_proto_rawDescGZIP(), []int{1}
}

func (x *RecognitionResponseItem) GetName() string {
	if x != nil {
		return x.Name
	}
	return ""
}

func (x *RecognitionResponseItem) GetConfidence() float32 {
	if x != nil {
		return x.Confidence
	}
	return 0
}

type RecognitionResponse struct {
	state         protoimpl.MessageState
	sizeCache     protoimpl.SizeCache
	unknownFields protoimpl.UnknownFields

	Items []*RecognitionResponseItem `protobuf:"bytes,1,rep,name=items,proto3" json:"items,omitempty"`
	Count int32                      `protobuf:"varint,2,opt,name=count,proto3" json:"count,omitempty"`
}

func (x *RecognitionResponse) Reset() {
	*x = RecognitionResponse{}
	if protoimpl.UnsafeEnabled {
		mi := &file_recognition_service_proto_msgTypes[2]
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		ms.StoreMessageInfo(mi)
	}
}

func (x *RecognitionResponse) String() string {
	return protoimpl.X.MessageStringOf(x)
}

func (*RecognitionResponse) ProtoMessage() {}

func (x *RecognitionResponse) ProtoReflect() protoreflect.Message {
	mi := &file_recognition_service_proto_msgTypes[2]
	if protoimpl.UnsafeEnabled && x != nil {
		ms := protoimpl.X.MessageStateOf(protoimpl.Pointer(x))
		if ms.LoadMessageInfo() == nil {
			ms.StoreMessageInfo(mi)
		}
		return ms
	}
	return mi.MessageOf(x)
}

// Deprecated: Use RecognitionResponse.ProtoReflect.Descriptor instead.
func (*RecognitionResponse) Descriptor() ([]byte, []int) {
	return file_recognition_service_proto_rawDescGZIP(), []int{2}
}

func (x *RecognitionResponse) GetItems() []*RecognitionResponseItem {
	if x != nil {
		return x.Items
	}
	return nil
}

func (x *RecognitionResponse) GetCount() int32 {
	if x != nil {
		return x.Count
	}
	return 0
}

var File_recognition_service_proto protoreflect.FileDescriptor

var file_recognition_service_proto_rawDesc = []byte{
	0x0a, 0x19, 0x72, 0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x5f, 0x73, 0x65,
	0x72, 0x76, 0x69, 0x63, 0x65, 0x2e, 0x70, 0x72, 0x6f, 0x74, 0x6f, 0x12, 0x0b, 0x72, 0x65, 0x63,
	0x6f, 0x67, 0x6e, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x22, 0x2a, 0x0a, 0x12, 0x52, 0x65, 0x63, 0x6f,
	0x67, 0x6e, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x52, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74, 0x12, 0x14,
	0x0a, 0x05, 0x69, 0x6d, 0x61, 0x67, 0x65, 0x18, 0x01, 0x20, 0x01, 0x28, 0x0c, 0x52, 0x05, 0x69,
	0x6d, 0x61, 0x67, 0x65, 0x22, 0x4d, 0x0a, 0x17, 0x52, 0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x74,
	0x69, 0x6f, 0x6e, 0x52, 0x65, 0x73, 0x70, 0x6f, 0x6e, 0x73, 0x65, 0x49, 0x74, 0x65, 0x6d, 0x12,
	0x12, 0x0a, 0x04, 0x6e, 0x61, 0x6d, 0x65, 0x18, 0x01, 0x20, 0x01, 0x28, 0x09, 0x52, 0x04, 0x6e,
	0x61, 0x6d, 0x65, 0x12, 0x1e, 0x0a, 0x0a, 0x63, 0x6f, 0x6e, 0x66, 0x69, 0x64, 0x65, 0x6e, 0x63,
	0x65, 0x18, 0x02, 0x20, 0x01, 0x28, 0x02, 0x52, 0x0a, 0x63, 0x6f, 0x6e, 0x66, 0x69, 0x64, 0x65,
	0x6e, 0x63, 0x65, 0x22, 0x67, 0x0a, 0x13, 0x52, 0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x74, 0x69,
	0x6f, 0x6e, 0x52, 0x65, 0x73, 0x70, 0x6f, 0x6e, 0x73, 0x65, 0x12, 0x3a, 0x0a, 0x05, 0x69, 0x74,
	0x65, 0x6d, 0x73, 0x18, 0x01, 0x20, 0x03, 0x28, 0x0b, 0x32, 0x24, 0x2e, 0x72, 0x65, 0x63, 0x6f,
	0x67, 0x6e, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x2e, 0x52, 0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x74,
	0x69, 0x6f, 0x6e, 0x52, 0x65, 0x73, 0x70, 0x6f, 0x6e, 0x73, 0x65, 0x49, 0x74, 0x65, 0x6d, 0x52,
	0x05, 0x69, 0x74, 0x65, 0x6d, 0x73, 0x12, 0x14, 0x0a, 0x05, 0x63, 0x6f, 0x75, 0x6e, 0x74, 0x18,
	0x02, 0x20, 0x01, 0x28, 0x05, 0x52, 0x05, 0x63, 0x6f, 0x75, 0x6e, 0x74, 0x32, 0x64, 0x0a, 0x12,
	0x52, 0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x53, 0x65, 0x72, 0x76, 0x69,
	0x63, 0x65, 0x12, 0x4e, 0x0a, 0x09, 0x52, 0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x7a, 0x65, 0x12,
	0x1f, 0x2e, 0x72, 0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x2e, 0x52, 0x65,
	0x63, 0x6f, 0x67, 0x6e, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x52, 0x65, 0x71, 0x75, 0x65, 0x73, 0x74,
	0x1a, 0x20, 0x2e, 0x72, 0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x2e, 0x52,
	0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x74, 0x69, 0x6f, 0x6e, 0x52, 0x65, 0x73, 0x70, 0x6f, 0x6e,
	0x73, 0x65, 0x42, 0x16, 0x5a, 0x14, 0x2e, 0x2f, 0x72, 0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x74,
	0x69, 0x6f, 0x6e, 0x73, 0x65, 0x72, 0x76, 0x69, 0x63, 0x65, 0x62, 0x06, 0x70, 0x72, 0x6f, 0x74,
	0x6f, 0x33,
}

var (
	file_recognition_service_proto_rawDescOnce sync.Once
	file_recognition_service_proto_rawDescData = file_recognition_service_proto_rawDesc
)

func file_recognition_service_proto_rawDescGZIP() []byte {
	file_recognition_service_proto_rawDescOnce.Do(func() {
		file_recognition_service_proto_rawDescData = protoimpl.X.CompressGZIP(file_recognition_service_proto_rawDescData)
	})
	return file_recognition_service_proto_rawDescData
}

var file_recognition_service_proto_msgTypes = make([]protoimpl.MessageInfo, 3)
var file_recognition_service_proto_goTypes = []any{
	(*RecognitionRequest)(nil),      // 0: recognition.RecognitionRequest
	(*RecognitionResponseItem)(nil), // 1: recognition.RecognitionResponseItem
	(*RecognitionResponse)(nil),     // 2: recognition.RecognitionResponse
}
var file_recognition_service_proto_depIdxs = []int32{
	1, // 0: recognition.RecognitionResponse.items:type_name -> recognition.RecognitionResponseItem
	0, // 1: recognition.RecognitionService.Recognize:input_type -> recognition.RecognitionRequest
	2, // 2: recognition.RecognitionService.Recognize:output_type -> recognition.RecognitionResponse
	2, // [2:3] is the sub-list for method output_type
	1, // [1:2] is the sub-list for method input_type
	1, // [1:1] is the sub-list for extension type_name
	1, // [1:1] is the sub-list for extension extendee
	0, // [0:1] is the sub-list for field type_name
}

func init() { file_recognition_service_proto_init() }
func file_recognition_service_proto_init() {
	if File_recognition_service_proto != nil {
		return
	}
	if !protoimpl.UnsafeEnabled {
		file_recognition_service_proto_msgTypes[0].Exporter = func(v any, i int) any {
			switch v := v.(*RecognitionRequest); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_recognition_service_proto_msgTypes[1].Exporter = func(v any, i int) any {
			switch v := v.(*RecognitionResponseItem); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
		file_recognition_service_proto_msgTypes[2].Exporter = func(v any, i int) any {
			switch v := v.(*RecognitionResponse); i {
			case 0:
				return &v.state
			case 1:
				return &v.sizeCache
			case 2:
				return &v.unknownFields
			default:
				return nil
			}
		}
	}
	type x struct{}
	out := protoimpl.TypeBuilder{
		File: protoimpl.DescBuilder{
			GoPackagePath: reflect.TypeOf(x{}).PkgPath(),
			RawDescriptor: file_recognition_service_proto_rawDesc,
			NumEnums:      0,
			NumMessages:   3,
			NumExtensions: 0,
			NumServices:   1,
		},
		GoTypes:           file_recognition_service_proto_goTypes,
		DependencyIndexes: file_recognition_service_proto_depIdxs,
		MessageInfos:      file_recognition_service_proto_msgTypes,
	}.Build()
	File_recognition_service_proto = out.File
	file_recognition_service_proto_rawDesc = nil
	file_recognition_service_proto_goTypes = nil
	file_recognition_service_proto_depIdxs = nil
}
