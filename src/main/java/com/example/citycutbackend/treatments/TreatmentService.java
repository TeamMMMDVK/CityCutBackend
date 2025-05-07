package com.example.citycutbackend.treatments;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;


    public TreatmentService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    public List<Treatment> getAllTreatmentsFromDB() {
        return treatmentRepository.findAll();
    }
}
