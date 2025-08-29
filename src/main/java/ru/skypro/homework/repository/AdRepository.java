package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.models.Ad;
import ru.skypro.homework.models.User;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Integer> {

    List<Ad> findAllByAuthor(User author);
}