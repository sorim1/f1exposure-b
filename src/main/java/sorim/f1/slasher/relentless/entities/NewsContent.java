package sorim.f1.slasher.relentless.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

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

    public NewsContent(LinkedHashMap<String, Object> data, long time) {
        this.code = MainUtility.generateCodeFromTitleAndId((String) data.get("title"), (String) data.get("id"));
        this.title = (String) data.get("title");
        this.title = this.title.replace("&amp;", "&");
        this.url = (String) data.get("url_overridden_by_dest");
        Double createdDouble = (Double) data.get("created");
        this.timestampCreated = new Date(createdDouble.longValue());
        this.setStatus(3);
        this.setTimestampCreated(new Date(time));
        this.setTimestampActivity(new Date(time - 18 * 60 * 60 * 1000));
        this.setCommentCount(0);
        this.numCommentsR = (Integer) data.get("num_comments");
    }

    public NewsContent(LinkedHashMap<String, Object> data, Integer status) {
        this.code = MainUtility.generateCodeFromTitleAndId((String) data.get("title"), (String) data.get("id"));
        this.title = (String) data.get("title");
        this.title = this.title.replace("&amp;", "&");
        this.url = (String) data.get("url_overridden_by_dest");
        if(status==5){
            LinkedHashMap<String, Object> media = (LinkedHashMap<String, Object>) data.get("media");
            if(media!=null){
                String type = (String) media.get("type");
                if("imgur.com".equals(type)){
                    LinkedHashMap<String, Object> oembed = (LinkedHashMap<String, Object>) media.get("oembed");
                    String thumbnail_url = (String) oembed.get("thumbnail_url");

                    Integer index = thumbnail_url.indexOf(".jpg");
                    if(index>0){
                        String urlBase = thumbnail_url.substring(0, index);
                        if(urlBase.endsWith("h")){
                            urlBase = urlBase.substring(0, urlBase.length()-1);
                        }
                        this.url = urlBase + ".mp4";
                        this.title = "[VIDEO] " + this.title;
                        this.imageUrl =  urlBase + ".jpg";
                    }
                }
            }
        }

        Double createdDouble = (Double) data.get("created");
        this.timestampCreated = new Date(createdDouble.longValue());
        this.setStatus(status);
        this.setCommentCount(0);
        this.numCommentsR = (Integer) data.get("num_comments");
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
