package com.chicos_ingenieros.zenkai.Categories.Infrastructure;

import com.chicos_ingenieros.zenkai.Categories.Application.CategoryService;
import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
import com.chicos_ingenieros.zenkai.Config.Application.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper; // Ayuda a convertir objetos a JSON

    private Category categorySample;

    @BeforeEach
    void setUp() {
        // Inicializamos un objeto de prueba común (Asumiendo constructor o setters en tu entidad Category)
        categorySample = new Category();
        categorySample.setCategoryId(1L);
        categorySample.setName("Electrónica"); // Ajusta según las propiedades reales de tu clase Category
    }

    @Test
    void save_ShouldReturnCreatedCategory() throws Exception {
        // Given
        when(categoryService.saveCategory(any(Category.class))).thenReturn(categorySample);

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categorySample)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.name").value("Electrónica"));

        verify(categoryService, times(1)).saveCategory(any(Category.class));
    }

    @Test
    void getAll_ShouldReturnListOfCategories() throws Exception {
        // Given
        List<Category> list = Arrays.asList(categorySample);
        when(categoryService.findAllCategories()).thenReturn(list);

        // When & Then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].categoryId").value(1L));

        verify(categoryService, times(1)).findAllCategories();
    }

    @Test
    void findById_ShouldReturnCategory() throws Exception {
        // Given
        when(categoryService.findCategoryById(1L)).thenReturn(categorySample);

        // When & Then
        mockMvc.perform(get("/api/categories/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1L));

        verify(categoryService, times(1)).findCategoryById(1L);
    }

    @Test
    void getCountCategories_ShouldReturnTotalCount() throws Exception {
        // Given
        when(categoryService.countCategories()).thenReturn(5L);

        // When & Then
        mockMvc.perform(get("/api/categories/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(categoryService, times(1)).countCategories();
    }

    @Test
    void update_ShouldReturnUpdatedCategory() throws Exception {
        // Given
        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(categorySample);

        // When & Then
        mockMvc.perform(put("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categorySample)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1L));

        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void delete_ShouldReturnStatusOk() throws Exception {
        // Given
        doNothing().when(categoryService).deleteCategoryById(1L);

        // When & Then
        mockMvc.perform(delete("/api/categories/{id}", 1L))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteCategoryById(1L);
    }
}