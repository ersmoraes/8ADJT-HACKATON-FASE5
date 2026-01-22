package br.com.susagenda.controller;

import br.com.susagenda.domain.enums.Especialidade;
import br.com.susagenda.dto.request.AgendamentoRequest;
import br.com.susagenda.dto.request.CancelarAgendamentoRequest;
import br.com.susagenda.dto.response.AgendamentoResponse;
import br.com.susagenda.dto.response.VagaDisponivelResponse;
import br.com.susagenda.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/agendamentos")
@RequiredArgsConstructor
@Tag(name = "Agendamentos", description = "Gerenciamento de agendamentos de consultas e exames")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    @Operation(summary = "Criar agendamento", description = "Realiza um novo agendamento de consulta ou exame")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Paciente ou profissional não encontrado"),
            @ApiResponse(responseCode = "409", description = "Horário não disponível")
    })
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody AgendamentoRequest request) {
        AgendamentoResponse response = agendamentoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID", description = "Retorna os dados de um agendamento pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    public ResponseEntity<AgendamentoResponse> buscarPorId(
            @Parameter(description = "ID do agendamento") @PathVariable Long id) {
        AgendamentoResponse response = agendamentoService.buscarPorIdResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Buscar agendamentos por paciente", description = "Retorna todos os agendamentos de um paciente")
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso")
    public ResponseEntity<List<AgendamentoResponse>> buscarPorPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long pacienteId) {
        List<AgendamentoResponse> agendamentos = agendamentoService.buscarPorPaciente(pacienteId);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/paciente/{pacienteId}/historico")
    @Operation(summary = "Buscar histórico do paciente", description = "Retorna o histórico completo de agendamentos de um paciente")
    @ApiResponse(responseCode = "200", description = "Histórico de agendamentos retornado com sucesso")
    public ResponseEntity<List<AgendamentoResponse>> buscarHistoricoPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long pacienteId) {
        List<AgendamentoResponse> historico = agendamentoService.buscarHistoricoPaciente(pacienteId);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/profissional/{profissionalId}")
    @Operation(summary = "Buscar agendamentos por profissional e data", description = "Retorna os agendamentos de um profissional em uma data específica")
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso")
    public ResponseEntity<List<AgendamentoResponse>> buscarPorProfissionalEData(
            @Parameter(description = "ID do profissional") @PathVariable Long profissionalId,
            @Parameter(description = "Data do agendamento (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<AgendamentoResponse> agendamentos = agendamentoService.buscarPorProfissionalEData(profissionalId, data);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/unidade/{unidadeId}")
    @Operation(summary = "Buscar agendamentos por unidade e data", description = "Retorna os agendamentos de uma unidade de saúde em uma data específica")
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso")
    public ResponseEntity<List<AgendamentoResponse>> buscarPorUnidadeEData(
            @Parameter(description = "ID da unidade de saúde") @PathVariable Long unidadeId,
            @Parameter(description = "Data do agendamento (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<AgendamentoResponse> agendamentos = agendamentoService.buscarPorUnidadeEData(unidadeId, data);
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/vagas-disponiveis")
    @Operation(summary = "Buscar vagas disponíveis", description = "Retorna as vagas disponíveis para agendamento por especialidade e período")
    @ApiResponse(responseCode = "200", description = "Lista de vagas disponíveis retornada com sucesso")
    public ResponseEntity<List<VagaDisponivelResponse>> buscarVagasDisponiveis(
            @Parameter(description = "Especialidade médica") @RequestParam Especialidade especialidade,
            @Parameter(description = "Data inicial (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final (YYYY-MM-DD)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<VagaDisponivelResponse> vagas = agendamentoService.buscarVagasDisponiveis(especialidade, dataInicio, dataFim);
        return ResponseEntity.ok(vagas);
    }

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar agendamento", description = "Confirma um agendamento (paciente confirmou presença)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento confirmado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
            @ApiResponse(responseCode = "422", description = "Agendamento não pode ser confirmado neste status")
    })
    public ResponseEntity<AgendamentoResponse> confirmar(
            @Parameter(description = "ID do agendamento") @PathVariable Long id) {
        AgendamentoResponse response = agendamentoService.confirmar(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/registrar-chegada")
    @Operation(summary = "Registrar chegada", description = "Registra a chegada do paciente na unidade de saúde")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chegada registrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
            @ApiResponse(responseCode = "422", description = "Não é possível registrar chegada neste status")
    })
    public ResponseEntity<AgendamentoResponse> registrarChegada(
            @Parameter(description = "ID do agendamento") @PathVariable Long id) {
        AgendamentoResponse response = agendamentoService.registrarChegada(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/iniciar-atendimento")
    @Operation(summary = "Iniciar atendimento", description = "Marca o início do atendimento pelo profissional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atendimento iniciado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
            @ApiResponse(responseCode = "422", description = "É necessário registrar a chegada primeiro")
    })
    public ResponseEntity<AgendamentoResponse> iniciarAtendimento(
            @Parameter(description = "ID do agendamento") @PathVariable Long id) {
        AgendamentoResponse response = agendamentoService.iniciarAtendimento(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Concluir atendimento", description = "Marca o atendimento como concluído")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atendimento concluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
            @ApiResponse(responseCode = "422", description = "Apenas atendimentos em andamento podem ser concluídos")
    })
    public ResponseEntity<AgendamentoResponse> concluirAtendimento(
            @Parameter(description = "ID do agendamento") @PathVariable Long id) {
        AgendamentoResponse response = agendamentoService.concluirAtendimento(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar agendamento", description = "Cancela um agendamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
            @ApiResponse(responseCode = "422", description = "Não é possível cancelar agendamentos já concluídos")
    })
    public ResponseEntity<AgendamentoResponse> cancelar(
            @Parameter(description = "ID do agendamento") @PathVariable Long id,
            @Valid @RequestBody CancelarAgendamentoRequest request) {
        AgendamentoResponse response = agendamentoService.cancelar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/nao-compareceu")
    @Operation(summary = "Marcar não comparecimento", description = "Marca que o paciente não compareceu ao agendamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Não comparecimento registrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
            @ApiResponse(responseCode = "422", description = "Apenas agendamentos agendados ou confirmados podem ser marcados")
    })
    public ResponseEntity<AgendamentoResponse> marcarNaoCompareceu(
            @Parameter(description = "ID do agendamento") @PathVariable Long id) {
        AgendamentoResponse response = agendamentoService.marcarNaoCompareceu(id);
        return ResponseEntity.ok(response);
    }
}
