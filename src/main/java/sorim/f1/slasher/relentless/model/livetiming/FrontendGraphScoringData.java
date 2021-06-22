package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sorim.f1.slasher.relentless.util.MainUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class FrontendGraphScoringData {
    public Integer year;
    public List<Integer> steering = new ArrayList<>();
    public List<Integer> gforceLat = new ArrayList<>();
    public List<Integer> gforceLong = new ArrayList<>();
    public List<Integer> brake = new ArrayList<>();
    public List<Integer> performance = new ArrayList<>();
    public List<Integer> throttle = new ArrayList<>();
    public List<String> driverCodes;

    public FrontendGraphScoringData(ScoresGraph scoresGraph, Integer year, List<String> driverCodes) {
        this.year = year;
        this.driverCodes = driverCodes;
        List<String> driverCodesDisorder = MainUtility.extractDriverCodes(scoresGraph.steering.getDataFields().keySet());
        List<Integer> order = MainUtility.orderDriverCodes(driverCodesDisorder, this.driverCodes);
        this.steering = MainUtility.extractDataFields(scoresGraph.steering.getDataFields(), order);
        this.gforceLat = MainUtility.extractDataFields(scoresGraph.gforceLat.getDataFields(), order);
        this.gforceLong = MainUtility.extractDataFields(scoresGraph.gforceLong.getDataFields(), order);
        this.brake = MainUtility.extractDataFields(scoresGraph.brake.getDataFields(), order);
        this.performance = MainUtility.extractDataFields(scoresGraph.performance.getDataFields(), order);
        this.throttle = MainUtility.extractDataFields(scoresGraph.throttle.getDataFields(), order);
    }

//    private List<Integer> extractDataFields(Map<String, Object> dataFields, List<Integer> order) {
//        List<Integer> output = new ArrayList<>();
//        AtomicReference<Integer> counter = new AtomicReference<>(0);
//        dataFields.values().forEach(row -> {
//            List<Integer> list = (List<Integer>) row;
//            output.add(order.get(counter.get()), list.get(1));
//            counter.set(counter.get() + 1);
//        });
//        return output;
//    }

}
