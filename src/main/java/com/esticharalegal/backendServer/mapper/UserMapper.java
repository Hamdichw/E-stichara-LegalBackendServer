package com.esticharalegal.backendServer.mapper;

import com.esticharalegal.backendServer.dto.SignUpDTO;
import com.esticharalegal.backendServer.dto.UserDTO;
import com.esticharalegal.backendServer.model.User;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDto(User user);

    User signUpToUser(SignUpDTO signUpDto);

}
