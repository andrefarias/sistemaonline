package com.jornada.client.ambiente.coordenador.curso;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.jornada.client.classes.listBoxes.MpSelectionCurso;
import com.jornada.client.classes.widgets.button.MpImageButton;
import com.jornada.client.classes.widgets.dialog.MpDialogBox;
import com.jornada.client.classes.widgets.panel.MpPanelLoading;
import com.jornada.client.classes.widgets.panel.MpPanelPageMainView;
import com.jornada.client.content.i18n.TextConstants;
import com.jornada.client.service.GWTServiceCurso;
import com.jornada.client.service.GWTServiceUsuario;
import com.jornada.shared.classes.Curso;
import com.jornada.shared.classes.TipoUsuario;
import com.jornada.shared.classes.Usuario;

public class AssociarCursoAluno extends VerticalPanel{
	
	
	private AsyncCallback<ArrayList<Curso>> callbackGetCursosFiltro;
	private AsyncCallback<ArrayList<Usuario>> callbackGetAlunosFiltro;
	private AsyncCallback<ArrayList<Usuario>> callbackGetAlunosAssociados;	
	private AsyncCallback<Boolean> callbackAssociarAlunoAoCurso;
	
	
	MpDialogBox mpDialogBoxConfirm = new MpDialogBox();
	MpDialogBox mpDialogBoxWarning = new MpDialogBox();
	MpPanelLoading mpPanelCursoLoading = new MpPanelLoading("images/radar.gif");
	MpPanelLoading mpPanelAlunoLoading = new MpPanelLoading("images/radar.gif");
	MpPanelLoading mpPanelAssociandoLoading = new MpPanelLoading("images/radar.gif");
	
//	private CellTable<Curso> cellTable;
//	private ListDataProvider<Curso> dataProvider = new ListDataProvider<Curso>();	
//	private NoSelectionModel<Curso> selModel;
//	
	
	private TextBox txtFiltroCurso;	
	private TextBox txtFiltroAluno;
	
	private MpSelectionCurso listBoxCursos;
	private ListBox multiBoxAlunoFiltrado;
	private ListBox multiBoxAlunoAssociado;
	
	TextConstants txtConstants;
	
	@SuppressWarnings("unused")
	private TelaInicialCurso telaInicialCurso;
	
	public AssociarCursoAluno(TelaInicialCurso telaInicialCurso){
		
		this.telaInicialCurso = telaInicialCurso;
		
		txtConstants = GWT.create(TextConstants.class);
		
		mpDialogBoxConfirm.setTYPE_MESSAGE(MpDialogBox.TYPE_CONFIRMATION);
		mpDialogBoxWarning.setTYPE_MESSAGE(MpDialogBox.TYPE_WARNING);
		
		mpPanelCursoLoading.setTxtLoading(txtConstants.geralCarregando());
		mpPanelCursoLoading.show();
		mpPanelCursoLoading.setVisible(false);
		
		mpPanelAlunoLoading.setTxtLoading(txtConstants.geralCarregando());
		mpPanelAlunoLoading.show();
		mpPanelAlunoLoading.setVisible(false);

		mpPanelAssociandoLoading.setTxtLoading(txtConstants.geralCarregando());
		mpPanelAssociandoLoading.show();
		mpPanelAssociandoLoading.setVisible(false);
		
		
		VerticalPanel vBodyPanel = new VerticalPanel();
		
		vBodyPanel.add(drawPassoUmSelecioneCurso());
		vBodyPanel.add(new InlineHTML("&nbsp;"));
		vBodyPanel.add(drawPassoDoisSelecioneAluno());
		vBodyPanel.add(new InlineHTML("&nbsp;"));
		vBodyPanel.add(drawPassoTresSubmeterAssociacao());		
		
		super.add(vBodyPanel);		
		
		
	}
	
	
	
