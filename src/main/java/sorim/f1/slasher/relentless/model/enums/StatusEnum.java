package sorim.f1.slasher.relentless.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sorim.f1.slasher.relentless.handling.Logger;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {
    ACTIVE(0, "ACTIVE"),
    DELETED(1, "DELETED");

    private final Integer value;
    private final String description;

    public static Integer valueFromDescription(String description) throws Exception {
        for (StatusEnum e : StatusEnum.values()) {
            if (e.getDescription().equals(description)) {
                return e.value;
            }
        }
        Logger.raiseException("StatusEnum description not found: " + description);
        return null;
    }

    public static StatusEnum fromDescription(String description) throws Exception {
        for (StatusEnum e : StatusEnum.values()) {
            if (e.getDescription().equals(description)) {
                return e;
            }
        }
        Logger.raiseException("StatusEnum description not found: " + description);
        return null;
    }
}
