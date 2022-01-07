package com.example.Life.account.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class responseregistermodel
{
    private String msg;
    public responseregistermodel(String msg)
    {
        this.msg= msg;
    }
}
