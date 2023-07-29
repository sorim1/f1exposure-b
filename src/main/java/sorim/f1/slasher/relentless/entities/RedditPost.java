package sorim.f1.slasher.relentless.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class RedditPost {

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
    private String t;
    @Transient
    @JsonIgnore
    private Boolean valid = false;

    @Transient
    private Integer ups;

    public RedditPost(LinkedHashMap<String, Object> data) {
        this.id = (String) data.get("id");
        this.title = (String) data.get("title");
        ArrayList<LinkedHashMap<String, Object>> link_flair_richtext = (ArrayList<LinkedHashMap<String, Object>>) data.get("link_flair_richtext");
        if (link_flair_richtext.size() > 0 && link_flair_richtext.get(0).get("t") != null) {
            this.t = link_flair_richtext.get(0).get("t").toString();
        }
        this.url = "https://reddit.com" + data.get("permalink");
        this.imageUrl = (String) data.get("url");
        this.valid = isItPhoto();
        this.ups = (Integer) data.get("ups");
        Double createdDouble = (Double) data.get("created");
        this.created = createdDouble.longValue();
    }

    public RedditPost(int ups) {
        this.ups = ups;
    }

    public Boolean isItPhoto() {
        if (this.imageUrl.startsWith("https://imgur.com/a/")) {
            //TODO ALBUM NECE RADIT https://imgur.com/a/ZBzAKwz
            type = 2;
            return false;
        }
        if (Objects.equals(this.t, "Photo")) {
            return true;
        }
        if (this.imageUrl.startsWith("https://i.redd")) {
            return true;
        }
        return this.imageUrl.startsWith("https://i.imgur.");
    }

    public Boolean isItJpeg() {
        if (isItPhoto()) {
            return this.imageUrl.endsWith("jpg") || this.imageUrl.endsWith("jpeg");
        }
        return false;
    }
}

