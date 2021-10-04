package sorim.f1.slasher.relentless.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.util.MainUtility;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Entity
@Table(name = "AWS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AwsContent {

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
    private List<AwsComment> comments;

    public AwsContent(LinkedHashMap<String, Object> data) {
        this.code = MainUtility.generateCodeFromTitleAndId((String) data.get("title"), (String) data.get("id"));
        this.title = (String) data.get("title");
        this.url = (String) data.get("url_overridden_by_dest");
        Double createdDouble = (Double) data.get("created");
        this.timestampCreated = new Date(createdDouble.longValue()) ;
        this.setStatus(3);
        this.setTimestampCreated(new Date(System.currentTimeMillis()));
        this.setTimestampActivity(new Date(System.currentTimeMillis()-24*60*60*1000));
        this.setCommentCount(0);
        this.setUsername("F1ExposureBot");
    }
}
