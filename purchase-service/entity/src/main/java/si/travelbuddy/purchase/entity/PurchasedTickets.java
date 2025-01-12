package si.travelbuddy.purchase.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name="PurchasedTickets")
@NamedQueries(value = {
        @NamedQuery(name = "PurchasedTickets.findAll", query = "SELECT pt FROM PurchasedTickets pt"),
        @NamedQuery(name = "PurchasedTickets.findByUser",query = "SELECT pt FROM PurchasedTickets pt WHERE pt.userId = :suppliedId"),
        @NamedQuery(name = "PurchasedTickets.findByTicket", query = "SELECT pt FROM PurchasedTickets pt WHERE pt.ticketId = :suppliedId"),
        @NamedQuery(name = "PurchasedTickets.findByState", query = "SELECT pt FROM PurchasedTickets pt WHERE pt.stanjeRacuna = :suppliedId")

})
public class PurchasedTickets implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_racuna")
    private UUID racunId;

    @Column(name="ticket_id")
    @PrimaryKeyJoinColumn
    private UUID ticketId;

    @Column(name="user_id")
    @PrimaryKeyJoinColumn
    private UUID userId;

    @Column(name="stanje_racuna")
    private int stanjeRacuna;

    public int getStanjeRacuna() {
        return stanjeRacuna;
    }

    public void setStanjeRacuna(int stanjeRacuna) {
        this.stanjeRacuna = stanjeRacuna;
    }

    public UUID getRacunId() {
        return racunId;
    }
    public void setRacunId(UUID idRacuna) {
        this.racunId = idRacuna;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
