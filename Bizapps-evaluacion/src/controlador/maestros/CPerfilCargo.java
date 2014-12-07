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
import modelo.maestros.NombreCurso;
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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
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
	private Intbox txtCargoPerfilCargo;
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
	@Wire
	private Label lblCargoPerfil;

	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idPerfilCargo = 0;
	private int idCargo = 0;
	protected List<PerfilCargo> listaGeneral = new ArrayList<PerfilCargo>();

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
				titulo = (String) mapa.get("titulo");
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
								.getId());
						lblCargoPerfil.setValue(perfilCargo.getCargo()
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

				if (validar()) {

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
						listaGeneral = servicioPerfilCargo
								.buscarTodos();
						catalogo.actualizarLista(listaGeneral);
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
				cerrarVentana(wdwVPerfilCargo, titulo, tabs);
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
													servicioPerfilCargo
															.eliminarVariosPerfiles(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													listaGeneral = servicioPerfilCargo
															.buscarTodos();
													catalogo.actualizarLista(listaGeneral);
													
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
													listaGeneral = servicioPerfilCargo
															.buscarTodos();
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
		botoneraPerfilCargo.appendChild(botonera);

	}

	public void limpiarCampos() {
		idPerfilCargo = 0;
		idCargo = 0;
		txtCargoPerfilCargo.setValue(null);
		txtDescripcionPerfilCargo.setValue("");
		cmbNivelAcademicoPerfilCargo.setValue("");
		txtEspecialidadPerfilCargo.setValue("");
		txtEspecializacionPerfilCargo.setValue("");
		txtExperienciaPerfilCargo.setValue("");
		txtIdiomaPerfilCargo.setValue("");
		txtObservacionesPerfilCargo.setValue("");
		catalogo.limpiarSeleccion();
		lblCargoPerfil.setValue("");
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

		listaGeneral = servicioPerfilCargo
				.buscarTodos();
		catalogo = new Catalogo<PerfilCargo>(catalogoPerfilCargo,
				"Catalogo de Perfil de Cargos", listaGeneral, false, false,
				false, "Cargo", "Descripción", "Nivel Académico",
				"Especialidad", "Especialización", "Experiencia Previa",
				"Idioma", "Observaciones") {

			@Override
			protected List<PerfilCargo> buscar(List<String> valores) {
				List<PerfilCargo> lista = new ArrayList<PerfilCargo>();

				for (PerfilCargo perfilCargo : listaGeneral) {
					if (perfilCargo.getCargo().getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& perfilCargo.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& perfilCargo.getNivelAcademico().toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& perfilCargo.getEspecialidad().toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& perfilCargo.getEspecializacion().toLowerCase()
									.contains(valores.get(4).toLowerCase())
							&& perfilCargo.getExperienciaPrevia().toLowerCase()
									.contains(valores.get(5).toLowerCase())
							&& perfilCargo.getIdioma().toLowerCase()
									.contains(valores.get(6).toLowerCase())
							&& perfilCargo.getObservaciones().toLowerCase()
									.contains(valores.get(7).toLowerCase())) {
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

		};
		catalogo.setParent(catalogoPerfilCargo);

	}

	@Listen("onChange = #txtCargoPerfilCargo; onOk = #txtCargoPerfilCargo")
	public boolean buscarCargo() {
		if (txtCargoPerfilCargo.getValue() != null) {

			Cargo cargo = servicioCargo.buscarCargo(txtCargoPerfilCargo.getValue());
			if (cargo != null) {
				lblCargoPerfil.setValue(cargo.getDescripcion());
				idCargo= cargo.getId();
				return true;
			} else {
				msj.mensajeAlerta(Mensaje.codigoCargo);
				txtCargoPerfilCargo.setFocus(true);
				return false;
			}

		} else
			return false;
	}

	@Listen("onClick = #btnBuscarCargo")
	public void mostrarCatalogoCargo() {
		final List<Cargo> listCargo = servicioCargo.buscarTodos();
		catalogoCargo = new Catalogo<Cargo>(divCatalogoCargo,
				"Catalogo de Cargos", listCargo, true, false, false,
				"Descripción", "Nómina", "Cargo Auxiliar", "Empresa Auxiliar") {

			@Override
			protected List<Cargo> buscar(List<String> valores) {
				List<Cargo> lista = new ArrayList<Cargo>();

				for (Cargo cargo : listCargo) {
					if (cargo.getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& cargo.getNomina().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& cargo.getIdCargoAuxiliar().toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& cargo.getIdEmpresaAuxiliar().toLowerCase()
									.contains(valores.get(3).toLowerCase())) {
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
		txtCargoPerfilCargo.setValue(cargo.getId());
		lblCargoPerfil.setValue(cargo.getDescripcion());
		catalogoCargo.setParent(null);
	}

}
