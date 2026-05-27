package com.chicos_ingenieros.zenkai.Lots.Infrastructure.Scheduler;

import com.chicos_ingenieros.zenkai.Lots.Application.UseCases.LotStatusUpdateUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LotStatusSchedulerTest {

    @Mock
    private LotStatusUpdateUseCase lotStatusUpdateUseCase;

    @InjectMocks
    private LotStatusScheduler scheduler;

    @Test
    void updateLotStatuses_delegatesToUseCase() {
        scheduler.updateLotStatuses();

        verify(lotStatusUpdateUseCase, times(1)).updateLotStatuses();
    }
}
