package com.percyyang.FantasyInn.service.impl;

import com.percyyang.FantasyInn.dto.LoginRequest;
import com.percyyang.FantasyInn.dto.Response;
import com.percyyang.FantasyInn.dto.UserDTO;
import com.percyyang.FantasyInn.entity.User;
import com.percyyang.FantasyInn.exception.OurException;
import com.percyyang.FantasyInn.repo.UserRepository;
import com.percyyang.FantasyInn.service.interfaces.IUserService;
import com.percyyang.FantasyInn.utils.JWTUtils;
import com.percyyang.FantasyInn.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response register(User user) {

        Response response = new Response();

        try {

            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }

            if (userRepository.existsByEmail(user.getId())) {
                throw new OurException("Email Already Exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage( "Error while registering a user: " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {

        Response response = new Response();

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new OurException("USer Not Found"));
            var token = jwtUtils.generateToken(user);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 days");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage( "Error while logging in a user: " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response getAllUsers() {

        Response response = new Response();

        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUserList(userDTOList);

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage( "Error while getting all users: " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response getUSerBookingHistory(String userId) {

        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(()-> new OurException("User Not Found"));
            UserDTO userDTO= Utils.mapUserEntityToUserDTOPlusUserBookingsAndHouse(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage( e.getMessage());

        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage( "Error while getting user booking history: " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {

        Response response = new Response();

        try {
            userRepository.findById(userId).orElseThrow(()-> new OurException("User Not Found"));
            userRepository.deleteById(userId);
            response.setStatusCode(200);
            response.setMessage("successful");

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage( e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage( "Error while deleting a user: " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {

        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(()-> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage( e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage( "Error while getting a user by id : " +e.getMessage());

        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {

        Response response = new Response();

        try {
            User user = userRepository.findByEmail(email).orElseThrow(()-> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage( e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage( "Error while getting a user infor: " +e.getMessage());

        }
        return response;
    }
}
