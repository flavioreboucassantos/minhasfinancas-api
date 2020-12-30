package com.flavioreboucassantos.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.flavioreboucassantos.minhasfinancas.model.entity.Lancamento;
import com.flavioreboucassantos.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento); // sem id
	
	Lancamento atualizar(Lancamento lancamento); // com id
	
	void deletar(Lancamento lancamento); // com id
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
	
	Optional<Lancamento> obterPorId(Long id);
	
	BigDecimal obterSaldoPorUsuario(Long id);
	
}
