package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonRoundFullDriverId implements Serializable {
    private Integer season;
    private Integer round;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver", referencedColumnName = "code")
    private ExposureDriver driver;
}
