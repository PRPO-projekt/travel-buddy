package si.travelbuddy.ticketsearch.service.bean;

import si.travelbuddy.ticketsearch.entity.ticketSearch;
import si.travelbuddy.ticketsearch.service.dto.ticketSearchDto;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.sql.Timestamp;
import java.util.UUID;

public class ticketSearchBean {
    @PersistenceContext(unitName = "ticketSearch-jpa")
    private EntityManager em;


    @PostConstruct
    public void init() {

    }

    public List<ticketSearch> getAllTickets(){
        return em.createNamedQuery("ticketSearch.findAll", ticketSearch.class).getResultList();
    }
    public ticketSearch getTicketById(UUID id){
        return em.find(ticketSearch.class, id);
    }
    public void createTicket(ticketSearch ticket){
        em.persist(ticket);
    }
    public void updateTicket(ticketSearch ticket){
        em.merge(ticket);
    }
    public void deleteTicket(ticketSearch ticket){
        em.remove(ticket);
    }

    public ticketSearch ticketSearchToDto(ticketSearchDto a){
        ticketSearch b = new ticketSearch();

        b.setArrival(a.getArrival());
        b.setDeparture(a.getDeparture());
        b.setId(a.getRouteId());
        b.setRouteId(a.getRouteId());
        b.setTo(a.getTo());
        b.setFrom(a.getFrom());
        return b;
    }

}
