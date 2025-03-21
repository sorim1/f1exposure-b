package sorim.f1.slasher.relentless.model.youtube;

import java.time.LocalDateTime;

public class YouTubeVideo {
    private String url;
    private String title;
    private String thumbnailUrl;
    private LocalDateTime publishedAt;

    public YouTubeVideo(String videoId, String title, String thumbnailUrl, LocalDateTime publishedAt) {
        this.url = "https://www.youtube.com/watch?v=" + videoId;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.publishedAt = publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
}
