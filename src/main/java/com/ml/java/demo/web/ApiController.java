package com.ml.java.demo.web;

import org.deeplearning4j.nn.graph.ComputationGraph;
import com.ml.java.demo.models.web.PredictRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.zoo.PretrainedType;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.deeplearning4j.zoo.util.imagenet.ImageNetLabels;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RestController
public class ApiController {

    @RequestMapping("/predict")
    public String predict(@RequestBody PredictRequest request) {

        try {
            log.info("Begin base64 to file conversion");
            String TEMP_FILE = "temp.jpeg";
            String data = request.getData().split(",")[1];
            byte[] decodedImg = Base64.decodeBase64(data.getBytes(StandardCharsets.UTF_8));
            Path destinationFile = Paths.get(".", TEMP_FILE);
            Files.write(destinationFile, decodedImg);
            log.info("Base64 conversion complete");
            log.info("Converting file to data image...");
            NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
            INDArray image = loader.asMatrix(destinationFile.toFile());
            log.info("Conversion to data image complete");
//            destinationFile.toFile().delete();
            log.info("Preprocessing image...");
            DataNormalization scaler = new VGG16ImagePreProcessor();
            scaler.transform(image);
            log.info("Image preprocessing complete");
            log.info("Creating model...");
            ZooModel zooModel = VGG16.builder().build();
            ComputationGraph pretrainedNet = (ComputationGraph) zooModel.initPretrained(PretrainedType.IMAGENET);
            INDArray[] output = pretrainedNet.output(false, image);
            ImageNetLabels imlabels = new ImageNetLabels();

            return imlabels.decodePredictions(output[0]);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/train")
    public String train() {
        return "Training complete";
    }

}