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
import modelo.maestros.Clase;
import modelo.maestros.Curso;

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

public class CCurso extends CGenerico {

	@Wire
	private Window wdwVCurso;
	@Wire
	private Div botoneraCurso;
	@Wire
	private Groupbox gpxRegistroCurso;
	@Wire
	private Textbox txtNombreCurso;
	@Wire
	private Textbox txtAreaCurso;
	@Wire
	private Button btnBuscarArea;
	@Wire
	private Spinner spnDuracionCurso;
	@Wire
	private Combobox cmbEstadoCurso;
	@Wire
	private Combobox cmbUnidadMedidaCurso;
	@Wire
	private Groupbox gpxDatosCurso;
	@Wire
	private Div catalogoCurso;
	@Wire
	private Div divCatalogoArea;

	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idCurso = 0;
	private int idArea = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<Curso> catalogo;
	Catalogo<Area> catalogoArea;

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
		txtAreaCurso.setFocus(true);
		cmbUnidadMedidaCurso.setValue("HORAS");
		mostrarCatalogo();
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Curso curso = catalogo.objetoSeleccionadoDelCatalogo();
						idCurso = curso.getId();
						idArea = curso.getArea().getId();
						txtNombreCurso.setValue(curso.getNombre());
						txtAreaCurso.setValue(curso.getArea().getDescripcion());
						spnDuracionCurso.setValue((int) mostrarDuracion(curso));
						cmbUnidadMedidaCurso.setValue(curso.getMedidaDuracion());
						cmbEstadoCurso.setValue(curso.getEstado());
						txtAreaCurso.setFocus(true);
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

					Area area = servicioArea.buscarArea(idArea);

