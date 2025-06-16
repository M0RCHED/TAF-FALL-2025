FROM node:alpine AS builder
WORKDIR /frontend
COPY frontend .

RUN npm install && \
    npm run build

FROM nginx:alpine

COPY --from=builder /frontend/dist/* /usr/share/nginx/html/
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf

WORKDIR /app

COPY frontend/nginx.conf .

COPY frontend/envsubst-file.sh .

RUN chmod 755 /app/envsubst-file.sh

ENTRYPOINT ["/app/envsubst-file.sh"]
