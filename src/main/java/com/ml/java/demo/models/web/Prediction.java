package com.ml.java.demo.models.web;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Prediction {
    private String label;
    private float probability;
}
