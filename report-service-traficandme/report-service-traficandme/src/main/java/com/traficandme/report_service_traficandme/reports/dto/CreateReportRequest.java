package com.traficandme.report_service_traficandme.reports.dto;

import lombok.Data;

@Data
public class CreateReportRequest {
    private TypeReport type;
    private double latitude;
    private double longitude;
    private StatusReport status;

    public TypeReport getType() {
        return type;
    }

    public void setType(TypeReport type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public StatusReport getStatus() {
        return status;
    }

    public void setStatus(StatusReport status) {
        this.status = status;
    }
}
