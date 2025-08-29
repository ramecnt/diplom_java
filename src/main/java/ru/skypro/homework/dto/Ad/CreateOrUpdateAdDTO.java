package ru.skypro.homework.dto.Ad;

import lombok.Data;

@Data
public class CreateOrUpdateAdDTO {
    private Long id;
    private String image;
    private String title;
    private int price;
    private String description;
}
