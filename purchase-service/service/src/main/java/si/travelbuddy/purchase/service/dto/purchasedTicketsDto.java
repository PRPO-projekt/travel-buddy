package si.travelbuddy.purchase.service.dto;

import java.io.Serializable;

public class purchasedTicketsDto implements Serializable {

    private String ticketId;
    private String userId;
    private int stanjeRacuna;


    public purchasedTicketsDto() {}

    public int getStanjeRacuna() {
        return stanjeRacuna;
    }
    public void setStanjeRacuna(int stanjeRacuna) {
        this.stanjeRacuna = stanjeRacuna;
    }
    public String getTicketId() {
        return ticketId;
    }
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

}
