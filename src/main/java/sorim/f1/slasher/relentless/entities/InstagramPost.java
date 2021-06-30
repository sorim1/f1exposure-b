package sorim.f1.slasher.relentless.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "INSTAGRAM_POSTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstagramPost {

    @Id
    private String code;
    private Long deviceTimestamp;
    private Integer likes;
    private Integer comments;
}
