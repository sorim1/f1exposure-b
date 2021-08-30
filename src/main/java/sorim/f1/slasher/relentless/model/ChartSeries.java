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
    private List<List<BigDecimal>> series = new ArrayList<>();

    public void add(Integer round, BigDecimal point){
        List<BigDecimal> newPoint = new ArrayList<>();
        newPoint.add(new BigDecimal(round));
        newPoint.add(point);
        series.add(newPoint);
    }
}
