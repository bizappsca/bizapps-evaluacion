package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Area;
import modelo.maestros.Empleado;
import modelo.maestros.FechaValidezEstado;
import modelo.maestros.Periodo;
import modelo.maestros.Revision;
import modelo.maestros.TipoFormacion;

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
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CFechaValidezEstado extends CGenerico {

	@Wire
	private Window wdwVRevision;
	@Wire
	private Div botoneraRevision;
	@Wire
	private Groupbox gpxRegistroRevision;
	/*@Wire
	private Textbox txtPeriodoRevision;
	@Wire
	private Label lblPeriodoRevision;
	@Wire
	private Combobox cmbEstadoRevision;
	@Wire
	private Button btnBuscarPeriodo;
	@Wire
	private Textbox txtDescripcionRevision;
	@Wire
	private Textbox txtMensajeInicio;*/
	@Wire
	private Textbox txtEstado;
	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Spinner spnGrado;
	
	@Wire
	private Groupbox gpxDatosRevision;
	@Wire
	private Div catalogoFechaValidezEstado;
	@Wire
	private Div divCatalogoPeriodo;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idFechaValidezEstado = 0;


	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<FechaValidezEstado> catalogo;
	Catalogo<Periodo> catalogoPeriodo;
	protected List<FechaValidezEstado> listaGeneral = new ArrayList<FechaValidezEstado>();
	
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
		//txtPeriodoRevision.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						FechaValidezEstado fechaValidezEstado = catalogo
								.objetoSeleccionadoDelCatalogo();
						idFechaValidezEstado = fechaValidezEstado.getId();
						
						txtEstado.setText(fechaValidezEstado.getEstado());
						dtbFechaInicio.setValue(fechaValidezEstado.getFechaDesde());
						dtbFechaFin.setValue(fechaValidezEstado.getFechaHasta());
						spnGrado.setValue(fechaValidezEstado.getGrado());
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}

			}

			@Override
			public void guardar() {
				// TODO Auto-generated method stub

							String usuario = nombreUsuarioSesion();
							Timestamp fechaAuditoria = new Timestamp(
									new Date().getTime());
							
							Timestamp fechaDesde = new java.sql.Timestamp(
									dtbFechaInicio.getValue().getTime());
							
							Timestamp fechaHasta = new java.sql.Timestamp(
									dtbFechaFin.getValue().getTime());
							FechaValidezEstado fechaValidezEstado = servicioFechaValidezEstado.buscarActividad(idFechaValidezEstado);
							fechaValidezEstado.setFechaDesde(fechaDesde);
							fechaValidezEstado.setFechaHasta(fechaHasta);
							fechaValidezEstado.setGrado(spnGrado.getValue());
							servicioFechaValidezEstado.guardar(fechaValidezEstado);
							
							msj.mensajeInformacion(Mensaje.guardado);
							limpiar();
							listaGeneral = servicioFechaValidezEstado
									.buscarTodas();
							catalogo.actualizarLista(listaGeneral);
							abrirCatalogo();
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
				cerrarVentana2(wdwVRevision, titulo,tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosRevision.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<FechaValidezEstado> eliminarLista = catalogo
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
													//servicioRevision.eliminarVariasRevisiones(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													listaGeneral = servicioFechaValidezEstado
															.buscarTodas();
													catalogo.actualizarLista(listaGeneral);
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idFechaValidezEstado != 0) {
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
													servicioRevision
															.eliminarUnaRevision(idFechaValidezEstado);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													listaGeneral = servicioFechaValidezEstado
															.buscarTodas();
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
		botoneraRevision.appendChild(botonera);

	}

	public void limpiarCampos() {
		idFechaValidezEstado = 0;
		txtEstado.setValue("");
		dtbFechaInicio.setValue(null);
		dtbFechaFin.setValue(null);
		catalogo.limpiarSeleccion();


	}

	public boolean camposEditando() {
		
			return false;
	}

	public boolean camposLLenos() {
	
			return true;
	}

	protected boolean validar() {

		if (!camposLLenos()) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else
			return true;

	}

	@Listen("onClick = #gpxRegistroRevision")
	public void abrirRegistro() {
		gpxDatosRevision.setOpen(false);
		gpxRegistroRevision.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosRevision")
	public void abrirCatalogo() {
		gpxDatosRevision.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosRevision.setOpen(false);
								gpxRegistroRevision.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosRevision.setOpen(true);
									gpxRegistroRevision.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosRevision.setOpen(true);
			gpxRegistroRevision.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<FechaValidezEstado> seleccionados = catalogo.obtenerSeleccionados();
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
		botonera.getChildren().get(5).setVisible(!bol);
		botonera.getChildren().get(2).setVisible(bol);
		botonera.getChildren().get(4).setVisible(bol);
		botonera.getChildren().get(8).setVisible(false);
		

	}


	public void mostrarCatalogo() {
		listaGeneral = servicioFechaValidezEstado.buscarTodas();
		
		catalogo = new Catalogo<FechaValidezEstado>(catalogoFechaValidezEstado,
				"Catalogo de Estados", listaGeneral, false,false,false,"Estado",
				"Fecha Desde", "Fecha Hasta") {

			@Override
			protected List<FechaValidezEstado> buscar(List<String> valores) {
				List<FechaValidezEstado> lista = new ArrayList<FechaValidezEstado>();

				for (FechaValidezEstado fechaValidezEstado : listaGeneral) {
					if (fechaValidezEstado.getEstado().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							) {
						lista.add(fechaValidezEstado);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(FechaValidezEstado fechaValidezEstado) {
				String[] registros = new String[3];
				registros[0] = fechaValidezEstado.getEstado();
				registros[1] = formatoFecha.format(fechaValidezEstado.getFechaDesde());
				registros[2] = formatoFecha.format(fechaValidezEstado.getFechaHasta());

				return registros;
			}

			

		};
		catalogo.setParent(catalogoFechaValidezEstado);

	}

	

	
	
	

}