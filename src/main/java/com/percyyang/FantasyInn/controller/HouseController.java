package com.percyyang.FantasyInn.controller;


import com.percyyang.FantasyInn.dto.Response;
import com.percyyang.FantasyInn.service.interfaces.IHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/houses")
public class HouseController {

    @Autowired
    private IHouseService houseService;


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewHouse(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "houseType", required = false) String houseType,
            @RequestParam(value = "housePrice", required = false) BigDecimal housePrice,
            @RequestParam(value = "houseDescription", required = false) String houseDescription
    ) {

        if (photo == null || photo.isEmpty() || houseType == null || houseType.isBlank() || housePrice == null) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("Please Provide values for all fields(photo, houseType, housePrice)");
            return ResponseEntity.status(response.getStatusCode()).body(response);

        }
            Response response = houseService.addNewHouse(photo, houseType, housePrice, houseDescription);
            return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/types")
    public List<String> getHouseTypes() {
        return houseService.getAllHouseTypes();
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllHouses() {
        Response response = houseService.getAllHouses();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{houseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteHouse(@PathVariable String houseId) {
        Response response = houseService.deleteHouse(houseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{houseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateHouse(
            @PathVariable String houseId,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "houseType", required = false) String houseType,
            @RequestParam(value = "housePrice", required = false) BigDecimal housePrice,
            @RequestParam(value = "houseDescription", required = false) String houseDescription) {
        Response response = houseService.updateHouse(houseId, photo, houseType, housePrice, houseDescription);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/house-by-id/{houseId}")
    public ResponseEntity<Response> getHouseByID(@PathVariable("houseId") String houseId) {
        Response response = houseService.getHouseById(houseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available-houses-by-date-and-type")
    public ResponseEntity<Response> getAvailableHousesByDateAndType(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) String houseType
    ) {
        if (checkInDate == null || checkOutDate == null || houseType.isBlank()) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage("All fields are required(checkInDate,checkOutDate,houseType )");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        Response response = houseService.getAvailableHousesByDateAndType(checkInDate, checkOutDate, houseType);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all-available-houses")
    public ResponseEntity<Response> getAvailableHouses() {
        Response response = houseService.getAllAvailableHouses();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
