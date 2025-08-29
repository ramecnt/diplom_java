package ru.skypro.homework.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.User.UpdatePasswordDTO;
import ru.skypro.homework.dto.User.UpdateUserDTO;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.service.UserService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(path = "/set_password")
    public ResponseEntity<HttpStatus> setPassword(@RequestBody UpdatePasswordDTO newPassword) {
        if (userService.changePassword(newPassword)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(path = "/me")
    public ResponseEntity<UserDTO> getDetailsAboutMe() {
        try {
            return ResponseEntity.ok(userService.getCurrentUser());
        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping(path = "/me")
    public ResponseEntity<UpdateUserDTO> updateDetails(@RequestBody UpdateUserDTO updateUserDTO) {
        try {
            userService.updateUser(updateUserDTO);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> updateImage(@RequestPart("image") MultipartFile file) {
        try {
            userService.changeImage(file);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}