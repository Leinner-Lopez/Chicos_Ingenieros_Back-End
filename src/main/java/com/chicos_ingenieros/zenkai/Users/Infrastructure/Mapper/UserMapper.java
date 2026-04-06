package com.chicos_ingenieros.zenkai.Users.Infrastructure.Mapper;

import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userEntityToUser(UserEntity userEntity);
    UserEntity userToUserEntity(User user);
}
