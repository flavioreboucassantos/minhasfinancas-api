package com.flavioreboucassantos.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flavioreboucassantos.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
