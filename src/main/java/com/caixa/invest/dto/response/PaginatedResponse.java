package com.caixa.invest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    
    private List<T> items;
    private int page;
    private int size;
    private long total;
    private int totalPages;
    
    /**
     * Cria uma resposta paginada
     * 
     * @param items Lista de itens da página atual
     * @param page Número da página (base 0)
     * @param size Tamanho da página
     * @param total Total de itens
     */
    public static <T> PaginatedResponse<T> of(List<T> items, int page, int size, long total) {
        int totalPages = (int) Math.ceil((double) total / size);
        
        return PaginatedResponse.<T>builder()
                .items(items)
                .page(page)
                .size(size)
                .total(total)
                .totalPages(totalPages)
                .build();
    }
}
