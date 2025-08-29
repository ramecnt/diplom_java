package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Ad.ExtendedAdDTO;
import ru.skypro.homework.models.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    @Mapping(source = "author.email", target = "email")
    @Mapping(source = "author.phone", target = "phone")
    ExtendedAdDTO toExtendedAd(Ad ad);

}