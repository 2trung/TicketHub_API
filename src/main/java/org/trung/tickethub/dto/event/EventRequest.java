package org.trung.tickethub.dto.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequest {
    String title;
    String bgColor;
    String bgImagePath;
    String description;
    long startDate;
    long endDate;
    String locationName;
    String locationAddressLine1;
    String locationAddressLine2;
    String locationCity;
    String locationState;
    String locationLatitude;
    String locationLongitude;
    boolean isOnlineEvent;
    Long organiserId;
    Long currencyId;
}
