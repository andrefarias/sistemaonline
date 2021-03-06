
package com.jornada.client.ambiente.coordenador.curso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.jornada.client.classes.listBoxes.ambiente.coordenador.MpListBoxMediaNota;
import com.jornada.client.classes.listBoxes.ambiente.coordenador.MpListBoxPorcentagemPresenca;
import com.jornada.client.classes.listBoxes.ambiente.coordenador.curso.MpListBoxEnsino;
import com.jornada.client.classes.listBoxes.ambiente.coordenador.curso.MpListBoxStatusCurso;
import com.jornada.client.classes.resources.CellTableStyle;
import com.jornada.client.classes.widgets.button.MpImageButton;
import com.jornada.client.classes.widgets.cells.MpSimplePager;
import com.jornada.client.classes.widgets.cells.MpStyledSelectionCell;
import com.jornada.client.classes.widgets.cells.MpTextAreaEditCell;
import com.jornada.client.classes.widgets.dialog.MpConfirmDialogBox;
import com.jornada.client.classes.widgets.dialog.MpDialogBox;
import com.jornada.client.classes.widgets.label.MpLabelRight;
import com.jornada.client.classes.widgets.panel.MpPanelLoading;
import com.jornada.client.classes.widgets.panel.MpSpaceVerticalPanel;
import com.jornada.client.classes.widgets.textbox.MpTextBox;
import com.jornada.client.content.i18n.TextConstants;
import com.jornada.client.service.GWTServiceCurso;
import com.jornada.shared.FieldVerifier;
import com.jornada.shared.classes.Curso;
import com.jornada.shared.classes.utility.MpUtilClient;


public class EditarCurso extends VerticalPanel {

//	private AsyncCallback<String> callbackUpdateRow;
//	private AsyncCallback<Boolean> callbackDelete;
	private AsyncCallback<ArrayList<Curso>> callbackGetCursosFiltro;
	
	private ListDataProvider<Curso> dataProvider = new ListDataProvider<Curso>();

	private CellTable<Curso> cellTable;
	private Column<Curso, String> nomeCursoColumn;
	private Column<Curso, String> descricaoColumn;
	private Column<Curso, String> objetivoColumn;
	private Column<Curso, String> mediaNotaColumn;
	private Column<Curso, String> porcetagemPresencaoColumn;
//	private Column<Curso, Date> dataInicialColumn;
//	private Column<Curso, Date> dataFinalColumn;
	private Column<Curso, String> statusColumn;
	private Column<Curso, String> ensinoColumn;
	
	private MpListBoxMediaNota mpListBoxMediaNota;
	private MpListBoxPorcentagemPresenca mpListBoxPorcentagemPresenca;
	private MpListBoxStatusCurso mpListBoxStatus;
	private MpListBoxEnsino listBoxEnsino;
	
	private LinkedHashMap<String, String> listaMediaNota = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> listaPorcentagemPresenca = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> listaStatus = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, String> listaEnsino = new LinkedHashMap<String, String>();

	MpDialogBox mpDialogBoxConfirm = new MpDialogBox();
	MpDialogBox mpDialogBoxWarning = new MpDialogBox();
	private MpPanelLoading mpPanelLoading = new MpPanelLoading("images/radar.gif");

    private static final String IMAGE_DELETE = "images/delete.png";
    private static final String IMAGE_EDIT = "images/comment_edit.png";
    
    MpLabelRight lblStatusCurso;
    MpLabelRight lblNomeCurso;
    MpImageButton btnFiltrar;

	private TelaInicialCurso telaInicialCurso;
	
	TextConstants txtConstants;
	
	private VerticalPanel vPanelEditGrid;
	
	private MpTextBox txtSearch;

