package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private List<List<Integer>> series2 = new ArrayList<>();

    public void add(Integer round, BigDecimal point){
        List<BigDecimal> newPoint = new ArrayList<>();
        newPoint.add(new BigDecimal(round));
        newPoint.add(point);
        series.add(newPoint);
    }

    public void add2(Integer x, Integer y){
        List<Integer> newPoint = new ArrayList<>();
        newPoint.add(x);
        newPoint.add(y);
        series2.add(newPoint);
    }

    public void calcSeries2Averages() {
        Integer intSumOfX = 0;
        Integer intSumOfY = 0;
        for(List<Integer> entry : series2){
            intSumOfX = intSumOfX + entry.get(0);
            intSumOfY = intSumOfY + entry.get(1);
        }
        BigDecimal sumOfX = new BigDecimal(intSumOfX);
        BigDecimal sumOfY = new BigDecimal(intSumOfY);
        BigDecimal x = sumOfX.divide(new BigDecimal(series2.size()),2, RoundingMode.HALF_UP);
        BigDecimal y = sumOfY.divide(new BigDecimal(series2.size()),2, RoundingMode.HALF_UP);
        List<BigDecimal> newPoint = new ArrayList<>();
        newPoint.add(x);
        newPoint.add(y);
        series.add(newPoint);
        series2 = null;
    }
}
