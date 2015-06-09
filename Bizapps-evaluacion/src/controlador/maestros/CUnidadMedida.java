package controlador.maestros;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.UnidadMedida;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CUnidadMedida extends CGenerico {

	@Wire
	private Window wdwVUnidadMedida;
	@Wire
	private Div botoneraUnidadMedida;
	@Wire
	private Groupbox gpxRegistroUnidadMedida;
	@Wire
	private Textbox txtDescripcionUnidadMedida;
	@Wire
	private Groupbox gpxDatosUnidadMedida;
	@Wire
	private Div catalogoUnidadMedida;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idUnidadMedida = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<UnidadMedida> catalogo;
	protected List<UnidadMedida> listaGeneral = new ArrayList<UnidadMedida>();

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
		txtDescripcionUnidadMedida.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						UnidadMedida unidadMedida = catalogo
								.objetoSeleccionadoDelCatalogo();
						idUnidadMedida = unidadMedida.getId();
						txtDescripcionUnidadMedida.setValue(unidadMedida
								.getDescripcion());
						txtDescripcionUnidadMedida.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}

			}

			@Override
			public void guardar() {
				// TODO Auto-generated method stub
				if (validar()) {
					String descripcion = txtDescripcionUnidadMedida.getValue();
					String usuario = nombreUsuarioSesion();
					Timestamp fechaAuditoria = new Timestamp(
							new Date().getTime());
					UnidadMedida unidadMedida = new UnidadMedida(
							idUnidadMedida, descripcion, usuario,
							fechaAuditoria, horaAuditoria);
					servicioUnidadMedida.guardar(unidadMedida);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					listaGeneral = servicioUnidadMedida.buscarTodas();
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
				cerrarVentana2(wdwVUnidadMedida, titulo,tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosUnidadMedida.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<UnidadMedida> eliminarLista = catalogo
								.obtenerSeleccionados();
						Messagebox
								.show("�Desea Eliminar los "
										+ eliminarLista.size() + " Registros?",
										"Alerta",
										Messagebox.OK | Messagebox.CANCEL,
										Messagebox.QUESTION,
										new org.zkoss.zk.ui.event.EventListener<Event>() {
											public void onEvent(Event evt)
													throws InterruptedException {
												if (evt.getName()
														.equals("onOK")) {
													servicioUnidadMedida
															.eliminarVariasUnidades(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													listaGeneral = servicioUnidadMedida.buscarTodas();
													catalogo.actualizarLista(listaGeneral);
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idUnidadMedida != 0) {
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
													servicioUnidadMedida
															.eliminarUnaUnidad(idUnidadMedida);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													listaGeneral = servicioUnidadMedida.buscarTodas();
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
				// TODO Auto-generated method stub
				abrirCatalogo();
			}

			@Override
			public void annadir() {
				// TODO Auto-generated method stub
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
		botoneraUnidadMedida.appendChild(botonera);

	}

	public void limpiarCampos() {
		idUnidadMedida = 0;
		txtDescripcionUnidadMedida.setValue("");
		catalogo.limpiarSeleccion();
		txtDescripcionUnidadMedida.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtDescripcionUnidadMedida.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroUnidadMedida")
	public void abrirRegistro() {
		gpxDatosUnidadMedida.setOpen(false);
		gpxRegistroUnidadMedida.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosUnidadMedida")
	public void abrirCatalogo() {
		gpxDatosUnidadMedida.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosUnidadMedida.setOpen(false);
								gpxRegistroUnidadMedida.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosUnidadMedida.setOpen(true);
									gpxRegistroUnidadMedida.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosUnidadMedida.setOpen(true);
			gpxRegistroUnidadMedida.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<UnidadMedida> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtDescripcionUnidadMedida.getText().compareTo("") == 0) {
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

		listaGeneral = servicioUnidadMedida
				.buscarTodas();
		catalogo = new Catalogo<UnidadMedida>(catalogoUnidadMedida,
				"Catalogo de Unidades de Medidas", listaGeneral,false,false,false,
				"Descripci�n") {

			@Override
			protected List<UnidadMedida> buscar(List<String> valores) {
				List<UnidadMedida> lista = new ArrayList<UnidadMedida>();

				for (UnidadMedida unidadMedida : listaGeneral) {
					if (unidadMedida.getDescripcion().toLowerCase()
									.contains(valores.get(0).toLowerCase())) {
						lista.add(unidadMedida);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(UnidadMedida unidadMedida) {
				String[] registros = new String[1];
				registros[0] = unidadMedida.getDescripcion();

				return registros;
			}

		};
		catalogo.setParent(catalogoUnidadMedida);

	}

}
