echo '----------------------------------'
echo "requires database server, verifying database server is up $DATABASE_SERVER_URL:$DATABASE_SERVER_PORT!"
echo '----------------------------------'

until nc -z -v -w30 $DATABASE_SERVER_URL $DATABASE_SERVER_PORT
do
echo "Waiting for database server response..."
# wait for 5 seconds before check again
sleep 5
done

echo '----------------------------------'
echo 'database server is up!'
echo '----------------------------------'

echo '----------------------------------'
echo 'starting server!'
echo '----------------------------------'

cd server && \
    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=$DEBUG_PORT"