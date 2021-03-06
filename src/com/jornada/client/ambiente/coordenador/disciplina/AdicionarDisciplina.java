package com.jornada.client.ambiente.coordenador.disciplina;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.jornada.client.ambiente.coordenador.curso.TelaInicialCurso;
import com.jornada.client.classes.listBoxes.MpSelectionCurso;
import com.jornada.client.classes.listBoxes.MpSelectionPeriodo;
import com.jornada.client.classes.listBoxes.ambiente.general.MpListBoxSimNao;
import com.jornada.client.classes.listBoxes.suggestbox.MpListBoxPanelHelper;
import com.jornada.client.classes.widgets.button.MpImageButton;
import com.jornada.client.classes.widgets.dialog.MpDialogBox;
import com.jornada.client.classes.widgets.label.MpLabelRight;
import com.jornada.client.classes.widgets.label.MpLabelTextBoxError;
import com.jornada.client.classes.widgets.panel.MpPanelLoading;
import com.jornada.client.classes.widgets.panel.MpSpaceVerticalPanel;
import com.jornada.client.classes.widgets.textbox.MpTextArea;
import com.jornada.client.classes.widgets.textbox.MpTextBox;
import com.jornada.client.content.i18n.TextConstants;
import com.jornada.client.service.GWTServiceDisciplina;
import com.jornada.shared.FieldVerifier;
import com.jornada.shared.classes.Disciplina;

public class AdicionarDisciplina extends VerticalPanel {

	private AsyncCallback<String> callbackAddDisciplina;

	MpDialogBox mpDialogBoxConfirm = new MpDialogBox();
	MpDialogBox mpDialogBoxWarning = new MpDialogBox();
	MpPanelLoading hPanelLoading = new MpPanelLoading("images/radar.gif");
	
	MpLabelTextBoxError lblErroNomeDisciplina;
	MpLabelTextBoxError lblErroPeriodo;
	MpLabelTextBoxError lblErroCargaHoraria;
	
	private MpSelectionCurso listBoxCurso;
	private MpSelectionPeriodo listBoxPeriodo;
	
	MpListBoxPanelHelper mpHelperCurso = new  MpListBoxPanelHelper();
	
	private MpTextBox txtNome;
	private MpTextBox txtCargaHoraria;	
	private MpTextArea txtDescricao;
	private MpTextArea txtObjetivo;
	
	private MpListBoxSimNao listBoxSimNao;
	

	TextConstants txtConstants;
	
	@SuppressWarnings("unused")
	private TelaInicialDisciplina telaInicialDisciplina;

