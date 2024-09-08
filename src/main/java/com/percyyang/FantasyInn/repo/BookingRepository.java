package com.percyyang.FantasyInn.repo;

import com.percyyang.FantasyInn.entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository<Booking, String> {

    // 在用户查询其预订详情时，通过用户提供的确认码来检索相关预订记录
    Optional<Booking> findByBookingConfirmationCode(String confirmationCode);

    // 自定义mongoDB查询, 查询某一时间段内所有有效的预订
    // checkInDate is less than or equal to the checkOutDate
    // and while the checkOutDate is greater than or equal to the checkInDate
    @Query("{ 'checkInDate': { $lte: ?1 }, 'checkOutDate': { $gte: ?0 } }")
    List<Booking> findBookingsByDateRange(LocalDate checkInDate,  LocalDate checkOutDate);

}
