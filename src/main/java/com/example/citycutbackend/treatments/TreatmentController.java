package com.example.citycutbackend.treatments;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/treatments")
public class TreatmentController {
    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @GetMapping("")
    public List<Treatment> getAllTreatmentsFromDB() {
        return treatmentService.getAllTreatmentsFromDB();
    }
}
