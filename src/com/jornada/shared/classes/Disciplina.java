package com.jornada.shared.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class Disciplina implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2606695220488374696L;
	private int idPeriodo;
	private int idDisciplina;
	private int idUsuario;
	private String nome;
	private int cargaHoraria;	
	private String descricao;
//	private String numeracao;
	private String objetivo;
//	private Date dataInicial;
//	private Date dataFinal;		
//	private Periodo periodo;
	
	private Usuario professor;
	
	private ArrayList<ConteudoProgramatico> listConteudoProgramatico;
	private ArrayList<Aula> listAula;
	
	
	public Disciplina(){
		
//		periodo = new Periodo();
		professor = new Usuario();
		listConteudoProgramatico = new ArrayList<ConteudoProgramatico>();
		listAula = new ArrayList<Aula>();
		
	}
	
	

	public Usuario getProfessor() {
		return professor;
	}



	public void setProfessor(Usuario professor) {
		this.professor = professor;
	}



	public int getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(int idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public int getIdDisciplina() {
		return idDisciplina;
	}

	public void setIdDisciplina(int idDisciplina) {
		this.idDisciplina = idDisciplina;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getCargaHoraria() {
		return cargaHoraria;
	}

	public void setCargaHoraria(int cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}	

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

//	public Periodo getPeriodo() {
//		return periodo;
//	}
//
//	public void setPeriodo(Periodo periodo) {
//		this.periodo = periodo;
//	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public ArrayList<ConteudoProgramatico> getListConteudoProgramatico() {
		return listConteudoProgramatico;
	}

	public void setListConteudoProgramatico(
			ArrayList<ConteudoProgramatico> listConteudoProgramatico) {
		this.listConteudoProgramatico = listConteudoProgramatico;
	}

	public ArrayList<Aula> getListAula() {
		return listAula;
	}
	
	public void setListAula(ArrayList<Aula> listAula) {
		this.listAula = listAula;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String toString(){
		return "projeto:com.jornada.shared.classes.Disciplina";
	}

}
