package controlador.maestros;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Cargo;

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

public class CCargo extends CGenerico {

	@Wire
	private Window wdwVCargo;
	@Wire
	private Div botoneraCargo;
	@Wire
	private Groupbox gpxRegistroCargo;
	@Wire
	private Textbox txtDescripcionCargo;
	@Wire
	private Textbox txtNivel;
	@Wire
	private Textbox txtIdioma;
	@Wire
	private Textbox txtObservaciones;
	@Wire
	private Textbox txtNominaCargo;
	@Wire
	private Textbox txtCargoAuxiliarCargo;
	@Wire
	private Textbox txtEmpresaAuxiliarCargo;
	@Wire
	private Groupbox gpxDatosCargo;
	@Wire
	private Div catalogoCargo;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idCargo = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Cargo> catalogo;
	protected List<Cargo> listaGeneral = new ArrayList<Cargo>();

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
		txtDescripcionCargo.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Cargo cargo = catalogo.objetoSeleccionadoDelCatalogo();
						idCargo = cargo.getId();
						txtDescripcionCargo.setValue(cargo.getDescripcion());
						txtNominaCargo.setValue(cargo.getNomina());
						if (cargo.getIdioma() != null)
							txtIdioma.setValue(cargo.getIdioma());
						else
							txtIdioma.setValue("");
						if (cargo.getNivelAcademico() != null)
							txtNivel.setValue(cargo.getNivelAcademico());
						else
							txtNivel.setValue("");
						if (cargo.getObservaciones() != null)
							txtObservaciones.setValue(cargo.getObservaciones());
						else
							txtObservaciones.setValue("");
						txtCargoAuxiliarCargo.setValue(cargo
								.getIdCargoAuxiliar());
						txtEmpresaAuxiliarCargo.setValue(cargo
								.getIdEmpresaAuxiliar());
						txtDescripcionCargo.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}

			}

			@Override
			public void guardar() {
				if (validar()) {
					String descripcion = txtDescripcionCargo.getValue();
					String nomina = txtNominaCargo.getValue();
					String idCargoAuxiliar = txtCargoAuxiliarCargo.getValue();
					String idEmpresaAuxiliar = txtEmpresaAuxiliarCargo
							.getValue();
					String usuario = nombreUsuarioSesion();
					Timestamp fechaAuditoria = new Timestamp(
							new Date().getTime());
					Cargo cargo = new Cargo(idCargo, descripcion,
							fechaAuditoria, horaAuditoria, idCargoAuxiliar,
							idEmpresaAuxiliar, nomina, usuario);
					cargo.setIdioma(txtIdioma.getValue());
					cargo.setObservaciones(txtObservaciones.getValue());
					cargo.setNivelAcademico(txtNivel.getValue());
					servicioCargo.guardar(cargo);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					listaGeneral = servicioCargo.buscarTodos();
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
				cerrarVentana2(wdwVCargo, titulo, tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosCargo.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Cargo> eliminarLista = catalogo
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
													servicioCargo
															.eliminarVariosCargos(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													listaGeneral = servicioCargo
															.buscarTodos();
													catalogo.actualizarLista(listaGeneral);
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idCargo != 0) {
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
													servicioCargo
															.eliminarUnCargo(idCargo);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													listaGeneral = servicioCargo
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
		botoneraCargo.appendChild(botonera);

	}

	public void limpiarCampos() {
		idCargo = 0;
		txtDescripcionCargo.setValue("");
		txtNominaCargo.setValue("");
		txtCargoAuxiliarCargo.setValue("");
		txtEmpresaAuxiliarCargo.setValue("");
		catalogo.limpiarSeleccion();
		txtDescripcionCargo.setFocus(true);
		txtIdioma.setValue("");
		txtNivel.setValue("");
		txtObservaciones.setValue("");
	}

	public boolean camposEditando() {
		if (txtDescripcionCargo.getText().compareTo("") != 0
				|| txtNominaCargo.getText().compareTo("") != 0
				|| txtCargoAuxiliarCargo.getText().compareTo("") != 0
				|| txtEmpresaAuxiliarCargo.getText().compareTo("") != 0
				|| txtIdioma.getText().compareTo("") != 0
				|| txtNivel.getText().compareTo("") != 0
				|| txtObservaciones.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroCargo")
	public void abrirRegistro() {
		gpxDatosCargo.setOpen(false);
		gpxRegistroCargo.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosCargo")
	public void abrirCatalogo() {
		gpxDatosCargo.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosCargo.setOpen(false);
								gpxRegistroCargo.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosCargo.setOpen(true);
									gpxRegistroCargo.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosCargo.setOpen(true);
			gpxRegistroCargo.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Cargo> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtDescripcionCargo.getText().compareTo("") == 0) {
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

		listaGeneral = servicioCargo.buscarTodos();
		catalogo = new Catalogo<Cargo>(catalogoCargo, "Catalogo de Cargos",
				listaGeneral, false, false, false, "Descripci�n", "N�mina",
				"Cargo Auxiliar", "Empresa Auxiliar") {

			@Override
			protected List<Cargo> buscar(List<String> valores) {
				List<Cargo> lista = new ArrayList<Cargo>();

				for (Cargo cargo : listaGeneral) {
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
		catalogo.setParent(catalogoCargo);

	}

}
