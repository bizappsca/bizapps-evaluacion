package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.maestros.Cargo;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CCargo extends CGenerico {

	@Wire
	private Div divVCargo;
	@Wire
	private Div botoneraCargo;
	@Wire
	private Groupbox gpxRegistroCargo;
	@Wire
	private Textbox txtDescripcionCargo;
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

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub

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
				// TODO Auto-generated method stub

				String descripcion = txtDescripcionCargo.getValue();
				String nomina = txtNominaCargo.getValue();
				String idCargoAuxiliar = txtCargoAuxiliarCargo.getValue();
				String idEmpresaAuxiliar = txtEmpresaAuxiliarCargo.getValue();
				String usuario = nombreUsuarioSesion();
				Timestamp fechaAuditoria = new Timestamp(new Date().getTime());
				Cargo cargo = new Cargo(idCargo, descripcion, fechaAuditoria,
						horaAuditoria, idCargoAuxiliar,
						idEmpresaAuxiliar, nomina, usuario);
				servicioCargo.guardar(cargo);
				msj.mensajeInformacion(Mensaje.guardado);
				limpiar();
				catalogo.actualizarLista(servicioCargo.buscarTodos());

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
				cerrarVentana(divVCargo, "Cargo");
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
													catalogo.actualizarLista(servicioCargo
															.buscarTodos());
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
													catalogo.actualizarLista(servicioCargo
															.buscarTodos());
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

	}

	public boolean camposEditando() {
		if (txtDescripcionCargo.getText().compareTo("") != 0
				|| txtNominaCargo.getText().compareTo("") != 0
				|| txtCargoAuxiliarCargo.getText().compareTo("") != 0
				|| txtEmpresaAuxiliarCargo.getText().compareTo("") != 0) {
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

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(3).setVisible(!bol);

	}

	public void mostrarCatalogo() {

		final List<Cargo> listCargo = servicioCargo.buscarTodos();
		catalogo = new Catalogo<Cargo>(catalogoCargo, "Catalogo de Cargos",
				listCargo, "C�digo cargo", "Descripci�n", "N�mina",
				"Cargo Auxiliar", "Empresa Auxiliar") {

			@Override
			protected List<Cargo> buscarCampos(List<String> valores) {
				List<Cargo> lista = new ArrayList<Cargo>();

				for (Cargo cargo : listCargo) {
					if (String.valueOf(cargo.getId()).toLowerCase()
							.startsWith(valores.get(0))
							&& cargo.getDescripcion().toLowerCase()
									.startsWith(valores.get(1))
							&& cargo.getNomina().toLowerCase()
							.startsWith(valores.get(2))
							&& cargo.getIdCargoAuxiliar().toLowerCase()
							.startsWith(valores.get(3))
							&& cargo.getIdEmpresaAuxiliar().toLowerCase()
							.startsWith(valores.get(4))) {
						lista.add(cargo);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Cargo cargo) {
				String[] registros = new String[5];
				registros[0] = String.valueOf(cargo.getId());
				registros[1] = cargo.getDescripcion();
				registros[2] = cargo.getNomina();
				registros[3] = cargo.getIdCargoAuxiliar();
				registros[4] = cargo.getIdEmpresaAuxiliar();

				return registros;
			}

			@Override
			protected List<Cargo> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("C�digo cargo"))
					return servicioCargo.filtroId(valor);
				else if (combo.equals("Descripci�n"))
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
		catalogo.setParent(catalogoCargo);

	}

}
