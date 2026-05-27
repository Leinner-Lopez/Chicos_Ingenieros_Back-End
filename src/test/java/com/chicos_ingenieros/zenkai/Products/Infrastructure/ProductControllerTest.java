package com.chicos_ingenieros.zenkai.Products.Infrastructure;

import com.chicos_ingenieros.zenkai.Config.Application.JwtService;
import com.chicos_ingenieros.zenkai.Products.Application.ProductService;
import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductDTO;
import com.chicos_ingenieros.zenkai.Products.Infrastructure.DTO.ProductInventoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@WithMockUser // Evita problemas de 401 para todos los métodos (simula usuario autenticado)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    // Mocks de Seguridad indispensables para levantar el contexto de Spring Security
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Product productSample;
    private ProductDTO productDTOSample;
    private ProductInventoryDTO productInventoryDTOSample;

    @BeforeEach
    void setUp() {
        productSample = Product.builder()
                .productId(1L)
                .name("Producto de Prueba")
                .price(new BigDecimal("3500.00"))
                .description("Descripción de Prueba")
                .minStock(10)
                .categoryId(1L)
                .build();

        // Estructura adaptada al ProductDTO del ecosistema
        productDTOSample = new ProductDTO(
                1L,
                "Producto de Prueba",
                "Lácteos",
                new BigDecimal("3500.00"),
                "https://res.cloudinary.com/fake/image.jpg",
                "H",
                5L
        );

        productInventoryDTOSample = new ProductInventoryDTO(1L, "Producto de Prueba", 10, 20L);
    }

    @Test
    @DisplayName("POST /api/products - Debería guardar un producto con su imagen y retornar 201 Created")
    void saveShouldReturnCreatedProduct() throws Exception {
        // Arrange
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido-de-imagen".getBytes()
        );

        MockMultipartFile dataJson = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(productSample)
        );

        when(productService.saveProduct(any(Product.class), any())).thenReturn(productSample);

        // Act & Assert
        mockMvc.perform(multipart("/api/products")
                        .file(dataJson)
                        .file(imageFile)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.name").value("Producto de Prueba"))
                .andExpect(jsonPath("$.price").value(3500.00));

        verify(productService, times(1)).saveProduct(any(Product.class), any());
    }

    @Test
    @DisplayName("GET /api/products - Debería retornar la lista de todos los productos en formato DTO y 200 OK")
    void getAllShouldReturnListOfProductDTO() throws Exception {
        // Arrange
        when(productService.findAllProducts()).thenReturn(List.of(productDTOSample));

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].name").value("Producto de Prueba"))
                .andExpect(jsonPath("$[0].categoryName").value("Lácteos"));

        verify(productService, times(1)).findAllProducts();
    }

    @Test
    @DisplayName("GET /api/products/inventory - Debería retornar el listado del inventario general y 200 OK")
    void getInventoryShouldReturnInventoryList() throws Exception {
        // Arrange
        when(productService.findGeneralInventory()).thenReturn(List.of(productInventoryDTOSample));

        // Act & Assert
        mockMvc.perform(get("/api/products/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].name").value("Producto de Prueba"))
                .andExpect(jsonPath("$[0].totalStock").value(20));

        verify(productService, times(1)).findGeneralInventory();
    }

    @Test
    @DisplayName("GET /api/products/{id} - Debería retornar un producto específico por su ID y 200 OK")
    void findByIdShouldReturnProduct() throws Exception {
        // Arrange
        Long id = 1L;
        when(productService.findProductById(id)).thenReturn(productSample);

        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.name").value("Producto de Prueba"));

        verify(productService, times(1)).findProductById(id);
    }

    @Test
    @DisplayName("GET /api/products/count - Debería retornar la cantidad total de productos y 200 OK")
    void countProductsShouldReturnTotalCount() throws Exception {
        // Arrange
        when(productService.countProducts()).thenReturn(15L);

        // Act & Assert
        mockMvc.perform(get("/api/products/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("15"));

        verify(productService, times(1)).countProducts();
    }

    @Test
    @DisplayName("GET /api/products/category/{categoryId} - Debería retornar productos filtrados por categoría y 200 OK")
    void getProductsInCategoryShouldReturnFilteredList() throws Exception {
        // Arrange
        Long categoryId = 1L;
        when(productService.findByCategory(categoryId)).thenReturn(List.of(productDTOSample));

        // Act & Assert
        mockMvc.perform(get("/api/products/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].categoryName").value("Lácteos"));

        verify(productService, times(1)).findByCategory(categoryId);
    }

    @Test
    @DisplayName("PUT /api/products - Debería actualizar un producto y retornar el resultado con 200 OK")
    void updateShouldReturnUpdatedProduct() throws Exception {
        // Arrange
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(productSample);

        // Act & Assert
        mockMvc.perform(put("/api/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productSample)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.name").value("Producto de Prueba"));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - Debería eliminar un producto y retornar 200 OK")
    void deleteShouldReturnStatusOk() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(productService).deleteProductById(id);

        // Act & Assert
        mockMvc.perform(delete("/api/products/{id}", id)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProductById(id);
    }
}