package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad.AdDTO;
import ru.skypro.homework.dto.Ad.ExtendedAdDTO;
import ru.skypro.homework.dto.Ad.AdsDTO;
import ru.skypro.homework.dto.Ad.CreateOrUpdateAdDTO;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.AdMapperSimple;
import ru.skypro.homework.models.Ad;
import ru.skypro.homework.models.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.FileUploader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final AdMapperSimple adMapperSimple;
    private final UserRepository userRepository;
    private final FileUploader fileUploader;

    @Override
    public Optional<Ad> findById(int id) {
        return adRepository.findById(id);
    }

    @Override
    public List<Ad> findAll() {
        return adRepository.findAll();
    }

    @Override
    public AdsDTO findAllWithCount() {
        List<Ad> listAd = findAll();
        List<AdDTO> listAds = adMapperSimple.toAdDTOList(listAd);
        AdsDTO ads = new AdsDTO();
        ads.setCount(listAd.size());
        ads.setResults(listAds);
        return ads;
    }

    @Override
    public boolean isAdOwner(int id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        return author.getAds().stream().anyMatch(ad -> ad.getId() == id);

    }

    @Override
    public AdDTO createAd(CreateOrUpdateAdDTO adDTO, MultipartFile file) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        Ad ad = adMapperSimple.toAd(adDTO);
        ad.setAuthor(author);
        Ad newAd = adRepository.save(ad);
        String extention = fileUploader.getExtension(file.getOriginalFilename());
        String pathToImage = "/file/ad_" + newAd.getId() + "." + extention;
        if (fileUploader.saveFile(file, "ad_" + newAd.getId())) {
            ad.setImage(pathToImage);
        } else {
            throw new RuntimeException();
        }
        adRepository.save(newAd);
        return adMapperSimple.toAdDTO(ad);


    }

    @Override
    public ExtendedAdDTO getAdDetailedById(int id) {
        Ad ad = adRepository.findById(id).orElseThrow(RuntimeException::new);
        return adMapper.toExtendedAd(ad);

    }

    @Override
    public AdDTO deleteAdById(int id) {
        Ad ad = adRepository.findById(id).orElseThrow(RuntimeException::new);
        adRepository.delete(ad);
        return adMapperSimple.toAdDTO(ad);
    }

    @Override
    public AdDTO updateAdById(CreateOrUpdateAdDTO adDTO, int id) {
        Ad ad = adRepository.findById(id).orElseThrow(RuntimeException::new);
        ad.setPrice(adDTO.getPrice());
        ad.setDescription(adDTO.getDescription());
        ad.setTitle(adDTO.getTitle());
        adRepository.save(ad);
        return adMapperSimple.toAdDTO(ad);
    }

    @Override
    public AdsDTO getUserAds() {
        String author_name = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = userRepository.findByEmail(author_name).orElseThrow(IllegalArgumentException::new);
        List<AdDTO> listAdsDTO = adMapperSimple.toAdDTOList(adRepository.findAllByAuthor(author));
        AdsDTO adsDTO = new AdsDTO();
        adsDTO.setCount(listAdsDTO.size());
        adsDTO.setResults(listAdsDTO);
        return adsDTO;


    }

    @Override
    public List<String> updateAdPhotoById(int id, MultipartFile file) throws IOException {
        String extention = fileUploader.getExtension(file.getOriginalFilename());
        Ad ad = adRepository.findById(id).orElseThrow(RuntimeException::new);
        String pathToImage = "/file/ad_" + ad.getId() + "." + extention;
        if (fileUploader.saveFile(file, "ad_" + ad.getId())) {
            ad.setImage(pathToImage);
        } else {
            throw new RuntimeException();
        }
        adRepository.save(ad);
        return new ArrayList<>(List.of(ad.getImage()));
    }

}