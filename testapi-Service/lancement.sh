#!/bin/bash

launch_main() {
  echo "Démarrage du frontend..."
  cd frontend || { echo "Le dossier frontend n'existe pas !" ; exit 1; }
  npm install || { echo "Échec de npm install" ; exit 1; }
  ng serve --open &
  cd ..

  echo "Démarrage de l'API..."
  cd testapi || { echo "Le dossier testapi n'existe pas !" ; exit 1; }
  mvn clean install || { echo "Échec de mvn clean install" ; exit 1; }
  mvn spring-boot:run &
  mvn clean install || { echo "Échec de mvn clean install final" ; exit 1; }

  echo "Tous les processus ont été lancés avec succès."
}

launch_docker() {
ENV_FILE=".docker_config.env"
    if [ ! -f "$ENV_FILE" ]; then
        echo "Erreur : Le fichier $ENV_FILE n'existe pas."
        exit 1
    fi

    docker compose --env-file "$ENV_FILE" up

    if [ $? -ne 0 ]; then
        echo "Erreur : Échec du démarrage de Docker Compose."
        exit 1
    fi
}

case "$1" in
  start)
    launch_main
    ;;
  docker)
    launch_docker
    ;;
  *)
    echo "Usage: $0 {start|other}"
    exit 1
    ;;
esac
