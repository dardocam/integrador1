#!/bin/sh
sudo docker-compose restart java-app
sleep 8
. getLogs.sh



#sudo docker exec -it maven_dev bash