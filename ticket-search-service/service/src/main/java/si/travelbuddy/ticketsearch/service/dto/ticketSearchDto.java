package si.travelbuddy.ticketsearch.service.dto;

import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.annotation.JsonbProperty;
import java.io.Serializable;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

import java.sql.*;
import java.util.UUID;

public class ticketSearchDto implements Serializable {

    private String id;
    private String routeId;

    private String from;
    private String to;
    private String departure;
    private String arrival;
    private float price;

    @JsonbProperty("stopTime")
    private String stop_time;

    public ticketSearchDto(){}

    public String getStop_time() {
        return stop_time;
    }
    public void setStop_time(String stop_time) {this.stop_time = stop_time;}
    public void setId(String id) {
        this.id = id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public void setRouteId(String routeId) { this.routeId = (routeId);}
    public void setPrice(float price) {this.price = price;}

    public String getId() {
        return id;
    }
    public float getPrice() {return price;}
    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public String getRouteId() {
        return routeId;
    }
}
