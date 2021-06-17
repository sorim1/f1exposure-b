package sorim.f1.slasher.relentless.entities.ergast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ERGAST_CIRCUIT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Circuit {
    @Id
    private String circuitId;
    private String url;
    private String circuitName;

    @JsonProperty("Location")
    @OneToOne(mappedBy="circuit",cascade= CascadeType.ALL)
    private Location location;
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="race", referencedColumnName = "round")
    private Race race;

}
