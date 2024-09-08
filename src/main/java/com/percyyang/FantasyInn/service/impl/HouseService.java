package com.percyyang.FantasyInn.service.impl;

import com.percyyang.FantasyInn.dto.HouseDTO;
import com.percyyang.FantasyInn.dto.Response;
import com.percyyang.FantasyInn.entity.Booking;
import com.percyyang.FantasyInn.entity.House;
import com.percyyang.FantasyInn.exception.OurException;
import com.percyyang.FantasyInn.repo.BookingRepository;
import com.percyyang.FantasyInn.repo.HouseRepository;
import com.percyyang.FantasyInn.service.AwsS3Service;
import com.percyyang.FantasyInn.service.interfaces.IHouseService;
import com.percyyang.FantasyInn.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class HouseService implements IHouseService {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AwsS3Service awsS3Service;


    @Override
    public Response addNewHouse(MultipartFile photo, String HouseType, BigDecimal HousePrice, String description) {

        Response response = new Response();

        try {
            String imageUrl = awsS3Service.saveImageToS3(photo);
            House house = new House();
            house.setHousePhotoUrl(imageUrl);
            house.setHousePrice(HousePrice);
            house.setHouseType(HouseType);
            house.setHouseDescription(description);

            House newHouse = houseRepository.save(house);
            HouseDTO houseDTO = Utils.mapHouseEntityToHouseDTO(newHouse);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setHouse(houseDTO);


        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while adding a house: " + e.getMessage());

        }
        return response;
    }

    @Override
    public List<String> getAllHouseTypes() {
        return houseRepository.findDistinctHouseTypes();
    }

    @Override
    public Response getAllHouses() {

        Response response = new Response();

        try {
            List<House> houseList = houseRepository.findAll();
            List<HouseDTO> houseDTOList = Utils.mapHouseListEntityToHouseListDTO(houseList);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setHouseList(houseDTOList);


        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while getting all houses: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteHouse(String HouseId) {
        Response response = new Response();

        try {
            houseRepository.findById(HouseId).orElseThrow(() -> new OurException("House Not Found"));
            houseRepository.deleteById(HouseId);

            response.setStatusCode(200);
            response.setMessage("successful");


        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while deleting a house: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateHouse(String HouseId, MultipartFile photo, String HouseType, BigDecimal HousePrice, String description) {
        Response response = new Response();

        try {
            String imageUrl = null;
            if (photo != null && !photo.isEmpty()){
                imageUrl = awsS3Service.saveImageToS3(photo);
            }

            House house = houseRepository.findById(HouseId).orElseThrow(()-> new OurException("House Not Found"));

            if (HouseType != null) house.setHouseType(HouseType);
            if (HousePrice != null) house.setHousePrice(HousePrice);
            if (description != null) house.setHouseDescription(description);
            if (imageUrl != null) house.setHousePhotoUrl(imageUrl);

            House updatedHouse = houseRepository.save(house);
            HouseDTO houseDTO = Utils.mapHouseEntityToHouseDTO(updatedHouse);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setHouse(houseDTO);


        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while updating a house: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getHouseById(String HouseId) {
        Response response = new Response();

        try {
            House house = houseRepository.findById(HouseId).orElseThrow(()-> new OurException("House Not Found"));
            HouseDTO houseDTO = Utils.mapHouseEntityToHouseDTOPlusBookings(house);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setHouse(houseDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while getting a house by id: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableHousesByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String houseType) {
        Response response = new Response();

        try {
            List<Booking> bookings = bookingRepository.findBookingsByDateRange(checkInDate, checkOutDate);
            List<String> bookedHousesId = bookings.stream().map(booking -> booking.getHouse().getId()).toList();

            List<House> availableHouses = houseRepository.findByHouseTypeLikeAndIdNotIn(houseType, bookedHousesId);
            List<HouseDTO> houseDTOList = Utils.mapHouseListEntityToHouseListDTO(availableHouses);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setHouseList(houseDTOList);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while getting available houses by date range: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableHouses() {
        Response response = new Response();

        try {
            List<House> houseList = houseRepository.findAllAvailableHouses();
            List<HouseDTO> houseDTOList = Utils.mapHouseListEntityToHouseListDTO(houseList);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setHouseList(houseDTOList);

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred while getting all available Houses: " + e.getMessage());
        }
        return response;
    }
}
