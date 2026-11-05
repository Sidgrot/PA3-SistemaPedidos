package com.isil.edu.pe.spedidos.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.isil.edu.pe.spedidos.exception.PedidoException;
import com.isil.edu.pe.spedidos.service.PedidoReporteService;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
class PedidoReportePa3SecurityAndValidationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private PedidoReporteService pedidoReporteService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("PA3 Test 3: Validación en Service rechaza totalMinimo negativo y minPedidos menor a 1")
    void validarParametros_conValoresInvalidos_lanzaPedidoException() {
        BigDecimal montoNegativo = new BigDecimal("-10.00");
        int minPedidosInvalido = 0;

        assertThrows(PedidoException.class, () ->
                pedidoReporteService.buscarPedidosPorClienteYTotalMinimo("Ana", montoNegativo)
        );
        assertThrows(PedidoException.class, () ->
                pedidoReporteService.listarClientesFrecuentes(minPedidosInvalido)
        );
    }

    @Test
    @WithMockUser(username = "consulta", roles = {"CONSULTA"})
    @DisplayName("PA3 Test 4: Rol CONSULTA puede acceder al reporte protegido y obtiene respuesta exitosa")
    void mostrarReportes_conRolConsulta_retornaStatus200() throws Exception {
        mockMvc.perform(get("/api/v1/pedidos/estadisticas/resumen"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "consulta", roles = {"CONSULTA"})
    @DisplayName("PA3 Test 5: Rol CONSULTA con CSRF válido no puede registrar pedido por POST (403 Forbidden)")
    void registrarPedido_conRolConsultaYCsrfValido_retornaStatus403Forbidden() throws Exception {
        mockMvc.perform(post("/pedidos")
                        .with(csrf())
                        .param("cliente", "Ana")
                        .param("productoCodigo", "P001")
                        .param("cantidad", "1"))
                .andExpect(status().isForbidden());
    }
}
