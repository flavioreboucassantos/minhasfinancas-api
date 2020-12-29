package com.flavioreboucassantos.minhasfinancas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.flavioreboucassantos.minhasfinancas.exceptions.ErroAutenticacao;
import com.flavioreboucassantos.minhasfinancas.exceptions.RegraNegocioException;
import com.flavioreboucassantos.minhasfinancas.model.entity.Usuario;
import com.flavioreboucassantos.minhasfinancas.model.repository.UsuarioRepository;
import com.flavioreboucassantos.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")

public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;

	@MockBean
	UsuarioRepository repository;

	@Test
	public void deveSalvarUmUsuario() {
		// cenário - Mocka o objeto inteiro ao invez de apenas o retorno
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1l).nome("nome").email("usuario@email.com").senha("senha").build();
		
		// quando salvar passando qualquer usuário return o usuário criado
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		// ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		// verificação
		Assertions.assertNotNull(usuarioSalvo);
		Assertions.assertEquals(usuarioSalvo.getId(), 1l);
		Assertions.assertEquals(usuarioSalvo.getNome(), "nome");
		Assertions.assertEquals(usuarioSalvo.getEmail(), "usuario@email.com");
		Assertions.assertEquals(usuarioSalvo.getSenha(), "senha");
	}

	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		// cenário
		String email = "usuario@email.com";
		Usuario usuario = Usuario.builder().email(email).build();		
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		// ação
		Throwable exception = assertThrows(RegraNegocioException.class, () -> {
			service.salvarUsuario(usuario);
		});
		
		// verificação
		// Espero que nunca tenha chamado o metodo de salvar usuário
		Mockito.verify(repository, Mockito.never()).save(usuario);
		
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		// cenário - Encontrou email e senha válidos
		String email = "usuario@email.com";
		String senha = "senha";

		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

		// ação
		Usuario result = service.autenticar(email, senha);

		// verificação
		Assertions.assertNotNull(result);
	}

	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		// cenário - Email não válido
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		// ação
		Throwable exception = assertThrows(ErroAutenticacao.class, () -> {
			service.autenticar("usuario@email.com", "senha");
		});
		assertEquals(exception.getMessage(), "Usuário não encontrado para o email informado.");
	}

	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		// cenário - Email válido mas senha invalida
		String email = "usuario@email.com";
		String senha = "senha";
		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

		// ação
		Throwable exception = assertThrows(ErroAutenticacao.class, () -> {
			service.autenticar(email, "senha2");
		});
		assertEquals(exception.getMessage(), "Senha inválida.");
	}

	@Test
	public void deveValidarEmail() {
		// cenário - configura mock injetado em service
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

		// ação
		service.validarEmail("usuario@email.com");
	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		// cenário - configura mock injetado em service
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

		// ação
		Throwable exception = assertThrows(RegraNegocioException.class, () -> {
			service.validarEmail("usuario@email.com");
		});
	}
}
