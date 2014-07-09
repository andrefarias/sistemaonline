package com.jornada.client.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.jornada.shared.classes.Curso;
import com.jornada.shared.classes.Usuario;

public interface GWTServiceCursoAsync {

	public void AdicionarCurso(Curso curso, AsyncCallback<Integer> callback);
	public void AdicionarCursoTemplate(int idCursoTemplate, Curso curso, AsyncCallback<Boolean> callback);	
	public void updateCursoRow(Curso curso, AsyncCallback<Boolean> callback);	
	public void deleteCursoRow(int id_curso, AsyncCallback<Boolean> callback);	
	public void getCursos(AsyncCallback<ArrayList<Curso>> callback);	
	public void getCursosPorPaiAmbientePais(Usuario usuario, AsyncCallback<ArrayList<Curso>> callback);
	public void getCursosPorAlunoAmbienteAluno(Usuario usuario, AsyncCallback<ArrayList<Curso>> callback);
	public void getCursosAmbienteProfessor(Usuario usuario, AsyncCallback<ArrayList<Curso>> callback);
	public void getCursos(String strFilter, AsyncCallback<ArrayList<Curso>> callback);	
	public void getTodosOsAlunosDoCurso(int id_curso, AsyncCallback<ArrayList<Usuario>> callback);	
	public void associarAlunosAoCurso(int id_curso, ArrayList<String> list_id_alunos, AsyncCallback<Boolean> callback);

}