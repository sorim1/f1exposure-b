package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.Replay;

import java.util.List;

public interface RacingfkService {

    Boolean fetchReplayLinks();

    List<Replay> getReplays(Integer page);
}
