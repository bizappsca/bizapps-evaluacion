package controlador.transacciones;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Actividad;
import modelo.maestros.ActividadCurso;
import modelo.maestros.Clase;
import modelo.maestros.Curso;
import modelo.maestros.Empleado;
import modelo.maestros.EmpleadoCurso;
import modelo.maestros.Periodo;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Catalogo;
import componentes.Mensaje;
import controlador.maestros.CGenerico;

public class CActividadCurso extends CGenerico {

	@Wire
	private Window wdwVActividadCurso;
	@Wire
	private Button btnGuardar;
	@Wire
	private Button btnLimpiar;
	@Wire
	private Button btnSalir;
	@Wire
	private Textbox txtCursoActividadCurso;
	@Wire
	private Textbox txtPeriodoActividadCurso;
	@Wire
	private Datebox dtbFecha;
	@Wire
	private Button btnBuscarCurso;
	@Wire
	private Listbox lsbActividad;
	@Wire
	private Div divCatalogoCurso;
	@Wire
	private Div divCatalogoPeriodo;
	List<ActividadCurso> actividadesCurso = new ArrayList<ActividadCurso>();
	private int idCurso = 0;
	private int idPeriodo = 0;

	Mensaje msj = new Mensaje();
	Catalogo<Curso> catalogoCurso;
	Catalogo<Periodo> catalogoPeriodo;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub

		HashMap<String, Object> mapa = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (mapa != null) {
			if (mapa.get("tabsGenerales") != null) {
				tabs = (List<Tab>) mapa.get("tabsGenerales");
				titulo = (String) mapa.get("titulo");
				mapa.clear();
				mapa = null;
			}
		}

