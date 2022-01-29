package sorim.f1.slasher.relentless.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.FourchanPost;

import javax.persistence.*;

@Entity
@Table(name = "SS_EVENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FourChanImageEntity {

    @Id
    private Integer id;
    private String name;

    @Column(name = "event_group")
    private Integer thread;


    public FourChanImageEntity(FourchanPost post, Integer threadId) {
        this.id = post.getNo();
        this.name = "https://i.4cdn.org/sp/" + post.getTim() + post.getExt();
        this.thread = threadId;
    }
}
