package com.chicos_ingenieros.zenkai.Sales.Domain;

import com.chicos_ingenieros.zenkai.Sale_Details.Domain.SaleDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sale {
    private Long saleId;
    private Long userId;
    private LocalDateTime saleDate;
    private BigDecimal totalPrice;
    private List<SaleDetail> details;
}
