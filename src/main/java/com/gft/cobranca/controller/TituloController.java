package com.gft.cobranca.controller;

import java.util.Arrays;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gft.cobranca.model.StatusTitulo;
import com.gft.cobranca.model.Titulo;
import com.gft.cobranca.repository.Titulos;


@Controller
@RequestMapping("/titulos")
public class TituloController {
	
	private static final String CADASTRO_VIEW = "CadastroTitulo";
	
	@Autowired
	private Titulos titulos;
	
	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		//mv.addObject("todosStatusTitulo", StatusTitulo.values());
		mv.addObject(new Titulo());
		return mv;
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes) {
		//ModelAndView mv = new ModelAndView("CadastroTitulo");
		if(errors.hasErrors()) {
			return CADASTRO_VIEW;
		}
		// salvar no banco de dados
		try {
		titulos.save(titulo);
		attributes.addFlashAttribute("mensagem", "Titulo salvo com sucesso!");
		//mv.addObject("todosStatusTitulo", StatusTitulo.values());
		return "redirect:/titulos/novo";
		} catch (DataIntegrityViolationException e) {
			errors.rejectValue("dataVencimento", null, "Formato de data invalido");
			return CADASTRO_VIEW;
		}
		}
	@RequestMapping
	public ModelAndView pesquisar() {
		List<Titulo> todosTitulos = titulos.findAll();
		ModelAndView mv = new ModelAndView("PesquisaTitulos");
		mv.addObject("titulos", todosTitulos);
		return mv;
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Titulo titulo) {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulo);
		return mv;
	}
	
	@RequestMapping(value="{codigo}",method = RequestMethod.POST)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		titulos.deleteById(codigo);
		attributes.addFlashAttribute("mensagem", "Titulo excluido com sucesso!");
		return"redirect:/titulos";
	}
	
	
	
	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo(){
		return Arrays.asList(StatusTitulo.values());
	}
}
