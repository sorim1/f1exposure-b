package sorim.f1.slasher.relentless.service;

import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import org.springframework.web.multipart.MultipartFile;
import sorim.f1.slasher.relentless.entities.FourChanPostEntity;
import sorim.f1.slasher.relentless.entities.Streamable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    List<FourChanPostEntity> saveChanPosts(List<FourChanPostEntity> body);

    Boolean deleteChanByStatus(Integer status);


    String postToInstagram(boolean personalMeme) throws IGLoginException;

    List<String> saveChanImages(MultipartFile[] files);

    byte[] getChanImage(Integer id);

    void cleanup();

    Integer deleteByStatus(Integer status);

    byte[] getAcceptedPngImages(HttpServletResponse response) throws IOException;
}
