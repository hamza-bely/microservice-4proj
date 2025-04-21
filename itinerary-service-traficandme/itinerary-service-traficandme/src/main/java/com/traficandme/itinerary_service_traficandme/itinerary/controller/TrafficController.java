package com.traficandme.itinerary_service_traficandme.itinerary.controller;


import com.traficandme.itinerary_service_traficandme.common.ApiResponse;
import com.traficandme.itinerary_service_traficandme.itinerary.dto.TrafficRequest;
import com.traficandme.itinerary_service_traficandme.itinerary.model.TrafficModel;
import com.traficandme.itinerary_service_traficandme.itinerary.service.TrafficService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traffic")
@CrossOrigin (origins = "*")
public class TrafficController {
    //TODO AJOUTER LE @PreAuthorize

    private final TrafficService trafficService;
    public TrafficController(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TrafficModel>> create(@RequestBody TrafficRequest request) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = principal.toString();
            if(username == null){
                throw new IllegalArgumentException("User undefined");
            }

            TrafficModel traffic = trafficService.createTraffic(request, username);
            return ResponseEntity.ok(new ApiResponse<>("TrafficData fetched successfully", traffic));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<TrafficModel>>> getAll() {
        try {
            List<TrafficModel> trafficList = trafficService.getAllTraffic();
            return ResponseEntity.ok(new ApiResponse<>("TrafficData fetched successfully", trafficList));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<TrafficModel>>> getAllByUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = principal.toString();
            if(username == null){
                throw new IllegalArgumentException("User undefined");
            }

            List<TrafficModel> userTrafficList = trafficService.getAllTrafficByUser(username);
            return ResponseEntity.ok(new ApiResponse<>("Traffic by user was successfully fetched",userTrafficList ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("{id}/delete-for-an-user")
    public ResponseEntity<ApiResponse<TrafficModel>> deleteForAnUSer(@PathVariable Integer id) {
        try {
            TrafficModel traffic = trafficService.deleteTrafficForAnUser(id);
            return ResponseEntity.ok(new ApiResponse<>("Traffic deleted successfully", traffic));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }

    @DeleteMapping("{id}/delete-definitive")
    public ResponseEntity<ApiResponse<Void>> deleteDefinitive(@PathVariable Integer id) {
        try {
            trafficService.deleteDefinitiveTrafficForAnAdmin(id);
            return ResponseEntity.ok(new ApiResponse<>("Traffic deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(e.getMessage(), null));
        }
    }
}
