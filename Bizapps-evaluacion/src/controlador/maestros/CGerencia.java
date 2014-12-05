package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Gerencia;

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

public class CGerencia extends CGenerico {

	@Wire
	private Window wdwVGerencia;
	@Wire
	private Div botoneraGerencia;
	@Wire
	private Groupbox gpxRegistroGerencia;
	@Wire
	private Textbox txtDescripcionGerencia;
	@Wire
	private Groupbox gpxDatosGerencia;
	@Wire
	private Div catalogoGerencia;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idGerencia = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Gerencia> catalogo;
	protected List<Gerencia> listaGeneral = new ArrayList<Gerencia>();

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				titulo = (String) map.get("titulo");
				map.clear();
				map = null;
			}
		}
		txtDescripcionGerencia.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Gerencia gerencia = catalogo
								.objetoSeleccionadoDelCatalogo();
						idGerencia = gerencia.getId();
						txtDescripcionGerencia.setValue(gerencia
								.getDescripcion());
						txtDescripcionGerencia.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}

			}

			@Override
			public void guardar() {
				if (validar()) {
					String descripcion = txtDescripcionGerencia.getValue();
					String usuario = nombreUsuarioSesion();
					Timestamp fechaAuditoria = new Timestamp(
							new Date().getTime());
					Gerencia gerencia = new Gerencia(idGerencia, descripcion,
							fechaAuditoria, horaAuditoria, usuario);
					servicioGerencia.guardar(gerencia);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					listaGeneral = servicioGerencia.buscarTodas();
					catalogo.actualizarLista(listaGeneral);
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
				cerrarVentana2(wdwVGerencia, titulo,tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosGerencia.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Gerencia> eliminarLista = catalogo
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
													servicioGerencia
															.eliminarVariasGerencias(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													listaGeneral = servicioGerencia.buscarTodas();
													catalogo.actualizarLista(listaGeneral);
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idGerencia != 0) {
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
													servicioGerencia
															.eliminarUnaGerencia(idGerencia);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													listaGeneral = servicioGerencia.buscarTodas();
													catalogo.actualizarLista(listaGeneral);
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
				abrirCatalogo();
				
			}

			@Override
			public void annadir() {
				abrirRegistro();
				mostrarBotones(false);
				
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
		botonera.getChildren().get(5).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botoneraGerencia.appendChild(botonera);

	}

	public void limpiarCampos() {
		idGerencia = 0;
		txtDescripcionGerencia.setValue("");
		catalogo.limpiarSeleccion();
		txtDescripcionGerencia.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtDescripcionGerencia.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroGerencia")
	public void abrirRegistro() {
		gpxDatosGerencia.setOpen(false);
		gpxRegistroGerencia.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosGerencia")
	public void abrirCatalogo() {
		gpxDatosGerencia.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosGerencia.setOpen(false);
								gpxRegistroGerencia.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosGerencia.setOpen(true);
									gpxRegistroGerencia.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosGerencia.setOpen(true);
			gpxRegistroGerencia.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Gerencia> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtDescripcionGerencia.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	protected boolean validar() {

		if (!camposLLenos()) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else
			return true;

	}

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(3).setVisible(!bol);
		botonera.getChildren().get(5).setVisible(!bol);
		botonera.getChildren().get(2).setVisible(bol);
		botonera.getChildren().get(4).setVisible(bol);
		botonera.getChildren().get(8).setVisible(false);

	}
	public void mostrarCatalogo() {

		listaGeneral = servicioGerencia.buscarTodas();
		catalogo = new Catalogo<Gerencia>(catalogoGerencia,
				"Catalogo de Gerencias", listaGeneral,false,false,false, "Descripción") {

			@Override
			protected List<Gerencia> buscar(List<String> valores) {
				List<Gerencia> lista = new ArrayList<Gerencia>();

				for (Gerencia gerencia : listaGeneral) {
					if (gerencia.getDescripcion().toLowerCase()
									.contains(valores.get(0).toLowerCase())) {
						lista.add(gerencia);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Gerencia gerencia) {
				String[] registros = new String[1];
				registros[0] = gerencia.getDescripcion();

				return registros;
			}

		};
		catalogo.setParent(catalogoGerencia);

	}

}
