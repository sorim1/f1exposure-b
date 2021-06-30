package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.Driver;
import sorim.f1.slasher.relentless.model.enums.ExposureStatusEnum;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExposureResponse {

    private List<Driver> drivers;
    private ExposureStatusEnum status;
    private String title;
    private String year;
}
