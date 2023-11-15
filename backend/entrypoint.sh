#!/bin/sh
# wait for database to start (should be replaced in the real world)
sleep 10
# make sure to run with URL from the environment and to use both the standard
# migrations and the test migrations in /opt/library/migration

java -Djava.net.preferIPv4Stack=true -jar /opt/library/libraryapp-*.jar db.url="${JDBC_URL}" flyway.locations="classpath:db,filesystem:/opt/library/migration"
