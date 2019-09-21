package com.ml.java.demo.ml;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.zoo.PretrainedType;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.deeplearning4j.zoo.util.imagenet.ImageNetLabels;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class Inference {

    private static final String TEMP_FILE = "temp.jpeg";

    public String predict(String rawData) {
        try {
            String noDescriptor = rawData.split(",")[1];
            byte[] decodedImg = Base64.decodeBase64(noDescriptor.getBytes(StandardCharsets.UTF_8));
            Path destinationFile = Paths.get(".", TEMP_FILE);
            Files.write(destinationFile, decodedImg);
            log.info("Base64 conversion complete");

            log.info("Converting file to data image...");
            NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
            INDArray image = loader.asMatrix(destinationFile.toFile());
            log.info("Conversion to data image complete");

            log.info("Deleting file...");
            destinationFile.toFile().delete();
            log.info("File deleted");

            log.info("Preprocessing image...");
            DataNormalization scaler = new VGG16ImagePreProcessor();
            scaler.transform(image);
            log.info("Image preprocessing complete");

            log.info("Loading model...");
            ZooModel zooModel = VGG16.builder().build();
            ComputationGraph pretrainedNet = (ComputationGraph) zooModel.initPretrained(PretrainedType.IMAGENET);
            log.info("Model loading complete");

            log.info("Predicting output...");
            INDArray[] output = pretrainedNet.output(false, image);
            ImageNetLabels imLabels = new ImageNetLabels();
            String rawLabels = imLabels.decodePredictions(output[0]);

            log.info("Raw Labels:\n'{}'", rawLabels);

            return rawLabels;
        } catch (IOException ex) {
           log.error(ex.getMessage());
        }

        return "";
    }
}
