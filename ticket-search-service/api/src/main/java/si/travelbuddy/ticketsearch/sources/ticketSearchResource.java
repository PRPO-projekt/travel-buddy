package si.travelbuddy.ticketsearch.sources;
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
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.travelbuddy.ticketsearch.entity.*;
import si.travelbuddy.ticketsearch.service.bean.*;
import si.travelbuddy.ticketsearch.service.dto.ticketSearchDto;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("ticketSearch")
@CrossOrigin(supportedMethods = "GET, POST, DELETE")
@ApplicationScoped
public class ticketSearchResource {

    @Context
    private UriInfo uriInfo;

    Logger log;


    @Inject
    private ticketSearchBean searchBean;

    @Operation(description = "Vrne seznam kart.", summary = "Seznam kart")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Seznam kart",
                    content = @Content(schema = @Schema(implementation = ticketSearchDto.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Število vrnjenih kart")}
            )
    })
    @GET
    public Response getTicketSearch() {
        // log.info("getTicketSearch has been entered");
        List<Tickets> tmp = searchBean.getAllTickets();
        Tickets a = tmp.get(0);
        //searchBean.createTicket(a);
        // log.info("getTicketSearch will be exited");
        return Response.ok().entity(tmp).build();
    }


    @Operation(description = "Nastavi novo karto", summary = "Naredi novo karto")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Naredi novo karto",
                    content = @Content(schema = @Schema(implementation = ticketSearchDto.class, type = SchemaType.OBJECT) ,
                            mediaType = MediaType.APPLICATION_JSON)
            )
    })
    @POST
        public Response createNewTicket(ticketSearchDto ticket) {
        if(ticket.getArrival() == null || ticket.getDeparture() == null ||
                ticket.getRouteId() == null ||
                ticket.getFrom() == null || ticket.getTo() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Tickets tickets = searchBean.ticketSearchToDto(ticket);
        searchBean.createTicket(tickets);
        return Response.ok().entity(ticket).build();

    }

    @Operation(description = "Vrne posamezno karto.", summary = "Karta z IDjem")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Karta",
                    content = @Content(schema = @Schema(implementation = ticketSearchDto.class, type = SchemaType.OBJECT))
            ),
            @APIResponse(responseCode = "404",
                    description =  "Ni bila najdena karta"
            )
    })
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    public Response getTicketSearchById(@PathParam("id") String id) {
        Tickets tmp = searchBean.getTicketById(UUID.fromString(id));
        if (tmp == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(tmp).build();
    }


    @DELETE
    @Path("{id}")
    public Response deleteTicket(@PathParam("id") String id) {
        Tickets dto = searchBean.getTicketById(UUID.fromString(id));
        searchBean.deleteTicket(dto);
        return Response.status(Response.Status.ACCEPTED).build();
    }
}
