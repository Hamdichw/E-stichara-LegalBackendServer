package com.esticharalegal.backendServer.mapper;

import com.esticharalegal.backendServer.dto.ClientDTO;
import com.esticharalegal.backendServer.dto.LawyerDTO;
import com.esticharalegal.backendServer.dto.SignUpClientDTO;
import com.esticharalegal.backendServer.dto.SignUpLawyerDTO;
import com.esticharalegal.backendServer.model.User;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    ClientDTO toClientDto(User user);
    @Mapping(target = "password", ignore = true)

    User signUpToUser(SignUpClientDTO signUpClientDto);

    LawyerDTO toLawyerDto(User user);
    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpLawyerDTO signUpLawyerDTO);

}
