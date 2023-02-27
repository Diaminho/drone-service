package test.drone.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import test.drone.dto.CreateDroneToMedicationDto;
import test.drone.dto.MedicationDto;
import test.drone.entity.Medication;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MedicationMapperTest {
    @InjectMocks
    private MedicationMapper medicationMapper = Mappers.getMapper(MedicationMapper.class);

    @Test
    public void entityToDtoTest() {
        var medication = new Medication();
        medication.setId(2L);
        medication.setWeight(100D);
        medication.setCode("w");
        medication.setName("name");
        medication.setImage("www");

        var dto = new MedicationDto(medication.getId(), medication.getName(), medication.getWeight(), medication.getCode(), medication.getImage());

        var result = medicationMapper.toDto(medication);
        assertEquals(dto, result);
    }

    @Test
    public void toCreateDroneToMedicationTest() {
        short count = 1;

        var medication = new Medication();
        medication.setId(2L);
        medication.setWeight(100D);
        medication.setCode("w");
        medication.setName("name");
        medication.setImage("www");

        var dto = new CreateDroneToMedicationDto(medication, count);

        var result = medicationMapper.toCreateDroneToMedication(medication, count);
        assertEquals(dto, result);
    }

}
