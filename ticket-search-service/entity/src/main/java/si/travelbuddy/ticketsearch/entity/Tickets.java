package si.travelbuddy.ticketsearch.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "Tickets")
@NamedQueries(value={
        @NamedQuery(name = "Tickets.findAll", query = "SELECT t FROM Tickets t"),
        @NamedQuery(name = "Tickets.findById", query = "SELECT t FROM Tickets t WHERE t.id = :id")
})

public class Tickets implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name="tolocation")
    private String to;

    @Column(name="fromlocation")
    private String from;

    @Column(name="departure")
    private Timestamp departure;

    @Column(name="arrival")
    private Timestamp arrival;

    @Column(name="routeId")
    private UUID routeId;

    @Column(name="price")
    private float price;

    @Column(name="stop_time")
    private UUID stopTime;

    public void setStopTime(String stopTime) {
        this.stopTime = UUID.fromString(stopTime);
    }


    public void setPrice(float price) {this.price = price;}

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
        this.routeId = UUID.fromString(routeId);
    }

    public UUID getStopTime(){return stopTime;}

    public float getPrice() { return price;}

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

    public UUID getRouteId() {
        return routeId;
    }
}
