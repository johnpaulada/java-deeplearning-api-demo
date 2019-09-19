package com.ml.java.demo.web;

import com.ml.java.demo.ml.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
public class ApiController {

    @Autowired
    private Training training;

    @RequestMapping("/predict")
    public String predict() {

        // TODO: Evaluate Network
        // TODO: Get Data From Link
        // TODO: Feed Data to Network
        // TODO: Return Result

        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/train")
    public String train() {
        training.train();
        return "Training complete";
    }

}