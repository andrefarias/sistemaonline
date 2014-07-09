package com.jornada.server.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jornada.server.database.ConnectionManager;
import com.jornada.shared.classes.Disciplina;
import com.jornada.shared.classes.Periodo;

public class DisciplinaServer {
	
	
	public static String DB_INSERT_DISCIPLINA = "INSERT INTO disciplina (nome_disciplina, carga_horaria, descricao, objetivo, id_periodo) VALUES (?,?,?,?,?) returning id_disciplina;";
	public static String DB_UPDATE_DISCIPLINA = "UPDATE disciplina set nome_disciplina=?, carga_horaria=?, descricao=?, objetivo=?, id_periodo=? where id_disciplina=?;";
	public static String DB_UPDATE_PROFESSOR_DISCIPLINA = "UPDATE disciplina set id_usuario=? where id_disciplina=?;";	
	public static String DB_SELECT_DISCIPLINA = "SELECT * FROM disciplina where (nome_disciplina ilike ?);";
	public static String DB_SELECT_ALL_DISCIPLINA = "SELECT * FROM disciplina order by nome_disciplina asc;";
	public static String DB_SELECT_DISCIPLINA_PELO_PERIODO = "SELECT * FROM disciplina where id_periodo=? order by nome_disciplina asc;";
	public static String DB_SELECT_DISCIPLINA_PELO_PERIODO_ILIKE = "SELECT * FROM disciplina where (id_periodo=?) and (nome_disciplina ilike ?) order by nome_disciplina asc;";
	public static String DB_SELECT_DISCIPLINA_ASSOCIADA_AO_PROFESSOR = "SELECT * FROM disciplina where (id_periodo=?) and (id_usuario=?) order by nome_disciplina asc;";	
	public static String DB_DELETE_DISCIPLINA = "delete from disciplina where id_disciplina=?;";		

	
	public DisciplinaServer(){
		
	}

	
	public static int AdicionarDisciplina(Disciplina disciplina) {
		
//		Boolean isOperationDone = false; 
		int idDisciplina=0;
		
//		JornadaDataBase dataBase = new JornadaDataBase();
		Connection conn = ConnectionManager.getConnection();
		try {
//			dataBase.createConnection();
//			Connection conn = dataBase.getConnection();
			
				int param = 0;
				PreparedStatement pstmtInsertPeriodo = conn.prepareStatement(DB_INSERT_DISCIPLINA);
				pstmtInsertPeriodo.setString(++param, disciplina.getNome());
				pstmtInsertPeriodo.setInt(++param, disciplina.getCargaHoraria());				
				pstmtInsertPeriodo.setString(++param, disciplina.getDescricao());
				pstmtInsertPeriodo.setString(++param, disciplina.getObjetivo());
				pstmtInsertPeriodo.setInt(++param, disciplina.getIdPeriodo());				
				
//				int numberUpdate = pstmtInsertPeriodo.executeUpdate();
				
				ResultSet rs = pstmtInsertPeriodo.executeQuery();			
				rs.next();
				
				idDisciplina = rs.getInt("id_disciplina");
			
		}
		catch(Exception ex)
		{
			idDisciplina=0;
			System.err.println(ex.getMessage());
		}
		finally
		{
//			dataBase.close();
			ConnectionManager.closeConnection(conn);
			
		}
		
		return idDisciplina;
	}
	
	
	public static ArrayList<Disciplina> getDisciplinas(int idPeriodo) {

		ArrayList<Disciplina> data = new ArrayList<Disciplina>();
		Connection conn = ConnectionManager.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(DB_SELECT_DISCIPLINA_PELO_PERIODO);			
			int count=0;
			ps.setInt(++count, idPeriodo);					
			data = getDisciplinaParameters(ps.executeQuery());

		} catch (SQLException sqlex) {
			System.err.println(sqlex.getMessage());
		} finally {
			ConnectionManager.closeConnection(conn);
		}
		
