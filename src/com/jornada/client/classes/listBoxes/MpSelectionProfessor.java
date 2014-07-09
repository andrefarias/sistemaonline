package com.jornada.client.classes.listBoxes;

import java.util.ArrayList;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.jornada.client.service.GWTServiceUsuario;
import com.jornada.shared.classes.TipoUsuario;
import com.jornada.shared.classes.Usuario;

public class MpSelectionProfessor extends MpSelection {	
	
	private AsyncCallback<ArrayList<Usuario>> callBackPopulateComboBox;
	
	private ListBox listBoxAux;

	
	public MpSelectionProfessor(){
		
		listBoxAux = new ListBox();


		
		/***********************Begin Callbacks**********************/
		callBackPopulateComboBox = new AsyncCallback<ArrayList<Usuario>>() {
			public void onSuccess(ArrayList<Usuario> lista) {
				
				finishLoadingListBox();		

				for (Usuario object : lista) {
					addItem(object.getPrimeiroNome() + " "+object.getSobreNome(),Integer.toString(object.getIdUsuario()));
					listBoxAux.addItem(object.getPrimeiroNome() + " "+object.getSobreNome(),Integer.toString(object.getIdUsuario()));
				}		

				setVisibleItemCount(1);

				DomEvent.fireNativeEvent(Document.get().createChangeEvent(), MpSelectionProfessor.this);
				
			}

			public void onFailure(Throwable cautch) {
				clear();
				addItem(new Label(ERRO_POPULAR).getText());

			}
		};

		/***********************End Callbacks**********************/
		
		
		/******** Begin Populate ********/
		populateComboBox();
		/******** End Populate ********/				

		
	}	
	
	public void populateComboBox() {
		startLoadingListBox();
		GWTServiceUsuario.Util.getInstance().getUsuariosPorTipoUsuario(TipoUsuario.PROFESSOR, callBackPopulateComboBox);
	}
	
	private void startLoadingListBox(){
		clear();
		addItem(CARREGANDO,Integer.toString(-1));
	}
	
	private void finishLoadingListBox(){
		listBoxAux.clear();
		clear();
	}	
	
	public void filterComboBox(String strFilter){
	
		clear();
		
		for(int i=0;i<listBoxAux.getItemCount();i++){
			addItem(listBoxAux.getItemText(i),listBoxAux.getValue(i));
		}		
		
		strFilter = strFilter.toUpperCase();
		
		if (!strFilter.isEmpty()) {
			
			int i=0;
			while(i<this.getItemCount()){
				
				String strNome = this.getItemText(i).toUpperCase();

				if (!strNome.contains(strFilter)) {
					this.removeItem(i);
					i = 0;
					continue;
				}
				i++;
			}
			
		}
		
	}

}