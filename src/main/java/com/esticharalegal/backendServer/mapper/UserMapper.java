package com.esticharalegal.backendServer.mapper;

import com.esticharalegal.backendServer.dto.*;
import com.esticharalegal.backendServer.model.User;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    ClientDTO toClientDto(User user);
    @Mapping(target = "password", ignore = true)

    User signUpToUser(SignUpClientDTO signUpClientDto);


    User credentialsGoogleToUser(CredentialsGoogle credentialsGoogle);
    LawyerDTO toLawyerDto(User user);

    LawyerDetailsDTO toLawyerDetailsDto(User user);
    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpLawyerDTO signUpLawyerDTO);

}
