package br.com.susagenda.repository;

import br.com.susagenda.domain.entity.HorarioDisponivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Long> {

    List<HorarioDisponivel> findByProfissionalId(Long profissionalId);

    List<HorarioDisponivel> findByProfissionalIdAndAtivoTrue(Long profissionalId);

    @Query("SELECT h FROM HorarioDisponivel h WHERE h.profissional.id = :profissionalId AND h.diaSemana = :diaSemana AND h.ativo = true")
    List<HorarioDisponivel> findByProfissionalAndDiaSemana(
            @Param("profissionalId") Long profissionalId,
            @Param("diaSemana") DayOfWeek diaSemana
    );

    @Query("SELECT h FROM HorarioDisponivel h WHERE h.profissional.id = :profissionalId AND h.diaSemana = :diaSemana AND h.ativo = true ORDER BY h.horaInicio")
    List<HorarioDisponivel> findHorariosOrdenados(
            @Param("profissionalId") Long profissionalId,
            @Param("diaSemana") DayOfWeek diaSemana
    );

    void deleteByProfissionalId(Long profissionalId);
}
