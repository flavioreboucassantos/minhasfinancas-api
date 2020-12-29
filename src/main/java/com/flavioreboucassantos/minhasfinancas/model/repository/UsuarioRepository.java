package com.flavioreboucassantos.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flavioreboucassantos.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> { // Extends JpaRepository implementa automaticamente os métodos

	boolean existsByEmail(String email);
	
	Optional<Usuario> findByEmail(String email);
}
