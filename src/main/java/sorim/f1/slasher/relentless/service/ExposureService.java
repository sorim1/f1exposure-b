package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import sorim.f1.slasher.relentless.entities.*;
import sorim.f1.slasher.relentless.model.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ExposureService {
    Boolean exposeDrivers(String[] exposedList, String ipAddress);

    ExposedChart getExposedChartData();

    ExposureResponse getExposureDriverList();

    boolean setExposureStartTime();

    boolean setExposureCloseTime();

    boolean exposureOn();
}
