package ru.skypro.homework.dto.Ad;

import lombok.Data;

@Data
public class ExtendedAdDTO extends AdDTO {
    private int pk;
    private String authorFirstName;
    private String authorLastName;
    private String description;
    private String email;
    private String image;
    private String phone;
    private int price;
    private String title;
}
