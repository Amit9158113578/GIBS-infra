cd /etc/migration
mvn clean install
java -jar /etc/migration/target/RwandaCreateUser-v1.5.jar ${ELASTICSEARCH_USERNAME} ${ELASTICSEARCH_PASSWORD} --spring.config.name=application --spring.config.location="/etc/migration/src/main/resources/application.properties"
