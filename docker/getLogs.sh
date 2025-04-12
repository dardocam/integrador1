#!/bin/sh

# Verifica si se proporcionó un argumento
#if [ $# -eq 0 ]; then
#    echo "Uso: $0 <nombre_contenedor>"
#    exit 1
#fi

#CONTAINER_NAME=$1
CONTAINER_NAME="java-app"

# Obtiene los IDs de los contenedores que coinciden con el nombre
CONTAINER_IDS=$(sudo docker ps -a --filter "name=$CONTAINER_NAME" --format "{{.ID}}")

# Verifica si no se encontraron contenedores
# if [ -z "$CONTAINER_IDS" ]; then
#     echo "No se encontraron contenedores en ejecución con el nombre: $CONTAINER_NAME" >&2
#     exit 1
# fi

# # Verifica si hay múltiples coincidencias
# NUM_CONTAINERS=$(echo "$CONTAINER_IDS" | wc -l)
# if [ "$NUM_CONTAINERS" -gt 1 ]; then
#     echo "Múltiples contenedores encontrados con el nombre '$CONTAINER_NAME':" >&2
#     echo "$CONTAINER_IDS" >&2
#     exit 1
# fi

# Muestra el ID del contenedor
#echo "$CONTAINER_IDS"

# sudo echo "" > $(docker inspect --format='{{.LogPath}}' $CONTAINER_IDS)
sudo docker logs $CONTAINER_IDS | grep -vF '[INFO]'
# sudo docker logs $CONTAINER_IDS 
