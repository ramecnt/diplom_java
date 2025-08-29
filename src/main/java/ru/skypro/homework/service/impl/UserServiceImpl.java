package ru.skypro.homework.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.User.UpdatePasswordDTO;
import ru.skypro.homework.dto.User.UpdateUserDTO;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final FileUploaderImpl fileUploader;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder encoder,
                           UserMapper userMapper,
                           FileUploaderImpl fileUploader) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.fileUploader = fileUploader;
    }

    @Override
    public UserDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(username);

        return user
                .map(userMapper::userToUserDTO)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
    }

    @Override
    public void updateUser(UpdateUserDTO updateUser) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            throw new RuntimeException("User with email " + username + " not found");
        }

        User newUser = user.get();

        newUser.setFirstName(updateUser.getFirstName());
        newUser.setLastName(updateUser.getLastName());
        newUser.setPhone(updateUser.getPhone());

        userRepository.save(newUser);

    }

    @Override
    public boolean changePassword(UpdatePasswordDTO newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            return false;
        }

        User newUser = user.get();

        if (!encoder.matches(newPassword.getCurrentPassword(), newUser.getPassword())) {
            return false;
        }

        newUser.setPassword(encoder.encode(newPassword.getNewPassword()));
        userRepository.save(newUser);

        return true;
    }

    @Override
    public void changeImage(MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            throw new RuntimeException("User with email " + username + " not found");
        }

        User newUser = user.get();

        String fileName = newUser.getId() + "-Avatar";
        try {
            if (fileUploader.saveFile(file, fileName)) {
                fileName = "/file/" + fileName + "." + fileUploader.getExtension(Objects.requireNonNull(file.getOriginalFilename()));

                newUser.setImage(fileName);

                userRepository.save(newUser);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}