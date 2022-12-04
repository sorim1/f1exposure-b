package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.model.StrawpollModelTwo;

public interface StrawpollService {

    StrawpollModelTwo createStrawpoll();

    String getStrawpollDrivers();

    String generateStrawpollDrivers();

    String setStrawpollDrivers(String drivers);
}
