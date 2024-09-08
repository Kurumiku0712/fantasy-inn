package com.percyyang.FantasyInn.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "houses")
public class House {

    @Id
    private String id;

    @NotBlank(message = "houseType is required")
    private String houseType;

    private BigDecimal housePrice;

    private String housePhotoUrl;

    private String houseDescription;

    @DBRef
    private List<Booking> bookings = new ArrayList<>();

    @Override
    public String toString() {
        return "House{" +
                "id=" + id +
                ", houseType='" + houseType + '\'' +
                ", housePrice=" + housePrice +
                ", housePhotoUrl='" + housePhotoUrl + '\'' +
                ", description='" + houseDescription + '\'' +
                '}';
    }
}
