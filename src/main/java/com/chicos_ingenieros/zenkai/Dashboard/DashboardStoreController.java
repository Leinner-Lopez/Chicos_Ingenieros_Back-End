package com.chicos_ingenieros.zenkai.Dashboard;

import com.chicos_ingenieros.zenkai.Lots.Application.LotService;
import com.chicos_ingenieros.zenkai.Lots.Infrastructure.DTO.LotDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dashboard/store")
public class DashboardStoreController {

    private final LotService lotService;

    @GetMapping("lots_expire")
    public ResponseEntity<List<LotDTO>> findLotsExpireThisWeek() {
        return new ResponseEntity<>(lotService.findLotsExpireThisWeek(), HttpStatus.OK);
    }

    @GetMapping("total_lots")
    public ResponseEntity<Long> countLots() {
        return new ResponseEntity<>(lotService.countLots(), HttpStatus.OK);
    }

    @GetMapping("losses_month")
    public ResponseEntity<BigDecimal> calculateCostOfShrinksOfTheMonth() {
        return new ResponseEntity<>(lotService.calculateCostOfShrinksOfTheMonth(), HttpStatus.OK);
    }
}
