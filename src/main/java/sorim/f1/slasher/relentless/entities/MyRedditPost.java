package sorim.f1.slasher.relentless.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import masecla.reddit4j.objects.RedditPost;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

@Entity
@Table(name = "REDDIT_POSTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyRedditPost {

    @Id
    private String id;
    private String title;
    private String url;
    private String imageUrl;
    private Long created;
    private Integer type = 1;

    @Transient
    @JsonIgnore
    private String mediaUrl2;
    @Transient
    @JsonIgnore
    private String linkFlairText;
    @Transient
    @JsonIgnore
    private Boolean valid = false;

    @Transient
    private Integer ups;

    public MyRedditPost(int ups) {
        this.ups = ups;
    }

    public MyRedditPost(RedditPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.linkFlairText = post.getLinkFlairText();

        this.url = "https://reddit.com" + post.getPermalink();
        this.imageUrl = post.getUrl();
        this.valid = isItPhoto();
        this.ups = post.getUps();
        this.created = post.getCreated();
    }

    public Boolean isItPhoto() {
        if(this.linkFlairText !=null && this.linkFlairText.contains("photo")){
            return true;
        }
        return this.imageUrl.startsWith("https://i.redd")
                || this.imageUrl.startsWith("https://i.imgur.");
    }

    public Boolean isItJpeg() {
        if (isItPhoto()) {
            return this.imageUrl.endsWith("jpg") || this.imageUrl.endsWith("jpeg");
        }
        return false;
    }
}

