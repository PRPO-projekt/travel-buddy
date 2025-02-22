package si.travelbuddy.purchase.sources;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.travelbuddy.purchase.entity.*;
import si.travelbuddy.purchase.service.bean.*;
import si.travelbuddy.purchase.service.dto.purchasedTicketsDto;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("ticketSearch")
@CrossOrigin(supportedMethods = "GET, POST, DELETE, PUT")
@ApplicationScoped
public class ticketPurchaseResource {
    @Context
    private UriInfo uriInfo;

    Logger log;


    @Inject
    private purchasedTicketsBean purchaseBean;

    @Operation(description = "Vrne seznam računov.", summary = "Seznam računov")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Seznam računov",
                    content = @Content(schema = @Schema(implementation = purchasedTicketsDto.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Število vrnjenih računov")}
            )
    })
    @GET
    public Response getTicketSearch() {
        // log.info("getTicketSearch has been entered");
        List<PurchasedTickets> tmp = purchaseBean.getPurchasedTickets();
        return Response.ok().entity(tmp).build();
    }


    @Operation(description = "\"Kupimo\" novo karto", summary = "Nastavi račun kot da bo kmalu procesiran")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Naredi nov račun",
                    content = @Content(schema = @Schema(implementation = purchasedTicketsDto.class, type = SchemaType.OBJECT) ,
                            mediaType = MediaType.APPLICATION_JSON)
            )
    })
    @POST
    public Response createNewTicket(purchasedTicketsDto ticket) {
        if(ticket.getUserId() == null || ticket.getTicketId() == null ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        purchaseBean.addTicket(ticket);
        return Response.ok().entity(ticket).build();

    }



    @Operation(description = "Nastavi račun na \n1 - Potrjen\n2 - Zavrnjen\n3 - Povrjnjen\n.", summary = "Račun je sprocesiran do konca")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Vrjnen račun",
                    content = @Content(schema = @Schema(implementation = purchasedTicketsDto.class, type = SchemaType.OBJECT))
            ),
            @APIResponse(responseCode = "404",
                    description =  "Ni bil najden noben račun s podanim IDjem."
            )
    })
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Response updatePurchasedTicketStatus(@PathParam("id") String id) {

        PurchasedTickets tmp = purchaseBean.getPurchasedTicketByRacunId(UUID.fromString(id));
        if (tmp == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        purchaseBean.purchaseTicket(tmp);
        return Response.ok().entity(tmp).build();
    }


    @Operation(description = "Vrne račun s podanim IDjem.", summary = "Račun z IDjem")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Vrjnen račun",
                    content = @Content(schema = @Schema(implementation = purchasedTicketsDto.class, type = SchemaType.OBJECT))
            ),
            @APIResponse(responseCode = "404",
                    description =  "Ni bil najden račun."
            )
    })
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    public Response getTicketSearchById(@PathParam("id") String id) {

        PurchasedTickets tmp = purchaseBean.getPurchasedTicketByRacunId(UUID.fromString(id));
        if (tmp == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(tmp).build();
    }


    @Operation(description = "Vrne račune uporabnika s podanim IDjem.", summary = "Računi uporabnika z IDjem")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Vrjneni računi",
                    content = @Content(schema = @Schema(implementation = purchasedTicketsDto.class, type = SchemaType.OBJECT))
            ),
            @APIResponse(responseCode = "404",
                    description =  "Ni bil najden noben račun."
            )
    })
    @Path("users/{id}")
    @GET
    public Response getUserPurchasedTickets(@PathParam("id") String id) {
        List<PurchasedTickets> tmp = purchaseBean.getPurchasedTicketsByUserId(UUID.fromString(id));

        return Response.ok().entity(tmp).build();
    }

    @Operation(description = "Vrne račune prodane posamezne karte", summary = "Računi s karto")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Vrjneni računi",
                    content = @Content(schema = @Schema(implementation = purchasedTicketsDto.class, type = SchemaType.OBJECT))
            ),
            @APIResponse(responseCode = "404",
                    description =  "Ni bil najden noben račun s to kupljeno karto."
            )
    })
    @Path("tickets/{id}")
    @GET
    public Response getSoldTickets(@PathParam("id") String id) {
        List<PurchasedTickets> tmp = purchaseBean.getPurchasedTicketsByPassId(UUID.fromString(id));
        return Response.ok().entity(tmp).build();
    }

    @Operation(description = "Vrne račune s podanim statusom.", summary = "Račun s statusom")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Vrjneni računi",
                    content = @Content(schema = @Schema(implementation = purchasedTicketsDto.class, type = SchemaType.OBJECT))
            ),
            @APIResponse(responseCode = "404",
                    description =  "Ni bil najden noben račun s tem statusom."
            )
    })
    @Path("status/{st}")
    @GET
    public Response getBillsStatus(@PathParam("st") int st) {
        List<PurchasedTickets> tmp = purchaseBean.getPurchasedTicketsByStanjeRacuna(st);
        return Response.ok().entity(tmp).build();
    }


    @Operation(description = "Izbriše račun s podanim IDjem.", summary = "Izbriše račun z IDjem")
    @APIResponses({
            @APIResponse(responseCode = "202",
                    description = "Račun uspešno izbrisan"
            ),
            @APIResponse(responseCode = "404",
                    description =  "Ni bil najden noben račun s tem IDjem."
            )
    })
    @DELETE
    @Path("{id}")
    public Response deleteTicket(@PathParam("id") String id) {
        purchaseBean.deleteTicketById(id);
        return Response.status(Response.Status.ACCEPTED).build();
    }
}
