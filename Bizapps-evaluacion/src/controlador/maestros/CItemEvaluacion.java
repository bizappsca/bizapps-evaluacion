package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.ItemEvaluacion;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CItemEvaluacion extends CGenerico {

	@Wire
	private Window wdwVItemEvaluacion;
	@Wire
	private Div botoneraItemEvaluacion;
	@Wire
	private Groupbox gpxRegistroItemEvaluacion;
	@Wire
	private Textbox txtDescripcionItemEvaluacion;
	@Wire
	private Combobox cmbPonderacionItemEvaluacion;
	@Wire
	private Groupbox gpxDatosItemEvaluacion;
	@Wire
	private Div catalogoItemEvaluacion;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idItemEvaluacion = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<ItemEvaluacion> catalogo;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, Object> mapa = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (mapa != null) {
			if (mapa.get("tabsGenerales") != null) {
				tabs = (List<Tab>) mapa.get("tabsGenerales");
				mapa.clear();
				mapa = null;
			}
		}
		txtDescripcionItemEvaluacion.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						ItemEvaluacion item = catalogo
								.objetoSeleccionadoDelCatalogo();
						idItemEvaluacion = item.getId();
						txtDescripcionItemEvaluacion.setValue(item
								.getDescripcion());
						cmbPonderacionItemEvaluacion.setValue(item
								.getPonderacion());
						txtDescripcionItemEvaluacion.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}

			}

			@Override
			public void guardar() {
				// TODO Auto-generated method stub

				boolean guardar = true;
				guardar = validar();
				if (guardar) {
					String descripcion = txtDescripcionItemEvaluacion
							.getValue();
					String tipo = cmbPonderacionItemEvaluacion.getValue();
					String usuario = nombreUsuarioSesion();
					Timestamp fechaAuditoria = new Timestamp(
							new Date().getTime());
					ItemEvaluacion item = new ItemEvaluacion(idItemEvaluacion,
							descripcion, tipo, fechaAuditoria, horaAuditoria,
							usuario);
					servicioItemEvaluacion.guardar(item);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(servicioItemEvaluacion
							.buscarTodos());
					abrirCatalogo();
				}

			}

			@Override
			public void limpiar() {
				// TODO Auto-generated method stub
				mostrarBotones(false);
				limpiarCampos();
			}

			@Override
			public void salir() {
				// TODO Auto-generated method stub
				cerrarVentana(wdwVItemEvaluacion, "Item de Evaluacion",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosItemEvaluacion.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<ItemEvaluacion> eliminarLista = catalogo
								.obtenerSeleccionados();
						Messagebox
								.show("¿Desea Eliminar los "
										+ eliminarLista.size() + " Registros?",
										"Alerta",
										Messagebox.OK | Messagebox.CANCEL,
										Messagebox.QUESTION,
										new org.zkoss.zk.ui.event.EventListener<Event>() {
											public void onEvent(Event evt)
													throws InterruptedException {
												if (evt.getName()
														.equals("onOK")) {
													servicioItemEvaluacion
															.eliminarVariosItems(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioItemEvaluacion
															.buscarTodos());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idItemEvaluacion != 0) {
						Messagebox
								.show(Mensaje.deseaEliminar,
										"Alerta",
										Messagebox.OK | Messagebox.CANCEL,
										Messagebox.QUESTION,
										new org.zkoss.zk.ui.event.EventListener<Event>() {
											public void onEvent(Event evt)
													throws InterruptedException {
												if (evt.getName()
														.equals("onOK")) {
													servicioItemEvaluacion
															.eliminarUnItem(idItemEvaluacion);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioItemEvaluacion
															.buscarTodos());
													abrirCatalogo();
												}
											}
										});
					} else
						msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);
				}

			}

		};
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(3).setVisible(false);
		botoneraItemEvaluacion.appendChild(botonera);

	}

	public void limpiarCampos() {
		idItemEvaluacion = 0;
		txtDescripcionItemEvaluacion.setValue("");
		cmbPonderacionItemEvaluacion.setValue("");
		catalogo.limpiarSeleccion();
		txtDescripcionItemEvaluacion.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtDescripcionItemEvaluacion.getText().compareTo("") != 0
				|| cmbPonderacionItemEvaluacion.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroItemEvaluacion")
	public void abrirRegistro() {
		gpxDatosItemEvaluacion.setOpen(false);
		gpxRegistroItemEvaluacion.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosItemEvaluacion")
	public void abrirCatalogo() {
		gpxDatosItemEvaluacion.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosItemEvaluacion.setOpen(false);
								gpxRegistroItemEvaluacion.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosItemEvaluacion.setOpen(true);
									gpxRegistroItemEvaluacion.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosItemEvaluacion.setOpen(true);
			gpxRegistroItemEvaluacion.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<ItemEvaluacion> seleccionados = catalogo.obtenerSeleccionados();
		if (seleccionados == null) {
			msj.mensajeAlerta(Mensaje.noHayRegistros);
			return false;
		} else {
			if (seleccionados.isEmpty()) {
				msj.mensajeAlerta(Mensaje.noSeleccionoItem);
				return false;
			} else {
				return true;
			}
		}
	}

	public boolean camposLLenos() {
		if (txtDescripcionItemEvaluacion.getText().compareTo("") == 0
				|| cmbPonderacionItemEvaluacion.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	protected boolean validar() {

		if (!camposLLenos()) {
			msj.mensajeAlerta(Mensaje.camposVacios);
			return false;
		} else
			return true;

	}

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(3).setVisible(!bol);

	}

	public void mostrarCatalogo() {

		final List<ItemEvaluacion> listItemEvaluacion = servicioItemEvaluacion
				.buscarTodos();
		catalogo = new Catalogo<ItemEvaluacion>(catalogoItemEvaluacion,
				"Catalogo de Items de Evaluación", listItemEvaluacion,false,false,false,
				"Descripción", "Tipo de Ponderación") {

			@Override
			protected List<ItemEvaluacion> buscar(List<String> valores) {
				List<ItemEvaluacion> lista = new ArrayList<ItemEvaluacion>();

				for (ItemEvaluacion item : listItemEvaluacion) {
					if (item.getDescripcion().toLowerCase()
							.startsWith(valores.get(0))
							&& item.getPonderacion().toLowerCase()
									.startsWith(valores.get(1))) {
						lista.add(item);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(ItemEvaluacion item) {
				String[] registros = new String[2];
				registros[0] = item.getDescripcion();
				registros[1] = item.getPonderacion();

				return registros;
			}
		};
		catalogo.setParent(catalogoItemEvaluacion);

	}

}
