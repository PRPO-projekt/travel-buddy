package si.travelbuddy.purchase.service.bean;


import si.travelbuddy.purchase.entity.PurchasedTickets;
import si.travelbuddy.purchase.service.dto.purchasedTicketsDto;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class purchasedTicketsBean {
    @PersistenceContext(unitName = "purchase-jpa")
    private EntityManager em;


    public List<PurchasedTickets> getPurchasedTickets(){
        return em.createNamedQuery("PurchasedTickets.findAll", PurchasedTickets.class).getResultList();
    }

    public PurchasedTickets getPurchasedTicketByRacunId(UUID id){
        return em.find(PurchasedTickets.class, id);
    }

    public List<PurchasedTickets> getPurchasedTicketsByPassId(UUID id){
        return em.createNamedQuery("PurchasedTickets.findByTicket", PurchasedTickets.class).setParameter("suppliedId", id).getResultList();
    }

    public List<PurchasedTickets> getPurchasedTicketsByUserId(UUID id){
        return em.createNamedQuery("PurchasedTickets.findByTicket", PurchasedTickets.class).setParameter("suppliedId", id).getResultList();
    }

    public List<PurchasedTickets> getPurchasedTicketsByStanjeRacuna(int id){
        return em.createNamedQuery("PurchasedTickets.findByState", PurchasedTickets.class).setParameter("suppliedId", id).getResultList();
    }

    @Transactional
    public void deleteTicketById(purchasedTicketsDto dto){
        PurchasedTickets tmp = purchasedTicketsDtoToNormal(dto);

        PurchasedTickets pT = em.find(PurchasedTickets.class, tmp.getRacunId());
        em.remove(pT);
    }

    @Transactional
    public void deleteTicketById(String dto){
        PurchasedTickets pT = em.find(PurchasedTickets.class, UUID.fromString(dto));
        em.remove(pT);
    }

    @Transactional
    public void addTicket(purchasedTicketsDto dto){
        PurchasedTickets tmp = purchasedTicketsDtoToNormal(dto);
        em.merge(tmp);
    }

    @Transactional
    public void purchaseTicket(purchasedTicketsDto purchasedTickets){
        int stanjeRacuna = stanjeRacuna();
        PurchasedTickets pt = purchasedTicketsDtoToNormal(purchasedTickets);
        pt.setStanjeRacuna(stanjeRacuna);
        em.merge(pt);
    }

    @Transactional
    public void purchaseTicket(PurchasedTickets pt){
        int stanjeRacuna = stanjeRacuna();
        pt.setStanjeRacuna(stanjeRacuna);
        em.merge(pt);
    }

    public PurchasedTickets purchasedTicketsDtoToNormal(purchasedTicketsDto dto){
        PurchasedTickets a = new PurchasedTickets();
        a.setStanjeRacuna(dto.getStanjeRacuna());
        a.setTicketId(UUID.fromString(dto.getTicketId()));
        a.setUserId(UUID.fromString(dto.getUserId()));
        return a;

    }

    private int stanjeRacuna(){
        return (int)(Math.random()*3)+1;
    }
}