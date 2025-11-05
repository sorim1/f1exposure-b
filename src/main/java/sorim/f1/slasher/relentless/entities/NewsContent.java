package sorim.f1.slasher.relentless.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import masecla.reddit4j.objects.RedditPost;
import sorim.f1.slasher.relentless.util.MainUtility;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "NEWS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsContent implements Comparable<NewsContent> {

    @Id
    private String code;
    private String title;
    private String url;
    private String textContent;
    private Date timestampCreated;
    private Date timestampActivity;
    private Integer status;
    private Integer commentCount;
    private String username;
    private String imageUrl;
    private String iconUrl;

    @JsonIgnore
    private String ip;

    @Transient
    private List<NewsComment> comments;
    @Transient
    private Integer numCommentsR;

    public NewsContent(RedditPost post, int status) {
        this.code = MainUtility.generateCodeFromTitleAndId(post.getTitle(), post.getId());
        this.title = post.getTitle();
        this.title = this.title.replace("&amp;", "&");
        this.url = post.getUrl();
        this.timestampCreated = new Date(post.getCreated());
        this.setStatus(status);
        this.setCommentCount(0);
        this.numCommentsR = post.getNumComments();

        if(status==5){
            Map<String, Object> media = (Map<String, Object>) post.getMedia();
            if(media!=null){
                String type = (String) media.get("type");
                if("imgur.com".equals(type)){
                    Map<String, Object> oembed = (Map<String, Object>) media.get("oembed");
                    String thumbnail_url = (String) oembed.get("thumbnail_url");

                    Integer index = thumbnail_url.indexOf(".jpg");
                    if(index>0){
                        String urlBase = thumbnail_url.substring(0, index);
                        if(urlBase.endsWith("h")){
                            urlBase = urlBase.substring(0, urlBase.length()-1);
                        }
                        this.url = urlBase + ".mp4";
                        this.imageUrl =  urlBase + ".jpg";
                    }
                }
            }
            this.title = "[VIDEO] " + this.title;
            this.numCommentsR = this.numCommentsR + 100;
        }
    }

    @Override
    public int compareTo(NewsContent u) {
        return u.numCommentsR - this.numCommentsR;
    }

    public void setDates(long time) {
        this.setTimestampCreated(new Date(time));
        this.setTimestampActivity(new Date(time - 18 * 60 * 60 * 1000));
    }
}