					if (area != null) {

						String nombre = txtNombreCurso.getValue();
						float duracion = transformarDuracion(spnDuracionCurso
								.getValue());
						String medidaDuracion = cmbUnidadMedidaCurso.getValue();
						String estado = cmbEstadoCurso.getValue();
						String usuario = nombreUsuarioSesion();
						Timestamp fechaAuditoria = new Timestamp(
								new Date().getTime());
						Curso curso = new Curso(idCurso, area, nombre, duracion,
								medidaDuracion,  estado, fechaAuditoria,
								 horaAuditoria, usuario);
						servicioCurso.guardar(curso);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();
						catalogo.actualizarLista(servicioCurso.buscarTodos());
						abrirCatalogo();
					} else {

						msj.mensajeAlerta(Mensaje.codigoArea);
						txtAreaCurso.setFocus(true);

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
				cerrarVentana(wdwVCurso, "Curso",tabs);
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				if (gpxDatosCurso.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Curso> eliminarLista = catalogo
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
													servicioCurso
															.eliminarVariosCursos(eliminarLista);
													msj.mensajeInformacion(Mensaje.eliminado);
													catalogo.actualizarLista(servicioCurso
															.buscarTodos());
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (idCurso != 0) {
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
													servicioCurso
															.eliminarUnCurso(idCurso);
													msj.mensajeInformacion(Mensaje.eliminado);
													limpiar();
													catalogo.actualizarLista(servicioCurso
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
		botoneraCurso.appendChild(botonera);

	}

	public void limpiarCampos() {
		idCurso = 0;
		idArea = 0;
		txtNombreCurso.setValue("");
		txtAreaCurso.setValue("");
		spnDuracionCurso.setValue(null);
		cmbEstadoCurso.setValue("");
		cmbUnidadMedidaCurso.setValue("HORAS");
		catalogo.limpiarSeleccion();
		txtAreaCurso.setFocus(true);

	}

	public boolean camposEditando() {
		if (txtAreaCurso.getText().compareTo("") != 0
				|| txtNombreCurso.getText().compareTo("") != 0
				|| spnDuracionCurso.getText().compareTo("") != 0
				|| cmbEstadoCurso.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistroCurso")
	public void abrirRegistro() {
		gpxDatosCurso.setOpen(false);
		gpxRegistroCurso.setOpen(true);
		mostrarBotones(false);

	}

	@Listen("onOpen = #gpxDatosCurso")
	public void abrirCatalogo() {
		gpxDatosCurso.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(Mensaje.estaEditando, "Alerta", Messagebox.YES
					| Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatosCurso.setOpen(false);
								gpxRegistroCurso.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatosCurso.setOpen(true);
									gpxRegistroCurso.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatosCurso.setOpen(true);
			gpxRegistroCurso.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean validarSeleccion() {
		List<Curso> seleccionados = catalogo.obtenerSeleccionados();
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
		if (txtAreaCurso.getText().compareTo("") == 0
				|| txtNombreCurso.getText().compareTo("") == 0
				|| spnDuracionCurso.getText().compareTo("") == 0
				|| cmbUnidadMedidaCurso.getText().compareTo("") == 0
				|| cmbEstadoCurso.getText().compareTo("") == 0) {
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

		final List<Curso> listCurso = servicioCurso.buscarTodos();
		catalogo = new Catalogo<Curso>(catalogoCurso, "Catalogo de Cursos",
				listCurso,false,false,false, "Área", "Nombre", "Duración") {

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
				registros[2] = String.valueOf(mostrarDuracion(curso)) + " " + curso.getMedidaDuracion();

				return registros;
			}

		};
		catalogo.setParent(catalogoCurso);

	}

	@Listen("onChange = #txtAreaCurso")
	public void buscarArea() {
		List<Area> areas = servicioArea.buscarPorNombres(txtAreaCurso
				.getValue());
		if (areas.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoArea);
			txtAreaCurso.setFocus(true);
		} else {

			idArea = areas.get(0).getId();
		}

	}

	@Listen("onClick = #btnBuscarArea")
	public void mostrarCatalogoArea() {
		final List<Area> listArea = servicioArea.buscarTodas();
		catalogoArea = new Catalogo<Area>(divCatalogoArea, "Catalogo de Areas",
				listArea, true,false,false,"Tipo de Formación", "Descripción") {

			@Override
			protected List<Area> buscar(List<String> valores) {
				List<Area> lista = new ArrayList<Area>();

				for (Area area : listArea) {
					if (area.getTipoFormacion().getDescripcion().toLowerCase()
							.startsWith(valores.get(0))
							&& area.getDescripcion().toLowerCase()
									.startsWith(valores.get(1))) {
						lista.add(area);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Area area) {
				String[] registros = new String[2];
				registros[0] = area.getTipoFormacion().getDescripcion();
				registros[1] = area.getDescripcion();

				return registros;
			}

		};

		catalogoArea.setClosable(true);
		catalogoArea.setWidth("80%");
		catalogoArea.setParent(divCatalogoArea);
		catalogoArea.doModal();
	}

	@Listen("onSeleccion = #divCatalogoArea")
	public void seleccionArea() {
		Area area = catalogoArea.objetoSeleccionadoDelCatalogo();
		idArea = area.getId();
		txtAreaCurso.setValue(area.getDescripcion());
		catalogoArea.setParent(null);
	}
	
	public float transformarDuracion(int duracion) {

		float duracionTransformada = 0;

		if (cmbUnidadMedidaCurso.getValue().equals("HORAS")) {

			duracionTransformada = duracion;
			return duracionTransformada;

		} else if (cmbUnidadMedidaCurso.getValue().equals("DIAS")) {

			duracionTransformada = spnDuracionCurso.getValue() * 24;
			return duracionTransformada;

		} 

		return duracion;

	}

	public float mostrarDuracion(Curso curso) {

		float duracionTransformada = 0;

		if (curso.getMedidaDuracion().equals("HORAS")) {

			duracionTransformada = curso.getDuracion();
			return duracionTransformada;

		} else if (curso.getMedidaDuracion().equals("DIAS")) {

			duracionTransformada = curso.getDuracion() / 24;
			return duracionTransformada;
		} 

		return duracionTransformada;

	}

}
