package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.FourChanPostEntity;
import sorim.f1.slasher.relentless.entities.Streamable;

import java.util.List;

public interface FourchanService {

    List<FourChanPostEntity> get4chanPosts(Integer page);

    List<Streamable> getStreamables();

    Boolean fetch4chanPosts();

    Boolean deleteFourChanPosts();

    String getExposureStrawpoll();

    Boolean deleteFourChanPost(Integer id);
}
