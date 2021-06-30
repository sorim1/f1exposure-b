package sorim.f1.slasher.relentless.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sorim.f1.slasher.relentless.handling.ExceptionHandling;

@Getter
@RequiredArgsConstructor
public enum ExposureStatusEnum {
    ACTIVE(0, "Active"),
    OVER(1, "Over"),
    SOON(2, "Soon"),
    VOTED(3, "Voted");

    private final Integer value;
    private final String description;

    public static Integer valueFromDescription(String description) throws Exception {
        for (ExposureStatusEnum e : ExposureStatusEnum.values()) {
            if (e.getDescription().equals(description)) {
                return e.value;
            }
        }
        ExceptionHandling.raiseException("RoundEnum description not found: " + description);
        return null;
    }

    public static ExposureStatusEnum fromDescription(String description) throws Exception {
        for (ExposureStatusEnum e : ExposureStatusEnum.values()) {
            if (e.getDescription().equals(description)) {
                return e;
            }
        }
        ExceptionHandling.raiseException("RoundEnum description not found: " + description);
        return null;
    }
}
