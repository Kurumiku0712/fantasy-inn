package com.percyyang.FantasyInn.utils;


import com.percyyang.FantasyInn.dto.*;
import com.percyyang.FantasyInn.entity.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        return userDTO;
    }

    public static HouseDTO mapHouseEntityToHouseDTO(House house) {
        HouseDTO houseDTO = new HouseDTO();

        houseDTO.setId(house.getId());
        houseDTO.setHouseType(house.getHouseType());
        houseDTO.setHousePrice(house.getHousePrice());
        houseDTO.setHousePhotoUrl(house.getHousePhotoUrl());
        houseDTO.setHouseDescription(house.getHouseDescription());

        return houseDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        return bookingDTO;
    }

    public static HouseDTO mapHouseEntityToHouseDTOPlusBookings(House house) {
        HouseDTO houseDTO = new HouseDTO();

        houseDTO.setId(house.getId());
        houseDTO.setHouseType(house.getHouseType());
        houseDTO.setHousePrice(house.getHousePrice());
        houseDTO.setHousePhotoUrl(house.getHousePhotoUrl());
        houseDTO.setHouseDescription(house.getHouseDescription());

        if (house.getBookings() != null) {
            houseDTO.setBookings(house.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList()));
        }

        return houseDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTOPlusBookedHouses(Booking booking, boolean mapUser) {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if (mapUser) {
            bookingDTO.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
        }

        if (booking.getHouse() != null) {

            HouseDTO HouseDTO = new HouseDTO();

            HouseDTO.setId(booking.getHouse().getId());
            HouseDTO.setHouseType(booking.getHouse().getHouseType());
            HouseDTO.setHousePrice(booking.getHouse().getHousePrice());
            HouseDTO.setHousePhotoUrl(booking.getHouse().getHousePhotoUrl());
            HouseDTO.setHouseDescription(booking.getHouse().getHouseDescription());

            bookingDTO.setHouse(HouseDTO);
        }

        return bookingDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndHouse(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        if (!user.getBookings().isEmpty()) {
            userDTO.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOPlusBookedHouses(booking, false)).collect(Collectors.toList()));
        }
        return userDTO;
    }

    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static List<HouseDTO> mapHouseListEntityToHouseListDTO(List<House> HouseList) {
        return HouseList.stream().map(Utils::mapHouseEntityToHouseDTO).collect(Collectors.toList());
    }

    public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList) {
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
    }

}
