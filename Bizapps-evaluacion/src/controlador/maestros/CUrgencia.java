package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Urgencia;

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
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CUrgencia extends CGenerico {

	@Wire
	private Window wdwVUrgencia;
	@Wire
	private Div botoneraUrgencia;
	@Wire
	private Groupbox gpxRegistroUrgencia;
	@Wire
	private Textbox txtDescripcionUrgencia;
	@Wire
	private Groupbox gpxDatosUrgencia;
	@Wire
	private Div catalogoUrgencia;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idUrgencia = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Urgencia> catalogo;

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

		txtDescripcionUrgencia.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Urgencia urgencia = catalogo
								.objetoSeleccionadoDelCatalogo();
						idUrgencia = urgencia.getId();
						txtDescripcionUrgencia.setValue(urgencia
								.getDescripcionUrgencia());
						txtDescripcionUrgencia.setFocus(true);
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
					String descripcionUrgencia = txtDescripcionUrgencia
							.getValue();
					String usuario = nombreUsuarioSesion();
					Timestamp fechaAuditoria = new Timestamp(
							new Date().getTime());
					Urgencia urgencia = new Urgencia(idUrgencia,
							descripcionUrgencia, usuario, fechaAuditoria,
							horaAuditoria);

					servicioUrgencia.guardar(urgencia);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(servicioUrgencia.buscarTodas());
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
				cerrarVentana(wdwVUrgencia, "Urgencia",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosUrgencia.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Urgencia> eliminarLista = catalogo
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
													servicioUrgencia
															.eliminarVariasUrgencias(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioUrgencia
															.buscarTodas());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idUrgencia != 0) {
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
													servicioUrgencia
															.eliminarUnaUrgencia(idUrgencia);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioUrgencia
															.buscarTodas());
													abrirCatalogo();
												}
											}
										});
					} else
						msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);
				}

			}

			@Override
			public void buscar() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void annadir() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void ayuda() {
				// TODO Auto-generated method stub
				
			}

		};
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(3).setVisible(false);
		botoneraUrgencia.appendChild(botonera);

	}

	public void limpiarCampos() {
		idUrgencia = 0;
		txtDescripcionUrgencia.setValue("");
		catalogo.limpiarSeleccion();
		txtDescripcionUrgencia.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtDescripcionUrgencia.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	public boolean camposLLenos() {
		if (txtDescripcionUrgencia.getText().compareTo("") == 0) {
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

	@Listen("onClick = #gpxRegistroUrgencia")
	public void abrirRegistro() {
		gpxDatosUrgencia.setOpen(false);
		gpxRegistroUrgencia.setOpen(true);
		txtDescripcionUrgencia.setFocus(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosUrgencia")
	public void abrirCatalogo() {
		gpxDatosUrgencia.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosUrgencia.setOpen(false);
								gpxRegistroUrgencia.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosUrgencia.setOpen(true);
									gpxRegistroUrgencia.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosUrgencia.setOpen(true);
			gpxRegistroUrgencia.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Urgencia> seleccionados = catalogo.obtenerSeleccionados();
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

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(3).setVisible(!bol);

	}

	public void mostrarCatalogo() {

		final List<Urgencia> listUrgencia = servicioUrgencia.buscarTodas();
		catalogo = new Catalogo<Urgencia>(catalogoUrgencia,
				"Catalogo de Urgencias", listUrgencia,false,false,false, "Descripción") {

			@Override
			protected List<Urgencia> buscar(List<String> valores) {
				List<Urgencia> lista = new ArrayList<Urgencia>();

				for (Urgencia urgencia : listUrgencia) {
					if (urgencia.getDescripcionUrgencia().toLowerCase()
									.contains(valores.get(0).toLowerCase())) {
						lista.add(urgencia);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Urgencia urgencia) {
				String[] registros = new String[1];
				registros[0] = urgencia.getDescripcionUrgencia();

				return registros;
			}
		};
		catalogo.setParent(catalogoUrgencia);

	}

}
