package com.jornada.client.ambiente.coordenador.periodo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.jornada.client.ambiente.coordenador.curso.TelaInicialCurso;
import com.jornada.client.classes.listBoxes.MpSelectionCurso;
import com.jornada.client.classes.listBoxes.ambiente.coordenador.MpListBoxPesoPeriodo;
import com.jornada.client.classes.listBoxes.suggestbox.MpListBoxPanelHelper;
import com.jornada.client.classes.widgets.button.MpImageButton;
import com.jornada.client.classes.widgets.datebox.MpDateBoxWithImage;
import com.jornada.client.classes.widgets.dialog.MpDialogBox;
import com.jornada.client.classes.widgets.label.MpLabelRight;
import com.jornada.client.classes.widgets.label.MpLabelTextBoxError;
import com.jornada.client.classes.widgets.panel.MpPanelLoading;
import com.jornada.client.classes.widgets.panel.MpSpaceVerticalPanel;
import com.jornada.client.classes.widgets.textbox.MpTextBox;
import com.jornada.client.content.i18n.TextConstants;
import com.jornada.client.service.GWTServicePeriodo;
import com.jornada.shared.FieldVerifier;
import com.jornada.shared.classes.Periodo;

public class AdicionarPeriodo extends VerticalPanel {

	private AsyncCallback<String> callbackAddPeriodo;

	private MpTextBox txtFiltroNomeCurso; 

	MpDialogBox mpDialogBoxConfirm = new MpDialogBox();
	MpDialogBox mpDialogBoxWarning = new MpDialogBox();
	MpPanelLoading hPanelLoading = new MpPanelLoading("images/radar.gif");
	
	

//	private ListBox listBoxCurso;
	private MpSelectionCurso listBoxCurso;
    MpListBoxPanelHelper mpHelperCurso = new MpListBoxPanelHelper();
    
    MpListBoxPesoPeriodo mpListBoxPeso = new MpListBoxPesoPeriodo(1,10);
	
	private MpTextBox txtNomePeriodo;
	private TextArea txtDescricaoPeriodo;
	private TextArea txtObjetivoPeriodo;
//	private MpTextBox txtPesoPeriodo;
	private MpDateBoxWithImage mpDateBoxInicial;
	private MpDateBoxWithImage mpDateBoxFinal;
	
	private MpLabelTextBoxError lblErroNomePeriodo;
	
	@SuppressWarnings("unused")
	private TelaInicialPeriodo telaInicialPeriodo;
	
	TextConstants txtConstants;
	
	private static AdicionarPeriodo uniqueInstance;
	
	public static AdicionarPeriodo getInstance(final TelaInicialPeriodo telaInicialPeriodo){

		if(uniqueInstance==null){
			uniqueInstance = new AdicionarPeriodo(telaInicialPeriodo);
		}
		
		return uniqueInstance;
		
	}

