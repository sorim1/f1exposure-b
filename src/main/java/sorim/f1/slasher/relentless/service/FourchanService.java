package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ForchanPost;
import sorim.f1.slasher.relentless.entities.TwitterPost;

import java.util.List;

public interface FourchanService {

    List<ForchanPost> get4chanPosts(Integer page);

    List<ForchanPost> fetch4chanPosts();
}
