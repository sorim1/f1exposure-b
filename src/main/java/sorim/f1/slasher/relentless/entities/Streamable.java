package sorim.f1.slasher.relentless.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.model.FourchanPost;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "STREAMABLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Streamable {

    @Id
    private Integer id;
    private String text;
    private Integer thread;


    public Streamable(FourchanPost post, Integer threadId) {
        this.id = post.getNo();
        this.text = post.getCom();
        this.thread = threadId;
    }
}
