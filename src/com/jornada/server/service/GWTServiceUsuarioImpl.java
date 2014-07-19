/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.jornada.server.service;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jornada.client.service.GWTServiceUsuario;
import com.jornada.server.classes.UsuarioServer;
import com.jornada.shared.classes.TipoUsuario;
import com.jornada.shared.classes.Usuario;


public class GWTServiceUsuarioImpl extends RemoteServiceServlet implements GWTServiceUsuario {


	private static final long serialVersionUID = -1315036586990464191L;

	public boolean AdicionarUsuario(Usuario usuario) {		
		return UsuarioServer.AdicionarUsuario(usuario);		
	}

	public boolean updateUsuarioRow(Usuario usuario){		
		return UsuarioServer.updateUsuarioRow(usuario);
	}
	
	public boolean atualizarSenha(int idUsuario, String password){		
		return UsuarioServer.atualizarSenha(idUsuario, password);
	}
	
	public boolean deleteUsuarioRow(int id_usuario){		
		return UsuarioServer.deleteUsuarioRow(id_usuario);		
	}	
	
	public ArrayList<Usuario> importarUsuariosUsandoExcel(String excelFile){
		return UsuarioServer.importarUsuariosUsandoExcel(excelFile);
	}
	
	public ArrayList<Usuario> getUsuarios() {		
		return UsuarioServer.getUsuarios();
	}
	
	public ArrayList<Usuario> getUsuarios(String strFilter) {				
		return UsuarioServer.getUsuarios(strFilter);
	}	
	
	public ArrayList<Usuario> getUsuarios(String strDBField, String strFilter) {				
		return UsuarioServer.getUsuarios(strDBField, strFilter);
	}		
	
	public ArrayList<Usuario> getAlunosPorCurso(int idCurso, String strFiltroUsuario) {				
		return UsuarioServer.getAlunosPorCurso(idCurso, strFiltroUsuario);
	}	
	
	public ArrayList<Usuario> getAlunosPorCurso(int idCurso) {				
		return UsuarioServer.getAlunosPorCurso(idCurso);
	}	
	
	public ArrayList<Usuario> getUsuariosPorCursoAmbientePai(Usuario usuarioPai, int idCurso) {		
		
		switch (usuarioPai.getIdTipoUsuario()) {
			case TipoUsuario.ADMINISTRADOR:
				return UsuarioServer.getAlunosPorCurso(idCurso);
			case TipoUsuario.COORDENADOR:
				return UsuarioServer.getAlunosPorCurso(idCurso);
			case TipoUsuario.PAIS:
				return UsuarioServer.getUsuariosPorCursoAmbientePai(usuarioPai, idCurso);		
			default:
				return null;
		}
	}	
	
	public ArrayList<Usuario> getUsuariosPorTipoUsuario(int id_tipo_usuario, String strFilter) {				
		return UsuarioServer.getUsuariosPorTipoUsuario(id_tipo_usuario, strFilter);
	}		
	
	public ArrayList<Usuario> getUsuariosPorTipoUsuario(int id_tipo_usuario) {				
		return UsuarioServer.getUsuariosPorTipoUsuario(id_tipo_usuario);
	}		
	
	public ArrayList<Usuario> getFilhoDoPaiAmbientePais(Usuario usuarioPai) {				
		
		switch (usuarioPai.getIdTipoUsuario()) {
			case TipoUsuario.ADMINISTRADOR:return UsuarioServer.getUsuariosPorTipoUsuario(TipoUsuario.ALUNO);
			case TipoUsuario.COORDENADOR:return UsuarioServer.getUsuariosPorTipoUsuario(TipoUsuario.ALUNO);
			case TipoUsuario.PAIS:return UsuarioServer.getFilhoDoPaiAmbientePais(usuarioPai);
			default:return null;
		}
	}		
	
	public ArrayList<TipoUsuario> getTipoUsuarios() {		
		return UsuarioServer.getTipoUsuarios();
	}
	
	public boolean associarPaisAoAluno(int id_aluno,ArrayList<String> list_id_pais){		
		return UsuarioServer.associarPaisAoAluno(id_aluno, list_id_pais);
	}	
	
	public ArrayList<Usuario> getTodosOsPaisDoAluno(int id_aluno){		
		return UsuarioServer.getTodosOsPaisDoAluno(id_aluno);	
	}		


}
