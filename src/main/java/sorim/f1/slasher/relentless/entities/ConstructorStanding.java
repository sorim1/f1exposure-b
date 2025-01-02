package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "CURRENT_CONSTRUCTOR_STANDINGS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstructorStanding {

    @Id
    private String id;
    private String name;
    private Integer position;
    private BigDecimal points;
    private Integer wins;
    private String url;

    public ConstructorStanding(ErgastStanding ergastStanding) {
        this.id = ergastStanding.getConstructor().getConstructorId();
        this.position = ergastStanding.getPosition();
        this.name = ergastStanding.getConstructor().getName();
        this.url = ergastStanding.getConstructor().getUrl();
        this.points = ergastStanding.getPoints();
        this.wins = ergastStanding.getWins();
    }
}
