package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Ad.AdDTO;
import ru.skypro.homework.dto.Ad.CreateOrUpdateAdDTO;
import ru.skypro.homework.models.Ad;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdMapperSimple {
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "id", target = "pk")
    AdDTO toAdDTO(Ad ad);

    Ad toAd(CreateOrUpdateAdDTO ad);

    List<AdDTO> toAdDTOList(List<Ad> ads);
}