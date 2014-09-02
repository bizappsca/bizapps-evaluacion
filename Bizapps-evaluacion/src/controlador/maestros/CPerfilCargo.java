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
import modelo.maestros.Cargo;
import modelo.maestros.PerfilCargo;

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
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CPerfilCargo extends CGenerico {

	@Wire
	private Window wdwVPerfilCargo;
	@Wire
	private Div botoneraPerfilCargo;
	@Wire
	private Groupbox gpxRegistroPerfilCargo;
	@Wire
	private Textbox txtDescripcionPerfilCargo;
	@Wire
	private Textbox txtCargoPerfilCargo;
	@Wire
	private Button btnBuscarCargo;
	@Wire
	private Combobox cmbNivelAcademicoPerfilCargo;
	@Wire
	private Textbox txtEspecialidadPerfilCargo;
	@Wire
	private Textbox txtEspecializacionPerfilCargo;
	@Wire
	private Textbox txtExperienciaPerfilCargo;
	@Wire
	private Textbox txtIdiomaPerfilCargo;
	@Wire
	private Textbox txtObservacionesPerfilCargo;
	@Wire
	private Groupbox gpxDatosPerfilCargo;
	@Wire
	private Div catalogoPerfilCargo;
	@Wire
	private Div divCatalogoCargo;

	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idPerfilCargo = 0;
	private int idCargo = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<PerfilCargo> catalogo;
	Catalogo<Cargo> catalogoCargo;

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
		txtCargoPerfilCargo.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						PerfilCargo perfilCargo = catalogo
								.objetoSeleccionadoDelCatalogo();
						idPerfilCargo = perfilCargo.getId();
						idCargo = perfilCargo.getCargo().getId();
						txtCargoPerfilCargo.setValue(perfilCargo.getCargo()
								.getDescripcion());
						txtDescripcionPerfilCargo.setValue(perfilCargo
								.getDescripcion());
						cmbNivelAcademicoPerfilCargo.setValue(perfilCargo
								.getNivelAcademico());
						txtEspecialidadPerfilCargo.setValue(perfilCargo
								.getEspecialidad());
						txtEspecializacionPerfilCargo.setValue(perfilCargo
								.getEspecializacion());
						txtExperienciaPerfilCargo.setValue(perfilCargo
								.getExperienciaPrevia());
						txtIdiomaPerfilCargo.setValue(perfilCargo.getIdioma());
						txtObservacionesPerfilCargo.setValue(perfilCargo
								.getObservaciones());
						txtCargoPerfilCargo.setFocus(true);
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

					Cargo cargo = servicioCargo.buscarCargo(idCargo);

					if (cargo != null) {

						String descripcion = txtDescripcionPerfilCargo
								.getValue();
						String nivelAcademico = cmbNivelAcademicoPerfilCargo
								.getValue();
						String especialidad = txtEspecialidadPerfilCargo
								.getValue();
						String especializacion = txtEspecializacionPerfilCargo
								.getValue();
						String experienciaPrevia = txtExperienciaPerfilCargo
								.getValue();
						String idioma = txtIdiomaPerfilCargo.getValue();
						String observaciones = txtObservacionesPerfilCargo
								.getValue();
						String usuario = nombreUsuarioSesion();
						Timestamp fechaAuditoria = new Timestamp(
								new Date().getTime());
						PerfilCargo perfilCargo = new PerfilCargo(
								idPerfilCargo, cargo, descripcion,
								nivelAcademico, especialidad, especializacion,
								experienciaPrevia, idioma, observaciones,
								fechaAuditoria, horaAuditoria, usuario);
						servicioPerfilCargo.guardar(perfilCargo);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();
						catalogo.actualizarLista(servicioPerfilCargo
								.buscarTodos());
						abrirCatalogo();
					} else {

						msj.mensajeAlerta(Mensaje.codigoCargo);
						txtCargoPerfilCargo.setFocus(true);

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
				cerrarVentana(wdwVPerfilCargo, "Perfil del Cargo",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosPerfilCargo.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<PerfilCargo> eliminarLista = catalogo
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
													servicioPerfilCargo
															.eliminarVariosPerfiles(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioPerfilCargo
															.buscarTodos());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idPerfilCargo != 0) {
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
													servicioPerfilCargo
															.eliminarUnPerfil(idPerfilCargo);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioPerfilCargo
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
		botoneraPerfilCargo.appendChild(botonera);

	}

	public void limpiarCampos() {
		idPerfilCargo = 0;
		idCargo = 0;
		txtCargoPerfilCargo.setValue("");
		txtDescripcionPerfilCargo.setValue("");
		cmbNivelAcademicoPerfilCargo.setValue("");
		txtEspecialidadPerfilCargo.setValue("");
		txtEspecializacionPerfilCargo.setValue("");
		txtExperienciaPerfilCargo.setValue("");
		txtIdiomaPerfilCargo.setValue("");
		txtObservacionesPerfilCargo.setValue("");
		catalogo.limpiarSeleccion();
		txtCargoPerfilCargo.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtCargoPerfilCargo.getText().compareTo("") != 0
				|| txtDescripcionPerfilCargo.getText().compareTo("") != 0
				|| cmbNivelAcademicoPerfilCargo.getText().compareTo("") != 0
				|| txtEspecialidadPerfilCargo.getText().compareTo("") != 0
				|| txtEspecializacionPerfilCargo.getText().compareTo("") != 0
				|| txtExperienciaPerfilCargo.getText().compareTo("") != 0
				|| txtIdiomaPerfilCargo.getText().compareTo("") != 0
				|| txtObservacionesPerfilCargo.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroPerfilCargo")
	public void abrirRegistro() {
		gpxDatosPerfilCargo.setOpen(false);
		gpxRegistroPerfilCargo.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosPerfilCargo")
	public void abrirCatalogo() {
		gpxDatosPerfilCargo.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosPerfilCargo.setOpen(false);
								gpxRegistroPerfilCargo.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosPerfilCargo.setOpen(true);
									gpxRegistroPerfilCargo.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosPerfilCargo.setOpen(true);
			gpxRegistroPerfilCargo.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<PerfilCargo> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtCargoPerfilCargo.getText().compareTo("") == 0) {
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

		final List<PerfilCargo> listPerfilCargo = servicioPerfilCargo
				.buscarTodos();
		catalogo = new Catalogo<PerfilCargo>(catalogoPerfilCargo,
				"Catalogo de Perfil de Cargos", listPerfilCargo, "Cargo",
				"Descripci�n", "Nivel Acad�mico", "Especialidad",
				"Especializaci�n", "Experiencia Previa", "Idioma",
				"Observaciones") {

			@Override
			protected List<PerfilCargo> buscarCampos(List<String> valores) {
				List<PerfilCargo> lista = new ArrayList<PerfilCargo>();

				for (PerfilCargo perfilCargo : listPerfilCargo) {
					if (perfilCargo.getCargo().getDescripcion().toLowerCase()
							.startsWith(valores.get(0))
							&& perfilCargo.getDescripcion().toLowerCase()
									.startsWith(valores.get(1))
							&& perfilCargo.getNivelAcademico().toLowerCase()
									.startsWith(valores.get(2))
							&& perfilCargo.getEspecialidad().toLowerCase()
									.startsWith(valores.get(3))
							&& perfilCargo.getEspecializacion().toLowerCase()
									.startsWith(valores.get(4))
							&& perfilCargo.getExperienciaPrevia().toLowerCase()
									.startsWith(valores.get(5))
							&& perfilCargo.getIdioma().toLowerCase()
									.startsWith(valores.get(6))
							&& perfilCargo.getObservaciones().toLowerCase()
									.startsWith(valores.get(7))) {
						lista.add(perfilCargo);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(PerfilCargo perfilCargo) {
				String[] registros = new String[8];
				registros[0] = perfilCargo.getCargo().getDescripcion();
				registros[1] = perfilCargo.getDescripcion();
				registros[2] = perfilCargo.getNivelAcademico();
				registros[3] = perfilCargo.getEspecialidad();
				registros[4] = perfilCargo.getEspecializacion();
				registros[5] = perfilCargo.getExperienciaPrevia();
				registros[6] = perfilCargo.getIdioma();
				registros[7] = perfilCargo.getObservaciones();

				return registros;
			}

			@Override
			protected List<PerfilCargo> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("Cargo"))
					return servicioPerfilCargo.filtroCargo(valor);
				else if (combo.equals("Descripci�n"))
					return servicioPerfilCargo.filtroNombre(valor);
				else if (combo.equals("Nivel Acad�mico"))
					return servicioPerfilCargo.filtroNivelAcademico(valor);
				else if (combo.equals("Especialidad"))
					return servicioPerfilCargo.filtroEspecialidad(valor);
				else if (combo.equals("Especializaci�n"))
					return servicioPerfilCargo.filtroEspecializacion(valor);
				else if (combo.equals("Experiencia Previa"))
					return servicioPerfilCargo.filtroExperiencia(valor);
				else if (combo.equals("Idioma"))
					return servicioPerfilCargo.filtroIdioma(valor);
				else if (combo.equals("Observaciones"))
					return servicioPerfilCargo.filtroObservaciones(valor);
				else
					return servicioPerfilCargo.buscarTodos();
			}

		};
		catalogo.setParent(catalogoPerfilCargo);

	}

	@Listen("onChange = #txtCargoPerfilCargo")
	public void buscarCargo() {
		List<Cargo> cargos = servicioCargo.buscarPorNombres(txtCargoPerfilCargo
				.getValue());
		if (cargos.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoCargo);
			txtCargoPerfilCargo.setFocus(true);
		} else {

			idCargo = cargos.get(0).getId();
		}

	}

	@Listen("onClick = #btnBuscarCargo")
	public void mostrarCatalogoCargo() {
		final List<Cargo> listCargo = servicioCargo.buscarTodos();
		catalogoCargo = new Catalogo<Cargo>(divCatalogoCargo,
				"Catalogo de Cargos", listCargo, "Descripci�n", "N�mina",
				"Cargo Auxiliar", "Empresa Auxiliar") {

			@Override
			protected List<Cargo> buscarCampos(List<String> valores) {
				List<Cargo> lista = new ArrayList<Cargo>();

				for (Cargo cargo : listCargo) {
					if (cargo.getDescripcion().toLowerCase()
							.startsWith(valores.get(0))
							&& cargo.getNomina().toLowerCase()
									.startsWith(valores.get(1))
							&& cargo.getIdCargoAuxiliar().toLowerCase()
									.startsWith(valores.get(2))
							&& cargo.getIdEmpresaAuxiliar().toLowerCase()
									.startsWith(valores.get(3))) {
						lista.add(cargo);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Cargo cargo) {
				String[] registros = new String[4];
				registros[0] = cargo.getDescripcion();
				registros[1] = cargo.getNomina();
				registros[2] = cargo.getIdCargoAuxiliar();
				registros[3] = cargo.getIdEmpresaAuxiliar();

				return registros;
			}

			@Override
			protected List<Cargo> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("Descripci�n"))
					return servicioCargo.filtroDescripcion(valor);
				else if (combo.equals("N�mina"))
					return servicioCargo.filtroNomina(valor);
				else if (combo.equals("Cargo Auxiliar"))
					return servicioCargo.filtroCargoAuxiliar(valor);
				else if (combo.equals("Empresa Auxiliar"))
					return servicioCargo.filtroEmpresaAuxiliar(valor);
				else
					return servicioCargo.buscarTodos();
			}

		};
		catalogoCargo.setClosable(true);
		catalogoCargo.setWidth("80%");
		catalogoCargo.setParent(divCatalogoCargo);
		catalogoCargo.doModal();
	}

	@Listen("onSeleccion = #divCatalogoCargo")
	public void seleccionCargo() {
		Cargo cargo = catalogoCargo.objetoSeleccionadoDelCatalogo();
		idCargo = cargo.getId();
		txtCargoPerfilCargo.setValue(cargo.getDescripcion());
		catalogoCargo.setParent(null);
	}

}
