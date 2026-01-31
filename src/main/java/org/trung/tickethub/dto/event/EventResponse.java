package org.trung.tickethub.dto.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponse {
    long id;
    String title;
    String bgColor;
    String bgImagePath;
    String description;
    long startDate;
    long endDate;
    int salesVolume;
    String locationName;
    String locationAddressLine1;
    String locationAddressLine2;
    String locationCity;
    String locationState;
    String locationLatitude;
    String locationLongitude;
    boolean isOnlineEvent;
    long createdAt;
    long updatedAt;
    Long organiserId;
    Long currencyId;
}
