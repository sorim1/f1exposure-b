package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartSeries {
    private String name;
    private String color;
    private List<List<Integer>> series = new ArrayList<>();

    public void add(Integer round, Integer point){
        List<Integer> newPoint = new ArrayList<>();
        newPoint.add(round);
        newPoint.add(point);
        series.add(newPoint);
    }
}
