package variantA.rest;

import variantA.dto.ItemDTO;
import variantA.mapper.ItemMapper;
import variantA.domain.Item;
import variantA.persistence.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @GET
    public Response list(@QueryParam("page") @DefaultValue("0") int page,
                         @QueryParam("size") @DefaultValue("20") int size,
                         @QueryParam("categoryId") Long categoryId) {
        EntityManager em = JPAUtil.em();
        try {
            String jpql = "SELECT i FROM Item i " +
                    (categoryId != null ? "WHERE i.category.id = :cid " : "") +
                    "ORDER BY i.id";
            TypedQuery<Item> q = em.createQuery(jpql, Item.class)
                    .setFirstResult(page * size)
                    .setMaxResults(size);
            if (categoryId != null) q.setParameter("cid", categoryId);

            List<ItemDTO> out = q.getResultList().stream().map(ItemMapper::toDTO).toList();
            return Response.ok(out).build();
        } finally {
            em.close();
        }
    }

    @GET @Path("/{id}")
    public Response byId(@PathParam("id") Long id) {
        EntityManager em = JPAUtil.em();
        try {
            Item i = em.find(Item.class, id);
            if (i == null) return Response.status(Response.Status.NOT_FOUND).build();
            return Response.ok(ItemMapper.toDTO(i)).build();
        } finally {
            em.close();
        }
    }
}