	public MpPanelPageMainView drawPassoUmSelecioneCurso(){

		
		MpPanelPageMainView mpPanel = new MpPanelPageMainView(txtConstants.cursoSelecionarCurso(), "images/folder_library_16_16.png");
		mpPanel.setWidth(Integer.toString(TelaInicialCurso.intWidthTable)+"px");
		
		FlexTable flexTableFiltrar = new FlexTable();		
		flexTableFiltrar.setCellSpacing(3);
		flexTableFiltrar.setCellPadding(3);
		flexTableFiltrar.setBorderWidth(0);		
		
		Label lblFiltrarCurso = new Label(txtConstants.cursoNome());
		txtFiltroCurso = new TextBox();		
		txtFiltroCurso.addKeyPressHandler(new EnterKeyPressHandlerFiltrarCurso());
		MpImageButton btnFiltrar = new MpImageButton(txtConstants.cursoFiltrar(), "images/magnifier.png");
		btnFiltrar.addClickHandler(new ClickHandlerFiltrarCurso());		
		
		lblFiltrarCurso.setStyleName("design_label");	
		lblFiltrarCurso.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);		
		txtFiltroCurso.setStyleName("design_text_boxes");		
		
		listBoxCursos = new MpSelectionCurso();
//		listBoxCursos.setStyleName("design_text_boxes");
//		listBoxCursos.setWidth("350px");
		listBoxCursos.addChangeHandler(new ChangeHandlerPopularAlunosAssociados());			

		
		flexTableFiltrar.setWidget(0, 0, lblFiltrarCurso);
		flexTableFiltrar.setWidget(0, 1, listBoxCursos);
		flexTableFiltrar.setWidget(0, 2, txtFiltroCurso);
		flexTableFiltrar.setWidget(0, 3, btnFiltrar);
		flexTableFiltrar.setWidget(0, 4, mpPanelCursoLoading);	
		
		mpPanel.add(flexTableFiltrar);
		
		callbackGetCursosFiltro = new AsyncCallback<ArrayList<Curso>>() {

			public void onSuccess(ArrayList<Curso> list) {
				
				mpPanelCursoLoading.setVisible(false);	
				
				listBoxCursos.clear();
				for(int i=0;i<list.size();i++){
					Curso curso = list.get(i);
					listBoxCursos.addItem(curso.getNome(), Integer.toString(curso.getIdCurso()));
				}
				
				popularAlunosAssociados();
				
			}
			
			public void onFailure(Throwable caught) {
				mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
				mpDialogBoxWarning.setBodyText(txtConstants.cursoErroCarregar());
				mpDialogBoxWarning.showDialog();

			}
		};		
		
		popularOnLoadListBoxCursos();
		
