package com.chicos_ingenieros.zenkai.Products.Infrastructure;

import com.chicos_ingenieros.zenkai.Config.Application.JwtService;
import com.chicos_ingenieros.zenkai.Products.Application.ProductService;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@WithMockUser // Evita problemas de 401 para todos los métodos
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    // Mocks de Seguridad indispensables para levantar el contexto
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Product productSample;

    @BeforeEach
    void setUp() {
        productSample = new Product();
        productSample.setProductId(1L);
        productSample.setName("Producto de Prueba");
        // Llena aquí más atributos de tu entidad Product si son obligatorios por @Valid
    }

    @Test
    void save_ShouldReturnCreatedProduct() throws Exception {
        // 1. Creamos el Mock del archivo (la imagen)
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido-de-imagen".getBytes()
        );

        // 2. Creamos el Mock del JSON de datos usando tu objeto productSample
        MockMultipartFile dataJson = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(productSample)
        );

        when(productService.saveProduct(any(Product.class), any())).thenReturn(productSample);

        // Usamos multipart() en lugar de post() para soportar archivos
        mockMvc.perform(multipart("/api/products")
                        .file(dataJson)
                        .file(imageFile)
                        .with(csrf())) // Evita el 403 por CSRF
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.name").value("Producto de Prueba"));

        verify(productService, times(1)).saveProduct(any(Product.class), any());
    }

    @Test
    void getAll_ShouldReturnListOfProductDTO() throws Exception {
        ProductDTO dto = new ProductDTO(
                1L,
                "Laptop",
                "Tecnología",
                new java.math.BigDecimal("1200.50"),
                "http://image.url"
        );
        List<ProductDTO> list = List.of(dto);

        when(productService.findAllProducts()).thenReturn(list);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].categoryName").value("Tecnología"));

        verify(productService, times(1)).findAllProducts();
    }

    @Test
    void getInventory_ShouldReturnInventoryList() throws Exception {
        ProductInventoryDTO inventoryDto = new ProductInventoryDTO(
                1L,
                "Laptop",
                5,
                50L
        );
        List<ProductInventoryDTO> list = List.of(inventoryDto);

        when(productService.findGeneralInventory()).thenReturn(list);

        mockMvc.perform(get("/api/products/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].totalStock").value(50));

        verify(productService, times(1)).findGeneralInventory();
    }

    @Test
    void findById_ShouldReturnProduct() throws Exception {
        when(productService.findProductById(1L)).thenReturn(productSample);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.name").value("Producto de Prueba"));

        verify(productService, times(1)).findProductById(1L);
    }

    @Test
    void countProducts_ShouldReturnTotalCount() throws Exception {
        when(productService.countProducts()).thenReturn(15L);

        mockMvc.perform(get("/api/products/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("15"));

        verify(productService, times(1)).countProducts();
    }

    @Test
    void update_ShouldReturnUpdatedProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(productSample);

        mockMvc.perform(put("/api/products")
                        .with(csrf()) // Evita el 403 por CSRF
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productSample)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void delete_ShouldReturnStatusOk() throws Exception {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete("/api/products/{id}", 1L)
                        .with(csrf())) // Evita el 403 por CSRF
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProductById(1L);
    }
}