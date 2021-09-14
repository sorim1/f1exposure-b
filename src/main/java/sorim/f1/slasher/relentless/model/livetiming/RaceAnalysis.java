package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaceAnalysis {
    Integer year;
    String title;
    Integer status;
    List<FrontendGraphWeatherData> weatherChartData;
    List<Driver> driverData;
}
