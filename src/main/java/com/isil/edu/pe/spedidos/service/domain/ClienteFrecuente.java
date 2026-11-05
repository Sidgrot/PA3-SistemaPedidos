package com.isil.edu.pe.spedidos.service.domain;

import com.isil.edu.pe.spedidos.repository.projection.ClienteFrecuenteProjection;
import java.math.BigDecimal;

public record ClienteFrecuente(
        String cliente,
        long totalPedidos,
        long totalUnidades,
        BigDecimal importeAcumulado,
        BigDecimal ticketPromedio
) {
    public static ClienteFrecuente from(ClienteFrecuenteProjection projection) {
        return new ClienteFrecuente(
                projection.cliente(),
                projection.totalPedidos(),
                projection.totalUnidades(),
                projection.importeAcumulado(),
                projection.ticketPromedio()
        );
    }
}
