package si.travelbuddy.ticketsearch.dto;

import org.hibernate.Session;
import java.sql.*;
import java.util.UUID;

public class ticketDto {

    private UUID id;
    private String from;
    private String to;
    private Timestamp departure;
    private Timestamp arrival;
    private String routeId;

    ticketDto(){
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
