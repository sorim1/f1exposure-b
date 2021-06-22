package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class WeatherData {
    public List<Double> pTrack;
    public List<Double> pAir;
    public List<Double> pRaining;
    @JsonProperty("pWind Speed")
    public List<Double> pWindSpeed;
    public List<Double> pHumidity;
    public List<Double> pPressure;
    @JsonProperty("pWind Dir")
    public List<Double> pWindDir;

}
