package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Medicion;

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

public class CMedicion extends CGenerico {

	@Wire
	private Window wdwVMedicion;
	@Wire
	private Div botoneraMedicion;
	@Wire
	private Groupbox gpxRegistroMedicion;
	@Wire
	private Textbox txtDescripcionMedicion;
	@Wire
	private Groupbox gpxDatosMedicion;
	@Wire
	private Div catalogoMedicion;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idMedicion = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Medicion> catalogo;

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
		txtDescripcionMedicion.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Medicion medicion = catalogo
								.objetoSeleccionadoDelCatalogo();
						idMedicion = medicion.getId();
						txtDescripcionMedicion.setValue(medicion
								.getDescripcionMedicion());
						txtDescripcionMedicion.setFocus(true);
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
					String descripcionMedicion = txtDescripcionMedicion
							.getValue();
					String usuario = nombreUsuarioSesion();
					Timestamp fechaAuditoria = new Timestamp(
							new Date().getTime());
					Medicion medicion = new Medicion(idMedicion,
							descripcionMedicion, usuario, fechaAuditoria,
							horaAuditoria);
					servicioMedicion.guardar(medicion);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(servicioMedicion.buscarTodas());
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
				cerrarVentana(wdwVMedicion, "Medicion",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosMedicion.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Medicion> eliminarLista = catalogo
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
													servicioMedicion
															.eliminarVariasMediciones(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioMedicion
															.buscarTodas());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idMedicion != 0) {
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
													servicioMedicion
															.eliminarUnaMedicion(idMedicion);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioMedicion
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
		botoneraMedicion.appendChild(botonera);

	}

	public void limpiarCampos() {
		idMedicion = 0;
		txtDescripcionMedicion.setValue("");
		catalogo.limpiarSeleccion();
		txtDescripcionMedicion.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtDescripcionMedicion.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroMedicion")
	public void abrirRegistro() {
		gpxDatosMedicion.setOpen(false);
		gpxRegistroMedicion.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosMedicion")
	public void abrirCatalogo() {
		gpxDatosMedicion.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosMedicion.setOpen(false);
								gpxRegistroMedicion.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosMedicion.setOpen(true);
									gpxRegistroMedicion.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosMedicion.setOpen(true);
			gpxRegistroMedicion.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Medicion> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtDescripcionMedicion.getText().compareTo("") == 0) {
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

		final List<Medicion> listMedicion = servicioMedicion.buscarTodas();
		catalogo = new Catalogo<Medicion>(catalogoMedicion,
				"Catalogo de Mediciones", listMedicion, false,false,false,"Descripción") {

			@Override
			protected List<Medicion> buscar(List<String> valores) {
				List<Medicion> lista = new ArrayList<Medicion>();

				for (Medicion medicion : listMedicion) {
					if (medicion.getDescripcionMedicion().toLowerCase()
									.contains(valores.get(0).toLowerCase())) {
						lista.add(medicion);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Medicion medicion) {
				String[] registros = new String[1];
				registros[0] = medicion.getDescripcionMedicion();

				return registros;
			}

		};
		catalogo.setParent(catalogoMedicion);

	}

}
