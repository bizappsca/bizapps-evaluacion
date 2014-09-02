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
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CRevision extends CGenerico {

	@Wire
	private Window wdwVRevision;
	@Wire
	private Div botoneraRevision;
	@Wire
	private Groupbox gpxRegistroRevision;
	@Wire
	private Textbox txtPeriodoRevision;
	@Wire
	private Combobox cmbEstadoRevision;
	@Wire
	private Button btnBuscarPeriodo;
	@Wire
	private Textbox txtDescripcionRevision;
	@Wire
	private Textbox txtMensajeInicio;
	@Wire
	private Groupbox gpxDatosRevision;
	@Wire
	private Div catalogoRevision;
	@Wire
	private Div divCatalogoPeriodo;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idRevision = 0;
	private int idPeriodo = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Revision> catalogo;
	Catalogo<Periodo> catalogoPeriodo;

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
		txtPeriodoRevision.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Revision revision = catalogo
								.objetoSeleccionadoDelCatalogo();
						idRevision = revision.getId();
						idPeriodo = revision.getPeriodo().getId();
						txtDescripcionRevision.setValue(revision
								.getDescripcion());
						txtPeriodoRevision.setValue(revision.getPeriodo()
								.getNombre());
						cmbEstadoRevision
								.setValue(revision.getEstadoRevision());
						txtMensajeInicio.setValue(revision.getMensajeInicio());
						txtPeriodoRevision.setFocus(true);
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
					Periodo periodo = servicioPeriodo.buscarPeriodo(idPeriodo);

					if (periodo != null) {

						if (!validarRevisionActiva()) {

							String descripcion = txtDescripcionRevision
									.getValue();
							String estadoRevision = cmbEstadoRevision
									.getValue();
							String mensaje = txtMensajeInicio.getValue();
							String usuario = nombreUsuarioSesion();
							Timestamp fechaAuditoria = new Timestamp(
									new Date().getTime());
							Revision revision = new Revision(idRevision,
									descripcion, estadoRevision,
									fechaAuditoria, horaAuditoria, usuario,
									periodo,mensaje);
							servicioRevision.guardar(revision);
							msj.mensajeInformacion(Mensaje.guardado);
							limpiar();
							catalogo.actualizarLista(servicioRevision
									.buscarTodas());
							abrirCatalogo();

						} else {

							msj.mensajeAlerta(Mensaje.revisionActiva);
							cmbEstadoRevision.setFocus(true);

						}

					} else {
						msj.mensajeAlerta(Mensaje.codigoPeriodo);
						txtPeriodoRevision.setFocus(true);
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
				cerrarVentana(wdwVRevision, "Revision",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosRevision.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Revision> eliminarLista = catalogo
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
													servicioRevision
															.eliminarVariasRevisiones(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioRevision
															.buscarTodas());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idRevision != 0) {
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
															.eliminarUnaRevision(idRevision);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioRevision
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
		botoneraRevision.appendChild(botonera);

	}

	public void limpiarCampos() {
		idRevision = 0;
		idPeriodo = 0;
		txtDescripcionRevision.setValue("");
		txtPeriodoRevision.setValue("");
		cmbEstadoRevision.setValue("");
		txtMensajeInicio.setValue("");
		catalogo.limpiarSeleccion();
		txtPeriodoRevision.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtDescripcionRevision.getText().compareTo("") != 0
				|| txtPeriodoRevision.getText().compareTo("") != 0
				|| cmbEstadoRevision.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	public boolean camposLLenos() {
		if (txtPeriodoRevision.getText().compareTo("") == 0
				|| cmbEstadoRevision.getText().compareTo("") == 0) {
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
		List<Revision> seleccionados = catalogo.obtenerSeleccionados();
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

	protected boolean validarRevisionActiva() {

		Revision revision = servicioRevision.buscarRevisionActiva();

		//CAMBIAR ***----
		if (cmbEstadoRevision.getValue().equals("ACTIVO") && revision != null) {
			return false;
		}

		return false;

	}

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(3).setVisible(!bol);

	}

	public void mostrarCatalogo() {
		final List<Revision> listRevision = servicioRevision.buscarTodas();
		catalogo = new Catalogo<Revision>(catalogoRevision,
				"Catalogo de Revisiones", listRevision, "Periodo",
				"Descripci�n", "Estado") {

			@Override
			protected List<Revision> buscarCampos(List<String> valores) {
				List<Revision> lista = new ArrayList<Revision>();

				for (Revision revision : listRevision) {
					if (revision.getPeriodo().getDescripcion().toLowerCase()
							.startsWith(valores.get(0))
							&& revision.getDescripcion().toLowerCase()
									.startsWith(valores.get(1))
							&& revision.getEstadoRevision().toLowerCase()
									.startsWith(valores.get(2))) {
						lista.add(revision);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Revision revision) {
				String[] registros = new String[3];
				registros[0] = revision.getPeriodo().getDescripcion();
				registros[1] = revision.getDescripcion();
				registros[2] = revision.getEstadoRevision();

				return registros;
			}

			@Override
			protected List<Revision> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("Periodo"))
					return servicioRevision.filtroPeriodo(valor);
				else if (combo.equals("Descripci�n"))
					return servicioRevision.filtroDescripcion(valor);
				else if (combo.equals("Estado"))
					return servicioRevision.filtroEstado(valor);
				else
					return servicioRevision.buscarTodas();
			}

		};
		catalogo.setParent(catalogoRevision);

	}

	@Listen("onChange = #txtPeriodoRevision")
	public void buscarPeriodo() {
		List<Periodo> periodos = servicioPeriodo.buscarPorNombres(txtPeriodoRevision
				.getValue());
		if (periodos.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoPeriodo);
			txtPeriodoRevision.setFocus(true);
		}else{
			
			idPeriodo = periodos.get(idPeriodo).getId();
			
		}

	}

	@Listen("onClick = #btnBuscarPeriodo")
	public void mostrarCatalogoPeriodo() {
		final List<Periodo> listPeriodo = servicioPeriodo.buscarTodos();
		catalogoPeriodo = new Catalogo<Periodo>(divCatalogoPeriodo,
				"Catalogo de Periodos", listPeriodo, "Nombre", "Descripci�n",
				"Fecha Inicio", "Fecha Fin", "Estado") {

			@Override
			protected List<Periodo> buscarCampos(List<String> valores) {
				List<Periodo> lista = new ArrayList<Periodo>();

				for (Periodo periodo : listPeriodo) {
					if (periodo.getNombre().toLowerCase()
							.startsWith(valores.get(0))
							&& periodo.getDescripcion().toLowerCase()
									.startsWith(valores.get(1))
							&& String
									.valueOf(
											formatoFecha.format(periodo
													.getFechaInicio()))
									.toLowerCase().startsWith(valores.get(2))
							&& String
									.valueOf(
											formatoFecha.format(periodo
													.getFechaFin()))
									.toLowerCase().startsWith(valores.get(3))
							&& periodo.getEstadoPeriodo().toLowerCase()
									.startsWith(valores.get(4))) {
						lista.add(periodo);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Periodo periodo) {
				String[] registros = new String[5];
				registros[0] = periodo.getNombre();
				registros[1] = periodo.getDescripcion();
				registros[2] = formatoFecha.format(periodo.getFechaInicio());
				registros[3] = formatoFecha.format(periodo.getFechaFin());
				registros[4] = periodo.getEstadoPeriodo();

				return registros;
			}

			@Override
			protected List<Periodo> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("Nombre"))
					return servicioPeriodo.filtroNombre(valor);
				else if (combo.equals("Descripci�n"))
					return servicioPeriodo.filtroDescripcion(valor);
				else if (combo.equals("Fecha Inicio"))
					return servicioPeriodo.filtroFechaInicio(valor);
				else if (combo.equals("Fecha Fin"))
					return servicioPeriodo.filtroFechaFin(valor);
				else if (combo.equals("Estado"))
					return servicioPeriodo.filtroEstado(valor);
				else
					return servicioPeriodo.buscarTodos();

			}

		};

		catalogoPeriodo.setClosable(true);
		catalogoPeriodo.setWidth("80%");
		catalogoPeriodo.setParent(divCatalogoPeriodo);
		catalogoPeriodo.doModal();
	}

	@Listen("onSeleccion = #divCatalogoPeriodo")
	public void seleccionPeriodo() {
		Periodo periodo = catalogoPeriodo.objetoSeleccionadoDelCatalogo();
		idPeriodo = periodo.getId();
		txtPeriodoRevision.setValue(periodo.getNombre());
		txtPeriodoRevision.setFocus(true);
		catalogoPeriodo.setParent(null);
	}

}