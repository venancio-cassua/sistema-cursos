package com.furb.sistemacursos.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ResponseEntity<ApiError> tratarRecursoNaoEncontrado(
			RecursoNaoEncontradoException excep, HttpServletRequest req){
		
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
		ApiError erro = new ApiError(
				LocalDateTime.now(), 
				HttpStatus.CONFLICT.value(), 
				"CONFLICT", 
				excep.getMessage(), 
				req.getRequestURI()
				);
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
	}
}
