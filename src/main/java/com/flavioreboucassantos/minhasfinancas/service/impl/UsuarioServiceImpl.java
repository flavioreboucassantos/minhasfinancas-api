package com.flavioreboucassantos.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flavioreboucassantos.minhasfinancas.exceptions.ErroAutenticacao;
import com.flavioreboucassantos.minhasfinancas.exceptions.RegraNegocioException;
import com.flavioreboucassantos.minhasfinancas.model.entity.Usuario;
import com.flavioreboucassantos.minhasfinancas.model.repository.UsuarioRepository;
import com.flavioreboucassantos.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		/* Cuidado pois throw error se existir 2 usuários com mesmo email ou um de senha null */
		//System.out.println(">>>>>> " + usuario);

		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}

		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}

		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if (existe)
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return repository.findById(id);
	}

}
