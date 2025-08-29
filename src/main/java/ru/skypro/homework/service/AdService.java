package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad.AdDTO;
import ru.skypro.homework.dto.Ad.ExtendedAdDTO;
import ru.skypro.homework.dto.Ad.AdsDTO;
import ru.skypro.homework.dto.Ad.CreateOrUpdateAdDTO;
import ru.skypro.homework.models.Ad;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AdService {
    Optional<Ad> findById(int id);

    List<Ad> findAll();

    AdDTO createAd(CreateOrUpdateAdDTO ad, MultipartFile file) throws IOException;

    ExtendedAdDTO getAdDetailedById(int id);

    AdDTO deleteAdById(int id);

    AdDTO updateAdById(CreateOrUpdateAdDTO ad, int id);

    AdsDTO getUserAds();

    List<String> updateAdPhotoById(int id, MultipartFile file) throws IOException;

    AdsDTO findAllWithCount();

    boolean isAdOwner(int id);
}