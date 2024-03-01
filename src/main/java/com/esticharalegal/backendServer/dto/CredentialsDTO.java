package com.esticharalegal.backendServer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record CredentialsDTO (String username, char[] password)
{

}