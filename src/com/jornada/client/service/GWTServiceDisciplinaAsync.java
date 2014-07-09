package com.jornada.client.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.jornada.shared.classes.Disciplina;
import com.jornada.shared.classes.Periodo;
import com.jornada.shared.classes.Usuario;

public interface GWTServiceDisciplinaAsync {

	public void AdicionarDisciplina(Disciplina disciplina, AsyncCallback<Integer> callback);	
	public void getDisciplinasPeloPeriodo(int idPeriodo, AsyncCallback<ArrayList<Disciplina>> callback);	
	public void getDisciplinasPeloPeriodo(int idPeriodo, String strSearch, AsyncCallback<ArrayList<Disciplina>> callback);		
	public void getDisciplinas(String strFilter, AsyncCallback<ArrayList<Disciplina>> callback);	
	public void getDisciplinasAssociadosAoProfessor(int idPeriodo, int idUsuario, AsyncCallback<ArrayList<Disciplina>> callback);
	public void getDisciplinasPeloPeriodoAmbienteProfessor(Usuario usuario, int idPeriodo, AsyncCallback<ArrayList<Disciplina>> callback);
	public void getDisciplinas(AsyncCallback<ArrayList<Disciplina>> callback);	
	public void getPeriodos(AsyncCallback<ArrayList<Periodo>> callback);	
	public void deleteDisciplinaRow(int id_disciplina, AsyncCallback<Boolean> callback);	
	public void updateDisciplinaRow(Disciplina disciplina, AsyncCallback<Boolean> callback);	
	public void updateDisciplinaComIdProfessor(int id_professor, ArrayList<String> list_id_disciplina, AsyncCallback<Boolean> callback);

}