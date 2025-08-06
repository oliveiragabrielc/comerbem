# Usar uma imagem base do OpenJDK 21
FROM openjdk:21-jdk-slim

# Definir o diretório de trabalho dentro do container
WORKDIR /app

# Copiar o arquivo pom.xml e baixar as dependências (para otimizar o cache do Docker)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Dar permissão de execução ao mvnw
RUN chmod +x ./mvnw

# Baixar as dependências
RUN ./mvnw dependency:go-offline -B

# Copiar o código fonte
COPY src ./src

# Compilar a aplicação
RUN ./mvnw clean package -DskipTests

# Expor a porta 8080
EXPOSE 8080

# Comando para executar a aplicação
CMD ["java", "-jar", "target/comerbem-1.0.0.jar"]

