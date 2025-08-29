package ru.skypro.homework.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad.AdDTO;
import ru.skypro.homework.dto.Ad.AdsDTO;
import ru.skypro.homework.dto.Ad.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.Ad.ExtendedAdDTO;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.impl.AdServiceImpl;


import java.io.IOException;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("ads")
public class AdController {

    private final AdService adService;
    private final AdServiceImpl adServiceImpl;
    private final ObjectMapper objectMapper;


    @GetMapping("")
    public ResponseEntity<AdsDTO> getAllAds() {
        try {
            return ResponseEntity.ok(adService.findAllWithCount());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdDTO> createAd(@RequestPart(value = "properties") String adJson,
                                          @RequestPart(value = "image") MultipartFile image) {
        try {
            CreateOrUpdateAdDTO ad = objectMapper.readValue(adJson, CreateOrUpdateAdDTO.class);
            AdDTO adObj = adService.createAd(ad, image);
            return ResponseEntity.ok(adObj);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDTO> getAdById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok().body(adService.getAdDetailedById(id));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAdOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<AdDTO> deleteAd(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(adService.deleteAdById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAdOwner(#id)")
    @PatchMapping("/{id}")
    public ResponseEntity<AdDTO> updateAd(@PathVariable("id") int id, @RequestBody CreateOrUpdateAdDTO ad) {
        try {
            return ResponseEntity.ok(adService.updateAdById(ad, id));
        } catch (RuntimeException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AdsDTO> getAllAdsMe() {
        return ResponseEntity.ok(adService.getUserAds());
    }

    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.isAdOwner(#id)")
    @PatchMapping(path = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<List<String>> updateAdImage(@PathVariable("id") int id, @RequestPart("image") MultipartFile image) throws IOException {
        return ResponseEntity.ok(adService.updateAdPhotoById(id, image));
    }


}