package sorim.f1.slasher.relentless.entities.ergast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ERGAST_LOCATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    private String locality;
    private String lat;
    @JsonProperty("long")
    private String longitude;
    private String country;
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="circuit", referencedColumnName = "circuitId")
    private Circuit circuit;

}
