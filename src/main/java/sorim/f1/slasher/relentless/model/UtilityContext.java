package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.AwsContent;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilityContext {
    private List<String> overlays;
}
