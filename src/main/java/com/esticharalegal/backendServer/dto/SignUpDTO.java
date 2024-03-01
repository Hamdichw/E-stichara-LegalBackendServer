package com.esticharalegal.backendServer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




public record SignUpDTO (String firstName, String lastName, String username, String password)
{

}
