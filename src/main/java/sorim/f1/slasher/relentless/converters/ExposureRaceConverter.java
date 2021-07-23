package sorim.f1.slasher.relentless.converters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.model.ExposureRace;


public class ExposureRaceConverter
        implements Converter<Race, ExposureRace> {

    @Override
    public ExposureRace convert(Race from) {
        return new ExposureRace(from);
    }
}
