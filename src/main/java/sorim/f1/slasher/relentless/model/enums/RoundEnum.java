package sorim.f1.slasher.relentless.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sorim.f1.slasher.relentless.handling.Logger;

@Getter
@RequiredArgsConstructor
public enum RoundEnum {
    PRACTICE_1(0, "Practice1"),
    PRACTICE_2(1, "Practice2"),
    PRACTICE_3(2, "Practice3"),
    QUALIFYING(3, "Qualifying"),
    RACE(4, "Race"),
    SPRINT_QUALIFYING(5, "Sprint_Qualifying"),
    SPRINT(6, "Sprint");

    private final Integer value;
    private final String description;

    public static Integer valueFromDescription(String description) throws Exception {
        for (RoundEnum e : RoundEnum.values()) {
            if (e.getDescription().equals(description)) {
                return e.value;
            }
        }
        Logger.raiseException("RoundEnum description not found: " + description);
        return null;
    }

    public static RoundEnum fromDescription(String description) throws Exception {
        for (RoundEnum e : RoundEnum.values()) {
            if (e.getDescription().equals(description)) {
                return e;
            }
        }
        Logger.raiseException("RoundEnum description not found: " + description);
        return null;
    }
}
