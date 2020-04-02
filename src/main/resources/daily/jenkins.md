mvn clean install -DskipTests
mv ./target/cykb-1.0.0-SNAPSHOT.jar ./
docker stop cykb-server_container
docker rm cykb-server_container
docker rmi cykb-server_image
docker build -t cykb-server_image .
docker run --name cykb-server_container -d -p 8012:8012 cykb-server_image