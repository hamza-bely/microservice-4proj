package com.traficandme.itinerary_service_traficandme.itinerary.service;


import com.traficandme.itinerary_service_traficandme.common.RouteData;
import com.traficandme.itinerary_service_traficandme.itinerary.dto.StatusTraffic;
import com.traficandme.itinerary_service_traficandme.itinerary.dto.TrafficRequest;
import com.traficandme.itinerary_service_traficandme.itinerary.model.TrafficModel;
import com.traficandme.itinerary_service_traficandme.itinerary.repository.TrafficRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class TrafficService {
    private final TrafficRepository trafficRepository;

    public TrafficService(TrafficRepository trafficRepository) {
        this.trafficRepository = trafficRepository;
    }

    public TrafficModel createTraffic(TrafficRequest request, String username) throws Exception {

        if (request.getStartLongitude() == null || request.getStartLongitude().isBlank()) {
            throw new IllegalArgumentException("Start longitude is required.");
        }
        if (request.getStartLatitude() == null || request.getStartLatitude().isBlank()) {
            throw new IllegalArgumentException("Start latitude is required.");
        }
        if (request.getEndLongitude() == null || request.getEndLongitude().isBlank()) {
            throw new IllegalArgumentException("End longitude is required.");
        }
        if (request.getEndLatitude() == null || request.getEndLatitude().isBlank()) {
            throw new IllegalArgumentException("End latitude is required.");
        }
        if (request.getMode() == null) {
            throw new IllegalArgumentException("Mode of circulation is required.");
        }


        int userTrafficCount = trafficRepository.countByUser(username);
        if (userTrafficCount >= 10) {
            throw new IllegalStateException("You have reached the maximum number of 10 saved routes. Please delete one before creating a new one.");
        }

        if (isTrafficExists(
                request.getStartLongitude(),
                request.getStartLatitude(),
                request.getEndLongitude(),
                request.getEndLatitude(),
                request.getUser())) {
            throw new Exception("Traffic record already exists for this route and user");
        }

        TrafficModel stop = new TrafficModel();
        stop.setStartLongitude(request.getStartLongitude());
        stop.setStartLatitude(request.getStartLatitude());
        stop.setEndLongitude(request.getEndLongitude());
        stop.setEndLatitude(request.getEndLatitude());
        stop.setAddress_start(request.getAddress_start());
        stop.setAddress_end(request.getAddress_end());
        stop.setMode(request.getMode());
        stop.setUser(username);
        stop.setPeage(request.isPeage());
        stop.setCreateDate(new Date());
        stop.setUpdateDate(new Date());

        return trafficRepository.save(stop);
    }

    public List<TrafficModel> getAllTraffic() {
        return trafficRepository.findAll();
    }

    public List<TrafficModel> getAllTrafficByUser(String username) {
        return  trafficRepository.findAll().stream()
                .filter(report -> report.getUser().equals(username))
                .collect(Collectors.toList());
    }

    public TrafficModel deleteTrafficForAnUser(Integer idTraffic) throws Exception {

        Optional<TrafficModel> optionalTraffic = trafficRepository.findById(idTraffic);

        if (optionalTraffic.isEmpty()) {
            throw new Exception("No traffic found for this user.");
        }

        TrafficModel traffic = optionalTraffic.get();
        traffic.setStatus(StatusTraffic.DELETED);
        traffic.setUser("Anonymous");
        traffic.setUpdateDate(new Date());

        return trafficRepository.save(traffic);
    }

    public Boolean deleteDefinitiveTrafficForAnAdmin(Integer id){
        Optional<TrafficModel> traffic = trafficRepository.findById(id);
        if(traffic.isPresent()){
            trafficRepository.delete(traffic.get());
            return true;
        }
        return false;
    }

    public boolean isTrafficExists(String startLongitude, String startLatitude, String endLongitude, String endLatitude, String user) {
        return trafficRepository.existsByStartLongitudeAndStartLatitudeAndEndLongitudeAndEndLatitudeAndUser(
                startLongitude, startLatitude, endLongitude, endLatitude, user);
    }

    public long getTotalTraffic() {
        return trafficRepository.count();
    }

    public List<RouteData> getRouteData() {
        List<Object[]> results = trafficRepository.countByMode();
        List<RouteData> routeDataList = new ArrayList<>();

        for (Object[] result : results) {
            String mode = (String) result[0];
            Long count = (Long) result[1];
            RouteData routeData = new RouteData();
            routeData.setMode(mode);
            routeData.setCount(count);
            routeDataList.add(routeData);
        }

        return routeDataList;
    }

}
