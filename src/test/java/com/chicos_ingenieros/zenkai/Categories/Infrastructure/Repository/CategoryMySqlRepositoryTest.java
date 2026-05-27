package com.chicos_ingenieros.zenkai.Categories.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Categories.Domain.Category;
import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Entity.CategoryEntity;
import com.chicos_ingenieros.zenkai.Categories.Infrastructure.Mapper.CategoryMapper;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryMySqlRepositoryTest {

    @Mock
    private SpringCategoryRepository springRepository;

    @Mock
    private CategoryMapper mapper;

    @InjectMocks
    private CategoryMySqlRepository categoryMySqlRepository;

    private Category domainCategory;
    private CategoryEntity categoryEntity;
    private final Long categoryId = 1L;

    @BeforeEach
    void setUp() {
        domainCategory = new Category();
        categoryEntity = new CategoryEntity();
    }

    @Test
    void save_ShouldReturnSavedDomainCategory_WhenSuccessful() {
        when(mapper.categoryToCategoryEntity(domainCategory)).thenReturn(categoryEntity);
        when(springRepository.save(categoryEntity)).thenReturn(categoryEntity);
        when(mapper.categoryEntityToCategory(categoryEntity)).thenReturn(domainCategory);
        Category result = categoryMySqlRepository.save(domainCategory);
        assertNotNull(result);
        assertEquals(domainCategory, result);
        verify(springRepository, times(1)).save(categoryEntity);
        verify(mapper, times(1)).categoryToCategoryEntity(domainCategory);
        verify(mapper, times(1)).categoryEntityToCategory(categoryEntity);
    }

    @Test
    void findById_ShouldReturnDomainCategory_WhenCategoryExists() {
        when(springRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(mapper.categoryEntityToCategory(categoryEntity)).thenReturn(domainCategory);
        Category result = categoryMySqlRepository.findById(categoryId);
        assertNotNull(result);
        assertEquals(domainCategory, result);
        verify(springRepository, times(1)).findById(categoryId);
        verify(mapper, times(1)).categoryEntityToCategory(categoryEntity);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenCategoryDoesNotExist() {
        when(springRepository.findById(categoryId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryMySqlRepository.findById(categoryId)
        );
        assertEquals("Category with id " + categoryId + " not found", exception.getMessage());
        verify(springRepository, times(1)).findById(categoryId);
        verifyNoInteractions(mapper); // El mapper nunca debe llamarse si no hay entidad
    }


    @Test
    void findAll_ShouldReturnListOfDomainCategories() {
        List<CategoryEntity> entities = List.of(categoryEntity, new CategoryEntity());
        when(springRepository.findAll()).thenReturn(entities);
        when(mapper.categoryEntityToCategory(any(CategoryEntity.class))).thenReturn(domainCategory);
        List<Category> result = categoryMySqlRepository.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(springRepository, times(1)).findAll();
        verify(mapper, times(2)).categoryEntityToCategory(any(CategoryEntity.class));
    }


    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        categoryMySqlRepository.deleteById(categoryId);
        verify(springRepository, times(1)).deleteById(categoryId);
    }


    @Test
    void countCategories_ShouldReturnTotalCount() {
        long expectedCount = 5L;
        when(springRepository.count()).thenReturn(expectedCount);
        Long result = categoryMySqlRepository.countCategories();
        assertEquals(expectedCount, result);
        verify(springRepository, times(1)).count();
    }

}