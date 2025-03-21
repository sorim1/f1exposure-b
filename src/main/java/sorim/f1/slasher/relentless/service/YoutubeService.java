package sorim.f1.slasher.relentless.service;


import org.jetbrains.annotations.NotNull;
import sorim.f1.slasher.relentless.model.youtube.YouTubeVideo;

import java.util.List;

public interface YoutubeService {

    List<YouTubeVideo> getYoutubeVideos();

}
