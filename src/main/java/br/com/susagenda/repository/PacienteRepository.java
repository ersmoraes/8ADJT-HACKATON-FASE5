package br.com.susagenda.repository;

import br.com.susagenda.domain.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByCpf(String cpf);

    Optional<Paciente> findByCartaoSus(String cartaoSus);

    boolean existsByCpf(String cpf);

    boolean existsByCartaoSus(String cartaoSus);

    List<Paciente> findByAtivoTrue();

    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Paciente> findByNomeContaining(@Param("nome") String nome);

    @Query("SELECT p FROM Paciente p WHERE p.cidade = :cidade AND p.ativo = true")
    List<Paciente> findByCidadeAndAtivo(@Param("cidade") String cidade);
}
