package si.travelbuddy.ticketsearch.service.bean;

import si.travelbuddy.ticketsearch.entity.Tickets;
import si.travelbuddy.ticketsearch.service.dto.ticketSearchDto;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ticketSearchBean {
    @PersistenceContext(unitName = "ticketSearch-jpa")
    private EntityManager em;


    @PostConstruct
    public void init() {

    }

    public List<Tickets> getAllTickets(){
        return em.createNamedQuery("Tickets.findAll", Tickets.class).getResultList();
    }
    public Tickets getTicketById(UUID id){
        return em.find(Tickets.class, id);
    }
    public void createTicket(Tickets ticket){
        em.persist(ticket);
    }
    public void updateTicket(Tickets ticket){
        em.merge(ticket);
    }
    public void deleteTicket(Tickets ticket){
        em.remove(ticket);
    }

    public Tickets ticketSearchToDto(ticketSearchDto a){
        Tickets b = new Tickets();

        b.setArrival(a.getArrival());
        b.setDeparture(a.getDeparture());
        b.setId(a.getRouteId());
        b.setRouteId(a.getRouteId());
        b.setTo(a.getTo());
        b.setFrom(a.getFrom());
        return b;
    }

}
