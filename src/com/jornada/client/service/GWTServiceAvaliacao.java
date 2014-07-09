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
package com.jornada.client.service;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.jornada.shared.classes.Avaliacao;
import com.jornada.shared.classes.CursoAvaliacao;
import com.jornada.shared.classes.TipoAvaliacao;

@RemoteServiceRelativePath("GWTServiceAvaliacao")
public interface GWTServiceAvaliacao extends RemoteService {


	public boolean AdicionarAvaliacao(Avaliacao object);	
	public boolean updateRow(Avaliacao object);
	public boolean deleteRow(int id_avaliacao); 	
	public ArrayList<Avaliacao> getAvaliacaoPeloConteudoProgramatico(int idConteudoProgramatico);	
	public ArrayList<CursoAvaliacao> getAvaliacaoPeloCurso(int idCurso, String locale);
	public ArrayList<TipoAvaliacao> getTipoAvaliacao();
	
	public static class Util {
		private static GWTServiceAvaliacaoAsync instance;
		public static GWTServiceAvaliacaoAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(GWTServiceAvaliacao.class);
			}
			return instance;
		}
	}
}