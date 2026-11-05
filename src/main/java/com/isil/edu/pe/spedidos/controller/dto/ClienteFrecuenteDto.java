package com.isil.edu.pe.spedidos.controller.dto;

import com.isil.edu.pe.spedidos.service.domain.ClienteFrecuente;
import java.math.BigDecimal;

public record ClienteFrecuenteDto(
        String cliente,
        long totalPedidos,
        long totalUnidades,
        BigDecimal importeAcumulado,
        BigDecimal ticketPromedio
) {
    public static ClienteFrecuenteDto from(ClienteFrecuente domain) {
        return new ClienteFrecuenteDto(
                domain.cliente(),
                domain.totalPedidos(),
                domain.totalUnidades(),
                domain.importeAcumulado(),
                domain.ticketPromedio()
        );
    }
}
