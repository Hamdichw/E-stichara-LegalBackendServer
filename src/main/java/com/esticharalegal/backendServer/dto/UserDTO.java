package com.esticharalegal.backendServer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long userID;
    private String firstName;
    private String lastName;
    private String username;
    private String token;

}