	public EditarCurso(TelaInicialCurso telaInicialCurso) {
		
		this.telaInicialCurso = telaInicialCurso;
		
		txtConstants = GWT.create(TextConstants.class);

		mpDialogBoxConfirm.setTYPE_MESSAGE(MpDialogBox.TYPE_CONFIRMATION);
		mpDialogBoxWarning.setTYPE_MESSAGE(MpDialogBox.TYPE_WARNING);
		mpPanelLoading.setTxtLoading(txtConstants.geralCarregando());
		mpPanelLoading.show();
		mpPanelLoading.setVisible(false);		
		
		mpListBoxMediaNota = new MpListBoxMediaNota();
		mpListBoxPorcentagemPresenca= new MpListBoxPorcentagemPresenca();
		
		listBoxEnsino = new MpListBoxEnsino();
		
        mpListBoxStatus = new MpListBoxStatusCurso();
//        mpListBoxStatus.addItem(txtConstants.cursoAtivo(), "true");
//        mpListBoxStatus.addItem(txtConstants.cursoDesativado(), "false");		
        mpListBoxStatus.setSelectItem("true");
        mpListBoxStatus.setWidth("80px");
		
		for(int i=0;i<mpListBoxMediaNota.getItemCount();i++){
			String key = mpListBoxMediaNota.getValue(i);
			String text = mpListBoxMediaNota.getItemText(i);
			listaMediaNota.put(key, text);
		}
		for(int i=0;i<mpListBoxPorcentagemPresenca.getItemCount();i++){
			String key = mpListBoxPorcentagemPresenca.getValue(i);
			String text = mpListBoxPorcentagemPresenca.getItemText(i);
			listaPorcentagemPresenca.put(key, text);
		}
		for(int i=0;i<listBoxEnsino.getItemCount();i++){
		    listaEnsino.put(listBoxEnsino.getValue(i), listBoxEnsino.getItemText(i));
		}
		
		listaStatus.put("true", txtConstants.cursoAtivo());
		listaStatus.put("false", txtConstants.cursoDesativado());
		
		
        lblStatusCurso = new MpLabelRight(txtConstants.cursoStatus());
        lblNomeCurso = new MpLabelRight(txtConstants.cursoNome());
        
        txtSearch = new MpTextBox();
        btnFiltrar = new MpImageButton(txtConstants.geralFiltrar(), "images/magnifier.png");        
        txtSearch.addKeyUpHandler(new EnterKeyUpHandlerFiltrar());
        btnFiltrar.addClickHandler(new ClickHandlerFiltrar());        
        txtSearch.setStyleName("design_text_boxes");      
        txtSearch.setWidth("150px");
        

		vPanelEditGrid = new VerticalPanel();			
		
		cellTable = new CellTable<Curso>(5,GWT.<CellTableStyle> create(CellTableStyle.class));
		cellTable.setWidth("100%");
		cellTable.setAutoHeaderRefreshDisabled(true);
		cellTable.setAutoFooterRefreshDisabled(true);
		
		final SelectionModel<Curso> selectionModel = new MultiSelectionModel<Curso>(Curso.KEY_PROVIDER);
		cellTable.setSelectionModel(selectionModel,DefaultSelectionEventManager.<Curso> createCheckboxManager());
		initTableColumns(selectionModel);
		
		dataProvider.addDataDisplay(cellTable);		
		
		
		MpSimplePager mpPager = new MpSimplePager();
		mpPager.setDisplay(cellTable);
		mpPager.setPageSize(15);	


		FlexTable flexTableFiltrar = new FlexTable();     
        flexTableFiltrar.setCellSpacing(3);
        flexTableFiltrar.setCellPadding(3);
        flexTableFiltrar.setBorderWidth(0);
        
		flexTableFiltrar.setWidget(0, 0, mpPager);
		flexTableFiltrar.setWidget(0, 1, new MpSpaceVerticalPanel());
		flexTableFiltrar.setWidget(0, 2, lblNomeCurso);
		flexTableFiltrar.setWidget(0, 3, txtSearch);
        flexTableFiltrar.setWidget(0, 4, new MpSpaceVerticalPanel());
        flexTableFiltrar.setWidget(0, 5, lblStatusCurso);
        flexTableFiltrar.setWidget(0, 6, mpListBoxStatus);		
		flexTableFiltrar.setWidget(0, 7, btnFiltrar);
		flexTableFiltrar.setWidget(0, 8, mpPanelLoading);	
		
		ScrollPanel scrollPanel = new ScrollPanel();
//		scrollPanel.setSize(Integer.toString(TelaInicialCurso.intWidthTable+20)+"px",Integer.toString(TelaInicialCurso.intHeightTable-90)+"px");
		scrollPanel.setHeight(Integer.toString(TelaInicialCurso.intHeightTable-90)+"px");
		scrollPanel.setAlwaysShowScrollBars(false);		
		scrollPanel.add(cellTable);
		
		
		vPanelEditGrid.add(flexTableFiltrar);
		vPanelEditGrid.add(scrollPanel);
//		vPanelEditGrid.add(cellTable);
		vPanelEditGrid.setWidth("100%");
		
		/************************* Begin Callback's *************************/
		


		/*********************** End Callbacks **********************/

		/******** Begin Populate ********/
		populateGrid();
		/******** End Populate ********/
//		initializeCellTable();

		setWidth("100%");
		super.add(vPanelEditGrid);

	}
	
	
	public void initializeCellTable(){
	    
      
	        
	    cellTable = new CellTable<Curso>(15,GWT.<CellTableStyle> create(CellTableStyle.class));
        cellTable.setWidth("100%");
        cellTable.setAutoHeaderRefreshDisabled(true);
        cellTable.setAutoFooterRefreshDisabled(true);
        
        final SelectionModel<Curso> selectionModel = new MultiSelectionModel<Curso>(Curso.KEY_PROVIDER);
        cellTable.setSelectionModel(selectionModel,DefaultSelectionEventManager.<Curso> createCheckboxManager());
        initTableColumns(selectionModel);
        
        dataProvider.addDataDisplay(cellTable);             
        
        MpSimplePager mpPager = new MpSimplePager();
        mpPager.setDisplay(cellTable);
//        mpPager.setPageSize(15);    
//        flexTableFiltrar.setWidget(0, 0, mpPager);
        

        
        ScrollPanel scrollPanel = new ScrollPanel();
//      scrollPanel.setSize(Integer.toString(TelaInicialCurso.intWidthTable+20)+"px",Integer.toString(TelaInicialCurso.intHeightTable-90)+"px");
        scrollPanel.setHeight(Integer.toString(TelaInicialCurso.intHeightTable-90)+"px");
        scrollPanel.setAlwaysShowScrollBars(false);     
        scrollPanel.add(cellTable);
        
        
        
        vPanelEditGrid.add(scrollPanel);
       vPanelEditGrid.add(cellTable);
        vPanelEditGrid.setWidth("100%");
        
        populateGrid();
        
	}

