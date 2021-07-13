package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.enums.ExposureModeEnum;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "EXPOSURE_CHAMPIONSHIP")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExposureChampionship {

    @EmbeddedId
    private ExposureChampionshipId id;
    private BigDecimal exposure;


}
