package com.ml.java.demo.web;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.
import com.ml.java.demo.models.web.PredictRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.zoo.model.VGG16;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.OutputStream;
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
            String TEMP_FILE = "temp.bmp";
            String data = request.getData();
            byte[] decodedImg = Base64.decodeBase64(data.getBytes(StandardCharsets.UTF_8));
            Path destinationFile = Paths.get(".", TEMP_FILE);
            Files.write(destinationFile, decodedImg);
            log.info("Conversion complete");
            log.info("Convert file to image...");
            NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
            INDArray image = loader.asMatrix(destinationFile.toFile());
            log.info("Conversion complete");
            destinationFile.toFile().delete();
            log.info("Preprocess image");
            DataNormalization scaler = new VGG16ImagePreProcessor();
            scaler.transform(image);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        // TODO: Get file
        // TODO: Get image
        // TODO: Send to predict

        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/train")
    public String train() {
        return "Training complete";
    }

}