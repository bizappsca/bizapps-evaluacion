package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Cargo;
import modelo.maestros.Clase;
import modelo.maestros.Curso;
import modelo.maestros.Empleado;

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
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CClase extends CGenerico {

	@Wire
	private Window wdwVClase;
	@Wire
	private Div botoneraClase;
	@Wire
	private Groupbox gpxRegistroClase;
	@Wire
	private Textbox txtCursoClase;
	@Wire
	private Button btnBuscarCurso;
	@Wire
	private Textbox txtContenidoClase;
	@Wire
	private Textbox txtObjetivoClase;
	@Wire
	private Textbox txtFacilitadorClase;
	@Wire
	private Textbox txtEntidadDidacticaClase;
	@Wire
	private Datebox dtbFechaClase;
	@Wire
	private Spinner spnDuracionClase;
	@Wire
	private Combobox cmbUnidadMedidaClase;
	@Wire
	private Textbox txtLugarClase;
	@Wire
	private Combobox cmbTipoEntrenamientoClase;
	@Wire
	private Combobox cmbModalidadClase;
	@Wire
	private Groupbox gpxDatosClase;
	@Wire
	private Div catalogoClase;
	@Wire
	private Div divCatalogoCurso;
	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idClase = 0;
	private int idCurso = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Clase> catalogo;
	Catalogo<Curso> catalogoCurso;

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
		txtCursoClase.setFocus(true);
		cmbUnidadMedidaClase.setValue("HORAS");
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Clase clase = catalogo.objetoSeleccionadoDelCatalogo();
						idClase = clase.getId();
						idCurso = clase.getCurso().getId();
						txtCursoClase.setValue(clase.getCurso().getNombre());
						txtContenidoClase.setValue(clase.getContenido());
						txtObjetivoClase.setValue(clase.getObjetivo());
						txtFacilitadorClase.setValue(clase.getFacilitador());
						txtEntidadDidacticaClase.setValue(clase
								.getEntidadDidactica());
						dtbFechaClase.setValue(clase.getFecha());
						spnDuracionClase.setValue((int) mostrarDuracion(clase));
						cmbUnidadMedidaClase
								.setValue(clase.getMedidaDuracion());
						txtLugarClase.setValue(clase.getLugar());
						cmbTipoEntrenamientoClase.setValue(clase
								.getTipoEntrenamiento());
						cmbModalidadClase.setValue(clase.getModalidad());
						txtCursoClase.setFocus(true);
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

					Curso curso = servicioCurso.buscarCurso(idCurso);

					if (curso != null) {

						String contenido = txtContenidoClase.getValue();
						String objetivo = txtObjetivoClase.getValue();
						String facilitador = txtFacilitadorClase.getValue();
						String entidadDidactica = txtEntidadDidacticaClase
								.getValue();
						Timestamp fecha = new java.sql.Timestamp(dtbFechaClase
								.getValue().getTime());
						float duracion = transformarDuracion(spnDuracionClase
								.getValue());
						String medidaDuracion = cmbUnidadMedidaClase.getValue();
						String lugar = txtLugarClase.getValue();
						String tipoEntrenamiento = cmbTipoEntrenamientoClase
								.getValue();
						String modalidad = cmbModalidadClase.getValue();
						String usuario = nombreUsuarioSesion();
						Timestamp fechaAuditoria = new Timestamp(
								new Date().getTime());
						Clase clase = new Clase(idClase, curso, contenido,
								objetivo, facilitador, entidadDidactica, fecha,
								duracion, medidaDuracion, lugar,
								tipoEntrenamiento, modalidad, fechaAuditoria,
								horaAuditoria, usuario);
						servicioClase.guardar(clase);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();
						catalogo.actualizarLista(servicioClase.buscarTodas());
						abrirCatalogo();

					} else {

						msj.mensajeAlerta(Mensaje.codigoCurso);
						txtCursoClase.setFocus(true);
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
				cerrarVentana(wdwVClase, "Clase",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosClase.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Clase> eliminarLista = catalogo
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
													servicioClase
															.eliminarVariasClases(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioClase
															.buscarTodas());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idClase != 0) {
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
													servicioClase
															.eliminarUnaClase(idClase);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioClase
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
		botoneraClase.appendChild(botonera);

	}

	public void limpiarCampos() {
		idClase = 0;
		idCurso = 0;
		txtCursoClase.setValue("");
		txtContenidoClase.setValue("");
		txtObjetivoClase.setValue("");
		txtFacilitadorClase.setValue("");
		txtEntidadDidacticaClase.setValue("");
		dtbFechaClase.setValue(null);
		spnDuracionClase.setValue(null);
		txtLugarClase.setValue("");
		cmbTipoEntrenamientoClase.setValue("");
		cmbModalidadClase.setValue("");
		cmbUnidadMedidaClase.setValue("HORAS");
		txtCursoClase.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtCursoClase.getText().compareTo("") != 0
				|| txtContenidoClase.getText().compareTo("") != 0
				|| txtObjetivoClase.getText().compareTo("") != 0
				|| txtFacilitadorClase.getText().compareTo("") != 0
				|| txtEntidadDidacticaClase.getText().compareTo("") != 0
				|| dtbFechaClase.getText().compareTo("") != 0
				|| spnDuracionClase.getText().compareTo("") != 0
				|| txtLugarClase.getText().compareTo("") != 0
				|| cmbTipoEntrenamientoClase.getText().compareTo("") != 0
				|| cmbModalidadClase.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroClase")
	public void abrirRegistro() {
		gpxDatosClase.setOpen(false);
		gpxRegistroClase.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosClase")
	public void abrirCatalogo() {
		gpxDatosClase.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosClase.setOpen(false);
								gpxRegistroClase.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosClase.setOpen(true);
									gpxRegistroClase.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosClase.setOpen(true);
			gpxRegistroClase.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Clase> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtCursoClase.getText().compareTo("") == 0
				|| txtContenidoClase.getText().compareTo("") == 0
				|| spnDuracionClase.getText().compareTo("") == 0
				|| cmbUnidadMedidaClase.getText().compareTo("") == 0) {
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

		final List<Clase> listClase = servicioClase.buscarTodas();
		catalogo = new Catalogo<Clase>(catalogoClase, "Catalogo de Clases",
				listClase,false,false,false, "Curso", "Contenido", "Objetivo", "Facilitador",
				"Entidad Didáctica", "Fecha", "Duración", "Lugar",
				"Tipo de Entrenamiento", "Modalidad") {

			@Override
			protected List<Clase> buscar(List<String> valores) {
				List<Clase> lista = new ArrayList<Clase>();

				for (Clase clase : listClase) {
					if (clase.getCurso().getNombre().toLowerCase()
							.startsWith(valores.get(0))
							&& clase.getContenido().toLowerCase()
									.startsWith(valores.get(1))
							&& clase.getObjetivo().toLowerCase()
									.startsWith(valores.get(2))
							&& clase.getFacilitador().toLowerCase()
									.startsWith(valores.get(3))
							&& clase.getEntidadDidactica().toLowerCase()
									.startsWith(valores.get(4))
							&& String
									.valueOf(
											formatoFecha.format(clase
													.getFecha())).toLowerCase()
									.startsWith(valores.get(5))
							&& String.valueOf(clase.getDuracion())
									.toLowerCase().startsWith(valores.get(6))
							&& clase.getLugar().toLowerCase()
									.startsWith(valores.get(7))
							&& clase.getTipoEntrenamiento().toLowerCase()
									.startsWith(valores.get(8))
							&& clase.getModalidad().toLowerCase()
									.startsWith(valores.get(10))) {
						lista.add(clase);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Clase clase) {
				String[] registros = new String[10];
				registros[0] = clase.getCurso().getNombre();
				registros[1] = clase.getContenido();
				registros[2] = clase.getObjetivo();
				registros[3] = clase.getFacilitador();
				registros[4] = clase.getEntidadDidactica();
				registros[5] = String.valueOf(formatoFecha.format(clase
						.getFecha()));
				registros[6] = String.valueOf(mostrarDuracion(clase)) + " "
						+ clase.getMedidaDuracion();
				registros[7] = clase.getLugar();
				registros[8] = clase.getTipoEntrenamiento();
				registros[9] = clase.getModalidad();

				return registros;
			}

		};
		catalogo.setParent(catalogoClase);

	}

	@Listen("onClick = #btnBuscarCurso")
	public void mostrarCatalogoCargo() {
		final List<Curso> listCurso = servicioCurso.buscarTodos();
		catalogoCurso = new Catalogo<Curso>(divCatalogoCurso,
				"Catalogo de Cursos", listCurso, true,false,false,"Área", "Nombre", "Duración") {

			@Override
			protected List<Curso> buscar(List<String> valores) {
				List<Curso> lista = new ArrayList<Curso>();

				for (Curso curso : listCurso) {
					if (curso.getArea().getDescripcion().toLowerCase()
							.startsWith(valores.get(0))
							&& curso.getNombre().toLowerCase()
									.startsWith(valores.get(1))

							&& String.valueOf(curso.getDuracion())
									.toLowerCase().startsWith(valores.get(2))) {
						lista.add(curso);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Curso curso) {
				String[] registros = new String[3];
				registros[0] = curso.getArea().getDescripcion();
				registros[1] = curso.getNombre();
				registros[2] = String.valueOf(curso.getDuracion());

				return registros;
			}

		};

		catalogoCurso.setClosable(true);
		catalogoCurso.setWidth("80%");
		catalogoCurso.setParent(divCatalogoCurso);
		catalogoCurso.doModal();
	}

	@Listen("onSeleccion = #divCatalogoCurso")
	public void seleccionCurso() {
		Curso curso = catalogoCurso.objetoSeleccionadoDelCatalogo();
		idCurso = curso.getId();
		txtCursoClase.setValue(curso.getNombre());
		catalogoCurso.setParent(null);
	}

	@Listen("onChange = #txtCursoClase")
	public void buscarCurso() {
		List<Curso> cursos = servicioCurso.buscarPorNombres(txtCursoClase
				.getValue());
		if (cursos.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoCurso);
			txtCursoClase.setFocus(true);
		} else {

			idCurso = cursos.get(0).getId();
		}

	}

	public float transformarDuracion(int duracion) {

		float duracionTransformada = 0;

		if (cmbUnidadMedidaClase.getValue().equals("HORAS")) {

			duracionTransformada = duracion;
			return duracionTransformada;

		} else if (cmbUnidadMedidaClase.getValue().equals("DIAS")) {

			duracionTransformada = spnDuracionClase.getValue() * 24;
			return duracionTransformada;

		} else if (cmbUnidadMedidaClase.getValue().equals("MINUTOS")) {

			duracionTransformada = spnDuracionClase.getValue() / 60;
			return duracionTransformada;

		}

		return duracion;

	}

	public float mostrarDuracion(Clase clase) {

		float duracionTransformada = 0;

		if (clase.getMedidaDuracion().equals("HORAS")) {

			duracionTransformada = clase.getDuracion();
			return duracionTransformada;

		} else if (clase.getMedidaDuracion().equals("DIAS")) {

			duracionTransformada = clase.getDuracion() / 24;
			return duracionTransformada;

		} else if (clase.getMedidaDuracion().equals("MINUTOS")) {

			duracionTransformada = clase.getDuracion() * 60;
			return duracionTransformada;

		}

		return duracionTransformada;

	}

}
