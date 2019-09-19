package com.ml.java.demo.ml;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Training {

    protected static final int numClasses = 5;
    protected static final long seed = 12345;

    private static final int trainPerc = 80;
    private static final int batchSize = 15;
    private static final String featureExtractionLayer = "fc2";

    public void train() {
        log.info("Started training");
    }
}
