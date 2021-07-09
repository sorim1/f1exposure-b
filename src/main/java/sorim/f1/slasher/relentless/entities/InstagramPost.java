package sorim.f1.slasher.relentless.entities;

import lombok.*;
import sorim.f1.slasher.relentless.model.enums.InstagramPostType;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "INSTAGRAM_POSTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstagramPost {

    @Id
    private String code;
    private Integer likes;
    private Integer comments;
    private Integer postType;
    private String location;
    private String caption;
    private String username;
    private String userpic;
    private String url;
    private Long takenAt;
    @Transient
    private List<String> urls;

    public String theGetUrlBackup() {
        if(this.postType.equals(InstagramPostType.TimelineCarouselMedia.getValue())){
            this.urls = Stream.of(this.url.split(",", -1))
                    .collect(Collectors.toList());
        }
        return this.url;
    }

}
