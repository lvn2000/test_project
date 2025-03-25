## Instructions to run the app

#### Prerequisites
Docker should be installed on your machine.

#### Steps to run the app
1. Open the app folder in the terminal: `cd /path/to/app`
2. Build an image and store it in the local Docker server:`sbt docker:publishLocal`
3. Wait until the image is built.
4. Start the app service using Docker Compose: `docker-compose up`
5. The app will be ready to receive requests when you will see the following:
   `(INFO  o.h.blaze.server.BlazeServerBuilder - http4s v0.23.18 on blaze v0.23.12 started at http://[::]:8080/
Gracefully stopping... (press Ctrl+C again to force))`
6. You can send a request to the application using the curl linux command (see below) or using special utilities like Postman.


#### Get index:
curl --request POST \
    --url http://localhost:8080/getindex \
    --header 'Content-Type: application/json' \
    --header 'User-Agent: insomnia/10.3.0' \
    --data '{
  	"data":[3,8,10,14],
    "target":18
  }'
  
#### Get target:  
curl --request GET \
    --url http://localhost:8080/target \
    --header 'Content-Type: application/json' \
    --header 'User-Agent: insomnia/10.3.0' \
    --data '{
  	"target":18
  }'  

#### Find:
curl --request POST \
    --url http://localhost:8080/find \
      --header 'Content-Type: application/json' \
    --header 'User-Agent: insomnia/10.3.0' \
    --data '{
      "data":[3,8,10,14],
      "target":18
    }'  
 
