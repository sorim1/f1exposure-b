package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.NewsComment;
import sorim.f1.slasher.relentless.entities.NewsContent;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aws {

    private List<NewsContent> newsContents;
    private List<NewsComment> newsComments;

}
