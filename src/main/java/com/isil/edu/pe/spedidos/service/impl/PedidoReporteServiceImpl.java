package com.isil.edu.pe.spedidos.service.impl;

import com.isil.edu.pe.spedidos.entity.Pedido;
import com.isil.edu.pe.spedidos.exception.PedidoException;
import com.isil.edu.pe.spedidos.repository.PedidoReporteJdbcRepository;
import com.isil.edu.pe.spedidos.repository.projection.ResumenPedidosProjection;
import com.isil.edu.pe.spedidos.service.PedidoReporteService;
import com.isil.edu.pe.spedidos.service.domain.ClienteFrecuente;
import com.isil.edu.pe.spedidos.service.domain.ProductoVendido;
import com.isil.edu.pe.spedidos.service.domain.ResumenPedidos;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PedidoReporteServiceImpl implements PedidoReporteService {

    private final PedidoReporteJdbcRepository reporteRepository;

    public PedidoReporteServiceImpl(PedidoReporteJdbcRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    @Override
    public ResumenPedidos obtenerResumen() {
        ResumenPedidosProjection p = reporteRepository.obtenerResumen();
        return new ResumenPedidos(
                p.totalPedidos(),
                p.totalUnidades(),
                p.importeAcumulado(),
                p.ticketPromedio()
        );
    }

    @Override
    public List<Pedido> buscarPorCliente(String cliente) {
        if (cliente == null || cliente.isBlank()) {
            throw new PedidoException("El parámetro cliente no puede estar vacío.");
        }
        return reporteRepository.buscarPorCliente(cliente.trim());
    }

    @Override
    public List<ProductoVendido> listarProductosMasVendidos() {
        return reporteRepository.listarProductosMasVendidos()
                .stream()
                .map(p -> new ProductoVendido(
                        p.productoCodigo(),
                        p.productoNombre(),
                        p.totalPedidos(),
                        p.totalUnidades(),
                        p.importeAcumulado()
                ))
                .toList();
    }

    @Override
    public List<Pedido> buscarPedidosPorClienteYTotalMinimo(String cliente, BigDecimal totalMinimo) {
        if (cliente == null || cliente.isBlank()) {
            throw new PedidoException("El nombre del cliente no puede estar vacío.");
        }
        if (totalMinimo == null || totalMinimo.compareTo(BigDecimal.ZERO) < 0) {
            throw new PedidoException("El total mínimo debe ser mayor o igual a 0.");
        }
        return reporteRepository.buscarPedidosPorClienteYTotalMinimo(cliente.trim(), totalMinimo);
    }

    @Override
    public List<ClienteFrecuente> listarClientesFrecuentes(int minPedidos) {
        if (minPedidos < 1) {
            throw new PedidoException("El número mínimo de pedidos debe ser mayor o igual a 1.");
        }
        return reporteRepository.listarClientesFrecuentes(minPedidos)
                .stream()
                .map(ClienteFrecuente::from)
                .toList();
    }
}
