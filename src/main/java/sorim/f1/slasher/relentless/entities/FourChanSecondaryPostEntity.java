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
public class FourChanSecondaryPostEntity {

    @Id
    private Integer id;
    private String name;

    @Column(name = "event_group")
    private Integer thread;


    public FourChanSecondaryPostEntity(FourchanPost post, Integer threadId) {
        this.id = post.getNo();
        this.name = post.getCom();
        this.thread = threadId;
    }
}
