package br.com.susagenda.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "unidades_saude")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnidadeSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false, unique = true, length = 20)
    private String cnes; // Cadastro Nacional de Estabelecimentos de Saúde

    @Column(nullable = false, length = 300)
    private String endereco;

    @Column(length = 100)
    private String bairro;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(length = 10)
    private String cep;

    @Column(length = 20)
    private String telefone;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "unidadeSaude", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Profissional> profissionais = new ArrayList<>();

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
