package sorim.f1.slasher.relentless.model.livetiming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrontendGraphWeatherData {
    public String name;
    public List<Double> yTrack = new ArrayList<>();
    public List<Double> yAir = new ArrayList<>();
    public List<Double> yRaining = new ArrayList<>();
    public List<Double> yWindSpeed = new ArrayList<>();
    public List<Double> yHumidity = new ArrayList<>();
    public List<Double> yPressure = new ArrayList<>();
    public List<Integer> yWindDir = new ArrayList<>();
    public List<Integer> xAxis = new ArrayList<>();


    public FrontendGraphWeatherData(WeatherData weatherData, String name) {
        this.name = name;
        for (int i = 0; i < 19; i++) {
            this.yWindDir.add(0);
        }

        for (int i = 0; i < weatherData.getPTrack().size(); i++) {
            if (i % 2 == 0) {
                int minute = (int) Math.round(weatherData.getPTrack().get(i) / 60);
                xAxis.add(minute);
            } else {
                yTrack.add(weatherData.getPTrack().get(i));
                yAir.add(weatherData.getPAir().get(i));
                yRaining.add(weatherData.getPRaining().get(i));
                yWindSpeed.add(weatherData.getPWindSpeed().get(i));
                yHumidity.add(weatherData.getPHumidity().get(i));
                yPressure.add(weatherData.getPPressure().get(i));
                int direction = Math.toIntExact(Math.round(weatherData.getPWindDir().get(i) / 20.0));
                yWindDir.set(direction,
                        yWindDir.get(direction) + 1);
            }
        }
        yWindDir.set(0,
                yWindDir.get(0) + yWindDir.get(18));
        yWindDir.remove(18);
    }

}
