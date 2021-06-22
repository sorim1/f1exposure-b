package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sorim.f1.slasher.relentless.util.MainUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class FrontendGraphLapPosData {
    public List<String> driverCodes = new ArrayList<>();
    public List<LapPosition> lapPositions = new ArrayList<>();

    public FrontendGraphLapPosData(LapPosGraph graph, List<String> driverCodes) {
        Map<String, Object> map = graph.data.getDataFields();
        this.driverCodes = driverCodes;
        List<String> driverCodesDisorder = MainUtility.extractDriverCodes(map.keySet());
        List<Integer> order = MainUtility.orderDriverCodes(driverCodesDisorder, this.driverCodes);
        LapPosition[] lpArray = new LapPosition[driverCodes.size()];
        AtomicInteger driverCounter = new AtomicInteger();
        map.forEach((ka, v) -> {
            List<Integer> laps = (List<Integer>) v;
            for (int i = laps.size()-2; i >= 0; i -= 2) {
          //  for (int i = 0; i<laps.size()/2; i ++) {
                laps.remove(i);
            }
            lpArray[order.get(driverCounter.get())] =new LapPosition(laps);
            driverCounter.getAndIncrement();
        });
        lapPositions.addAll(Arrays.asList(lpArray));

    }
}
