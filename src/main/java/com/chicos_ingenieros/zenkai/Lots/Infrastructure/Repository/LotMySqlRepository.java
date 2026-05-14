package com.chicos_ingenieros.zenkai.Lots.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotRepository;
import com.chicos_ingenieros.zenkai.Lots.Domain.LotStatus;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.Entity.LotEntity;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.Mapper.LotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class LotMySqlRepository implements LotRepository {

    private final SpringLotRepository repository;
    private final LotMapper mapper;

    @Override
    public Lot save(Lot lot) {
        LotEntity lotSaved = repository.save(mapper.lotToLotEntity(lot));
        return mapper.lotEntityToLot(lotSaved);
    }

    @Override
    public Lot findById(Long id) {
        return repository.findById(id).map(mapper::lotEntityToLot).orElseThrow(() ->
                new ResourceNotFoundException("Lot with id " + id + " not found")
        );
    }

    @Override
    public List<Lot> findAll() {

        return repository.findAll().stream().map(mapper::lotEntityToLot).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Long countLots() {
        return repository.count();
    }

    @Override
    public List<Lot> findAvailableLotsByProductOrderedByExpiration(Long productId) {
        return repository
                .findAvailableLotsOrderedByExpiration(
                        productId)
                .stream()
                .map(mapper::lotEntityToLot)
                .toList();
    }
}
