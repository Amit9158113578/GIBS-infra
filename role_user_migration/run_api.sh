cd /etc/role_user_migration
mvn clean install
java -jar /etc/role_user_migration/target/RwandaCreateUser-v1.0.jar ${ELASTICSEARCH_USERNAME} ${ELASTICSEARCH_PASSWORD} --spring.config.name=application --spring.config.location="/etc/role_user_migration/src/main/resources/${PROPERTY_FILE}"
