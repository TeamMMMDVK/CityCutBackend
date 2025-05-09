package com.example.citycutbackend.treatments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TreatmentServiceUnitTest {

    @Mock
    private TreatmentRepository treatmentRepository;

    @InjectMocks
    private TreatmentService treatmentService;

    @Test
    @DisplayName("add new treatment unit test")
    void addNewTreatment_savesTreatmentAndReturnsIt() {
        // Arrange
        Treatment input = new Treatment();
        input.setTitle("Haircut");
        input.setDescription("Basic haircut");
        input.setTimeslotAmount(1);
        input.setPrice(150);

        Treatment saved = new Treatment();
        saved.setId(1); // assume DB assigns ID
        saved.setTitle("Haircut");
        saved.setDescription("Basic haircut");
        saved.setTimeslotAmount(1);
        saved.setPrice(150);

        when(treatmentRepository.save(input)).thenReturn(saved);

        // Act
        Treatment result = treatmentService.addNewTreatment(input);

        // Assert
        assertEquals(1, result.getId());
        assertEquals("Haircut", result.getTitle());
        verify(treatmentRepository, times(1)).save(input);
    }
}