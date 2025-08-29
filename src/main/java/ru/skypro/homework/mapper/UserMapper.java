package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User userEntity);
}