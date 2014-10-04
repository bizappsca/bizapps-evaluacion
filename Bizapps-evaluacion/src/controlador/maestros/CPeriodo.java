package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Periodo;
import modelo.maestros.Revision;

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

public class CPeriodo extends CGenerico {

	@Wire
	private Window wdwVPeriodo;
	@Wire
	private Div botoneraPeriodo;
	@Wire
	private Groupbox gpxRegistroPeriodo;
	@Wire
	private Textbox txtNombrePeriodo;
	@Wire
	private Textbox txtDescripcionPeriodo;
	@Wire
	private Datebox dtbFechaInicioPeriodo;
	@Wire
	private Datebox dtbFechaFinPeriodo;
	@Wire
	private Combobox cmbEstadoPeriodo;
	@Wire
	private Groupbox gpxDatosPeriodo;
	@Wire
	private Div catalogoPeriodo;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idPeriodo = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Periodo> catalogo;

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
		txtNombrePeriodo.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Periodo periodo = catalogo
								.objetoSeleccionadoDelCatalogo();
						idPeriodo = periodo.getId();
						txtNombrePeriodo.setValue(periodo.getNombre());
						txtDescripcionPeriodo
								.setValue(periodo.getDescripcion());
						dtbFechaInicioPeriodo
								.setValue(periodo.getFechaInicio());
						dtbFechaFinPeriodo.setValue(periodo.getFechaFin());
						cmbEstadoPeriodo.setValue(periodo.getEstadoPeriodo());
						txtNombrePeriodo.setFocus(true);
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
						
					if (!validarEstadoActivo()) {

						String nombre = txtNombrePeriodo.getValue();
						String descripcion = txtDescripcionPeriodo.getValue();
						Timestamp fechaInicio = new java.sql.Timestamp(
								dtbFechaInicioPeriodo.getValue().getTime());
						Timestamp fechaFin = new java.sql.Timestamp(
								dtbFechaFinPeriodo.getValue().getTime());
						String usuario = nombreUsuarioSesion();
						Timestamp fechaAuditoria = new Timestamp(
								new Date().getTime());
						String estadoPeriodo = cmbEstadoPeriodo.getValue();
						Periodo periodo = new Periodo(idPeriodo, descripcion,
								estadoPeriodo, fechaAuditoria, fechaFin,
								fechaInicio, horaAuditoria, nombre, usuario);
						Revision revision = servicioRevision.buscarRevisionPorPeriodo(periodo);
						if (estadoPeriodo.equals("INACTIVO") && revision != null){
							msj.mensajeAlerta(Mensaje.revisionesActivas);
							
						}
						else{
						servicioPeriodo.guardar(periodo);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();
						catalogo.actualizarLista(servicioPeriodo.buscarTodos());
						abrirCatalogo();
						}
					} else {

						msj.mensajeAlerta(Mensaje.periodoActivo);
						cmbEstadoPeriodo.setFocus(true);

					}

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
				cerrarVentana(wdwVPeriodo, "Periodos",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosPeriodo.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Periodo> eliminarLista = catalogo
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
													servicioPeriodo
															.eliminarVariosPeriodos(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioPeriodo
															.buscarTodos());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idPeriodo != 0) {
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
													servicioPeriodo
															.eliminarUnPeriodo(idPeriodo);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioPeriodo
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
		botoneraPeriodo.appendChild(botonera);

	}

	public void limpiarCampos() {
		idPeriodo = 0;
		txtNombrePeriodo.setValue("");
		txtDescripcionPeriodo.setValue("");
		dtbFechaInicioPeriodo.setValue(null);
		dtbFechaFinPeriodo.setValue(null);
		cmbEstadoPeriodo.setValue("");
		catalogo.limpiarSeleccion();
		txtNombrePeriodo.setFocus(true);
	}

	public boolean camposEditando() {
		if (txtNombrePeriodo.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroPeriodo")
	public void abrirRegistro() {
		gpxDatosPeriodo.setOpen(false);
		gpxRegistroPeriodo.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosPeriodo")
	public void abrirCatalogo() {
		gpxDatosPeriodo.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosPeriodo.setOpen(false);
								gpxRegistroPeriodo.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosPeriodo.setOpen(true);
									gpxRegistroPeriodo.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosPeriodo.setOpen(true);
			gpxRegistroPeriodo.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Periodo> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtNombrePeriodo.getText().compareTo("") == 0
				|| dtbFechaInicioPeriodo.getText().compareTo("") == 0
				|| dtbFechaFinPeriodo.getText().compareTo("") == 0
				|| cmbEstadoPeriodo.getText().compareTo("") == 0) {
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

	protected boolean validarEstadoActivo() {

		Periodo periodo = servicioPeriodo.buscarPeriodoActivo();

		if (cmbEstadoPeriodo.getValue().equals("ACTIVO") && periodo != null) {
			return true;
		}

		return false;

	}

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(3).setVisible(!bol);

	}

	public void mostrarCatalogo() {

		final List<Periodo> listPeriodo = servicioPeriodo.buscarTodos();
		catalogo = new Catalogo<Periodo>(catalogoPeriodo,
				"Catalogo de Periodos", listPeriodo,false,false,false, "Nombre", "Descripción",
				"Fecha Inicio", "Fecha Fin", "Estado") {

			@Override
			protected List<Periodo> buscar(List<String> valores) {
				List<Periodo> lista = new ArrayList<Periodo>();

				for (Periodo periodo : listPeriodo) {
					if (periodo.getNombre().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& periodo.getDescripcion().toLowerCase()
							.contains(valores.get(1).toLowerCase())
							&& String
									.valueOf(
											formatoFecha.format(periodo
													.getFechaInicio()))
									.toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& String
									.valueOf(
											formatoFecha.format(periodo
													.getFechaFin()))
									.toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& periodo.getEstadoPeriodo().toLowerCase()
							.contains(valores.get(4).toLowerCase())) {
						lista.add(periodo);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Periodo periodo) {
				String[] registros = new String[6];
				registros[0] = periodo.getNombre();
				registros[1] = periodo.getDescripcion();
				registros[2] = formatoFecha.format(periodo.getFechaInicio());
				registros[3] = formatoFecha.format(periodo.getFechaFin());
				registros[4] = periodo.getEstadoPeriodo();

				return registros;
			}
		};
		catalogo.setParent(catalogoPeriodo);

	}

}
