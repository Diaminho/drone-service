package test.drone.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.MedicationDto;
import test.drone.entity.Medication;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MedicationMapperTest {
    @InjectMocks
    private MedicationMapper medicationMapper;

    @Test
    void entityToDtoTest() {
        var medication = new Medication();
        medication.setId(2L);
        medication.setWeight(100D);
        medication.setCode("w");
        medication.setName("name");
        medication.setImageUrl("www");

        var dto = new MedicationDto(medication.getId(), medication.getName(), medication.getWeight(), medication.getCode(), medication.getImageUrl());

        var result = medicationMapper.toDto(medication);
        assertEquals(dto, result);
    }

    @Test
    void toCreateDroneToMedicationTest() {
        short count = 1;

        var medicationDto = new MedicationDto(
                2L, "name", 100D, "w", "www"
        );

        var dto = new CreateDroneToMedicationDto(medicationDto, count);

        var medication = new Medication();
        medication.setId(medicationDto.id());
        medication.setWeight(medicationDto.weight());
        medication.setCode(medicationDto.code());
        medication.setName(medicationDto.name());
        medication.setImageUrl(medicationDto.image());

        var result = medicationMapper.toCreateDroneToMedication(medication, count);
        assertEquals(dto, result);
    }

}
