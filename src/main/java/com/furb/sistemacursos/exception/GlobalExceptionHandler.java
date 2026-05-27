package com.furb.sistemacursos.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ResponseEntity<ApiError> tratarRecursoNaoEncontrado(
			RecursoNaoEncontradoException excep, HttpServletRequest req){

		log.warn("Recurso não encontrado: {} {}", req.getMethod(), req.getRequestURI());

		ApiError erro = new ApiError(
				LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				"NOT_FOUND",
				excep.getMessage(),
				req.getRequestURI()
				);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}

	@ExceptionHandler(RecursoJaExistenteException.class)
	public ResponseEntity<ApiError> tratarRecursoExistente(
			RecursoJaExistenteException excep, HttpServletRequest req){

		log.warn("Conflito: {} {}", req.getMethod(), req.getRequestURI());

		ApiError erro = new ApiError(
				LocalDateTime.now(),
				HttpStatus.CONFLICT.value(),
				"CONFLICT",
				excep.getMessage(),
				req.getRequestURI()
				);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> tratarValidacao(
			MethodArgumentNotValidException ex, HttpServletRequest req) {

		String mensagem = ex.getBindingResult().getFieldErrors().stream()
				.map(e -> e.getField() + ": " + e.getDefaultMessage())
				.reduce("", (a, b) -> a.isEmpty() ? b : a + "; " + b);

		log.warn("Validação falhou: {} {}: {}", req.getMethod(), req.getRequestURI(), mensagem);

		ApiError erro = new ApiError(
				LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(),
				"BAD_REQUEST",
				mensagem,
				req.getRequestURI()
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> tratarIntegridade(
			DataIntegrityViolationException excep, HttpServletRequest req){

		log.warn("Violação de integridade: {} {}", req.getMethod(), req.getRequestURI());

		ApiError erro = new ApiError(
				LocalDateTime.now(),
				HttpStatus.CONFLICT.value(),
				"CONFLICT",
				"Operação viola integridade dos dados: registro em uso por outro cadastro",
				req.getRequestURI()
				);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiError> tratarCredenciaisInvalidas(
			BadCredentialsException excep, HttpServletRequest req){

		log.warn("Credenciais inválidas: {}", req.getRequestURI());

		ApiError erro = new ApiError(
				LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(),
				"UNAUTHORIZED",
				"Login ou senha inválidos",
				req.getRequestURI()
				);

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
	}
}
