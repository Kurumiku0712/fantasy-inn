package com.percyyang.FantasyInn.service.interfaces;


import com.percyyang.FantasyInn.dto.Response;
import com.percyyang.FantasyInn.entity.Booking;

public interface IBookingService {

    Response saveBooking(String rooId, String userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(String bookingId);
}
