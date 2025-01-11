package si.travelbuddy.ticketsearch.service.dto;

import java.sql.*;
import java.util.UUID;

public class ticketSearchDto {

    private UUID id;
    private String routeId;

    private String from;
    private String to;
    private Timestamp departure;
    private Timestamp arrival;


    ticketSearchDto(){
    }

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setDeparture(Timestamp departure) {
        this.departure = departure;
    }

    public void setArrival(Timestamp arrival) {
        this.arrival = arrival;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public UUID getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Timestamp getDeparture() {
        return departure;
    }

    public Timestamp getArrival() {
        return arrival;
    }

    public String getRouteId() {
        return routeId;
    }
}
