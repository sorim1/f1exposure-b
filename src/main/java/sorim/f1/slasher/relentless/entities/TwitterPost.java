package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import twitter4j.MediaEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TWITTER_POSTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TwitterPost implements Comparable<TwitterPost> {

    @Id
    private Long id;
    private String text;
    private Integer favoriteCount;
    private Integer retweetCount;
    private String url;
    private String mediaUrl;
    private String username;
    private Integer source;
    private String userPicture;
    private Date createdAt;

    @Override
    public int compareTo(@NotNull TwitterPost o) {
        return o.getId().compareTo(getId());
    }
}
