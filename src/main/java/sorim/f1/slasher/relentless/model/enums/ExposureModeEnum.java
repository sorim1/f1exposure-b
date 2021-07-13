package sorim.f1.slasher.relentless.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sorim.f1.slasher.relentless.handling.ExceptionHandling;

@Getter
@RequiredArgsConstructor
public enum ExposureModeEnum {
    Modern(0),
    Legacy(1);

    private final Integer value;
}
