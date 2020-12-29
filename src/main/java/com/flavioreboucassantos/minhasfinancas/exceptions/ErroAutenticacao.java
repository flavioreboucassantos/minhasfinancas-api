package com.flavioreboucassantos.minhasfinancas.exceptions;

public class ErroAutenticacao extends RuntimeException {
	
	public ErroAutenticacao(String message) {
		super(message);
	}
}
