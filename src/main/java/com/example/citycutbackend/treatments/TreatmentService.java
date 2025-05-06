package com.example.citycutbackend.treatments;

import org.springframework.stereotype.Service;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;


    public TreatmentService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }
}
