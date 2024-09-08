package com.percyyang.FantasyInn.service.interfaces;

import com.percyyang.FantasyInn.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IHouseService {

    Response addNewHouse(MultipartFile photo, String HouseType, BigDecimal HousePrice, String description);

    List<String> getAllHouseTypes();

    Response getAllHouses();

    Response deleteHouse(String HouseId);

    Response updateHouse(String HouseId, MultipartFile photo, String HouseType, BigDecimal HousePrice, String description);

    Response getHouseById(String HouseId);

    Response getAvailableHousesByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String HouseType);

    Response getAllAvailableHouses();

}
