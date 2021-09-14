package sorim.f1.slasher.relentless.service;

import sorim.f1.slasher.relentless.entities.ForchanPost;
import sorim.f1.slasher.relentless.entities.TwitterPost;

import java.util.List;

public interface ForchanService {

    List<ForchanPost> get4chanPosts(Integer page);

    //TODO TURN TO BOOLEAN OR VOID AFTER DEV
    List<ForchanPost> fetch4chanPosts();
}
