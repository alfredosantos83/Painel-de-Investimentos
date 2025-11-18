package com.caixa.invest.controller;

import com.caixa.invest.domain.Product;
import com.caixa.invest.dto.response.PaginatedResponse;
import com.caixa.invest.service.ProductService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class ProductController {

    @Inject
    ProductService productService;

    /**
     * Lista todos os produtos ativos (com cache)
     */
    @GET
    @Path("/all")
    public Response listAll() {
        List<Product> products = productService.findAllActive();
        return Response.ok(products).build();
    }

    /**
     * Lista produtos com paginação
     */
    @GET
    public Response list(
            @QueryParam("page") @DefaultValue("0") int page,
            
            @QueryParam("size") @DefaultValue("10") int size) {
        
        if (page < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Página deve ser maior ou igual a 0")
                    .build();
        }
        
        if (size < 1 || size > 100) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Tamanho deve estar entre 1 e 100")
                    .build();
        }

        List<Product> products = productService.findAllActivePaginated(page, size);
        long total = productService.countActive();
        
        PaginatedResponse<Product> response = PaginatedResponse.of(products, page, size, total);
        
        return Response.ok(response).build();
    }

    /**
     * Busca produtos por tipo (com cache)
     */
    @GET
    @Path("/tipo/{tipo}")
    public Response findByType(
            @PathParam("tipo") Product.TipoProduto tipo) {
        
        List<Product> products = productService.findByType(tipo);
        return Response.ok(products).build();
    }

    /**
     * Busca produtos por nível de risco (com cache)
     */
    @GET
    @Path("/risco/{risco}")
    public Response findByRisk(
            @PathParam("risco") Product.NivelRisco risco) {
        
        List<Product> products = productService.findByRisk(risco);
        return Response.ok(products).build();
    }

    /**
     * Busca produto por ID (com cache)
     */
    @GET
    @Path("/{id}")
    public Response findById(
            @PathParam("id") Long id) {
        
        Product product = productService.findById(id);
        
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Produto não encontrado")
                    .build();
        }
        
        return Response.ok(product).build();
    }
}
