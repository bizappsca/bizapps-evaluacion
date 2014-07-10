package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.maestros.Area;
import modelo.maestros.Cargo;
import modelo.maestros.Empleado;
import modelo.maestros.Empresa;
import modelo.maestros.UnidadOrganizativa;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CEmpleado extends CGenerico {

	@Wire
	private Window wdwVEmpleado;
	@Wire
	private Div botoneraEmpleado;
	@Wire
	private Groupbox gpxRegistroEmpleado;
	@Wire
	private Textbox txtNombreEmpleado;
	@Wire
	private Textbox txtEmpresaEmpleado;
	@Wire
	private Button btnBuscarEmpresa;
	@Wire
	private Textbox txtCargoEmpleado;
	@Wire
	private Button btnBuscarCargo;
	@Wire
	private Textbox txtUnidadEmpleado;
	@Wire
	private Button btnBuscarUnidad;
	@Wire
	private Textbox txtFichaEmpleado;
	@Wire
	private Textbox txtFichaSupervisorEmpleado;
	@Wire
	private Button btnBuscarSupervisor;
	@Wire
	private Spinner spnGradoAuxiliarEmpleado;
	@Wire
	private Groupbox gpxDatosEmpleado;
	@Wire
	private Div catalogoEmpleado;
	@Wire
	private Div divCatalogoEmpresa;
	@Wire
	private Div divCatalogoCargo;
	@Wire
	private Div divCatalogoSupervisor;
	@Wire
	private Div divCatalogoUnidad;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idEmpleado = 0;
	private int idCargo = 0;
	private int idEmpresa = 0;
	private int idUnidad = 0;
	private int idFichaSupervisor = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Empleado> catalogo;
	Catalogo<Empresa> catalogoEmpresa;
	Catalogo<Cargo> catalogoCargo;
	Catalogo<UnidadOrganizativa> catalogoUnidad;
	Catalogo<Empleado> catalogoSupervisor;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub

		txtEmpresaEmpleado.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Empleado empleado = catalogo
								.objetoSeleccionadoDelCatalogo();
						idEmpleado = empleado.getId();
						idCargo = empleado.getCargo().getId();
						idEmpresa = empleado.getEmpresa().getId();
						idUnidad = empleado.getUnidadOrganizativa().getId();
						idFichaSupervisor = Integer.parseInt(empleado
								.getFichaSupervisor());
						txtEmpresaEmpleado.setValue(empleado.getEmpresa()
								.getNombre());
						txtCargoEmpleado.setValue(empleado.getCargo()
								.getDescripcion());
						txtUnidadEmpleado.setValue(empleado
								.getUnidadOrganizativa().getDescripcion());
						txtNombreEmpleado.setValue(empleado.getNombre());
						txtFichaEmpleado.setValue(empleado.getFicha());
						Empleado fichaSupervisor = servicioEmpleado
								.buscar(Integer.parseInt(empleado
										.getFichaSupervisor()));
						if (fichaSupervisor != null)
							txtFichaSupervisorEmpleado.setValue(fichaSupervisor
									.getNombre());
						else
							txtFichaSupervisorEmpleado.setValue(empleado
									.getFichaSupervisor());
						spnGradoAuxiliarEmpleado.setValue(empleado
								.getGradoAuxiliar());
						txtEmpresaEmpleado.setFocus(true);
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

					Empresa empresa = servicioEmpresa.buscarEmpresa(idEmpresa);
					Cargo cargo = servicioCargo.buscarCargo(idCargo);
					UnidadOrganizativa unidadOrganizativa = servicioUnidadOrganizativa
							.buscarUnidad(idUnidad);
					Empleado fichaSupervisorEmpleado = servicioEmpleado
							.buscar(idFichaSupervisor);

					if (empresa != null && cargo != null
							&& unidadOrganizativa != null
							&& fichaSupervisorEmpleado != null) {
						String nombre = txtNombreEmpleado.getValue();
						String ficha = txtFichaEmpleado.getValue();
						String fichaSupervisor = String
								.valueOf(fichaSupervisorEmpleado.getId());
						int gradoAuxiliar = spnGradoAuxiliarEmpleado.getValue();
						String usuario = nombreUsuarioSesion();
						Timestamp fechaAuditoria = new Timestamp(
								new Date().getTime());
						Empleado empleado = new Empleado(idEmpleado,
								fechaAuditoria, ficha, fichaSupervisor,
								gradoAuxiliar, horaAuditoria, nombre, usuario,
								cargo, empresa, unidadOrganizativa);
						servicioEmpleado.guardar(empleado);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();
						catalogo.actualizarLista(servicioEmpleado.buscarTodos());
						abrirCatalogo();
					} else {

						if (empresa == null) {
							msj.mensajeAlerta(Mensaje.codigoEmpresa);
							txtEmpresaEmpleado.setFocus(true);
						} else if (cargo == null) {
							msj.mensajeAlerta(Mensaje.codigoCargo);
							txtCargoEmpleado.setFocus(true);
						} else if (unidadOrganizativa == null) {
							msj.mensajeAlerta(Mensaje.codigoUnidad);
							txtUnidadEmpleado.setFocus(true);
						} else if (fichaSupervisorEmpleado == null) {
							msj.mensajeAlerta(Mensaje.codigoSupervisor);
							txtFichaSupervisorEmpleado.setFocus(true);
						}

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
				cerrarVentana1(wdwVEmpleado, "Empleado");
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosEmpleado.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Empleado> eliminarLista = catalogo
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
													servicioEmpleado
															.eliminarVariosEmpleados(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioEmpleado
															.buscarTodos());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idEmpleado != 0) {
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
													servicioEmpleado
															.eliminarUnEmpleado(idEmpleado);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioEmpleado
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
		botoneraEmpleado.appendChild(botonera);

	}

	public void limpiarCampos() {
		idEmpleado = 0;
		idCargo = 0;
		idEmpresa = 0;
		idUnidad = 0;
		idFichaSupervisor = 0;
		txtNombreEmpleado.setValue("");
		txtEmpresaEmpleado.setValue("");
		spnGradoAuxiliarEmpleado.setValue(null);
		txtCargoEmpleado.setValue("");
		txtUnidadEmpleado.setValue("");
		txtNombreEmpleado.setValue("");
		txtFichaEmpleado.setValue("");
		txtFichaSupervisorEmpleado.setValue("");
		catalogo.limpiarSeleccion();
		txtEmpresaEmpleado.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtEmpresaEmpleado.getText().compareTo("") != 0
				|| txtCargoEmpleado.getText().compareTo("") != 0
				|| txtUnidadEmpleado.getText().compareTo("") != 0
				|| txtNombreEmpleado.getText().compareTo("") != 0
				|| txtFichaEmpleado.getText().compareTo("") != 0
				|| txtFichaSupervisorEmpleado.getText().compareTo("") != 0
				|| spnGradoAuxiliarEmpleado.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroEmpleado")
	public void abrirRegistro() {
		gpxDatosEmpleado.setOpen(false);
		gpxRegistroEmpleado.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosEmpleado")
	public void abrirCatalogo() {
		gpxDatosEmpleado.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosEmpleado.setOpen(false);
								gpxRegistroEmpleado.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosEmpleado.setOpen(true);
									gpxRegistroEmpleado.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosEmpleado.setOpen(true);
			gpxRegistroEmpleado.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Empleado> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtEmpresaEmpleado.getText().compareTo("") == 0
				|| txtCargoEmpleado.getText().compareTo("") == 0
				|| txtUnidadEmpleado.getText().compareTo("") == 0
				|| txtFichaSupervisorEmpleado.getText().compareTo("") == 0) {
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

		final List<Empleado> listEmpleado = servicioEmpleado.buscarTodos();
		catalogo = new Catalogo<Empleado>(catalogoEmpleado,
				"Catalogo de Empleados", listEmpleado, "Empresa", "Cargo",
				"Unidad Organizativa", "Nombre", "Ficha", "Ficha Supervisor",
				"Grado Auxiliar") {

			@Override
			protected List<Empleado> buscarCampos(List<String> valores) {
				List<Empleado> lista = new ArrayList<Empleado>();

				for (Empleado empleado : listEmpleado) {
					if (empleado.getEmpresa().getNombre().toLowerCase()
							.startsWith(valores.get(0))
							&& empleado.getCargo().getDescripcion()
									.toLowerCase().startsWith(valores.get(1))
							&& empleado.getUnidadOrganizativa()
									.getDescripcion().toLowerCase()
									.startsWith(valores.get(2))
							&& empleado.getNombre().toLowerCase()
									.startsWith(valores.get(3))
							&& empleado.getFicha().toLowerCase()
									.startsWith(valores.get(4))
							&& empleado.getFichaSupervisor().toLowerCase()
									.startsWith(valores.get(5))
							&& String.valueOf(empleado.getGradoAuxiliar())
									.toLowerCase().startsWith(valores.get(6))) {
						lista.add(empleado);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Empleado empleado) {
				String[] registros = new String[8];
				registros[0] = empleado.getEmpresa().getNombre();
				registros[1] = empleado.getCargo().getDescripcion();
				registros[2] = empleado.getUnidadOrganizativa()
						.getDescripcion();
				registros[3] = empleado.getNombre();
				registros[4] = empleado.getFicha();
				registros[5] = empleado.getFichaSupervisor();
				registros[6] = String.valueOf(empleado.getGradoAuxiliar());

				return registros;
			}

			@Override
			protected List<Empleado> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("Empresa"))
					return servicioEmpleado.filtroEmpresa(valor);
				else if (combo.equals("Cargo"))
					return servicioEmpleado.filtroCargo(valor);
				else if (combo.equals("Unidad Organizativa"))
					return servicioEmpleado.filtroUnidadOrganizativa(valor);
				else if (combo.equals("Nombre"))
					return servicioEmpleado.filtroNombre(valor);
				else if (combo.equals("Ficha"))
					return servicioEmpleado.filtroFicha(valor);
				else if (combo.equals("Ficha Supervisor"))
					return servicioEmpleado.filtroFichaSupervisor(valor);
				else if (combo.equals("Grado Auxiliar"))
					return servicioEmpleado.filtroGradoAuxiliar(valor);
				else
					return servicioEmpleado.buscarTodos();
			}

		};
		catalogo.setParent(catalogoEmpleado);

	}

	@Listen("onChange = #txtEmpresaEmpleado")
	public void buscarEmpresa() {
		List<Empresa> empresas = servicioEmpresa
				.buscarPorNombres(txtEmpresaEmpleado.getValue());
		if (empresas.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoEmpresa);
			txtEmpresaEmpleado.setFocus(true);
		} else {

			idEmpresa = empresas.get(0).getId();
		}

	}

	@Listen("onChange = #txtCargoEmpleado")
	public void buscarCargo() {
		List<Cargo> cargo = servicioCargo.buscarPorNombres(txtCargoEmpleado
				.getValue());
		if (cargo.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoCargo);
			txtCargoEmpleado.setFocus(true);
		} else {

			idCargo = cargo.get(0).getId();
		}

	}

	@Listen("onChange = #txtUnidadEmpleado")
	public void buscarUnidadOrganizativa() {
		List<UnidadOrganizativa> unidades = servicioUnidadOrganizativa
				.buscarPorNombres(txtUnidadEmpleado.getValue());
		if (unidades.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoUnidad);
			txtUnidadEmpleado.setFocus(true);
		} else {

			idUnidad = unidades.get(0).getId();
		}

	}

	@Listen("onChange = #txtFichaSupervisorEmpleado")
	public void buscarSupervisor() {
		List<Empleado> empleados = servicioEmpleado
				.buscarPorNombres(txtFichaSupervisorEmpleado.getValue());
		if (empleados.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoSupervisor);
			txtFichaSupervisorEmpleado.setFocus(true);
		} else {

			idFichaSupervisor = empleados.get(0).getId();
		}

	}

	@Listen("onClick = #btnBuscarEmpresa")
	public void mostrarCatalogoEmpresa() {
		final List<Empresa> listEmpresa = servicioEmpresa.buscarTodas();
		catalogoEmpresa = new Catalogo<Empresa>(divCatalogoEmpresa,
				"Catalogo de Empresas", listEmpresa, "Nombre", "Direcci�n",
				"Tel�fono 1", "Tel�fono 2", "Empresa Auxiliar") {

			@Override
			protected List<Empresa> buscarCampos(List<String> valores) {
				List<Empresa> lista = new ArrayList<Empresa>();

				for (Empresa empresa : listEmpresa) {
					if (empresa.getNombre().toLowerCase()
							.startsWith(valores.get(0))
							&& empresa.getDireccion().toLowerCase()
									.startsWith(valores.get(1))
							&& empresa.getTelefono1().toLowerCase()
									.startsWith(valores.get(2))
							&& empresa.getTelefono2().toLowerCase()
									.startsWith(valores.get(3))
							&& empresa.getIdEmpresaAuxiliar().toLowerCase()
									.startsWith(valores.get(4))) {
						lista.add(empresa);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Empresa empresa) {
				String[] registros = new String[5];
				registros[0] = empresa.getNombre();
				registros[1] = empresa.getDireccion();
				registros[2] = empresa.getTelefono1();
				registros[3] = empresa.getTelefono2();
				registros[4] = empresa.getIdEmpresaAuxiliar();

				return registros;
			}

			@Override
			protected List<Empresa> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("Nombre"))
					return servicioEmpresa.filtroNombre(valor);
				else if (combo.equals("Direcci�n"))
					return servicioEmpresa.filtroDireccion(valor);
				else if (combo.equals("Tel�fono 1"))
					return servicioEmpresa.filtroTelefono1(valor);
				else if (combo.equals("Tel�fono 2"))
					return servicioEmpresa.filtroTelefono2(valor);
				else if (combo.equals("Empresa Auxiliar"))
					return servicioEmpresa.filtroEmpresaAuxiliar(valor);
				else
					return servicioEmpresa.buscarTodas();
			}

		};

		catalogoEmpresa.setClosable(true);
		catalogoEmpresa.setWidth("80%");
		catalogoEmpresa.setParent(divCatalogoEmpresa);
		catalogoEmpresa.doModal();
	}

	@Listen("onSeleccion = #divCatalogoEmpresa")
	public void seleccionEmpresa() {
		Empresa empresa = catalogoEmpresa.objetoSeleccionadoDelCatalogo();
		idEmpresa = empresa.getId();
		txtEmpresaEmpleado.setValue(empresa.getNombre());
		catalogoEmpresa.setParent(null);
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
		txtCargoEmpleado.setValue(cargo.getDescripcion());
		catalogoCargo.setParent(null);
	}

	@Listen("onClick = #btnBuscarUnidad")
	public void mostrarCatalogoUnidad() {
		final List<UnidadOrganizativa> listUnidadOrganizativa = servicioUnidadOrganizativa
				.buscarTodas();
		catalogoUnidad = new Catalogo<UnidadOrganizativa>(divCatalogoUnidad,
				"Catalogo de UnidadOrganizativas", listUnidadOrganizativa,
				"Gerencia", "Descripci�n", "Nivel", "Sub-Nivel",
				"Empresa Auxiliar", "Unidad Auxiliar") {

			@Override
			protected List<UnidadOrganizativa> buscarCampos(List<String> valores) {
				List<UnidadOrganizativa> lista = new ArrayList<UnidadOrganizativa>();

				for (UnidadOrganizativa unidad : listUnidadOrganizativa) {
					if (unidad.getGerencia().getDescripcion().toLowerCase()
							.startsWith(valores.get(0))
							&& unidad.getDescripcion().toLowerCase()
									.startsWith(valores.get(1))
							&& String.valueOf(unidad.getNivel()).toLowerCase()
									.startsWith(valores.get(2))
							&& String.valueOf(unidad.getSubNivel())
									.toLowerCase().startsWith(valores.get(3))
							&& unidad.getIdEmpresaAuxiliar().toLowerCase()
									.startsWith(valores.get(4))
							&& unidad.getIdUnidadOrganizativaAuxiliar()
									.toLowerCase().startsWith(valores.get(5))) {
						lista.add(unidad);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(UnidadOrganizativa unidad) {
				String[] registros = new String[6];
				registros[0] = unidad.getGerencia().getDescripcion();
				registros[1] = unidad.getDescripcion();
				registros[2] = String.valueOf(unidad.getNivel());
				registros[3] = String.valueOf(unidad.getSubNivel());
				registros[4] = String.valueOf(unidad.getIdEmpresaAuxiliar());
				registros[5] = String.valueOf(unidad
						.getIdUnidadOrganizativaAuxiliar());
				return registros;
			}

			@Override
			protected List<UnidadOrganizativa> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("Gerencia"))
					return servicioUnidadOrganizativa.filtroGerencia(valor);
				else if (combo.equals("Descripci�n"))
					return servicioUnidadOrganizativa.filtroDescripcion(valor);
				else if (combo.equals("Nivel"))
					return servicioUnidadOrganizativa.filtroNivel(valor);
				else if (combo.equals("Sub-Nivel"))
					return servicioUnidadOrganizativa.filtroSubNivel(valor);
				else if (combo.equals("Empresa Auxiliar"))
					return servicioUnidadOrganizativa
							.filtroEmpresaAuxiliar(valor);
				else if (combo.equals("Unidad Auxiliar"))
					return servicioUnidadOrganizativa
							.filtroUnidadAuxiliar(valor);
				else
					return servicioUnidadOrganizativa.buscarTodas();
			}

		};

		catalogoUnidad.setClosable(true);
		catalogoUnidad.setWidth("80%");
		catalogoUnidad.setParent(divCatalogoUnidad);
		catalogoUnidad.doModal();
	}

	@Listen("onSeleccion = #divCatalogoUnidad")
	public void seleccionUnidad() {
		UnidadOrganizativa unidad = catalogoUnidad
				.objetoSeleccionadoDelCatalogo();
		idUnidad = unidad.getId();
		txtUnidadEmpleado.setValue(unidad.getDescripcion());
		catalogoUnidad.setParent(null);
	}

	@Listen("onClick = #btnBuscarSupervisor")
	public void mostrarCatalogoSupervisor() {
		final List<Empleado> listEmpleado = servicioEmpleado.buscarTodos();
		catalogoSupervisor = new Catalogo<Empleado>(divCatalogoSupervisor,
				"Catalogo Empleados", listEmpleado, "Empresa", "Cargo",
				"Unidad Organizativa", "Nombre", "Ficha", "Ficha Supervisor",
				"Grado Auxiliar") {

			@Override
			protected List<Empleado> buscarCampos(List<String> valores) {
				List<Empleado> lista = new ArrayList<Empleado>();

				for (Empleado empleado : listEmpleado) {
					if (empleado.getEmpresa().getNombre().toLowerCase()
							.startsWith(valores.get(0))
							&& empleado.getCargo().getDescripcion()
									.toLowerCase().startsWith(valores.get(1))
							&& empleado.getUnidadOrganizativa()
									.getDescripcion().toLowerCase()
									.startsWith(valores.get(2))
							&& empleado.getNombre().toLowerCase()
									.startsWith(valores.get(3))
							&& empleado.getFicha().toLowerCase()
									.startsWith(valores.get(4))
							&& empleado.getFichaSupervisor().toLowerCase()
									.startsWith(valores.get(5))
							&& String.valueOf(empleado.getGradoAuxiliar())
									.toLowerCase().startsWith(valores.get(6))) {
						lista.add(empleado);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Empleado empleado) {
				String[] registros = new String[7];
				registros[0] = empleado.getEmpresa().getNombre();
				registros[1] = empleado.getCargo().getDescripcion();
				registros[2] = empleado.getUnidadOrganizativa()
						.getDescripcion();
				registros[3] = empleado.getNombre();
				registros[5] = empleado.getFicha();
				registros[6] = empleado.getFichaSupervisor();
				registros[6] = String.valueOf(empleado.getGradoAuxiliar());

				return registros;
			}

			@Override
			protected List<Empleado> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("Empresa"))
					return servicioEmpleado.filtroEmpresa(valor);
				else if (combo.equals("Cargo"))
					return servicioEmpleado.filtroCargo(valor);
				else if (combo.equals("Unidad Organizativa"))
					return servicioEmpleado.filtroUnidadOrganizativa(valor);
				else if (combo.equals("Nombre"))
					return servicioEmpleado.filtroNombre(valor);
				else if (combo.equals("Ficha"))
					return servicioEmpleado.filtroFicha(valor);
				else if (combo.equals("Ficha Supervisor"))
					return servicioEmpleado.filtroFichaSupervisor(valor);
				else if (combo.equals("Grado Auxiliar"))
					return servicioEmpleado.filtroGradoAuxiliar(valor);
				else
					return servicioEmpleado.buscarTodos();
			}

		};

		catalogoSupervisor.setClosable(true);
		catalogoSupervisor.setWidth("80%");
		catalogoSupervisor.setParent(divCatalogoSupervisor);
		catalogoSupervisor.doModal();
	}

	@Listen("onSeleccion = #divCatalogoSupervisor")
	public void seleccionSupervisor() {
		Empleado empleado = catalogoSupervisor.objetoSeleccionadoDelCatalogo();
		idFichaSupervisor = Integer.parseInt(empleado.getFichaSupervisor());
		txtFichaSupervisorEmpleado.setValue(empleado.getNombre());
		catalogoSupervisor.setParent(null);
	}

}
