package com.ml.java.demo.models.web;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PredictResponse {
    private List<Prediction> predictions;
}
