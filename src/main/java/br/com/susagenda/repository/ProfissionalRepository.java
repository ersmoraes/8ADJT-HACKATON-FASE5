package br.com.susagenda.repository;

import br.com.susagenda.domain.entity.Profissional;
import br.com.susagenda.domain.enums.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    Optional<Profissional> findByCpf(String cpf);

    Optional<Profissional> findByRegistroProfissional(String registroProfissional);

    boolean existsByCpf(String cpf);

    boolean existsByRegistroProfissional(String registroProfissional);

    List<Profissional> findByAtivoTrue();

    List<Profissional> findByEspecialidade(Especialidade especialidade);

    List<Profissional> findByEspecialidadeAndAtivoTrue(Especialidade especialidade);

    @Query("SELECT p FROM Profissional p WHERE p.unidadeSaude.id = :unidadeId AND p.ativo = true")
    List<Profissional> findByUnidadeSaudeIdAndAtivo(@Param("unidadeId") Long unidadeId);

    @Query("SELECT p FROM Profissional p WHERE p.especialidade = :especialidade AND p.unidadeSaude.id = :unidadeId AND p.ativo = true")
    List<Profissional> findByEspecialidadeAndUnidadeSaude(
            @Param("especialidade") Especialidade especialidade,
            @Param("unidadeId") Long unidadeId
    );

    @Query("SELECT DISTINCT p.especialidade FROM Profissional p WHERE p.ativo = true ORDER BY p.especialidade")
    List<Especialidade> findEspecialidadesDisponiveis();
}
