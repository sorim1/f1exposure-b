package sorim.f1.slasher.relentless.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.FourchanPost;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SS_STREAM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FourChanPostEntity {

    @Id
    private Integer id;
    private String name;
    private Integer event;
    private String url;

    public FourChanPostEntity(FourchanPost post, Integer threadId) {
        this.id=post.getNo();
        this.name=post.getCom();
        this.event=threadId;
        this.url = "https://i.4cdn.org/sp/" + post.getTim() + post.getExt();
    }
}
