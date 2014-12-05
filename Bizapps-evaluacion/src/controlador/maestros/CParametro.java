package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Parametro;

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

public class CParametro extends CGenerico {

	@Wire
	private Window wdwVParametro;
	@Wire
	private Div botoneraParametro;
	@Wire
	private Groupbox gpxRegistroParametro;
	@Wire
	private Textbox txtDescripcionParametro;
	@Wire
	private Combobox cmbTipoParametro;
	@Wire
	private Groupbox gpxDatosParametro;
	@Wire
	private Div catalogoParametro;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idParametro = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Parametro> catalogo;

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
		txtDescripcionParametro.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Parametro parametro = catalogo
								.objetoSeleccionadoDelCatalogo();
						idParametro = parametro.getId();
						txtDescripcionParametro.setValue(parametro
								.getDescripcion());
						cmbTipoParametro.setValue(parametro.getTipo());
						txtDescripcionParametro.setFocus(true);
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
					String descripcion = txtDescripcionParametro
							.getValue();
					String tipo = cmbTipoParametro.getValue();
					String usuario = nombreUsuarioSesion();
					Timestamp fechaAuditoria = new Timestamp(
							new Date().getTime());
					Parametro parametro = new Parametro(idParametro,
							descripcion, tipo, fechaAuditoria, horaAuditoria,
							usuario);
					servicioParametro.guardar(parametro);
					msj.mensajeInformacion(Mensaje.guardado);
					limpiar();
					catalogo.actualizarLista(servicioParametro.buscarTodos());
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
				cerrarVentana(wdwVParametro, "Parametro de Evaluacion",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosParametro.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Parametro> eliminarLista = catalogo
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
													servicioParametro
															.eliminarVariosParametros(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioParametro
															.buscarTodos());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idParametro != 0) {
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
													servicioParametro
															.eliminarUnParametro(idParametro);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioParametro
															.buscarTodos());
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
				
			}

			@Override
			public void annadir() {
				// TODO Auto-generated method stub
				
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
		botoneraParametro.appendChild(botonera);

	}

	public void limpiarCampos() {
		idParametro = 0;
		txtDescripcionParametro.setValue("");
		cmbTipoParametro.setValue("");
		catalogo.limpiarSeleccion();
		txtDescripcionParametro.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtDescripcionParametro.getText().compareTo("") != 0
				|| cmbTipoParametro.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroParametro")
	public void abrirRegistro() {
		gpxDatosParametro.setOpen(false);
		gpxRegistroParametro.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosParametro")
	public void abrirCatalogo() {
		gpxDatosParametro.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosParametro.setOpen(false);
								gpxRegistroParametro.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosParametro.setOpen(true);
									gpxRegistroParametro.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosParametro.setOpen(true);
			gpxRegistroParametro.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Parametro> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtDescripcionParametro.getText().compareTo("") == 0
				|| cmbTipoParametro.getText().compareTo("") == 0) {
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

		final List<Parametro> listParametro = servicioParametro.buscarTodos();
		catalogo = new Catalogo<Parametro>(catalogoParametro,
				"Catalogo de Parametros", listParametro,false,false,false, "Descripción", "Tipo") {

			@Override
			protected List<Parametro> buscar(List<String> valores) {
				List<Parametro> lista = new ArrayList<Parametro>();

				for (Parametro parametro : listParametro) {
					if (parametro.getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& parametro.getTipo().toLowerCase()
							.contains(valores.get(1).toLowerCase())) {
						lista.add(parametro);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Parametro parametro) {
				String[] registros = new String[2];
				registros[0] = parametro.getDescripcion();
				registros[1] = parametro.getTipo();

				return registros;
			}

		};
		catalogo.setParent(catalogoParametro);

	}

}
