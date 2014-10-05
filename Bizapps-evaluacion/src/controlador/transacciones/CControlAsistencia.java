package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class CControlAsistencia extends CGenerico {

	@Wire
	private Window wdwVControlAsistencia;
	@Wire
	private Button btnGuardar;
	@Wire
	private Button btnLimpiar;
	@Wire
	private Button btnSalir;
	@Wire
	private Textbox txtCursoControlAsistencia;
	@Wire
	private Textbox txtPeriodoControlAsistencia;
	@Wire
	private Button btnBuscarPeriodo;
	@Wire
	private Div divCatalogoPeriodo;
	@Wire
	private Button btnBuscarCurso;
	@Wire
	private Listbox lsbEmpleado;
	@Wire
	private Div divCatalogoCurso;
	List<EmpleadoCurso> empleadosCurso = new ArrayList<EmpleadoCurso>();
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
				mapa.clear();
				mapa = null;
			}
		}

		txtPeriodoControlAsistencia.setFocus(true);

	}

	@Listen("onClick = #btnBuscarCurso")
	public void mostrarCatalogoCurso() {

		if (idPeriodo != 0) {

			Periodo periodo = servicioPeriodo.buscarPeriodo(idPeriodo);

			final List<Curso> listCurso = servicioCurso.buscarTodos();
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
					if (curso.getDuracion() == 0)
						registros[2] = String.valueOf(curso.getDuracion());
					else
						registros[2] = String.valueOf(mostrarDuracion(curso))
						+ " " + curso.getMedidaDuracion();

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
		txtCursoControlAsistencia.setValue(curso.getNombreCurso().getNombre());
		catalogoCurso.setParent(null);
		llenarLista();
	}

	@Listen("onChange = #txtCursoControlAsistencia")
	public void buscarCurso() {
		List<Curso> cursos = servicioCurso
				.buscarPorNombres(txtCursoControlAsistencia.getValue());

		if (cursos.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoCurso);
			txtCursoControlAsistencia.setFocus(true);
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
		txtPeriodoControlAsistencia.setValue(periodo.getNombre());
		catalogoPeriodo.setParent(null);
	}

	@Listen("onClick = #btnLimpiar")
	public void limpiarCampos() {
		idCurso = 0;
		txtPeriodoControlAsistencia.setValue("");
		txtCursoControlAsistencia.setValue("");
		lsbEmpleado.setModel(new ListModelList<Empleado>());
		txtPeriodoControlAsistencia.setFocus(true);
	}

	public boolean camposLLenos() {
		if (txtCursoControlAsistencia.getText().compareTo("") == 0) {
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

		cerrarVentana(wdwVControlAsistencia, "Control de  Asistencia", tabs);

	}

	public void llenarLista() {

		List<Empleado> empleados = new ArrayList<Empleado>();
		Curso curso = servicioCurso.buscarCurso(idCurso);
		empleadosCurso = servicioEmpleadoCurso.buscar(curso);

		for (int i = 0; i < empleadosCurso.size(); i++) {

			empleados.add(empleadosCurso.get(i).getEmpleado());
		}

		lsbEmpleado.setModel(new ListModelList<Empleado>(empleados));
		lsbEmpleado.renderAll();

		if (empleadosCurso.size() != 0) {

			for (int i = 0; i < lsbEmpleado.getItems().size(); i++) {

				for (int j = 0; j < empleadosCurso.size(); j++) {
					Listitem listItem = lsbEmpleado.getItemAtIndex(j);
					if (empleadosCurso.get(j).getEstadoCurso()
							.equals("APROBADO")) {

						((Checkbox) ((listItem.getChildren().get(5)))
								.getFirstChild()).setChecked(true);

					}

				}

			}

		}

	}

	@Listen("onClick = #btnGuardar")
	public void guardar() {

		boolean guardar = true;
		guardar = validar();
		if (guardar) {

			Curso cursoEmpleado = servicioCurso.buscarCurso(idCurso);

			if (cursoEmpleado != null) {

				if (lsbEmpleado.getItemCount() != 0) {

					for (int i = 0; i < lsbEmpleado.getItemCount(); i++) {

						Listitem listItem = lsbEmpleado.getItemAtIndex(i);

						int codigoEmpleado = ((Intbox) ((listItem.getChildren()
								.get(0))).getFirstChild()).getValue();

						String estadoCurso = null;

						if (((Checkbox) ((listItem.getChildren().get(5)))
								.getFirstChild()).isChecked()) {

							estadoCurso = "APROBADO";

						} else {

							estadoCurso = "REPROBADO";
						}

						Empleado empleado = servicioEmpleado
								.buscar(codigoEmpleado);
						Curso curso = servicioCurso.buscarCurso(idCurso);

						EmpleadoCurso empleadosCurso = servicioEmpleadoCurso
								.buscarPorempleadoYCurso(empleado, curso);
						empleadosCurso.setEstadoCurso(estadoCurso);
						servicioEmpleadoCurso.guardar(empleadosCurso);

					}

					msj.mensajeInformacion(Mensaje.guardado);
					limpiarCampos();
				} else {

					Messagebox
							.show("Actualmente no existen empleados asignados en el curso seleccionado",
									"Advertencia", Messagebox.OK,
									Messagebox.EXCLAMATION);

				}

			} else {

				msj.mensajeAlerta(Mensaje.codigoCurso);
				txtCursoControlAsistencia.setFocus(true);

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
