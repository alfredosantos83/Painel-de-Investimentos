package com.caixa.invest.service;

import com.caixa.invest.domain.Product;
import io.quarkus.cache.CacheResult;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProductService {

    /**
     * Busca todos os produtos ativos com cache
     * Cache expira em 5 minutos
     */
    @CacheResult(cacheName = "products-cache")
    public List<Product> findAllActive() {
        return Product.find("ativo = true").list();
    }

    /**
     * Busca produtos ativos com paginação
     * 
     * @param pageIndex Índice da página (base 0)
     * @param pageSize Tamanho da página
     * @return Lista paginada de produtos
     */
    public List<Product> findAllActivePaginated(int pageIndex, int pageSize) {
        return Product.find("ativo = true")
                .page(Page.of(pageIndex, pageSize))
                .list();
    }

    /**
     * Conta total de produtos ativos
     */
    public long countActive() {
        return Product.count("ativo = true");
    }

    /**
     * Busca produtos por tipo com cache
     * 
     * @param tipo Tipo do produto
     * @return Lista de produtos do tipo especificado
     */
    @CacheResult(cacheName = "products-by-type-cache")
    public List<Product> findByType(Product.TipoProduto tipo) {
        return Product.find("tipo = ?1 and ativo = true", tipo).list();
    }

    /**
     * Busca produtos por nível de risco com cache
     * 
     * @param risco Nível de risco
     * @return Lista de produtos com o risco especificado
     */
    @CacheResult(cacheName = "products-by-risk-cache")
    public List<Product> findByRisk(Product.NivelRisco risco) {
        return Product.find("risco = ?1 and ativo = true", risco).list();
    }

    /**
     * Busca produto por ID com cache
     * 
     * @param id ID do produto
     * @return Produto encontrado
     */
    @CacheResult(cacheName = "product-by-id-cache")
    public Product findById(Long id) {
        return Product.findById(id);
    }
}
