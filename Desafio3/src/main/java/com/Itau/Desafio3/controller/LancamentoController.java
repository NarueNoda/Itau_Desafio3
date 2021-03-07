package com.Itau.Desafio3.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.Itau.Desafio3.entity.Categoria;
import com.Itau.Desafio3.entity.Lancamento;
import com.Itau.Desafio3.service.CategoriaService;
import com.Itau.Desafio3.service.LancamentoService;

@RestController

public class LancamentoController {
	
	@Autowired
	private RestTemplate restTemplate;

	
	@RequestMapping("/lancamentos")
	public List<Lancamento> getLancamentos(){
		
		LancamentoService lancamentoModel = new LancamentoService(restTemplate);
		
		List<Lancamento> result = lancamentoModel.getLancamentoList().stream()
				.collect(Collectors.toList());
		
		return result;
	}
	
	//geral
	@RequestMapping()
	public ModelAndView getLancamentos(Model model){
		List<Lancamento> lancamentos = getLancamentos();
		
		List<Lancamento> lista = lancamentos.stream()
				.sorted(Comparator.comparing(Lancamento::getMes_lancamento))
				.collect(Collectors.toList());
		
		model.addAttribute("lancamentos", lista );
		model.addAttribute("count",lancamentos.stream().count());
		
		return new ModelAndView("index");
	}
	
	//categoria
	@RequestMapping("/categoria")
	public ModelAndView getLancamentosOfCategoria(Model model){
				
		Map<Integer, Double> sortedCategoria = getLancamentos().stream()
				.collect(
						Collectors.groupingBy(
								Lancamento::getCategoria,
								Collectors.summingDouble(Lancamento::getValor)
						)
				);
		
		model.addAttribute("lancamentos", sortedCategoria);
		model.addAttribute("count", sortedCategoria.size());
		
		return new ModelAndView("categoria");
	}
	
	//por mes
	@RequestMapping("/mes")
	public ModelAndView getLancamentosOfMes(Model model){
		
		Map<Integer, Double> sortedMes = getLancamentos().stream()
				.collect(
						Collectors.groupingBy(
								Lancamento::getMes_lancamento,
								Collectors.summingDouble(Lancamento::getValor)
						)
				);
		
		model.addAttribute("lancamentos", sortedMes);
		model.addAttribute("count", sortedMes.size());
		
		return new ModelAndView("mes");
		
	}
	
			
}

