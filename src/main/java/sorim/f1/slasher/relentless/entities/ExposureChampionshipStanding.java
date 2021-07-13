package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "EXPOSURE_CHAMPIONSHIP_STANDINGS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExposureChampionshipStanding {

    @EmbeddedId
    private ExposureChampionshipStandingId id;
    private BigDecimal exposure;


}
