package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "EXPOSED")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exposed {

    @EmbeddedId
    private SeasonRoundDriverId id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver", referencedColumnName = "code", insertable = false, updatable = false)
    private Driver relatedDriver;

    private Integer counter;


}
