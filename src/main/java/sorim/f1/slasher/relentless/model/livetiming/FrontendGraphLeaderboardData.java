package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class FrontendGraphLeaderboardData {
    public String title;
    public List<LeaderboardResult> leaderboardResults = new ArrayList<>();

    public FrontendGraphLeaderboardData(RawData freeData, RawData bestData) {

        this.title = (String) freeData.getDataField("R");
        List<LinkedHashMap> dr = (List<LinkedHashMap>) freeData.getDataField("DR");
        LeaderboardResult[] results = new LeaderboardResult[dr.size()];
        dr.forEach(row ->{
            List<String> coreData = (List<String>) row.get("F");
            results[Integer.parseInt(coreData.get(3))-1] = new LeaderboardResult(coreData);
        });
        dr = (List<LinkedHashMap>) bestData.getDataField("DR");
        AtomicInteger index = new AtomicInteger();
        dr.forEach(row ->{
            List<String> coreData = (List<String>) row.get("B");
            results[index.get()].setFastestLap(coreData.get(1));
            index.getAndIncrement();
        });
        leaderboardResults.addAll(Arrays.asList(results));
    }
}
