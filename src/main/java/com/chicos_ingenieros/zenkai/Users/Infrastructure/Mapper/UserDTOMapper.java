package com.chicos_ingenieros.zenkai.Users.Infrastructure.Mapper;

import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.DTO.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    UserDTO userToUserDTO(User user);

    @Mapping(target = "documentNumber", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "status", ignore = true)
    User UserDTOToUser(UserDTO userDTO);
}