	@SuppressWarnings("deprecation")
	private AdicionarPeriodo(final TelaInicialPeriodo telaInicialPeriodo) {
		
		txtConstants = GWT.create(TextConstants.class);
		
		this.telaInicialPeriodo=telaInicialPeriodo;

		mpDialogBoxConfirm.setTYPE_MESSAGE(MpDialogBox.TYPE_CONFIRMATION);
		mpDialogBoxWarning.setTYPE_MESSAGE(MpDialogBox.TYPE_WARNING);
		hPanelLoading.setTxtLoading(txtConstants.geralCarregando());
		hPanelLoading.show();
		hPanelLoading.setVisible(false);
		
		mpHelperCurso = new  MpListBoxPanelHelper();

		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(2);
		flexTable.setCellPadding(2);
		flexTable.setBorderWidth(0);
		flexTable.setSize(Integer.toString(TelaInicialPeriodo.intWidthTable),Integer.toString(TelaInicialPeriodo.intHeightTable));
		FlexCellFormatter cellFormatter = flexTable.getFlexCellFormatter();

		// Add a title to the form
//		cellFormatter.setColSpan(0, 0, 0);
		cellFormatter.setHorizontalAlignment(0, 0,HasHorizontalAlignment.ALIGN_CENTER);
		txtNomePeriodo = new MpTextBox();
		txtDescricaoPeriodo = new TextArea();
		txtObjetivoPeriodo = new TextArea();
//		txtPesoPeriodo = new MpTextBox();
		mpDateBoxInicial = new MpDateBoxWithImage();
		mpDateBoxInicial.getDate().setFormat(new DefaultFormat(DateTimeFormat.getFullDateFormat()));
		mpDateBoxFinal = new MpDateBoxWithImage();
		mpDateBoxFinal.getDate().setFormat(new DefaultFormat(DateTimeFormat.getFullDateFormat()));
		txtDescricaoPeriodo.setStyleName("design_text_boxes");
		txtObjetivoPeriodo.setStyleName("design_text_boxes");


		MpLabelRight lblCurso = new MpLabelRight(txtConstants.curso());
		MpLabelRight lblNomePeriodo = new MpLabelRight(txtConstants.periodoNome());		
		MpLabelRight lblDescricaoPeriodo = new MpLabelRight(txtConstants.periodoDescricao());		
		MpLabelRight lblObjetivoPeriodo = new MpLabelRight(txtConstants.periodoObjetivo());		
		MpLabelRight lblPesoPeriodo = new MpLabelRight("Peso");
		MpLabelRight lblDateInicial = new MpLabelRight(txtConstants.periodoDataInicial());		
		MpLabelRight lblDateFinal = new MpLabelRight(txtConstants.periodoDataFinal());
		
		lblErroNomePeriodo = new MpLabelTextBoxError();
		
		txtNomePeriodo.setWidth("350px");
//		txtPesoPeriodo.setWidth("80px");
		mpListBoxPeso.setWidth("80px");
		txtDescricaoPeriodo.setSize("350px", "50px");
		txtObjetivoPeriodo.setSize("350px", "50px");
		mpDateBoxInicial.getDate().setWidth("170px");
		mpDateBoxFinal.getDate().setWidth("170px");
		
		
	    txtFiltroNomeCurso = new MpTextBox();
	    txtFiltroNomeCurso.setWidth("150px");
	    txtFiltroNomeCurso.addKeyUpHandler(new EnterKeyUpHandlerFiltrarCurso());

		
		listBoxCurso = new MpSelectionCurso(true);
		listBoxCurso.addChangeHandler(new MpCursoSelectionChangeHandler());
		
		// Add some standard form options
		int row = 1;

		flexTable.setWidget(row, 0, lblCurso);flexTable.setWidget(row, 1, listBoxCurso);flexTable.setWidget(row++, 2, mpHelperCurso);//flexTable.setWidget(row++, 2, txtFiltroNomeCurso);
		
		flexTable.setWidget(row, 0, lblNomePeriodo);flexTable.setWidget(row, 1, txtNomePeriodo);flexTable.setWidget(row++, 2, lblErroNomePeriodo);
		flexTable.setWidget(row, 0, lblDescricaoPeriodo);flexTable.setWidget(row++, 1, txtDescricaoPeriodo);
		flexTable.setWidget(row, 0, lblObjetivoPeriodo);flexTable.setWidget(row++, 1, txtObjetivoPeriodo);
		flexTable.setWidget(row, 0, lblPesoPeriodo);flexTable.setWidget(row++, 1, mpListBoxPeso);
		flexTable.setWidget(row, 0, lblDateInicial);flexTable.setWidget(row++, 1, mpDateBoxInicial);
		flexTable.setWidget(row, 0, lblDateFinal);flexTable.setWidget(row++, 1, mpDateBoxFinal);

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

		// Callback para adicionar Periodo.
		callbackAddPeriodo = new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				hPanelLoading.setVisible(false);
				mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
				mpDialogBoxWarning.setBodyText(txtConstants.periodoErroSalvar());
				mpDialogBoxWarning.showDialog();
			}

