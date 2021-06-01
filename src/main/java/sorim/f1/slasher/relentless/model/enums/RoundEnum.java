package sorim.f1.slasher.relentless.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoundEnum {
    PRACTICE_1(0, "Practice1"),
    PRACTICE_2(1, "Practice2"),
    PRACTICE_3(2, "Practice3"),
    QUALIFYING(3, "Qualifying"),
    RACE(4, "Race");


    private final Integer value;
    private final String description;

    public static Integer valueFromDescription(String description) throws Exception {
        for (RoundEnum e : RoundEnum.values()) {
            if (e.getDescription().equals(description)) {
                return e.value;
            }
        }
        throw new Exception();
    }

    public static RoundEnum fromDescription(String description) throws Exception {
        for (RoundEnum e : RoundEnum.values()) {
            if (e.getDescription().equals(description)) {
                return e;
            }
        }
        throw new Exception(description + " isn't enum");
    }
}
