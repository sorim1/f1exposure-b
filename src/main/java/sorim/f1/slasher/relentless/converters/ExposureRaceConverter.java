package sorim.f1.slasher.relentless.converters;

import org.springframework.core.convert.converter.Converter;
import sorim.f1.slasher.relentless.entities.ergast.RaceData;
import sorim.f1.slasher.relentless.model.FrontendRace;


public class ExposureRaceConverter
        implements Converter<RaceData, FrontendRace> {

    @Override
    public FrontendRace convert(RaceData from) {
        return new FrontendRace(from);
    }
}