	private class MyImageCell extends ImageCell {

		@Override
		public Set<String> getConsumedEvents() {
			Set<String> consumedEvents = new HashSet<String>();
			consumedEvents.add("click");
			return consumedEvents;
		}

		@Override
		public void onBrowserEvent(Context context, Element parent,
				String value, NativeEvent event,
				ValueUpdater<String> valueUpdater) {
			switch (DOM.eventGetType((Event) event)) {
			case Event.ONCLICK:
				final Curso curso = (Curso) context.getKey();
				@SuppressWarnings("unused")
				CloseHandler<PopupPanel> closeHandler;
				
                if (value.equals(IMAGE_EDIT)){
                  MpDialogBoxAtualizarCurso.getInstance(telaInicialCurso, curso);
//                  mpAtualizarUsuario.showDialog();
//                    MpDialogBoxAtualizarUsuario.getInstance(telaInicialUsuario, object);
                }else if (value.equals(IMAGE_DELETE)){

                    MpConfirmDialogBox confirmationDialog = new MpConfirmDialogBox(txtConstants.cursoRemoverTitle(), txtConstants.cursoRemoverMsg(curso.getNome()), txtConstants.geralSim(), txtConstants.geralNao(),

                    closeHandler = new CloseHandler<PopupPanel>() {

                        public void onClose(CloseEvent<PopupPanel> event) {

                            MpConfirmDialogBox x = (MpConfirmDialogBox) event.getSource();

                            if (x.primaryActionFired()) {

                                GWTServiceCurso.Util.getInstance().deleteCursoRow(curso.getIdCurso(), new CallbackDelete());

                            }
                        }
                    }

                    );
                    confirmationDialog.paint();
                }
				
				break;

			default:
				Window.alert("Test default");
				break;
			}
		}

	}
	
	

