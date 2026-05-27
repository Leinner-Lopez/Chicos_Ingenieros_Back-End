package com.chicos_ingenieros.zenkai.Sales.Infrastructure;

import com.chicos_ingenieros.zenkai.Config.Application.JwtService;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.IlegalActionException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Sales.Application.SaleService;
import com.chicos_ingenieros.zenkai.Sales.Domain.Sale;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleHistoryDTO;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleItemRequest;
import com.chicos_ingenieros.zenkai.Sales.Infrastructure.DTO.SaleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SaleController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SaleService saleService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Sale saleSample;
    private SaleResponse saleResponseSample;
    private SaleHistoryDTO saleHistoryDTOSample;

    @BeforeEach
    void setUp() {
        saleSample = Sale.builder()
                .saleId(1L)
                .userId(10L)
                .saleDate(LocalDateTime.of(2026, 5, 27, 10, 30))
                .totalPrice(new BigDecimal("7000.00"))
                .details(List.of())
                .build();

        saleResponseSample = new SaleResponse(
                1L,
                10L,
                LocalDateTime.of(2026, 5, 27, 10, 30),
                new BigDecimal("7000.00")
        );

        saleHistoryDTOSample = new SaleHistoryDTO(
                1L,
                LocalDateTime.of(2026, 5, 27, 10, 30),
                "Arroz Diana",
                3L,
                2,
                new BigDecimal("3500.00"),
                new BigDecimal("7000.00")
        );
    }

    // ─── POST /api/sales ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/sales - Debería crear una venta y retornar 201 Created")
    void createSale_ShouldReturnCreatedSale() throws Exception {
        SaleItemRequest request = new SaleItemRequest(1L, 2, 10L);
        when(saleService.createSale(any(SaleItemRequest.class))).thenReturn(saleSample);

        mockMvc.perform(post("/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.saleId").value(1L))
                .andExpect(jsonPath("$.userId").value(10L))
                .andExpect(jsonPath("$.totalPrice").value(7000.00));

        verify(saleService, times(1)).createSale(any(SaleItemRequest.class));
    }

    @Test
    @DisplayName("POST /api/sales - Debería retornar 400 cuando no hay stock suficiente")
    void createSale_ShouldReturn400WhenInsufficientStock() throws Exception {
        SaleItemRequest request = new SaleItemRequest(1L, 9999, 10L);
        when(saleService.createSale(any(SaleItemRequest.class)))
                .thenThrow(new IlegalActionException("Product not enough stock"));

        mockMvc.perform(post("/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(saleService, times(1)).createSale(any(SaleItemRequest.class));
    }

    @Test
    @DisplayName("POST /api/sales - Debería retornar 404 cuando no hay lotes disponibles para el producto")
    void createSale_ShouldReturn404WhenNoLotsAvailable() throws Exception {
        SaleItemRequest request = new SaleItemRequest(99L, 1, 10L);
        when(saleService.createSale(any(SaleItemRequest.class)))
                .thenThrow(new ResourceNotFoundException("Lots not found for this product"));

        mockMvc.perform(post("/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(saleService, times(1)).createSale(any(SaleItemRequest.class));
    }

    // ─── GET /api/sales ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/sales - Debería retornar la lista de ventas como SaleResponse y 200 OK")
    void findAllSales_ShouldReturnSaleResponseList() throws Exception {
        when(saleService.findAllSales()).thenReturn(List.of(saleResponseSample));

        mockMvc.perform(get("/api/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].saleId").value(1L))
                .andExpect(jsonPath("$[0].userId").value(10L))
                .andExpect(jsonPath("$[0].totalPrice").value(7000.00));

        verify(saleService, times(1)).findAllSales();
    }

    @Test
    @DisplayName("GET /api/sales - Debería retornar lista vacía cuando no hay ventas")
    void findAllSales_ShouldReturnEmptyListWhenNoSales() throws Exception {
        when(saleService.findAllSales()).thenReturn(List.of());

        mockMvc.perform(get("/api/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(saleService, times(1)).findAllSales();
    }

    // ─── GET /api/sales/history ──────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/sales/history - Debería retornar el historial completo de ventas y 200 OK")
    void getSaleHistory_ShouldReturnHistoryList() throws Exception {
        when(saleService.getSaleHistory()).thenReturn(List.of(saleHistoryDTOSample));

        mockMvc.perform(get("/api/sales/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].saleId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("Arroz Diana"))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].totalPrice").value(7000.00));

        verify(saleService, times(1)).getSaleHistory();
    }

    // ─── GET /api/sales/{id} ─────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/sales/{id} - Debería retornar la venta correspondiente y 200 OK")
    void getSaleById_ShouldReturnSale() throws Exception {
        when(saleService.findSaleById(1L)).thenReturn(saleSample);

        mockMvc.perform(get("/api/sales/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saleId").value(1L))
                .andExpect(jsonPath("$.userId").value(10L))
                .andExpect(jsonPath("$.totalPrice").value(7000.00));

        verify(saleService, times(1)).findSaleById(1L);
    }

    @Test
    @DisplayName("GET /api/sales/{id} - Debería retornar 404 cuando la venta no existe")
    void getSaleById_ShouldReturn404WhenNotFound() throws Exception {
        when(saleService.findSaleById(99L))
                .thenThrow(new ResourceNotFoundException("Venta no encontrada: 99"));

        mockMvc.perform(get("/api/sales/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(saleService, times(1)).findSaleById(99L);
    }

    // ─── GET /api/sales/user/{id} ────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/sales/user/{id} - Debería retornar las ventas del usuario y 200 OK")
    void getSalesByUser_ShouldReturnUserSales() throws Exception {
        when(saleService.findSalesByUser(10L)).thenReturn(List.of(saleSample));

        mockMvc.perform(get("/api/sales/user/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].saleId").value(1L))
                .andExpect(jsonPath("$[0].userId").value(10L));

        verify(saleService, times(1)).findSalesByUser(10L);
    }

    @Test
    @DisplayName("GET /api/sales/user/{id} - Debería retornar lista vacía para usuario sin ventas")
    void getSalesByUser_ShouldReturnEmptyListForUserWithNoSales() throws Exception {
        when(saleService.findSalesByUser(5L)).thenReturn(List.of());

        mockMvc.perform(get("/api/sales/user/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(saleService, times(1)).findSalesByUser(5L);
    }

    // ─── DELETE /api/sales/{id}/cancel ───────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/sales/{id}/cancel - Debería cancelar la venta y retornar 204 No Content")
    void cancelSale_ShouldReturnNoContent() throws Exception {
        doNothing().when(saleService).cancelSale(1L);

        mockMvc.perform(delete("/api/sales/{id}/cancel", 1L))
                .andExpect(status().isNoContent());

        verify(saleService, times(1)).cancelSale(1L);
    }

    @Test
    @DisplayName("DELETE /api/sales/{id}/cancel - Debería retornar 404 cuando la venta no existe")
    void cancelSale_ShouldReturn404WhenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Venta no encontrada: 99"))
                .when(saleService).cancelSale(99L);

        mockMvc.perform(delete("/api/sales/{id}/cancel", 99L))
                .andExpect(status().isNotFound());

        verify(saleService, times(1)).cancelSale(99L);
    }
}