		return data;

	}	
	
	
	public static ArrayList<Disciplina> getDisciplinas(String strFilter) {

		ArrayList<Disciplina> data = new ArrayList<Disciplina>();
		Connection conn = ConnectionManager.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(DB_SELECT_DISCIPLINA);			
			int count=0;
			ps.setString(++count, strFilter);						
			data = getDisciplinaParameters(ps.executeQuery());		

		} catch (SQLException sqlex) {
			System.err.println(sqlex.getMessage());
		} finally {
			ConnectionManager.closeConnection(conn);
		}		

		return data;

	}
		
	public static ArrayList<Disciplina> getDisciplinasPeloPeriodo(int id_periodo) {

		ArrayList<Disciplina> data = new ArrayList<Disciplina>();
		Connection conn = ConnectionManager.getConnection();
		try {

			PreparedStatement ps = conn.prepareStatement(DB_SELECT_DISCIPLINA_PELO_PERIODO);			
			int count=0;
			ps.setInt(++count, id_periodo);				
			data = getDisciplinaParameters(ps.executeQuery());

		} catch (SQLException sqlex) {
			System.err.println(sqlex.getMessage());
		} finally {
			ConnectionManager.closeConnection(conn);			
		}
		return data;

	}	
	
	public static ArrayList<Disciplina> getDisciplinasPeloPeriodo(int id_periodo, String strSearch) {

		ArrayList<Disciplina> data = new ArrayList<Disciplina>();
//		JornadaDataBase dataBase = new JornadaDataBase();
		Connection conn = ConnectionManager.getConnection();
		try 
		{

//			dataBase.createConnection();			
//			Connection connection = dataBase.getConnection();

			PreparedStatement ps = conn.prepareStatement(DB_SELECT_DISCIPLINA_PELO_PERIODO_ILIKE);
			
			int count=0;
			ps.setInt(++count, id_periodo);	
			ps.setString(++count, strSearch);	
			
			
			data = getDisciplinaParameters(ps.executeQuery());

		} catch (SQLException sqlex) {
			System.err.println(sqlex.getMessage());
		} finally {
//			dataBase.close();
			ConnectionManager.closeConnection(conn);
		}

		return data;

	}		
	
	
	public static ArrayList<Disciplina> getDisciplinasAssociadosAoProfessor(int id_periodo, int id_usuario) {

		ArrayList<Disciplina> data = new ArrayList<Disciplina>();
//		JornadaDataBase dataBase = new JornadaDataBase();
		Connection conn = ConnectionManager.getConnection();
		try 
		{

//			dataBase.createConnection();			
//			Connection connection = dataBase.getConnection();

			PreparedStatement ps = conn.prepareStatement(DB_SELECT_DISCIPLINA_ASSOCIADA_AO_PROFESSOR);
			
			int count=0;
			ps.setInt(++count, id_periodo);	
			ps.setInt(++count, id_usuario);	
			
			data = getDisciplinaParameters(ps.executeQuery());

//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) 
//			{
//
//				Disciplina currentDisciplina = new Disciplina();
//				
//				currentDisciplina.setIdPeriodo(rs.getInt("id_periodo"));
//				currentDisciplina.setIdDisciplina(rs.getInt("id_disciplina"));
//				currentDisciplina.setIdUsuario(rs.getInt("id_usuario"));
//				currentDisciplina.setNome(rs.getString("nome_disciplina"));				
//				currentDisciplina.setCargaHoraria(rs.getInt("carga_horaria"));
//				currentDisciplina.setDescricao(rs.getString("descricao"));
//				currentDisciplina.setObjetivo(rs.getString("objetivo"));
////				currentCurso.put("id_departamento", rs.getDate("id_departamento"));
//
//				data.add(currentDisciplina);
//			}

		} catch (SQLException sqlex) {
			System.err.println(sqlex.getMessage());
		} finally {
//			dataBase.close();
			ConnectionManager.closeConnection(conn);
		}

		return data;

	}		
	
	public static ArrayList<Periodo> getPeriodos(){
		return PeriodoServer.getPeriodos();
	}
	
	public static ArrayList<Disciplina> getDisciplinas() {

		ArrayList<Disciplina> data = new ArrayList<Disciplina>();
//		JornadaDataBase dataBase = new JornadaDataBase();
		Connection conn = ConnectionManager.getConnection();

		try 
		{

//			dataBase.createConnection();			
//			Connection connection = dataBase.getConnection();

			PreparedStatement ps = conn.prepareStatement(DB_SELECT_ALL_DISCIPLINA);			
			
			data = getDisciplinaParameters(ps.executeQuery());
			
//	    	ResultSet rs = ps.executeQuery();
//			while (rs.next()) 
//			{
//
//				Disciplina currentDisciplina = new Disciplina();
//				
//				currentDisciplina.setIdPeriodo(rs.getInt("id_periodo"));
//				currentDisciplina.setIdDisciplina(rs.getInt("id_disciplina"));
//				currentDisciplina.setNome(rs.getString("nome_disciplina"));
//				currentDisciplina.setCargaHoraria(rs.getInt("carga_horaria"));
//				currentDisciplina.setDescricao(rs.getString("descricao"));
//				currentDisciplina.setObjetivo(rs.getString("objetivo"));
//
//				data.add(currentDisciplina);
//			}

		} catch (SQLException sqlex) {
			System.err.println(sqlex.getMessage());
		} finally {
//			dataBase.close();
			ConnectionManager.closeConnection(conn);
		}

		return data;

	}	
	
	public static boolean deleteDisciplinaRow(int id_disciplina){
		
		boolean success=false;

//		JornadaDataBase dataBase = new JornadaDataBase();
		Connection conn = ConnectionManager.getConnection();
		try {
//			dataBase.createConnection();
//			Connection connection = dataBase.getConnection();

			int count = 0;
			PreparedStatement ps = conn.prepareStatement(DB_DELETE_DISCIPLINA);
			ps.setInt(++count, id_disciplina);

			int numberUpdate = ps.executeUpdate();


			if (numberUpdate == 1) {
				success = true;
			}


		} catch (SQLException sqlex) {
			success=false;
			System.err.println(sqlex.getMessage());			
		} finally {
//			dataBase.close();
			ConnectionManager.closeConnection(conn);
		}
		
		return success;
	}		
	
	public static boolean updateDisciplinaRow(Disciplina disciplina){
		boolean success=false;

//		JornadaDataBase dataBase = new JornadaDataBase();
		Connection conn = ConnectionManager.getConnection();
		try {
//			dataBase.createConnection();
//			Connection connection = dataBase.getConnection();

			int count = 0;
			PreparedStatement ps = conn.prepareStatement(DB_UPDATE_DISCIPLINA);
			ps.setString(++count, disciplina.getNome());
			ps.setInt(++count, disciplina.getCargaHoraria());
			ps.setString(++count, disciplina.getDescricao());
			ps.setString(++count, disciplina.getObjetivo());
			ps.setInt(++count, disciplina.getIdPeriodo());
			ps.setInt(++count, disciplina.getIdDisciplina());

			int numberUpdate = ps.executeUpdate();


			if (numberUpdate == 1) {
				success = true;
			}


		} catch (SQLException sqlex) {
			success=false;
			System.err.println(sqlex.getMessage());			
		} finally {
//			dataBase.close();
			ConnectionManager.closeConnection(conn);
		}		


		return success;
	}	
	
	//DB_UPDATE_PROFESSOR_DISCIPLINA
	public static boolean updateDisciplinaComIdProfessor(int id_professor, ArrayList<String> list_id_disciplina){
		
		boolean success=false;

//		JornadaDataBase dataBase = new JornadaDataBase();
		Connection conn = ConnectionManager.getConnection();
		try {
//			dataBase.createConnection();
//			Connection connection = dataBase.getConnection();

			int count = 0;
			
			for (int i = 0; i < list_id_disciplina.size(); i++) {
				
				int id_disciplina = Integer.parseInt(list_id_disciplina.get(i));
				PreparedStatement ps = conn.prepareStatement(DB_UPDATE_PROFESSOR_DISCIPLINA);
				ps.setInt(++count, id_professor);
				ps.setInt(++count, id_disciplina);

				int numberUpdate = ps.executeUpdate();

				if (numberUpdate == 1) {
					success = true;
				}
			}


		} catch (SQLException sqlex) {
			success=false;
			System.err.println(sqlex.getMessage());			
		} finally {
//			dataBase.close();
			ConnectionManager.closeConnection(conn);
		}		


		return success;
		
	}
		
	
	public static ArrayList<Disciplina> getDisciplinaParameters(ResultSet rs){

		ArrayList<Disciplina> data = new ArrayList<Disciplina>();
		
		try{
		
		while (rs.next()) 
		{
			Disciplina currentDisciplina = new Disciplina();
			
			currentDisciplina.setIdPeriodo(rs.getInt("id_periodo"));
			currentDisciplina.setIdDisciplina(rs.getInt("id_disciplina"));				
			currentDisciplina.setNome(rs.getString("nome_disciplina"));				
			currentDisciplina.setCargaHoraria(rs.getInt("carga_horaria"));
			currentDisciplina.setDescricao(rs.getString("descricao"));
			currentDisciplina.setObjetivo(rs.getString("objetivo"));
			currentDisciplina.setIdUsuario(rs.getInt("id_usuario"));

			data.add(currentDisciplina);
		}
		}catch(Exception ex){
			System.err.println(ex.getMessage());
		}
		
		return data;

	}	

}