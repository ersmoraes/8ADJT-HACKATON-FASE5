package br.com.susagenda.repository;

import br.com.susagenda.domain.entity.UnidadeSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadeSaudeRepository extends JpaRepository<UnidadeSaude, Long> {

    Optional<UnidadeSaude> findByCnes(String cnes);

    boolean existsByCnes(String cnes);

    List<UnidadeSaude> findByAtivoTrue();

    List<UnidadeSaude> findByCidadeAndAtivoTrue(String cidade);

    @Query("SELECT u FROM UnidadeSaude u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND u.ativo = true")
    List<UnidadeSaude> findByNomeContaining(@Param("nome") String nome);

    @Query("SELECT u FROM UnidadeSaude u WHERE u.bairro = :bairro AND u.cidade = :cidade AND u.ativo = true")
    List<UnidadeSaude> findByBairroAndCidade(@Param("bairro") String bairro, @Param("cidade") String cidade);
}