			@Override
			public void onSuccess(String result) {
				hPanelLoading.setVisible(false);
				
				if (result.equals("true")) {
					cleanFields();
					mpDialogBoxConfirm.setTitle(txtConstants.geralConfirmacao());
					mpDialogBoxConfirm.setBodyText(txtConstants.periodoSalvoSucesso());
					mpDialogBoxConfirm.showDialog();
					telaInicialPeriodo.populateGrid();
				}else if(result.contains(Periodo.DB_UNIQUE_KEY)){
                    String strPeriodo = result.substring(result.indexOf("=(")+2);
                    strPeriodo = strPeriodo.substring(strPeriodo.indexOf(",")+1);
                    strPeriodo = strPeriodo.substring(0,strPeriodo.indexOf(")"));
                    mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
                    mpDialogBoxWarning.setBodyText(txtConstants.periodoErroSalvar() + " "+txtConstants.periodoDuplicado((strPeriodo)));  
                    mpDialogBoxWarning.showDialog();   
				}
				else {
					mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
					mpDialogBoxWarning.setBodyText(txtConstants.periodoErroSalvar()+" "+txtConstants.geralRecarregarAmbiente());
					mpDialogBoxWarning.showDialog();
				}
			}
		};

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize(Integer.toString(TelaInicialPeriodo.intWidthTable+30)+"px",Integer.toString(TelaInicialPeriodo.intHeightTable-40)+"px");
		scrollPanel.setAlwaysShowScrollBars(false);		
		scrollPanel.add(vFormPanel);

		super.add(scrollPanel);
//		super.add(vFormPanel);

	}

	
	
	/****************Begin Event Handlers*****************/
	
	private class ClickHandlerSave implements ClickHandler {

		public void onClick(ClickEvent event) {

//			if (txtNomePeriodo == null || txtNomePeriodo.getTextBox().getText().isEmpty()) {
//
//				mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
//				mpDialogBoxWarning.setBodyText(txtConstants.geralCampoObrigatorio(txtConstants.periodoNome()));
//				mpDialogBoxWarning.showDialog();
//
//			} else {
			if(checkFieldsValidator()){			

				hPanelLoading.setVisible(true);

				int intIdCurso = Integer.parseInt(listBoxCurso.getValue(listBoxCurso.getSelectedIndex()));

				Periodo periodo = new Periodo();
				periodo.setIdCurso(intIdCurso);
				periodo.setNomePeriodo(txtNomePeriodo.getText());
				periodo.setDescricao(txtDescricaoPeriodo.getText());
				periodo.setObjetivo(txtObjetivoPeriodo.getText());
				periodo.setDataInicial(mpDateBoxInicial.getDate().getValue());
				periodo.setDataFinal(mpDateBoxFinal.getDate().getValue());

				GWTServicePeriodo.Util.getInstance().adicionarPeriodoString(periodo, callbackAddPeriodo);

			}

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
		
		if(FieldVerifier.isValidName(txtNomePeriodo.getText())){
			isFieldsOk=true;	
			lblErroNomePeriodo.hideErroMessage();
			
            if (!txtNomePeriodo.getText().contains("[") && !txtNomePeriodo.getText().contains("]")) {
                isFieldsOk = true;
                lblErroNomePeriodo.hideErroMessage();
            } else {
                isFieldsOk = false;
                lblErroNomePeriodo.showErrorMessage(txtConstants.geralErroCaracterColchete());
            }
		}
		else{
			isFieldsOk=false;
			lblErroNomePeriodo.showErrorMessage(txtConstants.geralCampoObrigatorio(txtConstants.periodoNome()));
		}

		
		return isFieldsOk;
	}
	
	
	private void cleanFields(){
		lblErroNomePeriodo.hideErroMessage();
		txtNomePeriodo.setValue("");
		txtDescricaoPeriodo.setValue("");
		txtObjetivoPeriodo.setValue("");
//		txtPesoPeriodo.setValue("");
		mpListBoxPeso.setItemSelected(0, true);
		mpDateBoxInicial.getDate().setValue(null);
		mpDateBoxFinal.getDate().setValue(null);
	}


	public void updateClientData() {
		listBoxCurso.populateComboBox();
	}
	
    private class EnterKeyUpHandlerFiltrarCurso implements KeyUpHandler {
        public void onKeyUp(KeyUpEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                listBoxCurso.filterComboBox(txtFiltroNomeCurso.getText());
            }
        }
    }
	
    private class MpCursoSelectionChangeHandler implements ChangeHandler {
        public void onChange(ChangeEvent event) {  
            mpHelperCurso.populateSuggestBox(listBoxCurso);
        }  
    }
	
	
}
