package sorim.f1.slasher.relentless.converters;

import org.springframework.core.convert.converter.Converter;
import sorim.f1.slasher.relentless.entities.ergast.Race;
import sorim.f1.slasher.relentless.model.FrontendRace;


public class ExposureRaceConverter
        implements Converter<Race, FrontendRace> {

    @Override
    public FrontendRace convert(Race from) {
        return new FrontendRace(from);
    }
}
