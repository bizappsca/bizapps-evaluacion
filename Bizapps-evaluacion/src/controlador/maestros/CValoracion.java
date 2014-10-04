package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Valoracion;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CValoracion extends CGenerico {

	@Wire
	private Window wdwVValoracion;
	@Wire
	private Div botoneraValoracion;
	@Wire
	private Groupbox gpxRegistroValoracion;
	@Wire
	private Textbox txtNombreValoracion;
	@Wire
	private Textbox txtDescripcionValoracion;
	@Wire
	private Spinner spnOrdenValoracion;
	@Wire
	private Spinner spnRangoInferiorValoracion;
	@Wire
	private Spinner spnRangoSuperiorValoracion;
	@Wire
	private Spinner spnValorValoracion;
	@Wire
	private Groupbox gpxDatosValoracion;
	@Wire
	private Div catalogoValoracion;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idValoracion = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Valoracion> catalogo;

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
		txtNombreValoracion.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Valoracion valoracion = catalogo
								.objetoSeleccionadoDelCatalogo();
						idValoracion = valoracion.getId();
						txtNombreValoracion.setValue(valoracion.getNombre());
						txtDescripcionValoracion.setValue(valoracion
								.getDescripcion());
						spnOrdenValoracion.setValue(valoracion.getOrden());
						spnRangoInferiorValoracion.setValue(valoracion
								.getRangoInferior());
						spnRangoSuperiorValoracion.setValue(valoracion
								.getRangoSuperior());
						spnValorValoracion.setValue(valoracion.getValor());
						txtNombreValoracion.setFocus(true);
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
					String nombre = txtNombreValoracion.getValue();
					String descripcion = txtDescripcionValoracion.getValue();
					int orden = spnOrdenValoracion.getValue();
					int rangoInferior = spnRangoInferiorValoracion.getValue();
					int rangoSuperior = spnRangoSuperiorValoracion.getValue();
					int valor = spnValorValoracion.getValue();
					String usuario = nombreUsuarioSesion();
					Timestamp fechaAuditoria = new Timestamp(
							new Date().getTime());
					Valoracion valoracion = new Valoracion(idValoracion,
							descripcion, fechaAuditoria, horaAuditoria,
							usuario, nombre, orden, rangoInferior,
							rangoSuperior, valor);
					servicioValoracion.guardar(valoracion);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(servicioValoracion.buscarTodas());
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
				cerrarVentana(wdwVValoracion, "Valoracion",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosValoracion.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Valoracion> eliminarLista = catalogo
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
													servicioValoracion
															.eliminarVariasValoraciones(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioValoracion
															.buscarTodas());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idValoracion != 0) {
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
													servicioValoracion
															.eliminarUnaValoracion(idValoracion);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioValoracion
															.buscarTodas());
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
		botoneraValoracion.appendChild(botonera);

	}

	public void limpiarCampos() {
		idValoracion = 0;
		txtNombreValoracion.setValue("");
		txtDescripcionValoracion.setValue("");
		spnOrdenValoracion.setValue(null);
		spnRangoInferiorValoracion.setValue(null);
		spnRangoSuperiorValoracion.setValue(null);
		spnValorValoracion.setValue(null);
		catalogo.limpiarSeleccion();
		txtNombreValoracion.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtNombreValoracion.getText().compareTo("") != 0
				|| txtDescripcionValoracion.getText().compareTo("") != 0
				|| spnOrdenValoracion.getText().compareTo("") != 0
				|| spnRangoInferiorValoracion.getText().compareTo("") != 0
				|| spnRangoSuperiorValoracion.getText().compareTo("") != 0
				|| spnValorValoracion.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroValoracion")
	public void abrirRegistro() {
		gpxDatosValoracion.setOpen(false);
		gpxRegistroValoracion.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosValoracion")
	public void abrirCatalogo() {
		gpxDatosValoracion.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosValoracion.setOpen(false);
								gpxRegistroValoracion.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosValoracion.setOpen(true);
									gpxRegistroValoracion.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosValoracion.setOpen(true);
			gpxRegistroValoracion.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Valoracion> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtNombreValoracion.getText().compareTo("") == 0) {
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

		final List<Valoracion> listValoracion = servicioValoracion
				.buscarTodas();
		catalogo = new Catalogo<Valoracion>(catalogoValoracion,
				"Catalogo de Valoraciones", listValoracion,false,false,false, "Nombre",
				"Descripción", "Orden", "Rango inferior", "Rango superior",
				"Valor") {

			@Override
			protected List<Valoracion> buscar(List<String> valores) {
				List<Valoracion> lista = new ArrayList<Valoracion>();

				for (Valoracion valoracion : listValoracion) {
					if (valoracion.getNombre().toLowerCase()
									.contains(valores.get(0).toLowerCase())
							&& valoracion.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& String.valueOf(valoracion.getOrden())
									.toLowerCase().contains(valores.get(2).toLowerCase())
							&& String.valueOf(valoracion.getRangoInferior())
									.toLowerCase().contains(valores.get(3).toLowerCase())
							&& String.valueOf(valoracion.getRangoSuperior())
									.toLowerCase().contains(valores.get(4).toLowerCase())
							&& String.valueOf(valoracion.getValor())
									.toLowerCase().contains(valores.get(5).toLowerCase())) {
						lista.add(valoracion);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Valoracion valoracion) {
				String[] registros = new String[6];
				registros[0] = valoracion.getNombre();
				registros[1] = valoracion.getDescripcion();
				registros[2] = String.valueOf(valoracion.getOrden());
				registros[3] = String.valueOf(valoracion.getRangoInferior());
				registros[4] = String.valueOf(valoracion.getRangoSuperior());
				registros[5] = String.valueOf(valoracion.getValor());

				return registros;
			}

		};
		catalogo.setParent(catalogoValoracion);

	}

}