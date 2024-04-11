package com.esticharalegal.backendServer.mapper;

import com.esticharalegal.backendServer.dto.*;
import com.esticharalegal.backendServer.model.User;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


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

    @Mapping(target = "password", qualifiedByName = "passwordToBoolean")
    List<ClientDetailsDTO> toClientDetailsDTO(List<User> connections);

    @Named("passwordToBoolean")
    default boolean passwordToBoolean(String password) {
        return password != null;
    }
}
