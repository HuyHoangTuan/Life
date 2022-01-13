package com.example.Life.account.model;

import lombok.Getter;
import lombok.Setter;


public interface accountmodel
{
    long getId();
    String getDisplay_name();
    String getEmail();
    long getRole();
    boolean getActive();
}
