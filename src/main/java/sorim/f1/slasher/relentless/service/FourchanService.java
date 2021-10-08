package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ForchanPost;
import sorim.f1.slasher.relentless.model.FourchanCatalog;

import java.util.List;

public interface FourchanService {

    List<ForchanPost> get4chanPosts(Integer page);

    List<FourchanCatalog> fetch4chanPosts();

    String getExposureStrawpoll();
}
