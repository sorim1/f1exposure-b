package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ForchanPost;
import sorim.f1.slasher.relentless.entities.FourChanImageEntity;
import sorim.f1.slasher.relentless.entities.FourChanPostEntity;
import sorim.f1.slasher.relentless.model.FourchanCatalog;

import java.util.List;

public interface FourchanService {

    List<FourChanPostEntity> get4chanPosts(Integer page);

    Boolean fetch4chanPosts();

    String getExposureStrawpoll();

    Boolean deleteFourChanPost(Integer id);
}
