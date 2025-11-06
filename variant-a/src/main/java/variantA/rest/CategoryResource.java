package variantA.rest;

import variantA.domain.Category;
import variantA.domain.Item;
import variantA.mapper.ItemMapper;
import variantA.persistence.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @GET
    public Response list(@QueryParam("page") @DefaultValue("0") int page,
                         @QueryParam("size") @DefaultValue("20") int size) {
        EntityManager em = JPAUtil.em();
        try {
            TypedQuery<Category> q = em.createQuery(
                    "SELECT c FROM Category c ORDER BY c.id", Category.class);
            q.setFirstResult(page * size).setMaxResults(size);
            return Response.ok(q.getResultList()).build();
        } finally {
            em.close();
        }
    }

    @GET @Path("/{id}/items")
    public Response itemsByCategory(@PathParam("id") Long id,
                                    @QueryParam("page") @DefaultValue("0") int page,
                                    @QueryParam("size") @DefaultValue("20") int size) {
        EntityManager em = JPAUtil.em();
        try {
            TypedQuery<Item> q = em.createQuery(
                    "SELECT i FROM Item i WHERE i.category.id = :cid ORDER BY i.id", Item.class);
            q.setParameter("cid", id).setFirstResult(page * size).setMaxResults(size);

            var out = q.getResultList().stream().map(ItemMapper::toDTO).toList();
            return Response.ok(out).build();
        } finally {
            em.close();
        }
    }
}
