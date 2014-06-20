package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelos.Empresa;

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

public class CEmpresa extends CGenerico {

	@Wire
	private Div divVEmpresa;
	@Wire
	private Div botoneraEmpresa;
	@Wire
	private Groupbox gpxRegistroEmpresa;
	@Wire
	private Textbox txtNombreEmpresa;
	@Wire
	private Textbox txtDireccionEmpresa;
	@Wire
	private Textbox txtTelefono1Empresa;
	@Wire
	private Textbox txtTelefono2Empresa;
	@Wire
	private Textbox txtEmpresaAuxiliarEmpresa;
	@Wire
	private Groupbox gpxDatosEmpresa;
	@Wire
	private Div catalogoEmpresa;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idEmpresa = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Empresa> catalogo;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub

		txtNombreEmpresa.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Empresa empresa = catalogo
								.objetoSeleccionadoDelCatalogo();
						idEmpresa = empresa.getIdEmpresa();
						txtNombreEmpresa.setValue(empresa.getNombre());
						txtDireccionEmpresa.setValue(empresa.getDireccion());
						txtTelefono1Empresa.setValue(empresa.getTelefono1());
						txtTelefono2Empresa.setValue(empresa.getTelefono2());
						txtEmpresaAuxiliarEmpresa.setValue(empresa
								.getIdEmpresaAuxiliar());
						txtNombreEmpresa.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}

			}

			@Override
			public void guardar() {
				// TODO Auto-generated method stub

				String nombre = txtNombreEmpresa.getValue();
				String direccion = txtDireccionEmpresa.getValue();
				String telefono1 = txtTelefono1Empresa.getValue();
				String telefono2 = txtTelefono2Empresa.getValue();
				String idEmpresaAuxiliar = txtEmpresaAuxiliarEmpresa.getValue();
				String usuario = "JDE";
				Timestamp fechaAuditoria = new Timestamp(new Date().getTime());
				Empresa empresa = new Empresa(idEmpresa,direccion, fechaAuditoria,
						 horaAuditoria, idEmpresaAuxiliar, nombre,
						telefono1, telefono2, usuario);
				servicioEmpresa.guardar(empresa);
				msj.mensajeInformacion(Mensaje.guardado);
				limpiar();
				catalogo.actualizarLista(servicioEmpresa.buscarTodas());

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
				cerrarVentana(divVEmpresa, "Empresa");
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosEmpresa.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Empresa> eliminarLista = catalogo
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
													servicioEmpresa
															.eliminarVariasEmpresas(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioEmpresa
															.buscarTodas());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idEmpresa != 0) {
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
													servicioEmpresa
															.eliminarUnaEmpresa(idEmpresa);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioEmpresa
															.buscarTodas());
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
		botoneraEmpresa.appendChild(botonera);

	}

	public void limpiarCampos() {
		idEmpresa = 0;
		txtNombreEmpresa.setValue("");
		txtDireccionEmpresa.setValue("");
		txtTelefono1Empresa.setValue("");
		txtTelefono2Empresa.setValue("");
		txtEmpresaAuxiliarEmpresa.setValue("");
		catalogo.limpiarSeleccion();
		txtNombreEmpresa.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtNombreEmpresa.getText().compareTo("") != 0
				|| txtDireccionEmpresa.getText().compareTo("") != 0
				|| txtTelefono1Empresa.getText().compareTo("") != 0
				|| txtTelefono2Empresa.getText().compareTo("") != 0
				|| txtEmpresaAuxiliarEmpresa.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroEmpresa")
	public void abrirRegistro() {
		gpxDatosEmpresa.setOpen(false);
		gpxRegistroEmpresa.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosEmpresa")
	public void abrirCatalogo() {
		gpxDatosEmpresa.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosEmpresa.setOpen(false);
								gpxRegistroEmpresa.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosEmpresa.setOpen(true);
									gpxRegistroEmpresa.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosEmpresa.setOpen(true);
			gpxRegistroEmpresa.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Empresa> seleccionados = catalogo.obtenerSeleccionados();
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

		final List<Empresa> listEmpresa = servicioEmpresa.buscarTodas();
		catalogo = new Catalogo<Empresa>(catalogoEmpresa,
				"Catalogo de Empresas", listEmpresa, "C�digo empresa",
				"Nombre", "Direcci�n", "Tel�fono 1", "Tel�fono 2",
				"Empresa Auxiliar") {

			@Override
			protected List<Empresa> buscarCampos(List<String> valores) {
				List<Empresa> lista = new ArrayList<Empresa>();

				for (Empresa empresa : listEmpresa) {
					if (String.valueOf(empresa.getIdEmpresa()).toLowerCase()
							.startsWith(valores.get(0))
							&& empresa.getNombre().toLowerCase()
									.startsWith(valores.get(1))
							&& empresa.getDireccion().toLowerCase()
									.startsWith(valores.get(2))
							&& empresa.getTelefono1().toLowerCase()
									.startsWith(valores.get(3))
							&& empresa.getTelefono2().toLowerCase()
									.startsWith(valores.get(4))
							&& empresa.getIdEmpresaAuxiliar().toLowerCase()
									.startsWith(valores.get(5))) {
						lista.add(empresa);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Empresa empresa) {
				String[] registros = new String[6];
				registros[0] = String.valueOf(empresa.getIdEmpresa());
				registros[1] = empresa.getNombre();
				registros[2] = empresa.getDireccion();
				registros[3] = empresa.getTelefono1();
				registros[4] = empresa.getTelefono2();
				registros[5] = empresa.getIdEmpresaAuxiliar();

				return registros;
			}

			@Override
			protected List<Empresa> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				return null;
			}

		};
		catalogo.setParent(catalogoEmpresa);

	}

}