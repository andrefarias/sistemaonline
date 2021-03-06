package com.jornada.client.classes.listBoxes.ambiente.professor;

import java.util.ArrayList;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.jornada.client.classes.listBoxes.MpSelection;
import com.jornada.client.service.GWTServicePeriodo;
import com.jornada.shared.classes.Periodo;
import com.jornada.shared.classes.Usuario;

public class MpListBoxPeriodoAmbienteProfessor extends MpSelection {

	private AsyncCallback<ArrayList<Periodo>> callBackPopulateComboBox;
	
	private Usuario usuario;

	public MpListBoxPeriodoAmbienteProfessor(Usuario usuario) {
		
		this.usuario = usuario;


		/*********************** Begin Callbacks **********************/
		callBackPopulateComboBox = new AsyncCallback<ArrayList<Periodo>>() {
			public void onSuccess(ArrayList<Periodo> lista) {
				try {
					finishLoadingListBox();

					for (Periodo object : lista) {
						addItem(object.getNomePeriodo(),Integer.toString(object.getIdPeriodo()));
					}

					setVisibleItemCount(1);

					// DomEvent.fireNativeEvent(Document.get().createChangeEvent(),MpSelectionPeriodoAmbienteProfessor.this);
					try {
						DomEvent.fireNativeEvent(Document.get().createChangeEvent(),MpListBoxPeriodoAmbienteProfessor.this);
					} catch (Exception ex) {
						logoutAndRefreshPage();
					}
				} catch (Exception ex) {
					logoutAndRefreshPage();
				}
				
			}

			public void onFailure(Throwable cautch) {
				logoutAndRefreshPage();
				clear();
				addItem(new Label(ERRO_POPULAR).getText());

			}
		};

		/*********************** End Callbacks **********************/

	}

	public void populateComboBox(int idCurso) {
		startLoadingListBox();
		GWTServicePeriodo.Util.getInstance().getPeriodosPeloCursoAmbienteProfessor(this.usuario, idCurso,callBackPopulateComboBox);
	}

	private void startLoadingListBox() {
		clear();
		addItem(CARREGANDO, Integer.toString(-1));
	}

	private void finishLoadingListBox() {
		clear();
	}

}
