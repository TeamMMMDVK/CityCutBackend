package com.example.citycutbackend.treatments;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class TreatmentServiceTest {

    @Autowired
    TreatmentService treatmentService;

    @Autowired
    TreatmentRepository treatmentRepository;
    @Test
    void getAllTreatmentsFromDB() {
        //Arrange
        List<Treatment> list = new ArrayList<>();
        Treatment t1 = new Treatment();
        t1.setTitle("Treatment 1");
        t1.setDescription("I am a treatment 1");
        Treatment t2 = new Treatment();
        t2.setTitle("Treatment 2");
        t2.setDescription("I am a treatment 2");


        //Act
        treatmentRepository.save(t1);
        treatmentRepository.save(t2);
        list.addAll(treatmentService.getAllTreatmentsFromDB());


        //Assert
        assertEquals(2, list.size());
    }
}