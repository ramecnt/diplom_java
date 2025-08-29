package ru.skypro.homework.dto.Ad;

import lombok.Data;

@Data
public class AdDTO {
    private int author;
    private String image;
    private int pk;
    private int price;
    private String title;
}