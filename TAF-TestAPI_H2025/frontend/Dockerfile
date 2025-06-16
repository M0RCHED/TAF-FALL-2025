FROM node:alpine AS builder
WORKDIR /app
COPY . .
RUN npm install && \
    npm run build

FROM nginx:alpine

COPY --from=builder /app/dist/* /usr/share/nginx/html/
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

WORKDIR /app

COPY ./nginx.conf .

COPY ./envsubst-file.sh .

RUN chmod 755 /app/envsubst-file.sh

ENTRYPOINT ["/app/envsubst-file.sh"]
