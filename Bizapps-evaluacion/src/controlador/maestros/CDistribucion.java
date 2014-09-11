package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Distribucion;

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

public class CDistribucion extends CGenerico {

	@Wire
	private Window wdwVDistribucion;
	@Wire
	private Div botoneraDistribucion;
	@Wire
	private Groupbox gpxRegistroDistribucion;
	@Wire
	private Textbox txtDescripcionDistribucion;
	@Wire
	private Spinner spnPorcentajeDistribucion;
	@Wire
	private Groupbox gpxDatosDistribucion;
	@Wire
	private Div catalogoDistribucion;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idDistribucion = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Distribucion> catalogo;

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
		txtDescripcionDistribucion.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Distribucion distribucion = catalogo
								.objetoSeleccionadoDelCatalogo();
						idDistribucion = distribucion.getId();
						txtDescripcionDistribucion.setValue(distribucion
								.getDescripcion());
						spnPorcentajeDistribucion.setValue(distribucion
								.getPorcentaje());
						txtDescripcionDistribucion.setFocus(true);
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
					String descripcion = txtDescripcionDistribucion.getValue();
					int porcentaje = spnPorcentajeDistribucion.getValue();
					String usuario = nombreUsuarioSesion();
					Timestamp fechaAuditoria = new Timestamp(
							new Date().getTime());
					Distribucion distribucion = new Distribucion(
							idDistribucion, descripcion, usuario,
							fechaAuditoria, horaAuditoria, porcentaje);
					servicioDistribucion.guardar(distribucion);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(servicioDistribucion.buscarTodas());
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
				cerrarVentana(wdwVDistribucion, "Distribucion",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosDistribucion.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Distribucion> eliminarLista = catalogo
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
													servicioDistribucion
															.eliminarVariasDistribuciones(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioDistribucion
															.buscarTodas());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idDistribucion != 0) {
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
													servicioDistribucion
															.eliminarUnaDistribucion(idDistribucion);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioDistribucion
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
		botoneraDistribucion.appendChild(botonera);

	}

	public void limpiarCampos() {
		idDistribucion = 0;
		txtDescripcionDistribucion.setValue("");
		spnPorcentajeDistribucion.setValue(null);
		catalogo.limpiarSeleccion();
		txtDescripcionDistribucion.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtDescripcionDistribucion.getText().compareTo("") != 0
				|| spnPorcentajeDistribucion.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroDistribucion")
	public void abrirRegistro() {
		gpxDatosDistribucion.setOpen(false);
		gpxRegistroDistribucion.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosDistribucion")
	public void abrirCatalogo() {
		gpxDatosDistribucion.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosDistribucion.setOpen(false);
								gpxRegistroDistribucion.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosDistribucion.setOpen(true);
									gpxRegistroDistribucion.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosDistribucion.setOpen(true);
			gpxRegistroDistribucion.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Distribucion> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtDescripcionDistribucion.getText().compareTo("") == 0
				|| spnPorcentajeDistribucion.getText().compareTo("") == 0) {
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

		final List<Distribucion> listDistribucion = servicioDistribucion
				.buscarTodas();
		catalogo = new Catalogo<Distribucion>(catalogoDistribucion,
				"Catalogo de Distribuciones", listDistribucion, false,false,false,"Descripción",
				"Porcentaje") {

			@Override
			protected List<Distribucion> buscar(List<String> valores) {
				List<Distribucion> lista = new ArrayList<Distribucion>();

				for (Distribucion distribucion : listDistribucion) {
					if (distribucion.getDescripcion().toLowerCase()
							.startsWith(valores.get(0))
							&& String.valueOf(distribucion.getPorcentaje())
									.toLowerCase().startsWith(valores.get(1))) {
						lista.add(distribucion);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Distribucion distribucion) {
				String[] registros = new String[2];
				registros[0] = distribucion.getDescripcion();
				registros[1] = String.valueOf(distribucion.getPorcentaje());

				return registros;
			}


		};
		catalogo.setParent(catalogoDistribucion);

	}

}
