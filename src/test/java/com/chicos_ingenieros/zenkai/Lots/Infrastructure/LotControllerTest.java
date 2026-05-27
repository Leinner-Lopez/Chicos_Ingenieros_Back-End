package com.chicos_ingenieros.zenkai.Lots.Infrastructure;

import com.chicos_ingenieros.zenkai.Config.Application.JwtService;
import com.chicos_ingenieros.zenkai.Lots.Application.LotService;
import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.DTO.LotDTO;
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
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LotController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
class LotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LotService lotService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Lot lotSample;
    private LotDTO lotDTOSample;

    @BeforeEach
    void setUp() {
        lotSample = Lot.builder()
                .lotId(1L)
                .expirationDate(LocalDate.of(2027, 1, 1))
                .stockQuantity(100)
                .status(LotStatus.AVAILABLE)
                .productId(5L)
                .productPrice(new BigDecimal("3500.00"))
                .build();

        lotDTOSample = new LotDTO(
                1L,
                LocalDate.of(2027, 1, 1),
                100,
                "Arroz Diana"
        );
    }

    // ─── POST /api/lots ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/lots - Debería guardar un lote y retornar 201 Created")
    void save_ShouldReturnCreatedLot() throws Exception {
        when(lotService.saveLot(any(Lot.class))).thenReturn(lotSample);

        mockMvc.perform(post("/api/lots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lotSample)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lotId").value(1L))
                .andExpect(jsonPath("$.stockQuantity").value(100))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.productId").value(5L));

        verify(lotService, times(1)).saveLot(any(Lot.class));
    }

    // ─── GET /api/lots ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/lots - Debería retornar la lista de lotes como DTO y 200 OK")
    void getAll_ShouldReturnLotDTOList() throws Exception {
        when(lotService.findAllLots()).thenReturn(List.of(lotDTOSample));

        mockMvc.perform(get("/api/lots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].lotId").value(1L))
                .andExpect(jsonPath("$[0].stockQuantity").value(100))
                .andExpect(jsonPath("$[0].productName").value("Arroz Diana"));

        verify(lotService, times(1)).findAllLots();
    }

    @Test
    @DisplayName("GET /api/lots - Debería retornar lista vacía cuando no hay lotes")
    void getAll_ShouldReturnEmptyListWhenNoLots() throws Exception {
        when(lotService.findAllLots()).thenReturn(List.of());

        mockMvc.perform(get("/api/lots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(lotService, times(1)).findAllLots();
    }

    // ─── GET /api/lots/{id} ──────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/lots/{id} - Debería retornar el lote correspondiente y 200 OK")
    void findById_ShouldReturnLot() throws Exception {
        when(lotService.findLotById(1L)).thenReturn(lotSample);

        mockMvc.perform(get("/api/lots/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lotId").value(1L))
                .andExpect(jsonPath("$.stockQuantity").value(100))
                .andExpect(jsonPath("$.productId").value(5L));

        verify(lotService, times(1)).findLotById(1L);
    }

    @Test
    @DisplayName("GET /api/lots/{id} - Debería retornar 200 OK con body vacío cuando el lote no existe")
    void findById_ShouldReturnNullBodyWhenNotFound() throws Exception {
        when(lotService.findLotById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/lots/{id}", 99L))
                .andExpect(status().isOk());

        verify(lotService, times(1)).findLotById(99L);
    }

    // ─── GET /api/lots/count ─────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/lots/count - Debería retornar el total de lotes y 200 OK")
    void getCountLots_ShouldReturnTotalCount() throws Exception {
        when(lotService.countLots()).thenReturn(25L);

        mockMvc.perform(get("/api/lots/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("25"));

        verify(lotService, times(1)).countLots();
    }

    // ─── PUT /api/lots ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/lots - Debería actualizar un lote y retornar 200 OK")
    void update_ShouldReturnUpdatedLot() throws Exception {
        Lot updatedLot = Lot.builder()
                .lotId(1L)
                .expirationDate(LocalDate.of(2027, 6, 30))
                .stockQuantity(80)
                .status(LotStatus.AVAILABLE)
                .productId(5L)
                .productPrice(new BigDecimal("3500.00"))
                .build();

        when(lotService.updateLot(eq(1L), any(Lot.class))).thenReturn(updatedLot);

        mockMvc.perform(put("/api/lots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedLot)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lotId").value(1L))
                .andExpect(jsonPath("$.stockQuantity").value(80));

        verify(lotService, times(1)).updateLot(eq(1L), any(Lot.class));
    }

    @Test
    @DisplayName("PUT /api/lots - Debería retornar 200 OK con null cuando el lote a actualizar no existe")
    void update_ShouldReturnNullWhenLotNotFound() throws Exception {
        when(lotService.updateLot(eq(99L), any(Lot.class))).thenReturn(null);

        Lot nonExistent = Lot.builder().lotId(99L).stockQuantity(10).build();

        mockMvc.perform(put("/api/lots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistent)))
                .andExpect(status().isOk());
    }

    // ─── DELETE /api/lots/{id} ───────────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/lots/{id} - Debería eliminar el lote y retornar 200 OK")
    void delete_ShouldReturnStatusOk() throws Exception {
        doNothing().when(lotService).deleteLotById(1L);

        mockMvc.perform(delete("/api/lots/{id}", 1L))
                .andExpect(status().isOk());

        verify(lotService, times(1)).deleteLotById(1L);
    }
}
