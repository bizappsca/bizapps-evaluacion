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
import modelo.maestros.Competencia;
import modelo.maestros.ConductaCompetencia;
import modelo.maestros.Dominio;
import modelo.maestros.Empleado;
import modelo.maestros.Empresa;
import modelo.maestros.TipoFormacion;
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

public class CConductaCompetencia extends CGenerico {

	@Wire
	private Window wdwVConductaCompetencia;
	@Wire
	private Div botoneraConductaCompetencia;
	@Wire
	private Groupbox gpxRegistroConductaCompetencia;
	@Wire
	private Textbox txtCompetenciaConductaCompetencia;
	@Wire
	private Label lblCompetenciaConductaCompetencia;
	@Wire
	private Button btnBuscarCompetencia;
	@Wire
	private Textbox txtDominioConductaCompetencia;
	@Wire
	private Label lblDominioConductaCompetencia;
	@Wire
	private Button btnBuscarDominio;
	@Wire
	private Textbox txtDescripcionConductaCompetencia;
	@Wire
	private Spinner spnOrdenConductaCompetencia;
	@Wire
	private Groupbox gpxDatosConductaCompetencia;
	@Wire
	private Div catalogoConductaCompetencia;
	@Wire
	private Div divCatalogoCompetencia;
	@Wire
	private Div divCatalogoDominio;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idConductaCompetencia = 0;
	private int idCompetencia = 0;
	private int idDominio = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<ConductaCompetencia> catalogo;
	Catalogo<Competencia> catalogoCompetencia;
	Catalogo<Dominio> catalogoDominio;
	protected List<ConductaCompetencia> listaGeneral = new ArrayList<ConductaCompetencia>();

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
		txtCompetenciaConductaCompetencia.setFocus(true);
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						ConductaCompetencia conducta = catalogo
								.objetoSeleccionadoDelCatalogo();
						idConductaCompetencia = conducta.getId();
						idCompetencia = conducta.getCompetencia().getId();
						idDominio = conducta.getDominio().getId();
						txtCompetenciaConductaCompetencia.setValue(String.valueOf(conducta
								.getCompetencia().getId()));
						lblCompetenciaConductaCompetencia.setValue(conducta
								.getCompetencia().getDescripcion());
						txtDominioConductaCompetencia.setValue(String.valueOf(conducta
								.getDominio().getId()));
						lblDominioConductaCompetencia.setValue(conducta
								.getDominio().getDescripcionDominio());
						txtDescripcionConductaCompetencia.setValue(conducta
								.getDescripcion());
						spnOrdenConductaCompetencia.setValue(conducta
								.getOrden());
						txtCompetenciaConductaCompetencia.setFocus(true);
					} else
						msj.mensajeAlerta(Mensaje.editarSoloUno);
				}

			}

			@Override
			public void guardar() {
				// TODO Auto-generated method stub
				if (validar()) {

					Competencia competencia = servicioCompetencia
							.buscarCompetencia(idCompetencia);
					Dominio dominio = servicioDominio.buscarDominio(idDominio);

					if (competencia != null && dominio != null) {

						System.out.println("Entre en el if");
						String descripcion = txtDescripcionConductaCompetencia
								.getValue();
						int orden = Integer.valueOf(spnOrdenConductaCompetencia
								.getValue());
						String usuario = nombreUsuarioSesion();
						Timestamp fechaAuditoria = new Timestamp(
								new Date().getTime());
						ConductaCompetencia conducta = new ConductaCompetencia(
								idConductaCompetencia, descripcion,
								competencia, dominio, orden, fechaAuditoria,
								horaAuditoria, usuario);
						servicioConductaCompetencia.guardar(conducta);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();
						listaGeneral = servicioConductaCompetencia.buscarTodas();
						catalogo.actualizarLista(listaGeneral);
						abrirCatalogo();
					} else {

						if (competencia == null) {
							msj.mensajeAlerta(Mensaje.codigoCompetencia);
							txtCompetenciaConductaCompetencia.setFocus(true);
						} else if (dominio == null) {
							msj.mensajeAlerta(Mensaje.codigoDominio);
							txtDominioConductaCompetencia.setFocus(true);
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
				cerrarVentana2(wdwVConductaCompetencia,
						titulo,tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosConductaCompetencia.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<ConductaCompetencia> eliminarLista = catalogo
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
													servicioConductaCompetencia
															.eliminarVariasConductas(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													listaGeneral = servicioConductaCompetencia.buscarTodas();
													catalogo.actualizarLista(listaGeneral);
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idConductaCompetencia != 0) {
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
													servicioConductaCompetencia
															.eliminarUnaConducta(idConductaCompetencia);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													listaGeneral = servicioConductaCompetencia.buscarTodas();
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
		botoneraConductaCompetencia.appendChild(botonera);

	}

	public void limpiarCampos() {
		idConductaCompetencia = 0;
		idCompetencia = 0;
		idDominio = 0;
		txtCompetenciaConductaCompetencia.setValue("");
		txtDominioConductaCompetencia.setValue("");
		txtDescripcionConductaCompetencia.setValue("");
		lblDominioConductaCompetencia.setValue("");
		lblCompetenciaConductaCompetencia.setValue("");
		spnOrdenConductaCompetencia.setValue(0);
		catalogo.limpiarSeleccion();
		txtCompetenciaConductaCompetencia.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtCompetenciaConductaCompetencia.getText().compareTo("") != 0
				|| txtDominioConductaCompetencia.getText().compareTo("") != 0
				|| txtDescripcionConductaCompetencia.getText().compareTo("") != 0
				|| spnOrdenConductaCompetencia.getText().compareTo("0") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroConductaCompetencia")
	public void abrirRegistro() {
		gpxDatosConductaCompetencia.setOpen(false);
		gpxRegistroConductaCompetencia.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosConductaCompetencia")
	public void abrirCatalogo() {
		gpxDatosConductaCompetencia.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosConductaCompetencia.setOpen(false);
								gpxRegistroConductaCompetencia.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosConductaCompetencia.setOpen(true);
									gpxRegistroConductaCompetencia
											.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosConductaCompetencia.setOpen(true);
			gpxRegistroConductaCompetencia.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<ConductaCompetencia> seleccionados = catalogo
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
		if (txtCompetenciaConductaCompetencia.getText().compareTo("") == 0
				|| txtDominioConductaCompetencia.getText().compareTo("") == 0) {
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

		listaGeneral = servicioConductaCompetencia
				.buscarTodas();
		catalogo = new Catalogo<ConductaCompetencia>(
				catalogoConductaCompetencia,
				"Catalogo de ConductaCompetencias", listaGeneral, false,false,false,
				"Competencia", "Dominio", "Descripción", "Orden") {

			@Override
			protected List<ConductaCompetencia> buscar(
					List<String> valores) {
				List<ConductaCompetencia> lista = new ArrayList<ConductaCompetencia>();

				for (ConductaCompetencia conducta : listaGeneral) {
					if (conducta.getCompetencia().getDescripcion()
							.toLowerCase().contains(valores.get(0).toLowerCase())
							&& conducta.getDominio().getDescripcionDominio()
									.toLowerCase().contains(valores.get(1).toLowerCase())
							&& conducta.getDescripcion().toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& String.valueOf(conducta.getOrden())
									.toLowerCase().contains(valores.get(3).toLowerCase())) {
						lista.add(conducta);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(ConductaCompetencia conducta) {
				String[] registros = new String[4];
				registros[0] = conducta.getCompetencia().getDescripcion();
				registros[1] = conducta.getDominio().getDescripcionDominio();
				registros[2] = conducta.getDescripcion();
				registros[3] = String.valueOf(conducta.getOrden());

				return registros;
			}

		};
		catalogo.setParent(catalogoConductaCompetencia);

	}

//	@Listen("onChange = #txtCompetenciaConductaCompetencia")
	public void buscarCompetencia() {
		List<Competencia> competencias = servicioCompetencia
				.buscarPorNombres(txtCompetenciaConductaCompetencia.getValue());
		if (competencias.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoCompetencia);
			txtCompetenciaConductaCompetencia.setFocus(true);
		} else {

			idCompetencia = competencias.get(0).getId();

		}

	}

//	@Listen("onChange = #txtDominioConductaCompetencia")
	public void buscarDominio() {
		List<Dominio> dominios = servicioDominio
				.buscarPorNombres(txtDominioConductaCompetencia.getValue());
		if (dominios.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoDominio);
			txtDominioConductaCompetencia.setFocus(true);
		} else {

			idDominio = dominios.get(0).getId();

		}

	}

	@Listen("onClick = #btnBuscarCompetencia")
	public void mostrarCatalogoCompetencia() {
		final List<Competencia> listCompetencia = servicioCompetencia
				.buscarTodas();
		catalogoCompetencia = new Catalogo<Competencia>(divCatalogoCompetencia,
				"Catalogo de Competencias", listCompetencia,true,false,false, "Descripción",
				"Nivel", "Comentario") {

			@Override
			protected List<Competencia> buscar(List<String> valores) {
				List<Competencia> lista = new ArrayList<Competencia>();

				for (Competencia competencia : listCompetencia) {
					if (competencia.getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& competencia.getNivel().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& competencia.getComentario().toLowerCase()
									.contains(valores.get(2).toLowerCase())) {
						lista.add(competencia);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Competencia competencia) {
				String[] registros = new String[3];
				registros[0] = competencia.getDescripcion();
				registros[1] = competencia.getNivel();
				registros[2] = competencia.getComentario();

				return registros;
			}

		};

		catalogoCompetencia.setClosable(true);
		catalogoCompetencia.setWidth("80%");
		catalogoCompetencia.setParent(divCatalogoCompetencia);
		catalogoCompetencia.doModal();
		catalogoCompetencia.setTitle("Catalogo de Competencias");
	}

	@Listen("onSeleccion = #divCatalogoCompetencia")
	public void seleccionCompetencia() {
		Competencia competencia = catalogoCompetencia
				.objetoSeleccionadoDelCatalogo();
		idCompetencia = competencia.getId();
		txtCompetenciaConductaCompetencia
				.setValue(String.valueOf(competencia.getId()));
		lblCompetenciaConductaCompetencia
		.setValue(competencia.getDescripcion());
		txtCompetenciaConductaCompetencia.setFocus(true);
		catalogoCompetencia.setParent(null);
	}

	@Listen("onClick = #btnBuscarDominio")
	public void mostrarCatalogoDominio() {
		final List<Dominio> listDominio = servicioDominio.buscarTodos();
		catalogoDominio = new Catalogo<Dominio>(divCatalogoDominio,
				"Catalogo de Dominios", listDominio,true,false,false, "Descripción", "Tipo",
				"Comentario") {

			@Override
			protected List<Dominio> buscar(List<String> valores) {
				List<Dominio> lista = new ArrayList<Dominio>();

				for (Dominio dominio : listDominio) {
					if (dominio.getDescripcionDominio().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& dominio.getTipo().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& dominio.getComentario().toLowerCase()
									.contains(valores.get(2).toLowerCase())) {
						lista.add(dominio);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Dominio dominio) {
				String[] registros = new String[3];
				registros[0] = dominio.getDescripcionDominio();
				registros[1] = dominio.getTipo();
				registros[2] = dominio.getComentario();

				return registros;
			}

		};

		catalogoDominio.setClosable(true);
		catalogoDominio.setWidth("80%");
		catalogoDominio.setParent(divCatalogoDominio);
		catalogoDominio.doModal();
		catalogoDominio.setTitle("Catalogo de Dominios");
	}

	@Listen("onSeleccion = #divCatalogoDominio")
	public void seleccionDominio() {
		Dominio dominio = catalogoDominio.objetoSeleccionadoDelCatalogo();
		idDominio = dominio.getId();
		txtDominioConductaCompetencia.setValue(String.valueOf(dominio.getId()));
		lblDominioConductaCompetencia.setValue(dominio.getDescripcionDominio());
		txtDominioConductaCompetencia.setFocus(true);
		catalogoDominio.setParent(null);
	}
	
	@Listen("onChange = #txtCompetenciaConductaCompetencia; onOK =  #txtCompetenciaConductaCompetencia")
	public boolean buscarIdCompetencia() {
		try {
		if (txtCompetenciaConductaCompetencia.getText().compareTo("") != 0) {
			Competencia competencia = servicioCompetencia.buscarCompetencia(Integer.valueOf(txtCompetenciaConductaCompetencia.getValue()));
			if (competencia != null) {
				txtCompetenciaConductaCompetencia.setValue(String.valueOf(competencia.getId()));
				lblCompetenciaConductaCompetencia.setValue(competencia.getDescripcion());
				idCompetencia = competencia.getId();
				return false;
			} else {
				txtCompetenciaConductaCompetencia.setFocus(true);
				lblCompetenciaConductaCompetencia.setValue("");
				msj.mensajeError(Mensaje.codigoCompetencia);
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

	@Listen("onChange = #txtDominioConductaCompetencia; onOK =  #txtDominioConductaCompetencia")
	public boolean buscarIdDominio() {
		try {
		if (txtDominioConductaCompetencia.getText().compareTo("") != 0) {
			Dominio dominio = servicioDominio.buscarDominio(Integer.valueOf(txtDominioConductaCompetencia.getValue()));
			if (dominio != null) {
				txtDominioConductaCompetencia.setValue(String.valueOf(dominio.getId()));
				lblDominioConductaCompetencia.setValue(dominio.getDescripcionDominio());
				idDominio = dominio.getId();
				return false;
			} else {
				txtDominioConductaCompetencia.setFocus(true);
				lblDominioConductaCompetencia.setValue("");
				msj.mensajeError(Mensaje.codigoDominio);
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
