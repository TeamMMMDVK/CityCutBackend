package com.example.citycutbackend.treatments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private static final Logger logger = LoggerFactory.getLogger("TreatmentService");


    public TreatmentService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    public List<Treatment> getAllTreatmentsFromDB() {
        return treatmentRepository.findAll();
    }

    public Treatment addNewTreatment(Treatment treatment) {
        logger.info("Adding new treatment to the DB: " + String.valueOf(treatment));
        return treatmentRepository.save(treatment);
    }
}
