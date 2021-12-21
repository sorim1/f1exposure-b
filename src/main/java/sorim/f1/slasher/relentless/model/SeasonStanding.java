package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.DriverStanding;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonStanding {
    private Integer position;
    private String positionText;
    private BigDecimal points;
    private Integer wins;
    private String constructorId;
    private String constructorName;
    private Integer season;

    public SeasonStanding(Integer season, ErgastStanding ds) {
        this.season = season;
        this.position = ds.getPosition();
        this.positionText = ds.getPositionText();
        this.points = ds.getPoints();
        this.wins = ds.getWins();
        if(ds.getConstructors().size()>0) {
            this.constructorId = ds.getConstructors().get(0).getConstructorId();
            this.constructorName = ds.getConstructors().get(0).getName();
            if(ds.getConstructors().size()>1) {
                this.constructorName = "";
                ds.getConstructors().forEach(constructor->{
                    this.constructorName = this.constructorName + constructor.getName() + " / ";
                });
                this.constructorName = this.constructorName.substring(0, this.constructorName.length()-3);
            }
        }
    }
}
