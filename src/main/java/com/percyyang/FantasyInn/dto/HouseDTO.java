package com.percyyang.FantasyInn.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HouseDTO {

    private String id;
    private String houseType;
    private BigDecimal housePrice;
    private String housePhotoUrl;
    private String houseDescription;
    private List<BookingDTO> bookings;

}
