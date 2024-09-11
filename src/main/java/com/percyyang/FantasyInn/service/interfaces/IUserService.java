package com.percyyang.FantasyInn.service.interfaces;

import com.percyyang.FantasyInn.dto.LoginRequest;
import com.percyyang.FantasyInn.dto.Response;
import com.percyyang.FantasyInn.entity.User;

public interface IUserService {

    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUSerBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);

}
