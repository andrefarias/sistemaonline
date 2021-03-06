package com.jornada.client.ambiente.pais.ocorrencia;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.jornada.client.MainView;
import com.jornada.client.classes.widgets.header.MpHeaderWidget;
import com.jornada.client.content.i18n.TextConstants;

public class TelaInicialPaisOcorrencia extends Composite{	
	
	
	static TextConstants txtConstants = GWT.create(TextConstants.class);
	
//	private VerticalPanel verticalPanelPage;
//	VerticalPanel vPanelBody;
	private TabLayoutPanel tabLayoutPanel;

	public  static final int intWidthTable=1500;
	public static final int intHeightTable=500;
	
	private VisualizarPaisOcorrencia visualizarPaisOcorrencia;
//	private VisualizarAgenda visualizarAgenda;

	private MainView mainView;

	
	private static TelaInicialPaisOcorrencia uniqueInstance;
	public static TelaInicialPaisOcorrencia getInstance(MainView mainView){
		
		if(uniqueInstance==null){
			uniqueInstance = new TelaInicialPaisOcorrencia(mainView);
		}else{
			uniqueInstance.visualizarPaisOcorrencia.updateClientData();
		}
		
		return uniqueInstance;
	}		

	private TelaInicialPaisOcorrencia(MainView mainView) {
		
		this.mainView = mainView;
		
		this.visualizarPaisOcorrencia = VisualizarPaisOcorrencia.getInstance(this);
//		this.visualizarAgenda = VisualizarAgenda.getInstance(this);
		
		tabLayoutPanel = new TabLayoutPanel(2.5, Unit.EM);	

//		tabLayoutPanel.setPixelSize(intWidthTable+50, intHeightTable);
		tabLayoutPanel.setHeight(Integer.toString(intHeightTable)+"px");
		tabLayoutPanel.setAnimationDuration(500);
		tabLayoutPanel.setAnimationVertical(true);
		
//		vPanelBody = new VerticalPanel();
		
		tabLayoutPanel.add(visualizarPaisOcorrencia, new MpHeaderWidget(txtConstants.ocorrenciaPorAluno(), "images/elementary_school_16.png"));
//		stackPanel.add(visualizarAvaliacao, createHeaderWidget("Atividades", "images/application_view_detail.png"));
		
//		verticalPanelPage = new VerticalPanel();		
//		verticalPanelPage.add(tabLayoutPanel);
//		verticalPanelPage.add(new InlineHTML("&nbsp;"));
				
     	initWidget(tabLayoutPanel);
		
	}
	
		
	
	public MainView getMainView() {
		return mainView;
	}

//	private Widget createHeaderWidget(String text, String image) {
//		// Add the image and text to a horizontal panel
//		HorizontalPanel hPanel = new HorizontalPanel();
//		hPanel.setHeight("100%");
//		hPanel.setSpacing(0);
//		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//		
//		hPanel.add(new Image(image));
//		hPanel.add(new InlineHTML("&nbsp;"));
//		Label headerText = new Label(text);
//		headerText.setStyleName("gwt-TabLayoutPanel");
//		hPanel.add(headerText);
//		return new SimplePanel(hPanel);
//	}	


}
