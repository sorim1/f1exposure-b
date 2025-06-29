package sorim.f1.slasher.relentless.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sorim.f1.slasher.relentless.model.youtube.YouTubeApiResponse;
import sorim.f1.slasher.relentless.model.youtube.YouTubeVideo;
import sorim.f1.slasher.relentless.service.YoutubeService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
    @Service
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public class YoutubeServiceImpl implements YoutubeService {
    private final RestTemplate restTemplate;
    private final String API_KEY = "AIzaSyCKJ2o_YvgizB3c-mvLqszf2SS9R0F_e5I";
    private final String CHANNEL_ID = "UCB_qr75-ydFVKSF9Dmo6izg";
    private final String BASE_URL = "https://www.googleapis.com/youtube/v3/search";

    @Override
    public List<YouTubeVideo> getYoutubeVideos() {
            String url = BASE_URL + "?part=snippet&channelId=" + CHANNEL_ID +
                    "&order=date&type=video&maxResults=13&key=" + API_KEY;

            YouTubeApiResponse response = restTemplate.getForObject(url, YouTubeApiResponse.class);

        return Objects.requireNonNull(response).getItems().stream()
                .map(item -> new YouTubeVideo(
                        item.getId().getVideoId(),
                        item.getSnippet().getTitle(),
                        item.getSnippet().getThumbnailUrl(), // Extract high-quality thumbnail
                        item.getSnippet().getPublishedAt()  // Convert to LocalDateTime
                ))
                .collect(Collectors.toList());
    }
}
