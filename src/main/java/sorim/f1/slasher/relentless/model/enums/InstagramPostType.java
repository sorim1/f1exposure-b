package sorim.f1.slasher.relentless.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sorim.f1.slasher.relentless.handling.ExceptionHandling;

@Getter
@RequiredArgsConstructor
public enum InstagramPostType {
    TimelineCarouselMedia(0),
    TimelineImageMedia(1),
    TimelineVideoMedia(2);


    private final Integer value;

}