	public AdicionarDisciplina(final TelaInicialDisciplina telaInicialDisciplina) {
		
		txtConstants = GWT.create(TextConstants.class);
		
		this.telaInicialDisciplina=telaInicialDisciplina;

		mpDialogBoxConfirm.setTYPE_MESSAGE(MpDialogBox.TYPE_CONFIRMATION);
		mpDialogBoxWarning.setTYPE_MESSAGE(MpDialogBox.TYPE_WARNING);
		hPanelLoading.setTxtLoading(txtConstants.geralCarregando());
		hPanelLoading.show();
		hPanelLoading.setVisible(false);

		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(2);
		flexTable.setCellPadding(2);
		flexTable.setBorderWidth(0);
		flexTable.setSize(Integer.toString(TelaInicialDisciplina.intWidthTable),Integer.toString(TelaInicialDisciplina.intHeightTable));
		FlexCellFormatter cellFormatter = flexTable.getFlexCellFormatter();

		// Add a title to the form
//		cellFormatter.setColSpan(0, 0, 0);
		cellFormatter.setHorizontalAlignment(0, 0,HasHorizontalAlignment.ALIGN_CENTER);
		txtNome = new MpTextBox();
		txtCargaHoraria = new MpTextBox();
		txtDescricao = new MpTextArea();
		txtObjetivo = new MpTextArea();
//		final ListBox multiBox = new ListBox(true);
		
		listBoxSimNao = new MpListBoxSimNao();
	    

		
		
//		txtNome.setWidth("350px");
		listBoxSimNao.setWidth("50px");       
		txtCargaHoraria.setWidth("50px");		
//		txtDescricao.setSize("350px", "50px");
//		txtObjetivo.setSize("350px", "50px");
//		multiBox.setWidth("350px");
		

		MpLabelRight lblCurso = new MpLabelRight(txtConstants.curso());		
		MpLabelRight lblPeriodo = new MpLabelRight(txtConstants.periodo());		
		MpLabelRight lblNome = new MpLabelRight(txtConstants.disciplinaNome());		
		MpLabelRight lblObrigatorio = new MpLabelRight(txtConstants.disciplinaObrigatoria());         
		MpLabelRight lblCargaHoraria = new MpLabelRight(txtConstants.disciplinaCarga());		
		MpLabelRight lblDescricao = new MpLabelRight(txtConstants.disciplinaDescricao());		
		MpLabelRight lblObjetivo = new MpLabelRight(txtConstants.disciplinaObjetivo());
//		Label lblSelecionaPeriodo = new Label("Selecionar Periodos");
		
		lblErroNomeDisciplina = new MpLabelTextBoxError();
		lblErroPeriodo = new MpLabelTextBoxError();
		lblErroCargaHoraria = new MpLabelTextBoxError();
		
//		lblCurso.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//		lblPeriodo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);	
//		lblNome.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//		lblCargaHoraria.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);	
//		lblDescricao.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//		lblObjetivo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//		lblSelecionaPeriodo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

//		lblCurso.setStyleName("design_label");
//		lblPeriodo.setStyleName("design_label");
//		lblNome.setStyleName("design_label");
//		lblCargaHoraria.setStyleName("design_label");		
//		lblDescricao.setStyleName("design_label");
//		lblObjetivo.setStyleName("design_label");
//		lblCargaHoraria.setStyleName("design_label");
//		lblSelecionaPeriodo.setStyleName("design_label");



		// Add some standard form options
		int row = 1;
		flexTable.setWidget(row, 0, lblCurso);

		
		listBoxCurso = new MpSelectionCurso(true);
		listBoxCurso.addChangeHandler(new MpCursoSelectionChangeHandler());
		
		listBoxPeriodo = new MpSelectionPeriodo();
//		listBoxPeriodo.setStyleName("design_list_boxes", false);
		listBoxPeriodo.setMultipleSelect(true);
		listBoxPeriodo.setVisibleItemCount(5);
		listBoxPeriodo.setSize("350px", "70px");
		

		
		flexTable.setWidget(row, 0, lblCurso);flexTable.setWidget(row, 1, listBoxCurso);flexTable.setWidget(row++, 2, mpHelperCurso);
		flexTable.setWidget(row, 0, lblPeriodo);flexTable.setWidget(row, 1, listBoxPeriodo);flexTable.setWidget(row++, 2, lblErroPeriodo);
		flexTable.setWidget(row, 0, lblNome);flexTable.setWidget(row, 1, txtNome);flexTable.setWidget(row++, 2, lblErroNomeDisciplina);
		flexTable.setWidget(row, 0, lblObrigatorio);flexTable.setWidget(row++, 1, listBoxSimNao);
		flexTable.setWidget(row, 0, lblCargaHoraria);flexTable.setWidget(row, 1, txtCargaHoraria);flexTable.setWidget(row++, 2, lblErroCargaHoraria);		
		flexTable.setWidget(row, 0, lblDescricao);flexTable.setWidget(row++, 1, txtDescricao);
		flexTable.setWidget(row, 0, lblObjetivo);flexTable.setWidget(row++, 1, txtObjetivo);
//		flexTable.setWidget(row, 0, lblSelecionaPeriodo);flexTable.setWidget(row++, 1, multiBox);


		MpImageButton btnSave = new MpImageButton(txtConstants.geralSalvar(), "images/save.png");
		btnSave.addClickHandler(new ClickHandlerSave());
		MpImageButton btnClean = new MpImageButton(txtConstants.geralLimpar(), "images/erase.png");
		btnClean.addClickHandler(new ClickHandlerClean());

		VerticalPanel vFormPanel = new VerticalPanel();

		Grid gridSave = new Grid(1, 3);
		gridSave.setCellSpacing(2);
		gridSave.setCellPadding(2);
		{
			int i = 0;
			gridSave.setWidget(0, i++, btnSave);
			gridSave.setWidget(0, i++, btnClean);
			gridSave.setWidget(0, i++, hPanelLoading);
		}
		
		
		MpSpaceVerticalPanel mpSpaceVerticalPanel = new MpSpaceVerticalPanel();
		mpSpaceVerticalPanel.setWidth(Integer.toString(TelaInicialCurso.intWidthTable-700)+"px");

		vFormPanel.add(flexTable);
		vFormPanel.add(gridSave);
		vFormPanel.add(mpSpaceVerticalPanel);

		
		
		/***********************Begin Callbacks**********************/

		// Callback para adicionar Disciplina.
		callbackAddDisciplina = new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				hPanelLoading.setVisible(false);
				mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
				mpDialogBoxWarning.setBodyText(txtConstants.disciplinaErroSalvar());
				mpDialogBoxWarning.showDialog();
			}

			@Override
			public void onSuccess(String result) {
				// lblLoading.setVisible(false);
				hPanelLoading.setVisible(false);
				
				if (result.equals("true")) {
					cleanFields();
					mpDialogBoxConfirm.setTitle(txtConstants.geralConfirmacao());
					mpDialogBoxConfirm.setBodyText(txtConstants.disciplinaSalva());
					mpDialogBoxConfirm.showDialog();
					telaInicialDisciplina.getAdicionarProfessorDisciplina().updateClientData();
					telaInicialDisciplina.getEditarDisciplina().updateClientData();
				} else if(result.contains(Disciplina.DB_UNIQUE_KEY)){
                    String strDisciplina = result.substring(result.indexOf("=(")+2);
                    strDisciplina = strDisciplina.substring(strDisciplina.indexOf(",")+1);
                    strDisciplina = strDisciplina.substring(0,strDisciplina.indexOf(")"));
                    mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
                    mpDialogBoxWarning.setBodyText(txtConstants.disciplinaErroSalvar() + " "+txtConstants.disciplinaDuplicada((strDisciplina)));  
                    mpDialogBoxWarning.showDialog();
				}
				else {
					mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
					mpDialogBoxWarning.setBodyText(txtConstants.disciplinaErroSalvar()+" "+txtConstants.geralRegarregarPagina());
					mpDialogBoxWarning.showDialog();
				}
			}
		};
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize(Integer.toString(TelaInicialDisciplina.intWidthTable+30)+"px",Integer.toString(TelaInicialDisciplina.intHeightTable-40)+"px");
		scrollPanel.setAlwaysShowScrollBars(false);		
		scrollPanel.add(vFormPanel);