		txtPeriodoActividadCurso.setFocus(true);

	}

	@Listen("onClick = #btnBuscarCurso")
	public void mostrarCatalogoCurso() {

		if (idPeriodo != 0) {

			Periodo periodo = servicioPeriodo.buscarPeriodo(idPeriodo);

			final List<Curso> listCurso = servicioCurso
					.buscarPorPeriodo(periodo);
			catalogoCurso = new Catalogo<Curso>(divCatalogoCurso,
					"Catalogo de Cursos", listCurso, true, false, false,
					"Área", "Nombre", "Duración") {

				@Override
				protected List<Curso> buscar(List<String> valores) {
					List<Curso> lista = new ArrayList<Curso>();

					for (Curso curso : listCurso) {
						if (curso.getNombreCurso().getArea().getDescripcion()
								.toLowerCase()
								.contains(valores.get(0).toLowerCase())
								&& curso.getNombreCurso().getNombre()
										.toLowerCase()
										.contains(valores.get(1).toLowerCase())

								&& String.valueOf(curso.getDuracion())
										.toLowerCase()
										.contains(valores.get(2).toLowerCase())) {
							lista.add(curso);
						}
					}
					return lista;

				}

				@Override
				protected String[] crearRegistros(Curso curso) {
					String[] registros = new String[3];
					registros[0] = curso.getNombreCurso().getArea()
							.getDescripcion();
					registros[1] = curso.getNombreCurso().getNombre();
					if (curso.getDuracion() != 0)
						registros[2] = String.valueOf(mostrarDuracion(curso))
								+ " " + curso.getMedidaDuracion();
					else
						registros[2] = String.valueOf(curso.getDuracion());

						return registros;
				}

			};

			catalogoCurso.setClosable(true);
			catalogoCurso.setWidth("80%");
			catalogoCurso.setParent(divCatalogoCurso);
			catalogoCurso.doModal();

		} else {

			Messagebox.show("Debe selecionar el periodo", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);

		}

	}

	@Listen("onSeleccion = #divCatalogoCurso")
	public void seleccionCurso() {
		Curso curso = catalogoCurso.objetoSeleccionadoDelCatalogo();
		idCurso = curso.getId();
		txtCursoActividadCurso.setValue(curso.getNombreCurso().getNombre());
		catalogoCurso.setParent(null);
		llenarLista();
	}

	@Listen("onChange = #txtCursoActividadCurso")
	public void buscarCurso() {
		List<Curso> cursos = servicioCurso
				.buscarPorNombres(txtCursoActividadCurso.getValue());

		if (cursos.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoCurso);
			txtCursoActividadCurso.setFocus(true);
		} else {
			idCurso = cursos.get(0).getId();
			llenarLista();
		}

	}

	@Listen("onClick = #btnBuscarPeriodo")
	public void buscarPeriodo() {

		final List<Periodo> listPeriodo = servicioPeriodo.buscarTodos();
		catalogoPeriodo = new Catalogo<Periodo>(divCatalogoPeriodo,
				"Catalogo de Periodos", listPeriodo, true, false, false,
				"Nombre", "Descripción", "Fecha Inicio", "Fecha Fin", "Estado") {

			@Override
			protected List<Periodo> buscar(List<String> valores) {
				List<Periodo> lista = new ArrayList<Periodo>();

				for (Periodo periodo : listPeriodo) {
					if (periodo.getNombre().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& periodo.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& String
									.valueOf(
											formatoFecha.format(periodo
													.getFechaInicio()))
									.toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& String
									.valueOf(
											formatoFecha.format(periodo
													.getFechaFin()))
									.toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& periodo.getEstadoPeriodo().toLowerCase()
									.contains(valores.get(4).toLowerCase())) {
						lista.add(periodo);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Periodo periodo) {
				String[] registros = new String[6];
				registros[0] = periodo.getNombre();
				registros[1] = periodo.getDescripcion();
				registros[2] = formatoFecha.format(periodo.getFechaInicio());
				registros[3] = formatoFecha.format(periodo.getFechaFin());
				registros[4] = periodo.getEstadoPeriodo();

				return registros;
			}
		};
		catalogoPeriodo.setClosable(true);
		catalogoPeriodo.setWidth("80%");
		catalogoPeriodo.setParent(divCatalogoPeriodo);
		catalogoPeriodo.doModal();

	}

	@Listen("onSeleccion = #divCatalogoPeriodo")
	public void seleccionPeriodo() {
		Periodo periodo = catalogoPeriodo.objetoSeleccionadoDelCatalogo();
		idPeriodo = periodo.getId();
		txtPeriodoActividadCurso.setValue(periodo.getNombre());
		catalogoPeriodo.setParent(null);
	}

	@Listen("onClick = #btnLimpiar")
	public void limpiarCampos() {
		idCurso = 0;
		txtCursoActividadCurso.setValue("");
		txtPeriodoActividadCurso.setValue("");
		lsbActividad.setModel(new ListModelList<Actividad>());
		txtPeriodoActividadCurso.setFocus(true);
	}

	public boolean camposLLenos() {
		if (txtCursoActividadCurso.getText().compareTo("") == 0) {
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

	@Listen("onClick = #btnSalir")
	public void salir() {
		cerrarVentana2(wdwVActividadCurso, titulo, tabs);
	}

	public void llenarLista() {

		List<Actividad> actividades = servicioActividad.buscarTodas();
		Curso curso = servicioCurso.buscarCurso(idCurso);
		actividadesCurso = servicioActividadCurso.buscar(curso);

		lsbActividad.setModel(new ListModelList<Actividad>(actividades));
		lsbActividad.renderAll();

		if (actividadesCurso.size() != 0) {

			for (int i = 0; i < lsbActividad.getItems().size(); i++) {

				for (int j = 0; j < actividadesCurso.size(); j++) {
					Listitem listItem = lsbActividad.getItemAtIndex(j);
					if (actividadesCurso.get(j).getActividad().getId() == actividades
							.get(i).getId()) {

						if (actividadesCurso.get(j).getValor()
								.equals("REVISADO")) {

							Date fecha = actividadesCurso.get(j).getFecha();

							((Checkbox) ((listItem.getChildren().get(3)))
									.getFirstChild()).setChecked(true);

							((Datebox) ((listItem.getChildren().get(2)))
									.getFirstChild()).setValue(fecha);

						}

					}

				}

			}

		}

	}

	@Listen("onClick = #btnGuardar")
	public void guardar() {

		boolean guardar = true;
		boolean error = false;
		guardar = validar();
		if (guardar) {

			Curso curso = servicioCurso.buscarCurso(idCurso);

			if (curso != null) {

				if (lsbActividad.getItemCount() != 0) {

					for (int i = 0; i < lsbActividad.getItemCount(); i++) {

						Listitem listItem = lsbActividad.getItemAtIndex(i);

						int codigoActividad = ((Intbox) ((listItem
								.getChildren().get(0))).getFirstChild())
								.getValue();

						String valor = null;
						Timestamp fecha = null;

						if (((Checkbox) ((listItem.getChildren().get(3)))
								.getFirstChild()).isChecked()) {
							valor = "REVISADO";

							if (((Datebox) ((listItem.getChildren().get(2)))
									.getFirstChild()).getValue() != null) {

								fecha = new java.sql.Timestamp(
										((Datebox) ((listItem.getChildren()
												.get(2))).getFirstChild())
												.getValue().getTime());
							} else {

								error = true;
							}

						} else {

							valor = "SIN REVISAR";
						}

					}

					if (error) {

						msj.mensajeAlerta(Mensaje.noSeleccionoFecha);

					} else {

						for (int i = 0; i < lsbActividad.getItemCount(); i++) {

							Listitem listItem = lsbActividad.getItemAtIndex(i);

							int codigoActividad = ((Intbox) ((listItem
									.getChildren().get(0))).getFirstChild())
									.getValue();

							String valor = null;
							Timestamp fecha = null;

							if (((Checkbox) ((listItem.getChildren().get(3)))
									.getFirstChild()).isChecked()) {
								valor = "REVISADO";

								fecha = new java.sql.Timestamp(
										((Datebox) ((listItem.getChildren()
												.get(2))).getFirstChild())
												.getValue().getTime());
								System.out.println(fecha);

							} else {

								valor = "SIN REVISAR";
							}

							Actividad actividad = servicioActividad
									.buscarActividad(codigoActividad);
							ActividadCurso actividadCurso = new ActividadCurso(
									actividad, curso, valor, fecha);
							servicioActividadCurso.guardar(actividadCurso);

						}

						msj.mensajeInformacion(Mensaje.guardado);
						limpiarCampos();

					}

				} else {

					Messagebox
							.show("Actualmente no se encuentran registradas actividades",
									"Advertencia", Messagebox.OK,
									Messagebox.EXCLAMATION);

				}

			} else {

				msj.mensajeAlerta(Mensaje.codigoCurso);
				txtCursoActividadCurso.setFocus(true);

			}

		}

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
