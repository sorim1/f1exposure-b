package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.FourChanPostEntity;
import sorim.f1.slasher.relentless.entities.FourChanSecondaryPostEntity;

import java.util.List;

public interface FourchanService {

    List<FourChanPostEntity> get4chanPosts(Integer page);

    List<FourChanSecondaryPostEntity> get4chanSecondaryPosts();

    Boolean fetch4chanPosts();

    Boolean deleteFourChanPosts();

    String getExposureStrawpoll();

    Boolean deleteFourChanPost(Integer id);
}
