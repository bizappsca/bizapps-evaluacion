package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Competencia;

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

public class CCompetencia extends CGenerico {

	@Wire
	private Window wdwVCompetencia;
	@Wire
	private Div botoneraCompetencia;
	@Wire
	private Groupbox gpxRegistroCompetencia;
	@Wire
	private Textbox txtDescripcionCompetencia;
	@Wire
	private Combobox cmbNivelCompetencia;
	@Wire
	private Textbox txtComentarioCompetencia;
	@Wire
	private Groupbox gpxDatosCompetencia;
	@Wire
	private Div catalogoCompetencia;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idCompetencia = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Competencia> catalogo;

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
		txtDescripcionCompetencia.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Competencia competencia = catalogo
								.objetoSeleccionadoDelCatalogo();
						idCompetencia = competencia.getId();
						txtDescripcionCompetencia.setValue(competencia
								.getDescripcion());
						cmbNivelCompetencia.setValue(competencia.getNivel());
						txtComentarioCompetencia.setValue(competencia
								.getComentario());
						txtDescripcionCompetencia.setFocus(true);
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
					String descripcion = txtDescripcionCompetencia.getValue();
					String nivel = cmbNivelCompetencia.getValue();
					String comentario = txtComentarioCompetencia.getValue();
					String usuario = nombreUsuarioSesion();
					Timestamp fechaAuditoria = new Timestamp(
							new Date().getTime());
					Competencia competencia = new Competencia(idCompetencia,
							comentario, descripcion, fechaAuditoria,
							horaAuditoria, nivel, usuario);
					servicioCompetencia.guardar(competencia);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(servicioCompetencia.buscarTodas());
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
				cerrarVentana(wdwVCompetencia, "Competencia",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosCompetencia.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Competencia> eliminarLista = catalogo
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
													servicioCompetencia
															.eliminarVariasCompetencias(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioCompetencia
															.buscarTodas());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idCompetencia != 0) {
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
													servicioCompetencia
															.eliminarUnaCompetencia(idCompetencia);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioCompetencia
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
		botoneraCompetencia.appendChild(botonera);

	}

	public void limpiarCampos() {
		idCompetencia = 0;
		txtDescripcionCompetencia.setValue("");
		cmbNivelCompetencia.setValue("");
		txtComentarioCompetencia.setValue("");
		catalogo.limpiarSeleccion();
		txtDescripcionCompetencia.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtDescripcionCompetencia.getText().compareTo("") != 0
				|| cmbNivelCompetencia.getText().compareTo("") != 0
				|| txtComentarioCompetencia.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroCompetencia")
	public void abrirRegistro() {
		gpxDatosCompetencia.setOpen(false);
		gpxRegistroCompetencia.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosCompetencia")
	public void abrirCatalogo() {
		gpxDatosCompetencia.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosCompetencia.setOpen(false);
								gpxRegistroCompetencia.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosCompetencia.setOpen(true);
									gpxRegistroCompetencia.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosCompetencia.setOpen(true);
			gpxRegistroCompetencia.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Competencia> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtDescripcionCompetencia.getText().compareTo("") == 0
				|| cmbNivelCompetencia.getText().compareTo("") == 0) {
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

		final List<Competencia> listCompetencia = servicioCompetencia
				.buscarTodas();
		catalogo = new Catalogo<Competencia>(catalogoCompetencia,
				"Catalogo de Competencias", listCompetencia, false,false,false,"Descripción",
				"Nivel", "Comentario") {

			@Override
			protected List<Competencia> buscar(List<String> valores) {
				List<Competencia> lista = new ArrayList<Competencia>();

				for (Competencia competencia : listCompetencia) {
					if (competencia.getDescripcion().toLowerCase()
									.contains(valores.get(0).toLowerCase())
							&& competencia.getNivel().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& competencia.getComentario().toLowerCase()
									.contains(valores.get(2).toLowerCase())) {
						lista.add(competencia);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Competencia competencia) {
				String[] registros = new String[3];
				registros[0] = competencia.getDescripcion();
				registros[1] = competencia.getNivel();
				registros[2] = competencia.getComentario();

				return registros;
			}

		};
		catalogo.setParent(catalogoCompetencia);

	}

}
