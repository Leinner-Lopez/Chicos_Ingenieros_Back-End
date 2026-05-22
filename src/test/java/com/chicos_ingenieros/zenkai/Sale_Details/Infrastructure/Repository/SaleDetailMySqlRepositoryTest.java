package com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Sale_Details.Domain.SaleDetail;
import com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Entity.SaleDetailEntity;
import com.chicos_ingenieros.zenkai.Sale_Details.Infrastructure.Mapper.SaleDetailMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleDetailMySqlRepositoryTest {

    @Mock
    private SpringSaleDetailRepository springRepository;

    @Mock
    private SaleDetailMapper saleDetailMapper;

    @InjectMocks
    private SaleDetailMySqlRepository saleDetailMySqlRepository;

    @Test
    void save_ShouldReturnSavedSaleDetail_WhenSuccessful() {
        SaleDetail domainDetail = new SaleDetail();
        SaleDetailEntity initialEntity = new SaleDetailEntity();
        SaleDetailEntity savedEntity = new SaleDetailEntity();
        SaleDetail expectedDomain = new SaleDetail();

        when(saleDetailMapper.toEntity(domainDetail)).thenReturn(initialEntity);
        when(springRepository.save(initialEntity)).thenReturn(savedEntity);
        when(saleDetailMapper.toDomain(savedEntity)).thenReturn(expectedDomain);

        SaleDetail actualDomain = saleDetailMySqlRepository.save(domainDetail);

        assertNotNull(actualDomain);
        assertEquals(expectedDomain, actualDomain);
        verify(springRepository, times(1)).save(initialEntity);
    }

    @Test
    void findBySaleId_ShouldReturnListOfSaleDetails_WhenSaleIdExists() {
        Long saleId = 100L;
        List<SaleDetailEntity> entityList = List.of(new SaleDetailEntity(), new SaleDetailEntity());
        List<SaleDetail> expectedDomainList = List.of(new SaleDetail(), new SaleDetail());

        when(springRepository.findBySale_SaleId(saleId)).thenReturn(entityList);
        when(saleDetailMapper.toDomainList(entityList)).thenReturn(expectedDomainList);

        List<SaleDetail> actualDomainList = saleDetailMySqlRepository.findBySaleId(saleId);

        assertNotNull(actualDomainList);
        assertEquals(2, actualDomainList.size());
        assertEquals(expectedDomainList, actualDomainList);
        verify(springRepository, times(1)).findBySale_SaleId(saleId);
    }

    @Test
    void deleteBySaleId_ShouldInvokeRepositoryDelete() {
        Long saleId = 100L;
        doNothing().when(springRepository).deleteBySale_SaleId(saleId);

        saleDetailMySqlRepository.deleteBySaleId(saleId);

        verify(springRepository, times(1)).deleteBySale_SaleId(saleId);
    }
}