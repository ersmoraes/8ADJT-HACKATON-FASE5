package br.com.susagenda.domain.entity;

import br.com.susagenda.domain.enums.Especialidade;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profissionais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "registro_profissional", nullable = false, unique = true, length = 20)
    private String registroProfissional; // CRM, CRO, COREN, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especialidade especialidade;

    @Column(length = 20)
    private String telefone;

    @Column(length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_saude_id", nullable = false)
    private UnidadeSaude unidadeSaude;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL)
    @Builder.Default
    private List<HorarioDisponivel> horariosDisponiveis = new ArrayList<>();

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Agendamento> agendamentos = new ArrayList<>();

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
}
