package sorim.f1.slasher.relentless.model.youtube;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Setter
public class YouTubeSnippet {
    private String title;
    private String publishedAt; // Original string from API
    private Thumbnails thumbnails;

    public String getTitle() {
        return title;
    }

    public LocalDateTime getPublishedAt() {
        return LocalDateTime.parse(publishedAt, DateTimeFormatter.ISO_DATE_TIME);
    }

    public String getThumbnailUrl() {
        return (thumbnails != null && thumbnails.getHigh() != null) ? thumbnails.getHigh().getUrl() : null;
    }
}
