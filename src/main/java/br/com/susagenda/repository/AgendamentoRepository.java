package br.com.susagenda.repository;

import br.com.susagenda.domain.entity.Agendamento;
import br.com.susagenda.domain.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByPacienteId(Long pacienteId);

    List<Agendamento> findByProfissionalId(Long profissionalId);

    List<Agendamento> findByUnidadeSaudeId(Long unidadeId);

    List<Agendamento> findByDataAgendamento(LocalDate data);

    List<Agendamento> findByStatus(StatusAgendamento status);

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId AND a.status IN :status")
    List<Agendamento> findByPacienteIdAndStatusIn(
            @Param("pacienteId") Long pacienteId,
            @Param("status") List<StatusAgendamento> status
    );

    @Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.dataAgendamento = :data ORDER BY a.horaAgendamento")
    List<Agendamento> findByProfissionalIdAndData(
            @Param("profissionalId") Long profissionalId,
            @Param("data") LocalDate data
    );

    @Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.dataAgendamento = :data AND a.horaAgendamento = :hora AND a.status NOT IN ('CANCELADO', 'NAO_COMPARECEU')")
    List<Agendamento> findAgendamentosNoHorario(
            @Param("profissionalId") Long profissionalId,
            @Param("data") LocalDate data,
            @Param("hora") LocalTime hora
    );

    @Query("SELECT a FROM Agendamento a WHERE a.dataAgendamento = :data AND a.unidadeSaude.id = :unidadeId ORDER BY a.horaAgendamento")
    List<Agendamento> findByDataAndUnidade(
            @Param("data") LocalDate data,
            @Param("unidadeId") Long unidadeId
    );

    @Query("SELECT a FROM Agendamento a WHERE a.dataAgendamento BETWEEN :dataInicio AND :dataFim AND a.status = :status")
    List<Agendamento> findByPeriodoAndStatus(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("status") StatusAgendamento status
    );

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.dataAgendamento = :data AND a.horaAgendamento = :hora AND a.status NOT IN ('CANCELADO', 'NAO_COMPARECEU')")
    int countAgendamentosNoHorario(
            @Param("profissionalId") Long profissionalId,
            @Param("data") LocalDate data,
            @Param("hora") LocalTime hora
    );

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId ORDER BY a.dataAgendamento DESC, a.horaAgendamento DESC")
    List<Agendamento> findHistoricoPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT a FROM Agendamento a WHERE a.dataAgendamento = :data AND a.status = 'AGENDADO' ORDER BY a.horaAgendamento")
    List<Agendamento> findAgendamentosParaConfirmacao(@Param("data") LocalDate data);
}
