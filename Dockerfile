FROM maven:3-jdk-8
EXPOSE 8080
WORKDIR /app
COPY . /app
RUN mvn clean package
COPY vgg16_dl4j_inference.zip /root/.deeplearning4j/models/vgg16/vgg16_dl4j_inference.zip
CMD mvn spring-boot:run
