## Instructions to run the app

#### Prerequisites
Docker should be installed on your machine.

#### Steps to run the app
1. Open the app folder in the terminal: `cd /path/to/app`
2. Start the app service using Docker Compose: `docker-compose up`
3. Wait until the image is built.
4. The app will be ready to receive requests when you will see the following:
   (Server started, use Enter to stop and go back to the console...)
5. Open the app address in your browser: localhost:8080
6. Wait until the app will fetch all the needed dependencies.

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
 
