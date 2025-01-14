# Usar una imagen base de Eclipse Temurin con Alpine
FROM eclipse-temurin:21-alpine

# Etiquetas para metadatos
LABEL maintainer="bcrp.gob.pe"

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR de la aplicación al contenedor
COPY target/upi-process-service-0.0.1-SNAPSHOT.jar /app/upiProcessService.jar

# Crear un volumen temporal
VOLUME /tmp

# Comando de entrada
ENTRYPOINT ["java", "-jar", "upiProcessService.jar"]