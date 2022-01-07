package com.example.Life.account.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class responseloginmodel
{
    private String token;
    private String display_name;
    public responseloginmodel(String token, String display_name)
    {
        this.token = token;
        this.display_name =display_name;
    }
}
