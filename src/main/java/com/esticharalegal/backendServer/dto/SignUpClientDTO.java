package com.esticharalegal.backendServer.dto;


import lombok.Data;

import java.util.Date;

public record SignUpClientDTO(String firstName, String lastName, String email, String username, String password , String phoneNumber , Date birthday)
{

}
