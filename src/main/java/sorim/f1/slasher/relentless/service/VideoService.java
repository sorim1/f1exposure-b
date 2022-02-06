package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.Replay;

import java.util.List;

public interface VideoService {

    Boolean fetchReplayLinks();

    List<Replay> getReplays(Integer page);

    Boolean removeVideo(Integer id);

    List<Replay> saveVideos(List<Replay> videos);

    List<Replay> getVideos();
}
