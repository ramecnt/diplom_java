package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.User.UpdatePasswordDTO;
import ru.skypro.homework.dto.User.UpdateUserDTO;
import ru.skypro.homework.dto.User.UserDTO;

public interface UserService {
    boolean changePassword(UpdatePasswordDTO newPassword);

    void updateUser(UpdateUserDTO updateUser);

    UserDTO getCurrentUser();

    void changeImage(MultipartFile file);
}