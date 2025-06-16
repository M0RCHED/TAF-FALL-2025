#!/bin/bash
source .env
REGISTRY_URL=$DOKCER_URL
USERNAME=$DOKCER_USER
PASSWORD=$DOKCER_PASWORD
REGISTRY=$REGISTRY_BASE

echo '>>>> PUSH Image API_GATEWAY_VERSION : ' $API_GATEWAY_VERSION
docker login $REGISTRY_URL -u $USERNAME -p $PASSWORD
docker push ${REGISTRY}/lionel-test-team1-backend:${BACKEND_VERSION}
docker push ${REGISTRY}/lionel-test-team1-frontend:${FRONTEND_VERSION}
docker push ${REGISTRY}/lionel-test-team1-selenium:${SELENIUM_VERSION}
docker push ${REGISTRY}/lionel-test-team1-testapi:${TEST_API_VERSION}
docker push ${REGISTRY}/lionel-test-team1-testui:${TEST_UI_VERSION}
docker push ${REGISTRY}/lionel-test-team1-testui-team4-server:${TEST_TEAM4_SERVER_VERSION}
docker push ${REGISTRY}/lionel-test-team1-testui-team4-client:${TEST_TEAM4_CLIENT_VERSION}