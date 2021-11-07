package com.logmein.saulo.databaseChallenge.server.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "MAP")
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueEntity implements Serializable {

    private static final long serialVersionUID = -5873042165026769063L;

    @Id
    @ApiModelProperty(notes = "Entity key")
    private String key;

    @ApiModelProperty(notes = "Entity value")
    private String value;
}
