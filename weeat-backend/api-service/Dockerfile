FROM golang:1.22-alpine AS builder

WORKDIR /app

COPY go.mod go.sum ./

RUN go mod tidy

COPY . .

RUN go build -o /app/weeat ./app/weeat.go

FROM alpine:latest

COPY --from=builder /app/etc/weeat-api-prod.yaml /etc/weeat-api-prod.yaml

COPY --from=builder /app/weeat /usr/local/bin/weeat

# Expose port 8089 to the outside world
EXPOSE 8089

# Command to run the executable
CMD ["weeat", "-f", "/etc/weeat-api-prod.yaml"]