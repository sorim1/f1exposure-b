package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.NewsContent;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SidebarData {
    private NewsContent topNews;
    private Boolean exposureOn;
}
