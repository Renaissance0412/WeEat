# For development
gen_llm_service_grpc:
	@echo "Generating LLM service grpc server"
	@python -m grpc_tools.protoc -I./llm-service/proto --python_out=./llm-service/rpc --grpc_python_out=./llm-service/rpc ./llm-service/proto/llm_service.proto
	@echo "Done"

build_llm_service_docker:
	@echo "Building LLM service docker image"
	@docker buildx build -t weeat-llm-service -f ./llm-service/Dockerfile ./llm-service
	@echo "Done"

gen_asr_service_grpc:
	@echo "Generating ASR service grpc server"
	@python -m grpc_tools.protoc -I./asr-service/proto --python_out=./asr-service/rpc --grpc_python_out=./asr-service/rpc ./asr-service/proto/asr_service.proto
	@echo "Done"

build_asr_service_docker:
	@echo "Building ASR service docker image"
	@docker buildx build -t weeat-asr-service -f ./asr-service/Dockerfile ./asr-service
	@echo "Done"

gen_recognition_service_grpc:
	@echo "Generating Recognition service grpc server"
	@python -m grpc_tools.protoc -I./recognition-service/proto --python_out=./recognition-service/rpc --grpc_python_out=./recognition-service/rpc ./recognition-service/proto/recognition_service.proto
	@echo "Done"

build_recognition_service_docker:
	@echo "Building Recognition service docker image"
	@docker buildx build -t weeat-recognition-service -f ./recognition-service/Dockerfile ./recognition-service
	@echo "Done"

gen_rag_service_grpc:
	@echo "Generating RAG service grpc server"
	@python -m grpc_tools.protoc -I./rag-service/proto --python_out=./rag-service/rpc --grpc_python_out=./rag-service/rpc ./rag-service/proto/rag_service.proto
	@echo "Done"

tidy:
	@echo "Tidying up go modules"
	@(cd api-service & go mod tidy)
	@echo "Done"

gen_api_service_code:
	@echo "Generating API service code"
	@goctl api go -api ./api-service/weeat.api -dir ./api-service
	@echo "Done"

gen_api_service_rpc_code:
	@echo "Generating API service rpc code"
	# @for proto in $(wildcard ./api-service/protos/*.proto); do \
	# 	goctl rpc protoc $$proto --go_out=./api-service/rpc --go-grpc_out=./api-service/rpc --zrpc_out=./api-service/rpc; \
	# done
	@echo "Done"

gen_api_service_swagger:
	@echo "Generating Swagger..."
	@goctl api plugin -plugin goctl-swagger="swagger -filename weeat.json -host 127.0.0.1:8888" \
		-api ./api-service/weeat.api -dir ./api-service
	@echo "Swagger generated"

run_api_service_swagger:
	@echo "Running Swagger UI..."
	@docker run -p 8084:8080 -e SWAGGER_JSON=/api-service/weeat.json -v $(PWD)/api-service:/api-service swaggerapi/swagger-ui
	@echo "Swagger UI is running on 8084"

stop_api_service_swagger:
	@echo "Stopping Swagger UI..."
	@docker stop $(docker ps -a -q --filter ancestor=swaggerapi/swagger-ui)
	@echo "Swagger UI stopped"

# For development
run_docker_dev:
	@echo "Running weeat backend in development mode..."
	@docker compose -f docker-compose-dev.yml up -d
	@echo "All service is running in development mode"

stop_docker_dev:
	@echo "Stopping weeat backend in development mode..."
	@docker compose -f docker-compose-dev.yml down
	@echo "All service is stopped"

run_docker_prod:
	@echo "Running weeat backend  in production mode..."
	@docker compose -f docker-compose-prod.yml up -d
	@echo "All service is running in production mode"

stop_docker_prod:
	@echo "Stopping weeat backend  in production mode..."
	@docker compose -f docker-compose-prod.yml down
	@echo "All service is stopped"

formatting:
	@echo "Formatting python code..."
	@black .
	@echo "Python code format done"
	@echo "Formatting golang code..."
	@gofmt -w .
	@echo "Golang code format done"