package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.ergast.Circuit;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CircuitStatistics {
    private Circuit ergastCircuit;
    private List<StatisticsRace> races = new ArrayList<>();
    private String wikiSummary;
    private String wikiImage;

    public CircuitStatistics(Circuit circuit) {
        this.ergastCircuit = circuit;
    }

    public void addRace(RaceData race) {
        races.add(new StatisticsRace(race));
    }
}
