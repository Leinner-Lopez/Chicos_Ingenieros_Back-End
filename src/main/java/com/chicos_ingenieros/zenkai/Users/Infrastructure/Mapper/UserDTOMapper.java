package com.chicos_ingenieros.zenkai.Users.Infrastructure.Mapper;

import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.DTO.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    UserDTO userToUserDTO(User user);
    User UserDTOToUser(UserDTO userDTO);
}
