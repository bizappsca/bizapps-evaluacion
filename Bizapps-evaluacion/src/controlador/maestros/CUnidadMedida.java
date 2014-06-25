package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelo.maestros.UnidadMedida;

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

public class CUnidadMedida extends CGenerico {

	@Wire
	private Div divVUnidadMedida;
	@Wire
	private Div botoneraUnidadMedida;
	@Wire
	private Groupbox gpxRegistroUnidadMedida;
	@Wire
	private Textbox txtDescripcionUnidadMedida;
	@Wire
	private Groupbox gpxDatosUnidadMedida;
	@Wire
	private Div catalogoUnidadMedida;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idUnidadMedida = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<UnidadMedida> catalogo;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub

		txtDescripcionUnidadMedida.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						UnidadMedida unidadMedida = catalogo.objetoSeleccionadoDelCatalogo();
						idUnidadMedida = unidadMedida.getId();
						txtDescripcionUnidadMedida.setValue(unidadMedida.getDescripcion());
						txtDescripcionUnidadMedida.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}


			}

			@Override
			public void guardar() {
				// TODO Auto-generated method stub
				
				String descripcion = txtDescripcionUnidadMedida.getValue();
				String usuario = nombreUsuarioSesion();
				Timestamp fechaAuditoria = new Timestamp(new Date().getTime());
				UnidadMedida unidadMedida = new UnidadMedida(idUnidadMedida,descripcion,usuario,
						fechaAuditoria, horaAuditoria);
				servicioUnidadMedida.guardar(unidadMedida);
				msj.mensajeInformacion(Mensaje.guardado);
				limpiar();
				catalogo.actualizarLista(servicioUnidadMedida.buscarTodas());
				
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
				cerrarVentana(divVUnidadMedida, "Unidad de Medida");
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosUnidadMedida.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<UnidadMedida> eliminarLista = catalogo
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
													servicioUnidadMedida
															.eliminarVariasUnidades(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioUnidadMedida
															.buscarTodas());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idUnidadMedida != 0) {
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
													servicioUnidadMedida
															.eliminarUnaUnidad(idUnidadMedida);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioUnidadMedida.buscarTodas());
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
		botoneraUnidadMedida.appendChild(botonera);

	}

	public void limpiarCampos() {
		idUnidadMedida = 0;
		txtDescripcionUnidadMedida.setValue("");
		catalogo.limpiarSeleccion();
		txtDescripcionUnidadMedida.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtDescripcionUnidadMedida.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}
	
	
	@Listen("onClick = #gpxRegistroUnidadMedida")
	public void abrirRegistro() {
		gpxDatosUnidadMedida.setOpen(false);
		gpxRegistroUnidadMedida.setOpen(true);
		mostrarBotones(false);

	}
	
	@Listen("onOpen = #gpxDatosUnidadMedida")
	public void abrirCatalogo() {
		gpxDatosUnidadMedida.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosUnidadMedida.setOpen(false);
								gpxRegistroUnidadMedida.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosUnidadMedida.setOpen(true);
									gpxRegistroUnidadMedida.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosUnidadMedida.setOpen(true);
			gpxRegistroUnidadMedida.setOpen(false);
			mostrarBotones(true);
		}
	}
	
	

	public boolean validarSeleccion() {
		List<UnidadMedida> seleccionados = catalogo.obtenerSeleccionados();
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

		final List<UnidadMedida> listUnidadMedida = servicioUnidadMedida.buscarTodas();
		catalogo = new Catalogo<UnidadMedida>(catalogoUnidadMedida, "Catalogo de Unidades de Medidas",
				listUnidadMedida, "C�digo Unidad Medida", "Descripci�n") {

			@Override
			protected List<UnidadMedida> buscarCampos(List<String> valores) {
				List<UnidadMedida> lista = new ArrayList<UnidadMedida>();

				for (UnidadMedida unidadMedida : listUnidadMedida) {
					if (String.valueOf(unidadMedida.getId()).toLowerCase()
							.startsWith(valores.get(0))
							&& unidadMedida.getDescripcion().toLowerCase()
									.startsWith(valores.get(1))) {
						lista.add(unidadMedida);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(UnidadMedida unidadMedida) {
				String[] registros = new String[2];
				registros[0] = String.valueOf(unidadMedida.getId());
				registros[1] = unidadMedida.getDescripcion();

				return registros;
			}

			@Override
			protected List<UnidadMedida> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("C�digo Unidad Medida"))
					return servicioUnidadMedida.filtroId(valor);
				else if (combo.equals("Descripci�n"))
					return servicioUnidadMedida.filtroDescripcion(valor);
				else
					return servicioUnidadMedida.buscarTodas();
			}

		};
		catalogo.setParent(catalogoUnidadMedida);

	}

}
