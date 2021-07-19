package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ExposureDriver;
import sorim.f1.slasher.relentless.model.enums.ExposureStatusEnum;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExposureResponse {
    private List<ExposureDriver> drivers;
    private ExposureStatusEnum status;
    private String title;
    private String year;
    private Boolean exposureNow;
    private Boolean exposureToday;
    private LocalDateTime exposureTime;
    private Integer currentRound;
}
