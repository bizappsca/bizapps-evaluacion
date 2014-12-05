package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Gerencia;
import modelo.maestros.UnidadOrganizativa;

import org.zkoss.zk.ui.Sessions;
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
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CUnidadOrganizativa extends CGenerico {

	@Wire
	private Window wdwVUnidadOrganizativa;
	@Wire
	private Div botoneraUnidadOrganizativa;
	@Wire
	private Groupbox gpxRegistroUnidadOrganizativa;
	@Wire
	private Textbox txtGerenciaUnidadOrganizativa;
	@Wire
	private Button btnBuscarGerencia;
	@Wire
	private Textbox txtDescripcionUnidadOrganizativa;
	@Wire
	private Spinner spnNivelUnidadOrganizativa;
	@Wire
	private Spinner spnSubNivelUnidadOrganizativa;
	@Wire
	private Textbox txtEmpresaAuxiliarUnidadOrganizativa;
	@Wire
	private Textbox txtUnidadOrganizativaAuxiliar;
	@Wire
	private Groupbox gpxDatosUnidadOrganizativa;
	@Wire
	private Div catalogoUnidadOrganizativa;
	@Wire
	private Label lblGerenciaUnidadOrganizativa;
	@Wire
	private Div divCatalogoGerencia;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idUnidadOrganizativa = 0;
	private int idGerencia = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<UnidadOrganizativa> catalogo;
	Catalogo<Gerencia> catalogoGerencia;
	protected List<UnidadOrganizativa> listaGeneral = new ArrayList<UnidadOrganizativa>();


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
		txtGerenciaUnidadOrganizativa.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						UnidadOrganizativa unidad = catalogo
								.objetoSeleccionadoDelCatalogo();
						idUnidadOrganizativa = unidad.getId();
						idGerencia = unidad.getGerencia().getId();
						txtGerenciaUnidadOrganizativa.setValue(String.valueOf(unidad
								.getGerencia().getId()));
						txtDescripcionUnidadOrganizativa.setValue(unidad
								.getDescripcion());
						lblGerenciaUnidadOrganizativa.setValue(unidad
								.getGerencia().getDescripcion());
						spnNivelUnidadOrganizativa.setValue(unidad.getNivel());
						spnSubNivelUnidadOrganizativa.setValue(unidad
								.getSubNivel());
						txtEmpresaAuxiliarUnidadOrganizativa.setValue(unidad
								.getIdEmpresaAuxiliar());
						txtUnidadOrganizativaAuxiliar.setValue(unidad
								.getIdUnidadOrganizativaAuxiliar());
						txtGerenciaUnidadOrganizativa.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}

			}

			@Override
			public void guardar() {

				if (validar()) {
					String descripcion = txtDescripcionUnidadOrganizativa
							.getValue();
					Gerencia gerencia = servicioGerencia
							.buscarGerencia(idGerencia);

					if (gerencia != null) {

						int nivel = spnNivelUnidadOrganizativa.getValue();

						int subNivel = spnSubNivelUnidadOrganizativa.getValue();
						String idEmpresaAuxiliar = txtEmpresaAuxiliarUnidadOrganizativa
								.getValue();
						String idUnidadOrganizativaAuxiliar = txtUnidadOrganizativaAuxiliar
								.getValue();
						String usuario = nombreUsuarioSesion();
						Timestamp fechaAuditoria = new Timestamp(
								new Date().getTime());
						UnidadOrganizativa unidad = new UnidadOrganizativa(
								idUnidadOrganizativa, descripcion,
								fechaAuditoria, horaAuditoria,
								idEmpresaAuxiliar,
								idUnidadOrganizativaAuxiliar, nivel, subNivel,
								usuario, gerencia);
						servicioUnidadOrganizativa.guardar(unidad);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();
						listaGeneral = servicioUnidadOrganizativa
								.buscarTodas();
						catalogo.actualizarLista(listaGeneral);
						abrirCatalogo();
					} else {

						msj.mensajeAlerta(Mensaje.codigoGerencia);
						txtGerenciaUnidadOrganizativa.setFocus(true);
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
				cerrarVentana2(wdwVUnidadOrganizativa,titulo,tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosUnidadOrganizativa.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<UnidadOrganizativa> eliminarLista = catalogo
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
													servicioUnidadOrganizativa
															.eliminarVariasUnidades(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													listaGeneral = servicioUnidadOrganizativa
															.buscarTodas();
													catalogo.actualizarLista(listaGeneral);
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idUnidadOrganizativa != 0) {
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
													servicioUnidadOrganizativa
															.eliminarUnaUnidad(idUnidadOrganizativa);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													listaGeneral = servicioUnidadOrganizativa
															.buscarTodas();
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
		botoneraUnidadOrganizativa.appendChild(botonera);

	}

	public void limpiarCampos() {
		idUnidadOrganizativa = 0;
		idGerencia = 0;
		txtGerenciaUnidadOrganizativa.setValue("");
		txtDescripcionUnidadOrganizativa.setValue("");
		spnNivelUnidadOrganizativa.setValue(0);
		spnSubNivelUnidadOrganizativa.setValue(0);
		txtEmpresaAuxiliarUnidadOrganizativa.setValue("");
		txtUnidadOrganizativaAuxiliar.setValue("");
		lblGerenciaUnidadOrganizativa.setValue("");
		catalogo.limpiarSeleccion();
		txtGerenciaUnidadOrganizativa.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtGerenciaUnidadOrganizativa.getText().compareTo("") != 0
				|| txtDescripcionUnidadOrganizativa.getText().compareTo("") != 0
				|| spnNivelUnidadOrganizativa.getText().compareTo("0") != 0
				|| spnSubNivelUnidadOrganizativa.getText().compareTo("0") != 0
				|| txtEmpresaAuxiliarUnidadOrganizativa.getText().compareTo("") != 0
				|| txtUnidadOrganizativaAuxiliar.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroUnidadOrganizativa")
	public void abrirRegistro() {
		gpxDatosUnidadOrganizativa.setOpen(false);
		gpxRegistroUnidadOrganizativa.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosUnidadOrganizativa")
	public void abrirCatalogo() {
		gpxDatosUnidadOrganizativa.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosUnidadOrganizativa.setOpen(false);
								gpxRegistroUnidadOrganizativa.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosUnidadOrganizativa.setOpen(true);
									gpxRegistroUnidadOrganizativa
											.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosUnidadOrganizativa.setOpen(true);
			gpxRegistroUnidadOrganizativa.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<UnidadOrganizativa> seleccionados = catalogo
				.obtenerSeleccionados();
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
		if (txtGerenciaUnidadOrganizativa.getText().compareTo("") == 0) {
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

		listaGeneral = servicioUnidadOrganizativa
				.buscarTodas();
		catalogo = new Catalogo<UnidadOrganizativa>(catalogoUnidadOrganizativa,
				"Catalogo de UnidadOrganizativas", listaGeneral,false,false,false,
				"Gerencia", "Descripción", "Nivel", "Sub-Nivel",
				"Empresa Auxiliar", "Unidad Auxiliar") {

			@Override
			protected List<UnidadOrganizativa> buscar(List<String> valores) {
				List<UnidadOrganizativa> lista = new ArrayList<UnidadOrganizativa>();

				for (UnidadOrganizativa unidad : listaGeneral) {
					if (unidad.getGerencia().getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& unidad.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& String.valueOf(unidad.getNivel()).toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& String.valueOf(unidad.getSubNivel())
									.toLowerCase().contains(valores.get(3).toLowerCase())
							&& unidad.getIdEmpresaAuxiliar().toLowerCase()
									.contains(valores.get(4).toLowerCase())
							&& unidad.getIdUnidadOrganizativaAuxiliar()
									.toLowerCase().contains(valores.get(5).toLowerCase())) {
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
		};
		catalogo.setParent(catalogoUnidadOrganizativa);

	}

//	@Listen("onChange = #txtGerenciaUnidadOrganizativa")
//	public void buscarGerencia() {
//		List<Gerencia> gerencias = servicioGerencia
//				.buscarPorNombres(txtGerenciaUnidadOrganizativa.getValue());
//		if (gerencias.size() == 0) {
//			msj.mensajeAlerta(Mensaje.codigoGerencia);
//			txtGerenciaUnidadOrganizativa.setFocus(true);
//		} else {
//
//			idGerencia = gerencias.get(0).getId();
//		}
//
//	}

	@Listen("onClick = #btnBuscarGerencia")
	public void mostrarCatalogoGerencia() {
		final List<Gerencia> listGerencia = servicioGerencia.buscarTodas();
		catalogoGerencia = new Catalogo<Gerencia>(divCatalogoGerencia,
				"Catalogo de Gerencias", listGerencia,true,false,false, "Descripción") {

			@Override
			protected List<Gerencia> buscar(List<String> valores) {
				List<Gerencia> lista = new ArrayList<Gerencia>();

				for (Gerencia gerencia : listGerencia) {
					if (gerencia.getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())) {
						lista.add(gerencia);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Gerencia gerencia) {
				String[] registros = new String[1];
				registros[0] = gerencia.getDescripcion();

				return registros;
			}

		};

		catalogoGerencia.setClosable(true);
		catalogoGerencia.setWidth("80%");
		catalogoGerencia.setParent(divCatalogoGerencia);
		catalogoGerencia.doModal();
		catalogoGerencia.setTitle("Catalogo de Gerencias");
	}

	@Listen("onSeleccion = #divCatalogoGerencia")
	public void seleccionGerencia() {
		Gerencia gerencia = catalogoGerencia.objetoSeleccionadoDelCatalogo();
		idGerencia = gerencia.getId();
		txtGerenciaUnidadOrganizativa.setValue(String.valueOf(gerencia.getId()));
		lblGerenciaUnidadOrganizativa.setValue(gerencia.getDescripcion());
		txtGerenciaUnidadOrganizativa.setFocus(true);
		catalogoGerencia.setParent(null);
	}
	
	@Listen("onChange = #txtGerenciaUnidadOrganizativa; onOK =  #txtGerenciaUnidadOrganizativa")
	public boolean buscarIdGerencia() {
		try {
		if (txtGerenciaUnidadOrganizativa.getText().compareTo("") != 0) {
			Gerencia gerencia = servicioGerencia.buscarGerencia(Integer.valueOf(txtGerenciaUnidadOrganizativa.getValue()));
			if (gerencia != null) {
				txtGerenciaUnidadOrganizativa.setValue(String.valueOf(gerencia.getId()));
				lblGerenciaUnidadOrganizativa.setValue(gerencia.getDescripcion());
				return false;
			} else {
				txtGerenciaUnidadOrganizativa.setFocus(true);
				lblGerenciaUnidadOrganizativa.setValue("");
				msj.mensajeError(Mensaje.codigoGerencia);
				return true;
			}

		} else
			return false;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}



}