//		super.add(vFormPanel);
		super.add(scrollPanel);

	}
	
	/****************Begin Event Handlers*****************/
	
	private class ClickHandlerSave implements ClickHandler {

		public void onClick(ClickEvent event) {

			if(checkFieldsValidator()){	
				
				hPanelLoading.setVisible(true);

//				int intIdPeriodo = Integer.parseInt(listBoxPeriodo.getValue(listBoxPeriodo.getSelectedIndex()));
				
				ArrayList<Integer> arraySelectedPeriodo = new ArrayList<Integer>();
				
				for(int i=0;i<listBoxPeriodo.getItemCount();i++){
					if(listBoxPeriodo.isItemSelected(i)){
						int intIdPeriodo = Integer.parseInt(listBoxPeriodo.getValue(i));
						arraySelectedPeriodo.add(intIdPeriodo);
					}
				}
				
				Integer[] intPeriodos = arraySelectedPeriodo.toArray(new Integer[]{});
				
				int intCargaHoraria = 0;
				if(!txtCargaHoraria.getText().isEmpty()){
					intCargaHoraria = Integer.parseInt(txtCargaHoraria.getText());
				}
				
				String strIsObrigatorio = listBoxSimNao.getSelectedValue();

				Disciplina disciplina = new Disciplina();
//				disciplina.setIdPeriodo(intIdPeriodo);
				disciplina.setNome(txtNome.getText());
				disciplina.setCargaHoraria(intCargaHoraria);
				disciplina.setDescricao(txtDescricao.getText());
				disciplina.setObjetivo(txtObjetivo.getText());
				disciplina.setObrigatoria(Boolean.parseBoolean(strIsObrigatorio));

				GWTServiceDisciplina.Util.getInstance().AdicionarDisciplina(intPeriodos, disciplina, callbackAddDisciplina);

			}
		}
	}
	
	
	
	private class MpCursoSelectionChangeHandler implements ChangeHandler {
		public void onChange(ChangeEvent event) {
		    mpHelperCurso.populateSuggestBox(listBoxCurso);
			int idCurso = Integer.parseInt(listBoxCurso.getValue(listBoxCurso.getSelectedIndex()));
			listBoxPeriodo.populateComboBox(idCurso);
		}  
	}	
	
	private class ClickHandlerClean implements ClickHandler {
		public void onClick(ClickEvent event) {
			cleanFields();			
		}
	}		
	
	/****************End Event Handlers*****************/
	
	public boolean checkFieldsValidator(){
		boolean isFieldsOk = false;		
		
		boolean isNomeDisciplinaOk=false;		
		if(FieldVerifier.isValidName(txtNome.getText())){
			isNomeDisciplinaOk=true;	
			lblErroNomeDisciplina.hideErroMessage();
		}else{
			isNomeDisciplinaOk=false;
			lblErroNomeDisciplina.showErrorMessage(txtConstants.geralCampoObrigatorio(txtConstants.disciplinaNome()));
		}
		
		boolean isPeriodoOk=false;
		if(FieldVerifier.isValidListBoxSelectedValue(listBoxPeriodo.getSelectedIndex())){
			isPeriodoOk=true;	
			lblErroPeriodo.hideErroMessage();
		}else{
			isPeriodoOk=false;
			lblErroPeriodo.showErrorMessage(txtConstants.geralCampoObrigatorio(txtConstants.periodo()));
		}
		
		boolean isCargaHorarioOk=false;
		if(FieldVerifier.isValidInteger(txtCargaHoraria.getValue())){
			isCargaHorarioOk=true;	
			lblErroCargaHoraria.hideErroMessage();
		}else{
			isPeriodoOk=false;
			lblErroCargaHoraria.showErrorMessage(txtConstants.geralErroTipo(txtConstants.geralNumeroInteiro()));
		}		
		
		isFieldsOk = isNomeDisciplinaOk && isPeriodoOk && isCargaHorarioOk;
		
		return isFieldsOk;
	}
	
	
	private void cleanFields(){
		lblErroNomeDisciplina.hideErroMessage();
		lblErroPeriodo.hideErroMessage();
		lblErroCargaHoraria.hideErroMessage();
		txtNome.setValue("");
		txtCargaHoraria.setValue("");
		txtDescricao.setValue("");
		txtObjetivo.setValue("");
	}	
	
	public void updateClientData(){
		listBoxCurso.populateComboBox();
	}
	
	

}
