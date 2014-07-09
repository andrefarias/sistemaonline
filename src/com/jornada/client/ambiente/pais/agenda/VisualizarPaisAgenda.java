package com.jornada.client.ambiente.pais.agenda;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.AppointmentStyle;
import com.bradrydzewski.gwt.calendar.client.Calendar;
import com.bradrydzewski.gwt.calendar.client.CalendarFormat;
import com.bradrydzewski.gwt.calendar.client.CalendarViews;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.jornada.client.classes.animation.ElementFader;
import com.jornada.client.classes.listBoxes.ambiente.pais.MpSelectionCursoAmbientePais;
import com.jornada.client.classes.widgets.button.MpImageButton;
import com.jornada.client.classes.widgets.dialog.MpDialogBox;
import com.jornada.client.classes.widgets.panel.MpPanelLoading;
import com.jornada.client.content.i18n.TextConstants;
import com.jornada.client.service.GWTServiceAvaliacao;
import com.jornada.client.service.GWTServiceCurso;
import com.jornada.client.service.GWTServiceCursoAsync;
import com.jornada.shared.classes.CursoAvaliacao;
import com.jornada.shared.classes.utility.MpUtilClient;


public class VisualizarPaisAgenda extends VerticalPanel {

//	private AsyncCallback<Boolean> callbackAddCurso;
	static TextConstants txtConstants = GWT.create(TextConstants.class);

	MpDialogBox mpDialogBoxConfirm = new MpDialogBox();
	MpDialogBox mpDialogBoxWarning = new MpDialogBox();
	MpPanelLoading mpPanelLoading = new MpPanelLoading("images/radar.gif");
	
	Calendar calendar;

	private DatePicker datePicker;
	
	PopupPanel MyPopup;

	private MpSelectionCursoAmbientePais listBoxCurso;
	
	private TelaInicialPaisAgenda telaInicialPaisAgenda;	
	
	private static VisualizarPaisAgenda uniqueInstance;
	
	
	public static VisualizarPaisAgenda getInstance(TelaInicialPaisAgenda telaInicialPaisAgenda){
		
		if(uniqueInstance==null){
			uniqueInstance = new VisualizarPaisAgenda(telaInicialPaisAgenda);
		}
		return uniqueInstance;
	}