	protected void populateGrid() {
		mpPanelLoading.setVisible(true);	
		
		Boolean status =  Boolean.parseBoolean(mpListBoxStatus.getValue(mpListBoxStatus.getSelectedIndex()));
		GWTServiceCurso.Util.getInstance().getCursos("%" + txtSearch.getText() + "%", status, 
		
				new AsyncCallback<ArrayList<Curso>>() {

					@Override
					public void onFailure(Throwable caught) {
						mpPanelLoading.setVisible(false);	
						mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
						mpDialogBoxWarning.setBodyText(txtConstants.cursoErroCarregar());
					}

					@Override
					public void onSuccess(ArrayList<Curso> list) {
						MpUtilClient.isRefreshRequired(list);
						mpPanelLoading.setVisible(false);	
						cleanCellTable();
						dataProvider.getList().clear();
//						cellTable.setRowCount(0);
						

						for(int i=0;i<list.size();i++){
							dataProvider.getList().add(list.get(i));
						}
	                     
//						sortHandler = new ListHandler<Curso>(dataProvider.getList());
//						cellTable.addColumnSortHandler(sortHandler);	
						//cellTable = createCellTable(dataProvider);
						addCellTableData(dataProvider);
//						cellTable.setPageStart(0);
//						cellTable.redraw();		
						
					}
				});
	}
	
	
	
	private class EnterKeyUpHandlerFiltrar implements KeyUpHandler{
		
