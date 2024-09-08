package com.percyyang.FantasyInn.repo;

import com.percyyang.FantasyInn.entity.House;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface HouseRepository extends MongoRepository<House, String> {

    // 获取所有不重复的住房类型
    @Aggregation("{ $group: { _id: '$houseType' } }")
    List<String> findDistinctHouseTypes();

    // 查找没有任何预订的住房
    @Query("{ 'bookings': { $size: 0 } }")
    List<House> findAllAvailableHouses();

    List<House> findByHouseTypeLikeAndIdNotIn(String houseType, List<String> bookedHouseIds);
}
