package si.travelbuddy.ticketsearch.service.bean;

import si.travelbuddy.ticketsearch.entity.Tickets;
import si.travelbuddy.ticketsearch.service.dto.ticketSearchDto;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


@ApplicationScoped
public class ticketSearchBean {
    @PersistenceContext(unitName = "ticketSearch-jpa")
    private EntityManager em;


    @PostConstruct
    public void init() {

    }

    @Transactional
    public List<Tickets> getAllTickets(){
        List<Tickets> returnParam = em.createNamedQuery("Tickets.findAll", Tickets.class).getResultList();
        return returnParam;
    }
    @Transactional
    public Tickets getTicketById(UUID id){
        return em.find(Tickets.class, id);
    }

    @Transactional
    public void createTicket(Tickets ticket){
        em.merge(ticket);
    }

    @Transactional
    public void updateTicket(Tickets ticket){
        em.merge(ticket);
    }

    @Transactional
    public void deleteTicket(Tickets ticket){
        Tickets tmp = em.find(Tickets.class, ticket.getId());
        em.remove(tmp);
    }

    public Tickets ticketSearchToDto(ticketSearchDto a){
        Tickets b = new Tickets();

        b.setArrival(Timestamp.valueOf(a.getArrival()));
        b.setDeparture(Timestamp.valueOf(a.getDeparture()));
        b.setId(a.getId());
        b.setRouteId(a.getRouteId());
        b.setTo(a.getTo());
        b.setFrom(a.getFrom());
        b.setPrice(a.getPrice());
        b.setStopTime(a.getStop_time());
        return b;
    }

}
