package com.gft.cobranca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gft.cobranca.model.StatusTitulo;
import com.gft.cobranca.model.Titulo;
import com.gft.cobranca.repository.Titulos;
import com.gft.cobranca.repository.filter.TituloFilter;

@Service
public class CadastroTituloService {

	@Autowired
	private Titulos titulos;

	public void salvar(Titulo titulo) {

		try {
			titulos.save(titulo);

		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválida!");
		}
	}

	public void excluir(Long codigo) {
		titulos.deleteById(codigo);
	}

	public String receber(Long codigo) {
		Optional<Titulo> tituloCapturado = titulos.findById(codigo);
			Titulo titulo = new Titulo();
			titulo = tituloCapturado.get();
		titulo.setStatus(StatusTitulo.RECEBIDO);
		titulos.save(titulo);
			
			return StatusTitulo.RECEBIDO.getDescricao();
	}
	public List<Titulo> filtrar(TituloFilter filtro){
		String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();
		return titulos.findByDescricaoContaining(descricao);
	}
	
}

