# recipes
Demo app

## Start your local database - if you have docker installed

```
open a terminal and execute 'docker-compose -f devops/docker-compose.yml up'
```

## Run the tests abd build the jar file

````
./mvnw clean install -Dspring.profiles.active=inmem
````

## Start your service

If you could not start your local database through docker, then just use this command
````
java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=inmem
````

otherwise you can startup using this command.
````
java -jar target/demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
````

## Consult docs [OpenApi docs](http://localhost:8080/swagger-ui/index.html)
