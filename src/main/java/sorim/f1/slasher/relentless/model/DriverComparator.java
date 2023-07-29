package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.ergast.ErgastStanding;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverComparator {

    private Integer year;
    private Integer maxYear;
    private DriverCompared driver1;
    private DriverCompared driver2;

    public void updateFromRace(RaceData race) {
        Optional<ErgastStanding> es1 = race.getResults().stream().filter(es -> es.getDriver().getDriverId().equals(driver1.getDriverId())).findFirst();
        Optional<ErgastStanding> es2 = race.getResults().stream().filter(es -> es.getDriver().getDriverId().equals(driver2.getDriverId())).findFirst();

        if (es1.isPresent() && es2.isPresent()) {
            if (es1.get().getPosition() < es2.get().getPosition()) {
                this.driver1.setRace(this.driver1.getRace() + 1);
            } else {
                this.driver2.setRace(this.driver2.getRace() + 1);
            }
            if (es1.get().getGrid() < es2.get().getGrid()) {
                this.driver1.setQualifying(this.driver1.getQualifying() + 1);
            } else {
                this.driver2.setQualifying(this.driver2.getQualifying() + 1);
            }
        }
        if (es1.isPresent()) {
            if (this.driver1.getBestRace() == null || this.driver1.getBestRace() > es1.get().getPosition()) {
                this.driver1.setBestRace(es1.get().getPosition());
            }
            if (this.driver1.getBestQualifying() == null || this.driver1.getBestQualifying() > es1.get().getGrid()) {
                if (es1.get().getGrid() > 0) {
                    this.driver1.setBestQualifying(es1.get().getGrid());
                }
            }
            try {
                Integer.parseInt(es1.get().getPositionText());
            } catch (NumberFormatException e) {
                this.driver1.setDnf(this.driver1.getDnf() + 1);
            }
        }
        if (es2.isPresent()) {
            if (this.driver2.getBestRace() == null || this.driver2.getBestRace() > es2.get().getPosition()) {
                this.driver2.setBestRace(es2.get().getPosition());
            }
            if (this.driver2.getBestQualifying() == null || this.driver2.getBestQualifying() > es2.get().getGrid()) {
                if (es2.get().getGrid() > 0) {
                    this.driver2.setBestQualifying(es2.get().getGrid());
                }
            }
            try {
                Integer.parseInt(es2.get().getPositionText());
            } catch (NumberFormatException e) {
                this.driver2.setDnf(this.driver2.getDnf() + 1);
            }
        }
    }
}
