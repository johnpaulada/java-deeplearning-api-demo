package com.ml.java.demo.web;

import com.ml.java.demo.ml.Inference;
import com.ml.java.demo.models.web.PredictRequest;
import com.ml.java.demo.models.web.PredictResponse;
import com.ml.java.demo.models.web.Prediction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RestController
public class ApiController {

    @Autowired
    private Inference inference;

    @RequestMapping("/predict")
    public PredictResponse predict(@RequestBody PredictRequest request) {

        try {
            String rawLabels = inference.predict(request.getData());

            List<Prediction> predictions = Stream.of(rawLabels
                    .replace("Predictions for batch  :\n", "")
                    .split("\n"))
                    .map(x -> {
                        String[] s = x.trim().split(",");

                        log.info("Sureness '{}'", Float.parseFloat(s[0].trim().replace("%", "")));
                        log.info("Label '{}'", s[1].trim());

                        return Prediction
                                .builder()
                                .probability(Float.parseFloat(s[0].trim().replace("%", "")) / 100)
                                .label(s[1].trim())
                                .build();
                    })
                    .collect(Collectors.toList());

            return PredictResponse
                    .builder()
                    .predictions(predictions)
                    .build();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return null;
    }

}