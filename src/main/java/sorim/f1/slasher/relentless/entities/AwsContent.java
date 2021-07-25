package sorim.f1.slasher.relentless.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
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

    @Transient
    private List<AwsComment> comments;
}
