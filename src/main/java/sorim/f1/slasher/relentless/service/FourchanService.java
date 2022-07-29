package sorim.f1.slasher.relentless.service;

import com.gargoylesoftware.htmlunit.WebClient;
import sorim.f1.slasher.relentless.entities.FourChanPostEntity;
import sorim.f1.slasher.relentless.entities.Streamable;
import sorim.f1.slasher.relentless.model.FourchanPost;

import java.util.List;

public interface FourchanService {

    List<FourChanPostEntity> get4chanPosts(Integer page);

    List<Streamable> getStreamables();

    Boolean fetch4chanPosts();

    Boolean deleteFourChanPosts();

    String getExposureStrawpoll();

    Boolean deleteFourChanPost(Integer id);

    Boolean reverseGoogleImage(String url, Boolean logResponse);

    List<FourChanPostEntity> getChanPostsByStatus(Integer status);

    List<Integer> getChanPostsSums();

    String setNoDuplicatesFound(String newValue);
}
