package org.trung.tickethub.dto.authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDataResponse {
    String username;
    String phoneNumber;
    String gender;
    String email;
    String profilePictureUrl;
}