		return mpPanel;
		
	}
	
	
	public MpPanelPageMainView drawPassoDoisSelecioneAluno(){
		
		MpPanelPageMainView mpPanel = new MpPanelPageMainView(txtConstants.cursoSelecionarAluno(), "images/people.png");
		mpPanel.setWidth(Integer.toString(TelaInicialCurso.intWidthTable)+"px");
		
		FlexTable flexTableFiltrar = new FlexTable();		
		flexTableFiltrar.setCellSpacing(3);
		flexTableFiltrar.setCellPadding(3);
		flexTableFiltrar.setBorderWidth(0);		
		
		Label lblFiltrarAluno = new Label(txtConstants.alunoNome());
		txtFiltroAluno = new TextBox();		
		txtFiltroAluno.addKeyPressHandler(new EnterKeyPressHandlerFiltrarAluno());
		MpImageButton btnFiltrar = new MpImageButton(txtConstants.geralFiltrar(), "images/magnifier.png");
		btnFiltrar.addClickHandler(new ClickHandlerFiltrarAluno());		
		
		lblFiltrarAluno.setStyleName("design_label");	
		lblFiltrarAluno.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);		
		txtFiltroAluno.setStyleName("design_text_boxes");		
		
		flexTableFiltrar.setWidget(0, 0, lblFiltrarAluno);
		flexTableFiltrar.setWidget(0, 1, txtFiltroAluno);
		flexTableFiltrar.setWidget(0, 2, btnFiltrar);
		flexTableFiltrar.setWidget(0, 3, mpPanelAlunoLoading);
		
		
		FlexTable flexTableBotoes = new FlexTable();
		flexTableBotoes.setCellSpacing(3);
		flexTableBotoes.setCellPadding(3);
		flexTableBotoes.setBorderWidth(0);
		
		MpImageButton mpButtonParaEsquerda = new MpImageButton("", "images/resultset_previous.png");
		MpImageButton mpButtonParaDireita = new MpImageButton("", "images/resultset_next.png");
		
		mpButtonParaDireita.addClickHandler(new ClickHandlerAlunoParaDireita());
		mpButtonParaEsquerda.addClickHandler(new ClickHandlerAlunoParaEsquerda());		
		
		flexTableBotoes.setWidget(0, 0, mpButtonParaDireita);
		flexTableBotoes.setWidget(1, 0, mpButtonParaEsquerda);		
		
		FlexTable flexTableSelecionar = new FlexTable();
		flexTableSelecionar.setCellSpacing(3);
		flexTableSelecionar.setCellPadding(3);
		flexTableSelecionar.setBorderWidth(0);		

		Label lblAluno = new Label(txtConstants.cursoAlunosAssociados());
		lblAluno.setStyleName("design_label");
		
		multiBoxAlunoFiltrado = new ListBox(true);
	    multiBoxAlunoFiltrado.setWidth("450px");
	    multiBoxAlunoFiltrado.setHeight("130px");
	    multiBoxAlunoFiltrado.setVisibleItemCount(10);	
	    multiBoxAlunoFiltrado.setStyleName("design_text_boxes");
	    
		multiBoxAlunoAssociado = new ListBox(true);
	    multiBoxAlunoAssociado.setWidth("450px");
	    multiBoxAlunoAssociado.setHeight("130px");
	    multiBoxAlunoAssociado.setVisibleItemCount(10);	
	    multiBoxAlunoAssociado.setStyleName("design_text_boxes");
	    

	    
	    flexTableSelecionar.setWidget(0, 0, flexTableFiltrar);
	    flexTableSelecionar.setWidget(0, 1, new InlineHTML("&nbsp;"));
	    flexTableSelecionar.setWidget(0, 2, lblAluno);
	    flexTableSelecionar.setWidget(1, 0, multiBoxAlunoFiltrado);
	    flexTableSelecionar.setWidget(1, 1, flexTableBotoes);
	    flexTableSelecionar.setWidget(1, 2, multiBoxAlunoAssociado);
	    
		
	    mpPanel.add(flexTableSelecionar);
	    
		callbackGetAlunosFiltro = new AsyncCallback<ArrayList<Usuario>>() {

			public void onSuccess(ArrayList<Usuario> list) {
				
				mpPanelAlunoLoading.setVisible(false);				
				
				//Begin Cleaning fields
				multiBoxAlunoFiltrado.clear();

				//End Cleaning fields
				
				for(int i=0;i<list.size();i++){
					Usuario usuario = list.get(i);
					multiBoxAlunoFiltrado.addItem(usuario.getPrimeiroNome() +" "+usuario.getSobreNome(), Integer.toString(usuario.getIdUsuario()));
				}


			}
			
			public void onFailure(Throwable caught) {
				mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
				mpDialogBoxWarning.setBodyText(txtConstants.cursoErroCarregar());
				mpDialogBoxWarning.showDialog();

			}
		};
		
		
		callbackGetAlunosAssociados = new AsyncCallback<ArrayList<Usuario>>() {

			public void onSuccess(ArrayList<Usuario> list) {
				
				mpPanelCursoLoading.setVisible(false);				
				
				//Begin Cleaning fields
				multiBoxAlunoAssociado.clear();

				//End Cleaning fields
				
				for(int i=0;i<list.size();i++){
					Usuario usuario = list.get(i);
					multiBoxAlunoAssociado.addItem(usuario.getPrimeiroNome() +" "+usuario.getSobreNome(), Integer.toString(usuario.getIdUsuario()));
				}

			}
			
			public void onFailure(Throwable caught) {
				mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
				mpDialogBoxWarning.setBodyText(txtConstants.cursoErroCarregar());
				mpDialogBoxWarning.showDialog();

			}
		};		
		
		return mpPanel;
	}
	
	
	public VerticalPanel drawPassoTresSubmeterAssociacao(){
		
//		MpPanelPageMainView mpPanel = new MpPanelPageMainView("3 - Por favor, clique no botao submeter para finalizar a associacao", "images/categorycheck.png");
//		mpPanel.setWidth(Integer.toString(CadastroCurso.intWidthTable)+"px");
		

		FlexTable flexTable = new FlexTable();	
		flexTable.setCellSpacing(3);
		flexTable.setCellPadding(3);
		flexTable.setBorderWidth(0);		

		
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanel.setWidth(Integer.toString(TelaInicialCurso.intWidthTable)+"px");

		MpImageButton btnSubmeterAssociacao = new MpImageButton(txtConstants.cursoAssociarAlunos(), "images/image002.png");
		btnSubmeterAssociacao.addClickHandler(new ClickHandlerSubmeterAssociarCursoAoAluno());		


		flexTable.setWidget(0, 0, btnSubmeterAssociacao);
		flexTable.setWidget(0, 1, mpPanelAssociandoLoading);
		flexTable.setWidget(0, 2, new InlineHTML("&nbsp;"));

		vPanel.add(flexTable);
		vPanel.add(new InlineHTML("&nbsp;"));
		
		
		// Callback para adicionar Curso.
		callbackAssociarAlunoAoCurso = new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
				mpDialogBoxWarning.setBodyText(txtConstants.cursoErroAssociarAluno());
				mpDialogBoxWarning.showDialog();
			}

			@Override
			public void onSuccess(Boolean result) {
				
				mpPanelAssociandoLoading.setVisible(false);
				if(result.booleanValue()){
				mpDialogBoxConfirm.setTitle(txtConstants.geralConfirmacao());
				mpDialogBoxConfirm.setBodyText(txtConstants.cursoAlunosInseridosComSucesso());
				mpDialogBoxConfirm.showDialog();
				}
				else
				{
					mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
					mpDialogBoxWarning.setBodyText(txtConstants.cursoErroAssociarAluno());
					mpDialogBoxWarning.showDialog();
					
				}
			}
		};		
	
