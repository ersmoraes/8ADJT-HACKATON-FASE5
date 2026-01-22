package br.com.susagenda.domain.entity;

import br.com.susagenda.domain.enums.Especialidade;
import br.com.susagenda.domain.enums.Prioridade;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lista_espera")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListaEspera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especialidade especialidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_saude_id")
    private UnidadeSaude unidadeSaudePreferida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao;

    @Column(name = "data_agendamento")
    private LocalDateTime dataAgendamento;

    @Column(nullable = false)
    @Builder.Default
    private Boolean atendido = false;

    @Column(length = 500)
    private String observacoes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (dataSolicitacao == null) {
            dataSolicitacao = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void marcarComoAtendido() {
        this.atendido = true;
        this.dataAgendamento = LocalDateTime.now();
    }
}
