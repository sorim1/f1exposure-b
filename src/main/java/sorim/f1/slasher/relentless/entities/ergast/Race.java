package sorim.f1.slasher.relentless.entities.ergast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ERGAST_CURRENT_SEASON_RACES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Race {

    @Id
    private Integer round;
    private String season;
    private String url;
    private String raceName;

    @JsonProperty("Circuit")
    @OneToOne(mappedBy="race",cascade= CascadeType.ALL)
    private Circuit circuit;
    private String date;
    private String time;

}
