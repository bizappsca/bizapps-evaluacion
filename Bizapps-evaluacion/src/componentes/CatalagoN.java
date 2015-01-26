package componentes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SpinnerDateModel;

import modelo.maestros.Evaluacion;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Space;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public abstract class CatalagoN<Clase> extends Window {

	private static final long serialVersionUID = 1L;
	Listbox lsbCatalagoN;
	Button exportador;
	Button pagineo;
	Mensaje msj = new Mensaje();
	Textbox txtSY;
	Label labelSYNombre;
	Textbox txtRT;
	Label labelRTNombre;
	Label labelBuscado;

	private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
			"/META-INF/ConfiguracionAplicacion.xml");

	public CatalagoN(final Component cGenerico, String titulo,
			List<Clase> lista, boolean emergente, boolean udc, boolean textbox,
			String... campos) {
		super("", "2", false);
		this.setId("cmpCatalagoN" + titulo);
		this.setStyle("background-header:#FF7925; background: #f4f2f2");
		// this.setWidth("auto");
		crearLista(lista, campos, emergente, udc, textbox);
		lsbCatalagoN.addEventListener(Events.ON_SELECT,
				new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						Events.postEvent(cGenerico, new Event("onSeleccion"));
					}
				});
	}

	public void crearLista(List<Clase> lista, String[] campos,
			final boolean emergente, boolean udc, boolean textbox) {
		exportador = new Button();
		exportador.setTooltiptext("Exportar los Datos como un Archivo");
		exportador.setStyle("width: 28px; height: 28px");
		exportador.setSrc("/public/imagenes/botones/exportar.png");
		// ; float: right
		exportador.addEventListener(Events.ON_CLICK,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						exportar();
					}
				});
		pagineo = new Button();
		pagineo.setTooltiptext("Presione para mostrar todos los registros en una sola lista, sin pagineo");
		pagineo.setStyle("width: 28px; height: 28px");
		pagineo.setSrc("/public/imagenes/botones/pagineo.png");
		pagineo.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				pagineo();
			}
		});
		Hbox box = new Hbox();
		final Separator separador1 = new Separator();
		final Separator separador2 = new Separator();
		lsbCatalagoN = new Listbox();
		lsbCatalagoN.setMold("paging");
		lsbCatalagoN.setPagingPosition("top");
		lsbCatalagoN.setPageSize(10);
		
		final Auxhead cabecera = new Auxhead();
		Listhead lhdEncabezado = new Listhead();
		lhdEncabezado.setSizable(true);
		for (int i = 0; i < campos.length; i++) {
			final Textbox cajaTexto = new Textbox();
			cajaTexto.setContext(campos[i]);
			cajaTexto.setHflex("1");
			cajaTexto.setWidth("auto");
			cajaTexto.addEventListener(Events.ON_OK,
					new EventListener<KeyEvent>() {
						@Override
						public void onEvent(KeyEvent e) throws Exception {
							List<String> valores = new ArrayList<>();
							for (int i = 0; i < cabecera.getChildren().size(); i++) {
								Auxheader cabeceraFila = (Auxheader) cabecera
										.getChildren().get(i);
								Textbox te = (Textbox) cabeceraFila
										.getChildren().get(0);
								valores.add(te.getValue());
							}
							;
							String valor = cajaTexto.getValue();
							List<Clase> listaNueva = buscar(valores);
							lsbCatalagoN.setModel(new ListModelList<Clase>(
									listaNueva));
							if (!emergente) {
								lsbCatalagoN.setMultiple(false);
								lsbCatalagoN.setCheckmark(false);
								lsbCatalagoN.setMultiple(true);
								lsbCatalagoN.setCheckmark(true);
							}
							cajaTexto.setValue(valor);
						}
					});
			cajaTexto.setPlaceholder("Buscar....");
			cajaTexto
					.setTooltiptext("Presione Enter para Filtrar la Informacion");
			Auxheader cabeceraFila = new Auxheader();
			cabeceraFila.appendChild(cajaTexto);
			cabecera.appendChild(cabeceraFila);
			Listheader listheader = new Listheader(campos[i]);
			lhdEncabezado.appendChild(listheader);
		}
		lsbCatalagoN.appendChild(cabecera);
		lsbCatalagoN.appendChild(lhdEncabezado);
		lsbCatalagoN.setSizedByContent(true);
		lsbCatalagoN.setSpan("true");
		cabecera.setVisible(true);
		lhdEncabezado.setVisible(true);
		lsbCatalagoN.setModel(new ListModelList<Clase>(lista));
		lsbCatalagoN.setItemRenderer(new ListitemRenderer<Clase>() {

			@Override
            public void render(Listitem fila, Clase objeto, int arg2)
                            throws Exception {
                    fila.setValue(objeto);
                   Doublespinner textbox = new Doublespinner();
                    String[] registros = crearRegistros(objeto);
                    for (int i = 0; i < registros.length; i++) {
                            Listcell celda = new Listcell();
                            if(i==registros.length-1){
                            	textbox.setValue(Double.valueOf(registros[i]));
                            }
                            else 
                                celda.setLabel(registros[i]);
                            celda.appendChild(textbox);        
                            celda.setParent(fila);
                    }
            }

		});

		if (emergente) {
			this.setClosable(true);
			this.setWidth("80%");
			this.setTitle("Registros");
			if (udc) {
				Div div = new Div();
				Hbox vbox1 = new Hbox();
				vbox1.setWidth("100%");
				vbox1.setHeight("12px");
				vbox1.setAlign("start");
				vbox1.setPack("start");
				labelSYNombre = new Label("Cd producto: ");
				labelSYNombre.setClass("etiqueta");
				vbox1.appendChild(labelSYNombre);
				txtSY = new Textbox();
				txtSY.setDisabled(true);
				Space space = new Space();
				space.setWidth("9px");
				vbox1.appendChild(space);
				vbox1.appendChild(txtSY);
				vbox1.appendChild(new Label());
				Hbox vbox2 = new Hbox();
				vbox2.setWidth("100%");
				vbox2.setHeight("12px");
				labelRTNombre = new Label("Cd def Usuario: ");
				labelRTNombre.setClass("etiqueta");
				vbox2.appendChild(labelRTNombre);
				txtRT = new Textbox();
				txtRT.setDisabled(true);
				vbox2.appendChild(txtRT);
				labelBuscado = new Label();
				vbox2.appendChild(labelBuscado);
				vbox2.setAlign("center");
				vbox2.setPack("center");
				Vbox cajaVertical = new Vbox();
				cajaVertical.setWidth("100%");
				cajaVertical.setAlign("center");
				cajaVertical.setPack("center");
				cajaVertical.appendChild(vbox1);
				cajaVertical.appendChild(vbox2);
				div.appendChild(cajaVertical);

				this.appendChild(div);
			}

			this.appendChild(separador2);
			this.appendChild(lsbCatalagoN);
			// this.exportador.setVisible(false);
			// this.pagineo.setVisible(false);
			lsbCatalagoN.setMultiple(true);
			lsbCatalagoN.setCheckmark(true);
			lsbCatalagoN.setMultiple(false);
			lsbCatalagoN.setCheckmark(false);

		} else if (textbox) {
			Space espacio = new Space();
			espacio.setHeight("10px");
			espacio.setStyle("background:white");
			box.appendChild(espacio);
			box.setStyle("background:white");
			box.appendChild(exportador);
			box.appendChild(pagineo);
			box.setWidth("100%");
			box.setAlign("end");
			box.setHeight("10px");
			box.setWidths("96%,2%,2%");
			this.setWidth("auto");
			this.setClosable(false);
			this.appendChild(separador1);
			this.appendChild(box);
			this.appendChild(separador2);
			this.appendChild(lsbCatalagoN);
			lsbCatalagoN.setMultiple(false);
			lsbCatalagoN.setCheckmark(false);
			lsbCatalagoN.setMultiple(true);
			lsbCatalagoN.setCheckmark(true);
		} else {
			Space espacio = new Space();
			espacio.setHeight("10px");
			espacio.setStyle("background:white");
			box.appendChild(espacio);
			box.setStyle("background:white");
			box.appendChild(exportador);
			box.appendChild(pagineo);
			box.setWidth("100%");
			box.setAlign("end");
			box.setHeight("10px");
			box.setWidths("96%,2%,2%");
			this.setWidth("auto");
			this.setClosable(false);
			this.appendChild(separador1);
			this.appendChild(box);
			this.appendChild(separador2);
			this.appendChild(lsbCatalagoN);
			lsbCatalagoN.setMultiple(false);
			lsbCatalagoN.setCheckmark(false);
			lsbCatalagoN.setMultiple(true);
			lsbCatalagoN.setCheckmark(true);
		}
	}

	protected void pagineo() {
		try{
		if (lsbCatalagoN.getPagingPosition().equals("top")) {
			lsbCatalagoN.setMold("default");
			lsbCatalagoN.setPagingPosition("both");
			lsbCatalagoN.setPageSize(10);
			pagineo.setTooltiptext("Presione para mostrar la lista con pagineo");
//			lsbCatalagoN.setHeight("10px");
//			lsbCatalagoN.setStyle("height:50px");
//			lsbCatalagoN.setVflex(true);
//			lsbCatalagoN.setHflex("1");
//			lsbCatalagoN.setStyle("overflow:auto !important; data-scrollable:true");
//			lsbCatalagoN.setSclass("vertical-scroll !important; data-scrollable:true");
//			lsbCatalagoN.setAttribute("org.zkoss.zul.listbox.initRodSize", "10");
//			lsbCatalagoN.setAttribute("org.zkoss.zul.listbox.rod", "true");
//			lsbCatalagoN.setAttribute("org.zkoss.zul.client.rod", "true");

			
		} else {
			lsbCatalagoN.setMold("paging");
			lsbCatalagoN.setPagingPosition("top");
			lsbCatalagoN.setPageSize(10);
			pagineo.setTooltiptext("Presione para mostrar todos los registros en una sola lista, sin pagineo");
//			lsbCatalagoN.setHeight("10px");
//			lsbCatalagoN.setStyle("height:50px");
//			lsbCatalagoN.setVflex(true);
//			lsbCatalagoN.setHflex("1");
//			lsbCatalagoN.setStyle("overflow:auto !important; data-scrollable:true");
//			lsbCatalagoN.setSclass("vertical-scroll !important; data-scrollable:true");
//			lsbCatalagoN.setAttribute("org.zkoss.zul.listbox.initRodSize", "10");
//			lsbCatalagoN.setAttribute("org.zkoss.zul.listbox.rod", "true");
//			lsbCatalagoN.setAttribute("org.zkoss.zul.client.rod", "true");
		}
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.toString());
	}
	}
	

	protected void exportar() {
		if (lsbCatalagoN.getItemCount() != 0) {
			String s = ";";
			final StringBuffer sb = new StringBuffer();

			for (Object head : lsbCatalagoN.getHeads()) {
				String h = "";
				if (head instanceof Listhead) {
					for (Object header : ((Listhead) head).getChildren()) {
						h += ((Listheader) header).getLabel() + s;
					}
					sb.append(h + "\n");
				}
			}
			for (Object item : lsbCatalagoN.getItems()) {
				String i = "";
				for (Object cell : ((Listitem) item).getChildren()) {
					i += ((Listcell) cell).getLabel() + s;
				}
				sb.append(i + "\n");
			}
			Messagebox.show(Mensaje.exportar, "Alerta", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								Filedownload.save(sb.toString().getBytes(),
										"text/plain", "datos.csv");
							}
						}
					});
		} else
			msj.mensajeAlerta(Mensaje.noHayRegistros);
	}

	/**
	 * Metodo que permite llamar un servicio dependiendo el controlador que
	 * busque, es decir que haga un filtro dentro del CatalagoN, ayudando asi al
	 * usuario a encontrar el registro buscado con mayor facilidad
	 */
	protected abstract List<Clase> buscar(List<String> valores);

	/**
	 * Metodo que permite por cada controlador indicar cuales son los registros
	 * que quiere mostrar en su CatalagoN, formando una matriz de String
	 */
	protected abstract String[] crearRegistros(Clase objeto);

	public Clase objetoSeleccionadoDelCatalagoN() {
		return lsbCatalagoN.getSelectedItem().getValue();
	}

	public Listbox getListbox() {
		return lsbCatalagoN;
	}

	public void actualizarLista(List<Clase> lista) {
		lsbCatalagoN.setModel(new ListModelList<Clase>(lista));
		lsbCatalagoN.setMultiple(false);
		lsbCatalagoN.setCheckmark(false);
		lsbCatalagoN.setMultiple(true);
		lsbCatalagoN.setCheckmark(true);
	}

	public Double obtenertext(int index){
		
		
		//Listitem listItem = lsbCatalagoN.getSelectedItem();
		Listitem listItem = lsbCatalagoN.getItemAtIndex(index);
		Double aprobado = ((Doublespinner) ((listItem.getChildren().get(listItem.getChildren().size()-1)))
				.getFirstChild()).getValue();
		return aprobado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Clase> obtenerSeleccionados() {
		List<Clase> valores = new ArrayList<Clase>();
		boolean entro = false;
		if (lsbCatalagoN.getItemCount() != 0) {
			final List<Listitem> list1 = lsbCatalagoN.getItems();
			for (int i = 0; i < list1.size(); i++) {
				if (list1.get(i).isSelected()) {
					Clase clase = list1.get(i).getValue();
					
					if (clase instanceof Evaluacion) {
						try
						{
							Listitem listItem = lsbCatalagoN.getItemAtIndex(i);
							Double aprobado = ((Doublespinner) ((listItem.getChildren().get(listItem.getChildren().size()-1)))
									.getFirstChild()).getValue();
							
							Evaluacion evaluacion= (Evaluacion)clase;
							evaluacion.setResultadoFinal(aprobado);
							clase= (Clase)evaluacion;
							
						}
						catch (Exception ex)
						{
							
						}
						
						
					}
					
					
					entro = true;
					valores.add(clase);
				}
			}
			if (!entro) {
				valores.clear();
				return valores;
			}
			return valores;
		} else
			return null;
	}

	/**
	 * Metodo que permite limpiar los items seleccionados en el CatalagoN
	 */
	public void limpiarSeleccion() {

		lsbCatalagoN.clearSelection();

	}

}