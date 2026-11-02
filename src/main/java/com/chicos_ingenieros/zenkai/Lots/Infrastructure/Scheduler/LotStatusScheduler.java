package com.chicos_ingenieros.zenkai.Lots.Infrastructure.Scheduler;

import com.chicos_ingenieros.zenkai.Lots.Application.UseCases.LotStatusUpdateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LotStatusScheduler {

    private final LotStatusUpdateUseCase lotStatusUpdateUseCase;

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Bogota")
    public void updateLotStatuses() {
        log.info("Iniciando actualización diaria de estados de lotes");
        lotStatusUpdateUseCase.updateLotStatuses();
        log.info("Actualización de estados de lotes completada");
    }
}
