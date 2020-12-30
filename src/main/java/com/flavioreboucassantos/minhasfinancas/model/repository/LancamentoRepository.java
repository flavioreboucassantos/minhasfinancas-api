package com.flavioreboucassantos.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flavioreboucassantos.minhasfinancas.model.entity.Lancamento;
import com.flavioreboucassantos.minhasfinancas.model.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	// JPQL - usa nome do objeto ao invés da tabela
	
	@Query(value = ""
			+ "select sum(l.valor) from Lancamento l join l.usuario u "
			+ "where u.id = :idUsuario and l.tipo = :tipo group by u")
	public BigDecimal obterSaldoPorTipoLancamentoEUsuario(
			@Param("idUsuario") Long idUsuario,
			@Param("tipo") TipoLancamento tipo);

}
