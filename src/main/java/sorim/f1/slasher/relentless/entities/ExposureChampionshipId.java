package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.enums.ExposureModeEnum;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExposureChampionshipId implements Serializable {
    private Integer season;
    private Integer round;
    private String driver;
    private ExposureModeEnum mode;
}
