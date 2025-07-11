#!/bin/bash
source .env
REGISTRY_URL=$DOKCER_URL
USERNAME=$DOKCER_USER
PASSWORD=$DOKCER_PASWORD
REGISTRY=$REGISTRY_BASE

docker compose -f docker-compose-build.yml build backend

docker tag lionel-test-team1-backend ${REGISTRY}/lionel-test-team1-backend:latest
docker tag lionel-test-team1-frontend ${REGISTRY}/lionel-test-team1-frontend:latest
docker tag lionel-test-team1-testapi ${REGISTRY}/lionel-test-team1-testapi:latest
docker tag lionel-test-team1-testui ${REGISTRY}/lionel-test-team1-testui:latest
docker tag lionel-test-team1-testui-team4-server ${REGISTRY}/lionel-test-team1-testui-team4-server:latest
docker tag lionel-test-team1-testui-team4-client ${REGISTRY}/lionel-test-team1-testui-team4-client:latest

echo '>>>> PUSH Image API_GATEWAY_VERSION : ' $API_GATEWAY_VERSION
docker login $REGISTRY_URL -u $USERNAME -p $PASSWORD
docker push ${REGISTRY}/lionel-test-team1-backend:latest
docker push ${REGISTRY}/lionel-test-team1-frontend:latest
docker push ${REGISTRY}/lionel-test-team1-testapi:latest
docker push ${REGISTRY}/lionel-test-team1-testui:latest
docker push ${REGISTRY}/lionel-test-team1-testui-team4-server:latest
docker push ${REGISTRY}/lionel-test-team1-testui-team4-client:latest