package com.logmein.saulo.databaseChallenge.server.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class KeyValueDTO implements Serializable {

    private static final long serialVersionUID = -8047094490590052438L;

    @ApiModelProperty(notes = "Entity Key")
    @Column(unique = true)
    private String key;

    @ApiModelProperty(notes = "Entity Value")
    private String value;

}
