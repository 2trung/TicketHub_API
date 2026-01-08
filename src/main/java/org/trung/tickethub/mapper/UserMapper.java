package org.trung.tickethub.mapper;

import org.mapstruct.Mapper;
import org.trung.tickethub.dto.authentication.UserDataResponse;
import org.trung.tickethub.dto.authentication.UserRegisterRequest;
import org.trung.tickethub.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegisterRequest request);
    UserDataResponse toUserDataResponse(User user);
}
