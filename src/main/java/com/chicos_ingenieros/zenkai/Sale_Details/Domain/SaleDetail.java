package com.chicos_ingenieros.zenkai.Sale_Details.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleDetail {
        private Long detailId;
        private Long saleId;
        private Long lotId;
        private Integer quantity;
        private BigDecimal unitPrice;
}
