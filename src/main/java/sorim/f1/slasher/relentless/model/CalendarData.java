package sorim.f1.slasher.relentless.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sorim.f1.slasher.relentless.entities.F1Calendar;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarData {

    private F1Calendar f1Calendar;
    private Map<String, Integer> countdownData;
    private List<String> overlays;
    private String iframeLink;
}
