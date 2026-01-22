package br.com.susagenda.domain.entity;

import br.com.susagenda.domain.enums.Prioridade;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "cartao_sus", nullable = false, unique = true, length = 15)
    private String cartaoSus; // Cartão Nacional de Saúde (CNS)

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(length = 20)
    private String telefone;

    @Column(length = 100)
    private String email;

    @Column(length = 300)
    private String endereco;

    @Column(length = 100)
    private String bairro;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Column(length = 10)
    private String cep;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Prioridade prioridade = Prioridade.NORMAL;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
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
        calcularPrioridadeAutomatica();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public int getIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    private void calcularPrioridadeAutomatica() {
        if (prioridade == Prioridade.NORMAL) {
            int idade = getIdade();
            if (idade >= 60) {
                this.prioridade = Prioridade.IDOSO;
            } else if (idade <= 12) {
                this.prioridade = Prioridade.CRIANCA;
            }
        }
    }
}
