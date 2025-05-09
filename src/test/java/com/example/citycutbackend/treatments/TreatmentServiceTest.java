package com.example.citycutbackend.treatments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TreatmentServiceTest {

    @Autowired
    TreatmentService treatmentService;

    @Autowired
    TreatmentRepository treatmentRepository;
    @BeforeEach
    void clearDatabase() {
        treatmentRepository.deleteAll(); // clean state
    }

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
        assertEquals(2, list.size()); //List should've grown by 2 after adding treatments
        assertEquals(t1.getTitle(), list.get(0).getTitle()); //Checking index[x] treatment title
        assertEquals(t2.getTitle(), list.get(1).getTitle());
        assertEquals(t1.getDescription(), list.get(0).getDescription());
        assertEquals(t2.getDescription(), list.get(1).getDescription());//Checking index[x] treatment description
    }

    @Test
    @DisplayName("add new treatment integration test")
    void addNewTreatment_returnCreatedTreatmentWithId(){
        Treatment treatment = new Treatment();
        treatment.setTitle("Haircut");
        treatment.setDescription("Basic haircut");
        treatment.setTimeslotAmount(1);
        treatment.setPrice(150);


        Treatment saved = treatmentService.addNewTreatment(treatment);

        assertNotNull(saved.getId(), "Saved treatment should have an ID");
        assertEquals("Haircut", saved.getTitle());
        assertEquals("Basic haircut", saved.getDescription());
        assertEquals(1, saved.getTimeslotAmount());
        assertEquals(150, saved.getPrice());


        Optional<Treatment> fromDb = treatmentRepository.findById(saved.getId());
        assertTrue(fromDb.isPresent(), "Treatment should exist in DB");
        assertEquals("Haircut", fromDb.get().getTitle());

    }
}