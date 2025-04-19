package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountdownFooterData {
    private List<NavbarData> list = new ArrayList<>();
}
