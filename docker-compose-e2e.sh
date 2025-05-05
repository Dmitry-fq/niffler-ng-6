#!/bin/bash
source ./docker.properties
export COMPOSE_PROFILES=test
export PROFILE=docker
export PREFIX="${IMAGE_PREFIX}"

export ALLURE_DOCKER_API=http://allure:5050/
export HEAD_COMMIT_MESSAGE="local build"
export ARCH=$(uname -m)

echo '### Java version ###'
java --version

docker compose down
docker_containers=$(docker ps -a -q)
docker_images=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'niffler')

if [ ! -z "$docker_containers" ]; then
  echo "### Stop containers: $docker_containers ###"
  docker stop $docker_containers
  docker rm $docker_containers
fi

images=("postgres" "zookeeper" "kafka" "niffler-auth-docker" "niffler-currency-docker" "niffler-gateway-docker"
 "niffler-spend-docker" "niffler-userdata-docker" "niffler-ng-client-docker" "selenoid" "selenoid-ui"
  "niffler-e-2-e-tests" "allure-docker-service" "allure-docker-service-ui")

build_needed=false

for image in "${images[@]}"; do
  if ! docker images --format "{{.Repository}}" | grep -q "${image}"; then
    echo "### Image not found: '${image}'. Build needed ###"
    build_needed=true
  fi
done

if $build_needed; then
  echo "### Build all images ###"
  bash ./gradlew clean
  bash ./gradlew jibDockerBuild -x :niffler-e-2-e-tests:test
else
  echo "### All images exist. No build needed ###"
fi

if [ "$1" == "firefox" ]; then
  docker pull selenoid/vnc_firefox:125.0
  export BROWSER="firefox"
else
  docker pull selenoid/vnc_chrome:127.0
  export BROWSER="chrome"
fi
echo "### Browser used: $BROWSER ###"

docker compose up -d
docker ps -a
