package br.com.susagenda.repository;

import br.com.susagenda.domain.entity.ListaEspera;
import br.com.susagenda.domain.enums.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListaEsperaRepository extends JpaRepository<ListaEspera, Long> {

    List<ListaEspera> findByPacienteId(Long pacienteId);

    List<ListaEspera> findByEspecialidade(Especialidade especialidade);

    List<ListaEspera> findByAtendidoFalse();

    @Query("SELECT l FROM ListaEspera l WHERE l.especialidade = :especialidade AND l.atendido = false ORDER BY l.prioridade DESC, l.dataSolicitacao ASC")
    List<ListaEspera> findFilaOrdenadaPorPrioridade(@Param("especialidade") Especialidade especialidade);

    @Query("SELECT l FROM ListaEspera l WHERE l.unidadeSaudePreferida.id = :unidadeId AND l.atendido = false ORDER BY l.prioridade DESC, l.dataSolicitacao ASC")
    List<ListaEspera> findByUnidadeOrdenadoPorPrioridade(@Param("unidadeId") Long unidadeId);

    @Query("SELECT COUNT(l) FROM ListaEspera l WHERE l.especialidade = :especialidade AND l.atendido = false")
    long countPacientesNaFila(@Param("especialidade") Especialidade especialidade);

    @Query("SELECT l FROM ListaEspera l WHERE l.paciente.id = :pacienteId AND l.especialidade = :especialidade AND l.atendido = false")
    List<ListaEspera> findPacienteNaFilaEspecialidade(
            @Param("pacienteId") Long pacienteId,
            @Param("especialidade") Especialidade especialidade
    );
}
