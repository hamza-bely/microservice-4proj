package com.traficandme.user_service_traficandme.user.service;

import com.traficandme.user_service_traficandme.user.dto.StatusUser;
import com.traficandme.user_service_traficandme.user.dto.UserMapper;
import com.traficandme.user_service_traficandme.user.dto.UserResponse;
import com.traficandme.user_service_traficandme.user.entity.UserInfo;
import com.traficandme.user_service_traficandme.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AdminService ( UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper= userMapper;
        this.passwordEncoder =passwordEncoder;
    }

    public boolean deleteUserForAnAdmin(Integer id){
        Optional<UserInfo> user = userRepository.findById(id);
        if(user.isPresent()){
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    public UserResponse changeStatusUserForAnAdmin(Integer id, StatusUser newStatus) {
        if (newStatus == null || !EnumSet.allOf(StatusUser.class).contains(newStatus)) {
            throw new IllegalArgumentException("Status \"" + newStatus + "\" does not exist");
        }

        UserInfo user = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found with id: " + id));

        user.setStatus(newStatus);
        user.setUpdateDate(new Date());


        ///TODO FAIRE LA MEMEM CHOSE AVEC LE ROUTE CHANGE le EMAIL "Anonymous User"
        if (newStatus == StatusUser.DELETED) {
            String randomSuffix = UUID.randomUUID().toString().substring(0, 8);

            String originalUsername = user.getUsername();

            user.setEmail("deleted_" + randomSuffix + "@example.com");
            user.setFirstName("Anonymous");
            user.setLastName("User");
            user.setPassword(UUID.randomUUID().toString());

            //TODO
           /* List<Report> userReports = reportRepository.findAll().stream()
                    .filter(report -> report.getUser().equals(originalUsername))
                    .collect(Collectors.toList());

            for (Report report : userReports) {
                report.setUser("Anonymous User");
            }*/

            //reportRepository.saveAll(userReports);
        }

       UserResponse userCanceled = userRepository.findById(user.getId())
                .map(this.userMapper::toResponse)
                .orElseThrow();;

        return userCanceled;
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long countUsersByStatus(StatusUser status) {
        return userRepository.countByStatus(status);
    }
}
