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
import modelo.maestros.Empresa;
import modelo.maestros.TipoFormacion;

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
	private Button btnBuscarCompetencia;
	@Wire
	private Textbox txtDominioConductaCompetencia;
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
						txtCompetenciaConductaCompetencia.setValue(conducta
								.getCompetencia().getDescripcion());
						txtDominioConductaCompetencia.setValue(conducta
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
				boolean guardar = true;
				guardar = validar();
				if (guardar) {

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
						catalogo.actualizarLista(servicioConductaCompetencia
								.buscarTodas());
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
				cerrarVentana(wdwVConductaCompetencia,
						"Conducta por Competencia",tabs);
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
													catalogo.actualizarLista(servicioConductaCompetencia
															.buscarTodas());
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
													catalogo.actualizarLista(servicioConductaCompetencia
															.buscarTodas());
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
		botoneraConductaCompetencia.appendChild(botonera);

	}

	public void limpiarCampos() {
		idConductaCompetencia = 0;
		idCompetencia = 0;
		idDominio = 0;
		txtCompetenciaConductaCompetencia.setValue("");
		txtDominioConductaCompetencia.setValue("");
		txtDescripcionConductaCompetencia.setValue("");
		spnOrdenConductaCompetencia.setValue(null);
		catalogo.limpiarSeleccion();
		txtCompetenciaConductaCompetencia.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtCompetenciaConductaCompetencia.getText().compareTo("") != 0
				|| txtDominioConductaCompetencia.getText().compareTo("") != 0
				|| txtDescripcionConductaCompetencia.getText().compareTo("") != 0
				|| spnOrdenConductaCompetencia.getText().compareTo("") != 0) {
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

		final List<ConductaCompetencia> listConductaCompetencia = servicioConductaCompetencia
				.buscarTodas();
		catalogo = new Catalogo<ConductaCompetencia>(
				catalogoConductaCompetencia,
				"Catalogo de ConductaCompetencias", listConductaCompetencia, false,false,false,
				"Competencia", "Dominio", "Descripción", "Orden") {

			@Override
			protected List<ConductaCompetencia> buscar(
					List<String> valores) {
				List<ConductaCompetencia> lista = new ArrayList<ConductaCompetencia>();

				for (ConductaCompetencia conducta : listConductaCompetencia) {
					if (conducta.getCompetencia().getDescripcion()
							.toLowerCase().startsWith(valores.get(0))
							&& conducta.getDominio().getDescripcionDominio()
									.toLowerCase().startsWith(valores.get(1))
							&& conducta.getDescripcion().toLowerCase()
									.startsWith(valores.get(2))
							&& String.valueOf(conducta.getOrden())
									.toLowerCase().startsWith(valores.get(3))) {
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

	@Listen("onChange = #txtCompetenciaConductaCompetencia")
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

	@Listen("onChange = #txtDominioConductaCompetencia")
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
							.startsWith(valores.get(0))
							&& competencia.getNivel().toLowerCase()
									.startsWith(valores.get(1))
							&& competencia.getComentario().toLowerCase()
									.startsWith(valores.get(2))) {
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
	}

	@Listen("onSeleccion = #divCatalogoCompetencia")
	public void seleccionCompetencia() {
		Competencia competencia = catalogoCompetencia
				.objetoSeleccionadoDelCatalogo();
		idCompetencia = competencia.getId();
		txtCompetenciaConductaCompetencia
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
							.startsWith(valores.get(0))
							&& dominio.getTipo().toLowerCase()
									.startsWith(valores.get(1))
							&& dominio.getComentario().toLowerCase()
									.startsWith(valores.get(2))) {
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
	}

	@Listen("onSeleccion = #divCatalogoDominio")
	public void seleccionDominio() {
		Dominio dominio = catalogoDominio.objetoSeleccionadoDelCatalogo();
		idDominio = dominio.getId();
		txtDominioConductaCompetencia.setValue(dominio.getDescripcionDominio());
		txtDominioConductaCompetencia.setFocus(true);
		catalogoDominio.setParent(null);
	}

}
