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
@Table(name = "FOURCHAN_POSTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FourChanPostEntity {

    @Id
    private Integer id;
    private String text;
    private Integer thread;
    private String url;

    public FourChanPostEntity(FourchanPost post, Integer status) {
        this.id=post.getNo();
        this.text ="( " + post.getW() + " x " + post.getH() + " )";
        this.thread =status;
        this.url = "https://i.4cdn.org/sp/" + post.getTim() + post.getExt();
    }
}
