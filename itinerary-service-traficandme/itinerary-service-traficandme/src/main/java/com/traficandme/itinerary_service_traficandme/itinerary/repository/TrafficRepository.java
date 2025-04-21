package com.traficandme.itinerary_service_traficandme.itinerary.repository;

import com.traficandme.itinerary_service_traficandme.itinerary.model.TrafficModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrafficRepository extends JpaRepository<TrafficModel,Integer> {

    boolean existsByStartLongitudeAndStartLatitudeAndEndLongitudeAndEndLatitudeAndUser(
            String startLongitude, String startLatitude, String endLongitude, String endLatitude, String userInfo);

    List<TrafficModel> findByUser(String user);
    int countByUser(String email);

    @Query(value = "SELECT mode, COUNT(*) AS count" +
            " FROM traffic GROUP BY mode",
            nativeQuery = true)
    List<Object[]> countByMode();
}
