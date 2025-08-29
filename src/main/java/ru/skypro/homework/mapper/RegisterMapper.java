package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.models.User;

@Mapper(componentModel = "spring")
public interface RegisterMapper {
    @Mapping(source = "username", target = "email")
    @Mapping(target = "id", ignore = true)
    User registerToUser(Register register);
}