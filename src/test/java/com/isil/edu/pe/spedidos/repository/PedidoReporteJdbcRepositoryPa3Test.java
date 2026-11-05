package com.isil.edu.pe.spedidos.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.isil.edu.pe.spedidos.entity.Pedido;
import com.isil.edu.pe.spedidos.repository.projection.ClienteFrecuenteProjection;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PedidoReporteJdbcRepositoryPa3Test {

    @Autowired
    private PedidoReporteJdbcRepository jdbcRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @BeforeEach
    void setUp() {
        pedidoRepository.deleteAll();
    }

    @Test
    @DisplayName("PA3 Test 1: buscarPedidosPorClienteYTotalMinimo retorna solo pedidos que cumplen los filtros")
    void buscarPedidosPorClienteYTotalMinimo_conDatosDePrueba_retornaPedidosFiltrados() {
        // Arrange
        pedidoRepository.save(new Pedido(null, "Ana Torres", "P001", "Teclado", new BigDecimal("50.00"), 1, new BigDecimal("50.00"), LocalDateTime.now()));
        Pedido esperado1 = pedidoRepository.save(new Pedido(null, "Ana Torres", "P002", "Monitor", new BigDecimal("600.00"), 1, new BigDecimal("600.00"), LocalDateTime.now()));
        Pedido esperado2 = pedidoRepository.save(new Pedido(null, "Ana Gomez", "P003", "Laptop", new BigDecimal("2500.00"), 1, new BigDecimal("2500.00"), LocalDateTime.now()));

        // Act
        List<Pedido> resultado = jdbcRepository.buscarPedidosPorClienteYTotalMinimo("Ana", new BigDecimal("500.00"));

        // Assert
        assertEquals(2, resultado.size(), "Solo 2 pedidos de 'Ana' superan los S/ 500.00");
        assertEquals(esperado2.getId(), resultado.get(0).getId(), "Orden DESC por ID");
        assertEquals(esperado1.getId(), resultado.get(1).getId());
    }

    @Test
    @DisplayName("PA3 Test 2: listarClientesFrecuentes aplica GROUP BY y HAVING correctamente")
    void listarClientesFrecuentes_conMinPedidosDos_retornaValoresAgregados() {
        // Arrange
        pedidoRepository.save(new Pedido(null, "Luis Perez", "P001", "Mouse", new BigDecimal("40.00"), 2, new BigDecimal("80.00"), LocalDateTime.now()));
        pedidoRepository.save(new Pedido(null, "Luis Perez", "P002", "Teclado", new BigDecimal("120.00"), 1, new BigDecimal("120.00"), LocalDateTime.now()));
        pedidoRepository.save(new Pedido(null, "Maria Lopez", "P003", "Monitor", new BigDecimal("500.00"), 1, new BigDecimal("500.00"), LocalDateTime.now()));

        // Act
        List<ClienteFrecuenteProjection> frecuentes = jdbcRepository.listarClientesFrecuentes(2);

        // Assert
        assertEquals(1, frecuentes.size(), "Solo Luis Perez tiene >= 2 pedidos");
        ClienteFrecuenteProjection luis = frecuentes.get(0);
        assertEquals("Luis Perez", luis.cliente());
        assertEquals(2L, luis.totalPedidos());
        assertEquals(3L, luis.totalUnidades());
        assertEquals(0, new BigDecimal("200.00").compareTo(luis.importeAcumulado()));
    }
}
