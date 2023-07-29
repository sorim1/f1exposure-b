package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.JsonRepositoryTwoModel;
import sorim.f1.slasher.relentless.model.strawpoll.StrawpollModelThree;
import sorim.f1.slasher.relentless.model.strawpoll.StrawpollPoll;
import sorim.f1.slasher.relentless.model.strawpoll.StrawpollPollOption;

import java.util.List;

public interface StrawpollService {

    StrawpollModelThree generateStrawpoll();

    List<StrawpollPollOption> getStrawpollDrivers();

    List<StrawpollPollOption> generateStrawpollDrivers();

    String setStrawpollDrivers(String drivers);

    JsonRepositoryTwoModel getStrawpoll();

    StrawpollModelThree saveStrawpoll(StrawpollModelThree body);

    StrawpollPoll postStrawpoll();
}