		public void onKeyUp(KeyUpEvent event){
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			    
				populateGrid();
//			    initializeCellTable();
				
//				GWTServiceComunicado.Util.getInstance().getComunicados("%" + txtSearch.getText() + "%", callbackGetComunicadosFiltro);
			}
		}
	}	
	
	private class ClickHandlerFiltrar implements ClickHandler {
		public void onClick(ClickEvent event) {
		    
			populateGrid();
//		    initializeCellTable();
			
				//GWTServiceUsuario.Util.getInstance().getUsuarios("%" + txtSearch.getText() + "%", callbackGetUsuariosFiltro);
		}
	}		
	
	public void loadCursos(){
		mpPanelLoading.setVisible(true);	
		boolean status =  Boolean.parseBoolean(mpListBoxStatus.getValue(mpListBoxStatus.getSelectedIndex()));
		GWTServiceCurso.Util.getInstance().getCursos("%" + txtSearch.getText() + "%", status,callbackGetCursosFiltro);		
	}
	
	
	
	private void addCellTableData(ListDataProvider<Curso> dataProvider){

		
		 ListHandler<Curso> sortHandler = new ListHandler<Curso>(dataProvider.getList());
		 
		 cellTable.addColumnSortHandler(sortHandler);	
		 
		 initSortHandler(sortHandler);

		
	
	}	
	
	
	private void initTableColumns(final SelectionModel<Curso> selectionModel){
	
		
		nomeCursoColumn = new Column<Curso, String>(new EditTextCell()) {
			@Override
			public String getValue(Curso object) {
				return object.getNome();
			}
		};
		nomeCursoColumn.setFieldUpdater(new FieldUpdater<Curso, String>() {
			@Override
			public void update(int index, Curso object, String value) {
				// Called when the user changes the value.
				if (FieldVerifier.isValidName(value)) {
					object.setNome(value);
					GWTServiceCurso.Util.getInstance().updateCursoRow(object, new CallbackUpdateRow());
				} else {
					mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
					mpDialogBoxWarning.setBodyText(txtConstants.cursoCampoNomeObrigatorio());
					mpDialogBoxWarning.showDialog();
				}
			}
		});
		
//		cellTable.setWidth("100%", true);
//		cellTable.setColumnWidth(descricaoColumn, 65.0, Unit.PCT);


		MpTextAreaEditCell lbl = new MpTextAreaEditCell(5,40);
		descricaoColumn = new Column<Curso, String>(lbl) {
			@Override
			public String getValue(Curso object) {
				return object.getDescricao();
			}
		};
		descricaoColumn.setFieldUpdater(new FieldUpdater<Curso, String>() {
			@Override
			public void update(int index, Curso object, String value) {
				// Called when the user changes the value.
				object.setDescricao(value);
				GWTServiceCurso.Util.getInstance().updateCursoRow(object, new CallbackUpdateRow());
			}
		});


		objetivoColumn = new Column<Curso, String>(new MpTextAreaEditCell(5,40)) {
			@Override
			public String getValue(Curso object) {
				return object.getEmenta();
			}
		};
		objetivoColumn.setFieldUpdater(new FieldUpdater<Curso, String>() {
			@Override
			public void update(int index, Curso object, String value) {
				// Called when the user changes the value.
				object.setEmenta(value);
				GWTServiceCurso.Util.getInstance().updateCursoRow(object, new CallbackUpdateRow());
			}
		});
		
		MpStyledSelectionCell statusCell = new MpStyledSelectionCell(listaStatus,"design_text_boxes");
        statusColumn = new Column<Curso, String>(statusCell) {
            @Override
            public String getValue(Curso object) {                
                String status = Boolean.toString(object.isStatus());
                return status;
            }
        };
        statusColumn.setFieldUpdater(new FieldUpdater<Curso, String>() {
            @Override
            public void update(int index, Curso object, String value) {
                // Called when the user changes the value.
                object.setStatus(Boolean.parseBoolean(value));
                GWTServiceCurso.Util.getInstance().updateCursoRow(object, new CallbackUpdateRow());
            }
        });		
        
        MpStyledSelectionCell ensinoCell = new MpStyledSelectionCell(listaEnsino,"design_text_boxes");
        ensinoColumn = new Column<Curso, String>(ensinoCell) {
            @Override
            public String getValue(Curso object) {    
//                listBoxEnsino.setSelectItem(object.getEnsino());
                return object.getEnsino();
            }
        };
        ensinoColumn.setFieldUpdater(new FieldUpdater<Curso, String>() {
            @Override
            public void update(int index, Curso object, String value) {
                // Called when the user changes the value.
                object.setEnsino(value);
                GWTServiceCurso.Util.getInstance().updateCursoRow(object, new CallbackUpdateRow());
            }
        }); 
		
		
	    MpStyledSelectionCell mediaNotaCell = new MpStyledSelectionCell(listaMediaNota,"design_text_boxes");
	    mediaNotaColumn = new Column<Curso, String>(mediaNotaCell) {
	      @Override
	      public String getValue(Curso object) {
	        return object.getMediaNota();
	      }
	    };	    
    
	    mediaNotaColumn.setFieldUpdater(new FieldUpdater<Curso, String>() {
			@Override
			public void update(int index, Curso object, String value) {
				// Called when the user changes the value.
				object.setMediaNota(value);
				GWTServiceCurso.Util.getInstance().updateCursoRow(object, new CallbackUpdateRow());
			}
		});
	    
	    MpStyledSelectionCell porcentagemPresencaCell = new MpStyledSelectionCell(listaPorcentagemPresenca,"design_text_boxes");
	    porcetagemPresencaoColumn = new Column<Curso, String>(porcentagemPresencaCell) {
	      @Override
	      public String getValue(Curso object) {
	        return object.getPorcentagemPresenca();
	      }
	    };	    
    
	    porcetagemPresencaoColumn.setFieldUpdater(new FieldUpdater<Curso, String>() {
			@Override
			public void update(int index, Curso object, String value) {
				// Called when the user changes the value.
				object.setPorcentagemPresenca(value);
				GWTServiceCurso.Util.getInstance().updateCursoRow(object, new CallbackUpdateRow());
			}
		});			    

//		dataInicialColumn = new Column<Curso, Date>(new MpDatePickerCell()) {
//			@Override
//			public Date getValue(Curso object) {
//				return object.getDataInicial();
//			}
//		};
//		dataInicialColumn.setFieldUpdater(new FieldUpdater<Curso, Date>() {
//			@Override
//			public void update(int index, Curso object, Date value) {
//				// Called when the user changes the value.
//				object.setDataInicial(value);
//				GWTServiceCurso.Util.getInstance().updateCursoRow(object,callbackUpdateRow);
//			}
//		});

//		dataFinalColumn = new Column<Curso, Date>(new MpDatePickerCell()) {
//			@Override
//			public Date getValue(Curso object) {
//				return object.getDataFinal();
//			}
//		};
//		dataFinalColumn.setFieldUpdater(new FieldUpdater<Curso, Date>() {
//			@Override
//			public void update(int index, Curso curso, Date value) {
//				// Called when the user changes the value.
//				curso.setDataFinal(value);
//				GWTServiceCurso.Util.getInstance().updateCursoRow(curso,callbackUpdateRow);
//			}
//		});
		
		
        Column<Curso, String> editColumn = new Column<Curso, String>(new MyImageCell()) {
            @Override
            public String getValue(Curso object) {
                return IMAGE_EDIT;
            }
        };		

		Column<Curso, String> removeColumn = new Column<Curso, String>(new MyImageCell()) {
			@Override
			public String getValue(Curso object) {
				return IMAGE_DELETE;
			}
		};

		cellTable.addColumn(nomeCursoColumn, txtConstants.cursoNome());
//		cellTable.addColumn(descricaoColumn, txtConstants.cursoDescricao());
//		cellTable.addColumn(objetivoColumn, txtConstants.cursoEmenta());
		cellTable.addColumn(statusColumn, txtConstants.cursoStatus());
		cellTable.addColumn(ensinoColumn, "Ensino");
		cellTable.addColumn(mediaNotaColumn, txtConstants.cursoMediaNota());
		cellTable.addColumn(porcetagemPresencaoColumn, txtConstants.cursoPorcentagemPresenca());
//		cellTable.addColumn(dataInicialColumn, txtConstants.cursoDataInicial());
//		cellTable.addColumn(dataFinalColumn, txtConstants.cursoDataFinal());
		cellTable.addColumn(editColumn, txtConstants.geralEditar());
		cellTable.addColumn(removeColumn, txtConstants.geralRemover());
		

		cellTable.getColumn(cellTable.getColumnIndex(nomeCursoColumn)).setCellStyleNames("edit-cell");
//		cellTable.getColumn(cellTable.getColumnIndex(descricaoColumn)).setCellStyleNames("edit-cell");
//		cellTable.getColumn(cellTable.getColumnIndex(objetivoColumn)).setCellStyleNames("edit-cell");
		cellTable.getColumn(cellTable.getColumnIndex(editColumn)).setCellStyleNames("hand-over");
		cellTable.getColumn(cellTable.getColumnIndex(removeColumn)).setCellStyleNames("hand-over");
	}

	public void initSortHandler(ListHandler<Curso> sortHandler){
		
		nomeCursoColumn.setSortable(true);
	    sortHandler.setComparator(nomeCursoColumn, new Comparator<Curso>() {
	      @Override
	      public int compare(Curso o1, Curso o2) {
	        return o1.getNome().compareTo(o2.getNome());
	      }
	    });	
	    
	    descricaoColumn.setSortable(true);
	    sortHandler.setComparator(descricaoColumn, new Comparator<Curso>() {
	      @Override
	      public int compare(Curso o1, Curso o2) {
	        return o1.getDescricao().compareTo(o2.getDescricao());
	      }
	    });	
	    
	    objetivoColumn.setSortable(true);
	    sortHandler.setComparator(objetivoColumn, new Comparator<Curso>() {
	      @Override
	      public int compare(Curso o1, Curso o2) {
	        return o1.getEmenta().compareTo(o2.getEmenta());
	      }
	    });
	    
        statusColumn.setSortable(true);
        sortHandler.setComparator(statusColumn, new Comparator<Curso>() {
            @Override
            public int compare(Curso o1, Curso o2) {
                String boo1 = Boolean.toString(o1.isStatus());
                String boo2 = Boolean.toString(o2.isStatus());
                return boo1.compareTo(boo2);
            }
        });
	    
	    mediaNotaColumn.setSortable(true);
	    sortHandler.setComparator(mediaNotaColumn, new Comparator<Curso>() {
	      @Override
	      public int compare(Curso o1, Curso o2) {
	        return o1.getMediaNota().compareTo(o2.getMediaNota());
	      }
	    });		    
	    
	    porcetagemPresencaoColumn.setSortable(true);
	    sortHandler.setComparator(porcetagemPresencaoColumn, new Comparator<Curso>() {
	      @Override
	      public int compare(Curso o1, Curso o2) {
	        return o1.getPorcentagemPresenca().compareTo(o2.getPorcentagemPresenca());
	      }
	    });	    
	    
	    ensinoColumn.setSortable(true);
	    sortHandler.setComparator(ensinoColumn, new Comparator<Curso>() {
	      @Override
	      public int compare(Curso o1, Curso o2) {
	        return o1.getEnsino().compareTo(o2.getEnsino());
	      }
	    });	  
//	    
//	    dataFinalColumn.setSortable(true);
//	    sortHandler.setComparator(dataFinalColumn, new Comparator<Curso>() {
//	      @Override
//	      public int compare(Curso o1, Curso o2) {
//	        return o1.getDataFinal().compareTo(o2.getDataFinal());
//	      }
//	    });	 	    	    
	}
	
	
    private class CallbackUpdateRow implements AsyncCallback<String> {

        public void onSuccess(String result) {
            mpPanelLoading.setVisible(false);
            if (result.equals("true")) {
                EditarCurso.this.telaInicialCurso.updateAssociarCurso();
                EditarCurso.this.telaInicialCurso.updateAdicionarCurso();
            } else if (result.contains(Curso.DB_UNIQUE_KEY)) {
                String strCurso = result.substring(result.indexOf("=(") + 2);
                strCurso = strCurso.substring(strCurso.indexOf(",") + 1);
                strCurso = strCurso.substring(0, strCurso.indexOf(")"));
                mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
                mpDialogBoxWarning.setBodyText(txtConstants.cursoErroAtualizar() + " " + txtConstants.cursoDuplicado((strCurso)));
                mpDialogBoxWarning.showDialog();
            } else {
                mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
                mpDialogBoxWarning.setBodyText(txtConstants.cursoErroAtualizar() + " " + txtConstants.geralRegarregarPagina());
                mpDialogBoxWarning.showDialog();
            }

        }

        public void onFailure(Throwable caught) {
            mpPanelLoading.setVisible(false);
            mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
            mpDialogBoxWarning.setBodyText(txtConstants.cursoErroAtualizar());
            mpDialogBoxWarning.showDialog();
        }
    }

    private class CallbackDelete implements AsyncCallback<Boolean> {
        public void onSuccess(Boolean success) {

            if (success == true) {
                populateGrid();
                EditarCurso.this.telaInicialCurso.updateAssociarCurso();
                EditarCurso.this.telaInicialCurso.updateAdicionarCurso();
                // SC.say("Periodo removido com sucesso.");
            } else {
                mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
                mpDialogBoxWarning.setBodyText(txtConstants.cursoErroRemover());
                mpDialogBoxWarning.showDialog();
            }

        }

        public void onFailure(Throwable caught) {
            mpDialogBoxWarning.setTitle(txtConstants.geralAviso());
            mpDialogBoxWarning.setBodyText(txtConstants.cursoErroRemover());
            mpDialogBoxWarning.showDialog();

        }
    }
    
    protected void cleanCellTable() {
        cellTable.setPageStart(0);
        cellTable.redraw();
        dataProvider.refresh(); 
    }
	
}