	private VisualizarPaisAgenda(final TelaInicialPaisAgenda telaInicialPaisAgenda) {
		
		this.telaInicialPaisAgenda = telaInicialPaisAgenda;

		mpDialogBoxConfirm.setTYPE_MESSAGE(MpDialogBox.TYPE_CONFIRMATION);
		mpDialogBoxWarning.setTYPE_MESSAGE(MpDialogBox.TYPE_WARNING);
		mpPanelLoading.setTxtLoading("");
		mpPanelLoading.show();
		mpPanelLoading.setVisible(false);
		mpPanelLoading.setWidth("50px");

		Label lblCursoEdit = new Label(txtConstants.curso());
			
		listBoxCurso = new MpSelectionCursoAmbientePais(telaInicialPaisAgenda.getMainView().getUsuarioLogado());
		listBoxCurso.setWidth("250px");
		listBoxCurso.addChangeHandler(new MpCursoSelectionChangeHandler());

		Image imgDateControl = new Image("images/calendar-icon_36.png");
		imgDateControl.addClickHandler(new ImgClickChandler());
		imgDateControl.setStyleName("hand-over");
		
		MpImageButton mpBtnDay = new MpImageButton(txtConstants.agendaDia(),"images/date_control_day.png");
		mpBtnDay.addClickHandler(new ClickHandlerDay());

		MpImageButton mpBtnWeek = new MpImageButton(txtConstants.agendaSemana(),"images/date_control_week.png");
		mpBtnWeek.addClickHandler(new ClickHandlerWeek());

		MpImageButton mpBtnMonth = new MpImageButton(txtConstants.agendaMes(),"images/date_control_month.png");
		mpBtnMonth.addClickHandler(new ClickHandlerMonth());
		
		VerticalPanel vPanelSpace = new VerticalPanel();
		vPanelSpace.setWidth("200px");
		
				
		Grid gridComboBox = new Grid(1, 13);
		gridComboBox.setCellSpacing(2);
		gridComboBox.setCellPadding(2);
		{

			int column=0;
			gridComboBox.setWidget(0, column++, lblCursoEdit);
			gridComboBox.setWidget(0, column++, listBoxCurso);
			gridComboBox.setWidget(0, column++, new InlineHTML("&nbsp;"));
			gridComboBox.setWidget(0, column++, mpPanelLoading);
			gridComboBox.setWidget(0, column++, new InlineHTML("&nbsp;"));
			gridComboBox.setWidget(0, column++, imgDateControl);
			gridComboBox.setWidget(0, column++,vPanelSpace);
			gridComboBox.setWidget(0, column++, mpBtnDay);
			gridComboBox.setWidget(0, column++, new InlineHTML("&nbsp;"));
			gridComboBox.setWidget(0, column++, mpBtnWeek);
			gridComboBox.setWidget(0, column++, new InlineHTML("&nbsp;"));
			gridComboBox.setWidget(0, column++, mpBtnMonth);
			
		}			

	
		calendar = new Calendar();
		calendar.setDate(new Date()); //calendar date, not required
		calendar.setDays(7); //number of days displayed at a time, not required
		calendar.setView(CalendarViews.DAY);
		calendar.setSize(Integer.toString(TelaInicialPaisAgenda.intWidthTable-50)+ "px",Integer.toString(TelaInicialPaisAgenda.intHeightTable+60)+"px");

		
		CalendarFormat.INSTANCE.setDayOfMonthFormat(txtConstants.agendaSetDayOfMonthFormat());
		CalendarFormat.INSTANCE.setTimeFormat(txtConstants.agendaSetTimeFormat());
		CalendarFormat.INSTANCE.setDateFormat(txtConstants.agendaSetDateFormat());
		CalendarFormat.INSTANCE.setDayOfWeekFormat(txtConstants.agendaSetDayOfWeekFormat());
		CalendarFormat.INSTANCE.setAm(txtConstants.agendaSetAm());
		CalendarFormat.INSTANCE.setPm(txtConstants.agendaSetPm());
		CalendarFormat.INSTANCE.setFirstDayOfWeek(0);
		CalendarFormat.INSTANCE.setNoon(txtConstants.agendaSetNoon());
		
		MyPopup = new PopupPanel(true);
		MyPopup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
	          public void setPosition(int offsetWidth, int offsetHeight) {
	            int left = (Window.getClientWidth() - offsetWidth) / 5;
	            int top = (Window.getClientHeight() - offsetHeight) / 5;
	            MyPopup.setPopupPosition(left, top);
	          }
	        });

		
		datePicker = new DatePicker();
		datePicker.setValue(new Date());
		datePicker.setYearAndMonthDropdownVisible(true);
		datePicker.setYearArrowsVisible(true);
		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
                calendar.setDate(event.getValue());
            }
        });
		
		MyPopup.setWidget(datePicker);

		
		VerticalPanel vPanelDatePicker = new VerticalPanel();
		vPanelDatePicker.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		vPanelDatePicker.add(MyPopup);
		vPanelDatePicker.setBorderWidth(0);
		vPanelDatePicker.setSize("100%", Integer.toString(TelaInicialPaisAgenda.intHeightTable+80)+"px");

		
		VerticalPanel vPanelEditGrid = new VerticalPanel();		
		vPanelEditGrid.setHeight(Integer.toString(TelaInicialPaisAgenda.intHeightTable+100)+"px");
		vPanelEditGrid.add(gridComboBox);
		vPanelEditGrid.add(calendar);		
		

		super.add(vPanelEditGrid);

	}

	public static GWTServiceCursoAsync getServiceCursoAsync() {
		return GWT.create(GWTServiceCurso.class);
	}
	
	
	protected void populateGrid() {
		mpPanelLoading.setVisible(true);	
		String locale = LocaleInfo.getCurrentLocale().getLocaleName();
		int idCurso = Integer.parseInt(listBoxCurso.getValue(listBoxCurso.getSelectedIndex()));
		GWTServiceAvaliacao.Util.getInstance().getAvaliacaoPeloCurso(idCurso,locale,
		
				new AsyncCallback<ArrayList<CursoAvaliacao>>() {

					@Override
					public void onFailure(Throwable caught) {
						mpPanelLoading.setVisible(false);	
						mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
						mpDialogBoxWarning.setBodyText(txtConstants.avaliacaoErroCarregar());
					}

					@Override
					public void onSuccess(ArrayList<CursoAvaliacao> list) {
						mpPanelLoading.setVisible(false);	
						
						calendar.clearAppointments();
						
						
						for(CursoAvaliacao object : list){
							
							
//							Date dataAvaliacao = MpUtilClient.convertStringToDate(object.getDataAvaliacao());
							Date dataAvaliacao = object.getDataAvaliacao();
							Time horaAvaliacao = MpUtilClient.convertStringToTime(object.getHoraAvaliacao());
							

							Date startDateTime = new Date();						

							startDateTime.setTime(dataAvaliacao.getTime()+horaAvaliacao.getTime());
							
							long oneHour = 1*60*60*1000;
							Date endDateTime = new Date(startDateTime.getTime()+oneHour);							

							
							Appointment appt = new Appointment();
							appt.setStart(startDateTime);
							appt.setEnd(endDateTime);
							appt.setReadOnly(true);

							
							appt.setDescription(object.getNomeAvaliacao());
							String strDescription="";

							if(object.getIdTipoAvaliacao()==CursoAvaliacao.INT_TRABALHO_GRUPO || object.getIdTipoAvaliacao()==CursoAvaliacao.INT_TRABALHO_INDIVIDUAL){
								strDescription = txtConstants.agendaTrabalho()+" "+object.getNomeDisciplina();
								appt.setStyle(AppointmentStyle.YELLOW);	
							}else if(object.getIdTipoAvaliacao()==CursoAvaliacao.INT_PROVA_INDIVIDUAL || object.getIdTipoAvaliacao()==CursoAvaliacao.INT_PROVA_GRUPO){
								strDescription = txtConstants.agendaProva()+" "+object.getNomeDisciplina();
								appt.setStyle(AppointmentStyle.RED);	
							}else if(object.getIdTipoAvaliacao()==CursoAvaliacao.INT_EXERCICIO_FIXACAO){
								strDescription = txtConstants.agendaExercicio()+" "+object.getNomeDisciplina();
								appt.setStyle(AppointmentStyle.BLUE);	
							}
							appt.setTitle(strDescription);
							
							
							calendar.addAppointment(appt);
							
						}
												
						

					}
				});
	}	
	/****************Begin Event Handlers*****************/
	
	private class MpCursoSelectionChangeHandler implements ChangeHandler {
		public void onChange(ChangeEvent event) {
				populateGrid();			
		}	  
	}
	
	private class ImgClickChandler implements ClickHandler{
		public void onClick(ClickEvent event){
			new ElementFader().fade(MyPopup.getElement(), 0, 1, 500);
			MyPopup.show();
		}
	}
	
	private class ClickHandlerDay implements ClickHandler{
		public void onClick(ClickEvent event){
			calendar.setView(CalendarViews.DAY);
			calendar.setDays(1);
		}
	}	
	
	private class ClickHandlerWeek implements ClickHandler{
		public void onClick(ClickEvent event){
			calendar.setView(CalendarViews.DAY);
			calendar.setDays(7);
		}
	}
	
	private class ClickHandlerMonth implements ClickHandler{
		public void onClick(ClickEvent event){
			calendar.setView(CalendarViews.MONTH);
		}
	}
	
	
	/****************End Event Handlers*****************/
	

	public void updateClientData(){
		listBoxCurso.populateComboBox(this.telaInicialPaisAgenda.getMainView().getUsuarioLogado());
	}
	

	
}