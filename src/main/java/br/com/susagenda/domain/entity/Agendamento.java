package br.com.susagenda.domain.entity;

import br.com.susagenda.domain.enums.StatusAgendamento;
import br.com.susagenda.domain.enums.TipoAtendimento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "agendamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_saude_id", nullable = false)
    private UnidadeSaude unidadeSaude;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDate dataAgendamento;

    @Column(name = "hora_agendamento", nullable = false)
    private LocalTime horaAgendamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atendimento", nullable = false)
    private TipoAtendimento tipoAtendimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusAgendamento status = StatusAgendamento.AGENDADO;

    @Column(length = 500)
    private String observacoes;

    @Column(name = "motivo_cancelamento", length = 300)
    private String motivoCancelamento;

    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;

    @Column(name = "data_chegada")
    private LocalDateTime dataChegada;

    @Column(name = "data_inicio_atendimento")
    private LocalDateTime dataInicioAtendimento;

    @Column(name = "data_fim_atendimento")
    private LocalDateTime dataFimAtendimento;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void confirmar() {
        this.status = StatusAgendamento.CONFIRMADO;
        this.dataConfirmacao = LocalDateTime.now();
    }

    public void registrarChegada() {
        this.dataChegada = LocalDateTime.now();
    }

    public void iniciarAtendimento() {
        this.status = StatusAgendamento.EM_ATENDIMENTO;
        this.dataInicioAtendimento = LocalDateTime.now();
    }

    public void concluirAtendimento() {
        this.status = StatusAgendamento.CONCLUIDO;
        this.dataFimAtendimento = LocalDateTime.now();
    }

    public void cancelar(String motivo) {
        this.status = StatusAgendamento.CANCELADO;
        this.motivoCancelamento = motivo;
    }

    public void marcarNaoCompareceu() {
        this.status = StatusAgendamento.NAO_COMPARECEU;
    }
}
