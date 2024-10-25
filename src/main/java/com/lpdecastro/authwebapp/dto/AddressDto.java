package com.lpdecastro.authwebapp.dto;

import lombok.Data;

@Data
public class AddressDto {

    private String address1;
    private String address2;
    private String city;
    private String province;
    private String zip;
    private String country;
}
