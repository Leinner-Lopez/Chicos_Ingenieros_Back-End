package com.chicos_ingenieros.zenkai.Lots.Infrastructure;

import com.chicos_ingenieros.zenkai.Lots.Application.LotService;
import com.chicos_ingenieros.zenkai.Lots.Domain.Lot;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lots")
public class LotController {
    private final LotService service;

    @PostMapping
    public ResponseEntity<Lot> save(@RequestBody Lot Lot) {
        Lot saved = service.saveLot(Lot);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Lot>> getAll() {
        List<Lot> lotList = service.findAllLots();
        return new ResponseEntity<>(lotList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lot> findById(@PathVariable Long id){
        Lot Lot = service.findLotById(id);
        return new ResponseEntity<>(Lot, HttpStatus.OK);
    }

    @GetMapping("/count")
    public Long getCountCategories() {
        return service.countLots();
    }

    @PutMapping
    public ResponseEntity<Lot> update(@RequestBody Lot Lot) {
        Lot LotDB = service.updateLot(Lot.getIdLot(),Lot);
        return new ResponseEntity<>(LotDB, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteLotById(id);
    }
}