//		mpPanel.add(vPanel);
		
		return vPanel;
	}
	
	public void popularOnLoadListBoxCursos(){
		mpPanelCursoLoading.setVisible(true);				
		GWTServiceCurso.Util.getInstance().getCursos("%" + txtFiltroCurso.getText() + "%", callbackGetCursosFiltro);
	}
	
	public void popularAlunosAssociados(){
		mpPanelCursoLoading.setVisible(true);	
		int id_curso = Integer.parseInt(listBoxCursos.getValue(listBoxCursos.getSelectedIndex()));
		GWTServiceCurso.Util.getInstance().getTodosOsAlunosDoCurso(id_curso, callbackGetAlunosAssociados);
	}
	
	
	
	
	private class ClickHandlerFiltrarCurso implements ClickHandler {
		public void onClick(ClickEvent event) {
				mpPanelCursoLoading.setVisible(true);				
				GWTServiceCurso.Util.getInstance().getCursos("%" + txtFiltroCurso.getText() + "%", callbackGetCursosFiltro);
		}
	}
	
	private class EnterKeyPressHandlerFiltrarCurso implements KeyPressHandler{
		public void onKeyPress(KeyPressEvent event){
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				mpPanelCursoLoading.setVisible(true);				
				GWTServiceCurso.Util.getInstance().getCursos("%" + txtFiltroCurso.getText() + "%", callbackGetCursosFiltro);
			}
		}
	}	
	
	private class ClickHandlerFiltrarAluno implements ClickHandler {
		public void onClick(ClickEvent event) {
				mpPanelAlunoLoading.setVisible(true);				
				GWTServiceUsuario.Util.getInstance().getUsuariosPorTipoUsuario(TipoUsuario.ALUNO, "%" + txtFiltroAluno.getText() + "%", callbackGetAlunosFiltro);
		}
	}	
	
	private class EnterKeyPressHandlerFiltrarAluno implements KeyPressHandler{
		public void onKeyPress(KeyPressEvent event){
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				mpPanelAlunoLoading.setVisible(true);				
				GWTServiceUsuario.Util.getInstance().getUsuariosPorTipoUsuario(TipoUsuario.ALUNO, "%" +  txtFiltroAluno.getText() + "%", callbackGetAlunosFiltro);
			}
		}
	}	
	
	private class ClickHandlerAlunoParaDireita implements ClickHandler {
		public void onClick(ClickEvent event) {

//			for(int i=0;i<multiBoxAlunoFiltrado.getItemCount();i++){
			
			int i=0;
			while(i<multiBoxAlunoFiltrado.getItemCount()){			
			
				if(multiBoxAlunoFiltrado.isItemSelected(i)){
					String value = multiBoxAlunoFiltrado.getValue(multiBoxAlunoFiltrado.getSelectedIndex());
					String item = multiBoxAlunoFiltrado.getItemText(multiBoxAlunoFiltrado.getSelectedIndex());
					if(!containsItem(multiBoxAlunoAssociado, item)){
						multiBoxAlunoAssociado.addItem(item, value);
					}
					multiBoxAlunoFiltrado.removeItem(multiBoxAlunoFiltrado.getSelectedIndex());
					i=0;
					continue;
				}
				i++;
			}
		}
	}
	
	
	private class ClickHandlerAlunoParaEsquerda implements ClickHandler {
		public void onClick(ClickEvent event) {			

//			for (int i = 0; i < multiBoxAlunoAssociado.getItemCount(); i++) {
			int i=0;
			while(i<multiBoxAlunoAssociado.getItemCount()){			
				if (multiBoxAlunoAssociado.isItemSelected(i)) {
					String value = multiBoxAlunoAssociado.getValue(multiBoxAlunoAssociado.getSelectedIndex());
					String item = multiBoxAlunoAssociado.getItemText(multiBoxAlunoAssociado.getSelectedIndex());
					if (!containsItem(multiBoxAlunoFiltrado, item)) {
						multiBoxAlunoFiltrado.addItem(item, value);
					}
					multiBoxAlunoAssociado.removeItem(multiBoxAlunoAssociado.getSelectedIndex());
					i=0;
					continue;
				}
				i++;
			}
		}
	}

	
	private class ChangeHandlerPopularAlunosAssociados implements ChangeHandler{
		public void onChange(ChangeEvent event){
			popularAlunosAssociados();			

		}	
	}
	
	
	
	private class ClickHandlerSubmeterAssociarCursoAoAluno implements ClickHandler{
		
		public void onClick(ClickEvent event){
			
			mpPanelAssociandoLoading.setVisible(true);
			
			ArrayList<String> list_id_aluno = new ArrayList<String>();
			for(int i=0;i<multiBoxAlunoAssociado.getItemCount();i++){
				list_id_aluno.add(multiBoxAlunoAssociado.getValue(i));
			}
			int id_curso = Integer.parseInt(listBoxCursos.getValue(listBoxCursos.getSelectedIndex()));
			GWTServiceCurso.Util.getInstance().associarAlunosAoCurso(id_curso, list_id_aluno, callbackAssociarAlunoAoCurso);
			
		}
	}
	
	
	private boolean containsItem(ListBox listBox, String item){
		boolean contain = false;
		
		for(int i=0;i<listBox.getItemCount();i++){
			String strItem = listBox.getItemText(i);
			if(strItem.equals(item)){
				contain=true;
				break;
			}
		}			
		return contain;
	}	
	
	
	
	

}