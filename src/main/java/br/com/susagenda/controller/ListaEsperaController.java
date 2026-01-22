package br.com.susagenda.controller;

import br.com.susagenda.domain.enums.Especialidade;
import br.com.susagenda.dto.request.ListaEsperaRequest;
import br.com.susagenda.dto.response.ListaEsperaResponse;
import br.com.susagenda.service.ListaEsperaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lista-espera")
@RequiredArgsConstructor
@Tag(name = "Lista de Espera", description = "Gerenciamento da fila de espera para consultas")
public class ListaEsperaController {

    private final ListaEsperaService listaEsperaService;

    @PostMapping
    @Operation(summary = "Adicionar à lista de espera", description = "Adiciona um paciente à lista de espera para uma especialidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente adicionado à lista de espera"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
            @ApiResponse(responseCode = "422", description = "Paciente já está na fila para esta especialidade")
    })
    public ResponseEntity<ListaEsperaResponse> adicionarNaFila(@Valid @RequestBody ListaEsperaRequest request) {
        ListaEsperaResponse response = listaEsperaService.adicionarNaFila(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna os dados de uma entrada na lista de espera pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrada encontrada"),
            @ApiResponse(responseCode = "404", description = "Entrada não encontrada")
    })
    public ResponseEntity<ListaEsperaResponse> buscarPorId(
            @Parameter(description = "ID da entrada na lista de espera") @PathVariable Long id) {
        ListaEsperaResponse response = listaEsperaService.buscarPorIdResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/especialidade/{especialidade}")
    @Operation(summary = "Buscar fila por especialidade", description = "Retorna a fila de espera ordenada por prioridade para uma especialidade")
    @ApiResponse(responseCode = "200", description = "Lista de espera retornada com sucesso")
    public ResponseEntity<List<ListaEsperaResponse>> buscarFilaPorEspecialidade(
            @Parameter(description = "Especialidade médica") @PathVariable Especialidade especialidade) {
        List<ListaEsperaResponse> fila = listaEsperaService.buscarFilaPorEspecialidade(especialidade);
        return ResponseEntity.ok(fila);
    }

    @GetMapping("/unidade/{unidadeId}")
    @Operation(summary = "Buscar fila por unidade de saúde", description = "Retorna a fila de espera de uma unidade de saúde")
    @ApiResponse(responseCode = "200", description = "Lista de espera retornada com sucesso")
    public ResponseEntity<List<ListaEsperaResponse>> buscarFilaPorUnidade(
            @Parameter(description = "ID da unidade de saúde") @PathVariable Long unidadeId) {
        List<ListaEsperaResponse> fila = listaEsperaService.buscarFilaPorUnidade(unidadeId);
        return ResponseEntity.ok(fila);
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Buscar lista de espera por paciente", description = "Retorna todas as entradas na lista de espera de um paciente")
    @ApiResponse(responseCode = "200", description = "Lista de espera do paciente retornada com sucesso")
    public ResponseEntity<List<ListaEsperaResponse>> buscarPorPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long pacienteId) {
        List<ListaEsperaResponse> entradas = listaEsperaService.buscarPorPaciente(pacienteId);
        return ResponseEntity.ok(entradas);
    }

    @GetMapping("/contagem/{especialidade}")
    @Operation(summary = "Contar pacientes na fila", description = "Retorna a quantidade de pacientes na fila de espera para uma especialidade")
    @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    public ResponseEntity<Long> contarPacientesNaFila(
            @Parameter(description = "Especialidade médica") @PathVariable Especialidade especialidade) {
        long total = listaEsperaService.contarPacientesNaFila(especialidade);
        return ResponseEntity.ok(total);
    }

    @PatchMapping("/{id}/atendido")
    @Operation(summary = "Marcar como atendido", description = "Marca um paciente da lista de espera como atendido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marcado como atendido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Entrada não encontrada"),
            @ApiResponse(responseCode = "422", description = "Paciente já foi atendido")
    })
    public ResponseEntity<ListaEsperaResponse> marcarComoAtendido(
            @Parameter(description = "ID da entrada na lista de espera") @PathVariable Long id) {
        ListaEsperaResponse response = listaEsperaService.marcarComoAtendido(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover da lista de espera", description = "Remove um paciente da lista de espera")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Removido da lista de espera com sucesso"),
            @ApiResponse(responseCode = "404", description = "Entrada não encontrada")
    })
    public ResponseEntity<Void> removerDaFila(
            @Parameter(description = "ID da entrada na lista de espera") @PathVariable Long id) {
        listaEsperaService.removerDaFila(id);
        return ResponseEntity.noContent().build();
    }
}
