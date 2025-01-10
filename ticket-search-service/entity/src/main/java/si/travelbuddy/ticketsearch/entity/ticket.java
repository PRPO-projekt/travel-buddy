package si.travelbuddy.ticketsearch.entity;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity

@Table(name = "Tickets")
@NamedQueries({
        @NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Tickets t"),
        @NamedQuery(name = "Ticket.findById", query = "SELECT t FROM Tickets t WHERE t.id = :id")
})
public class ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name="to")
    private String to;

    @Column(name="from")
    private String from;

    @Column(name="departure")
    private Timestamp departure;

    @Column(name="arrival")
    private Timestamp arrival;

    @Column(name="routeId")
    private String routeId;

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
