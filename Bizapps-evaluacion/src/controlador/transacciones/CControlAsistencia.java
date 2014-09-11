package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import modelo.maestros.Clase;
import modelo.maestros.Curso;
import modelo.maestros.Empleado;
import modelo.maestros.EmpleadoCurso;

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
	private Datebox dbfecha;
	@Wire
	private Button btnBuscarCurso;
	@Wire
	private Listbox lsbEmpleado;
	@Wire
	private Div divCatalogoCurso;
	List<EmpleadoCurso> empleadosCurso = new ArrayList<EmpleadoCurso>();
	private int idCurso = 0;

	Mensaje msj = new Mensaje();
	Catalogo<Curso> catalogoCurso;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub

		txtCursoControlAsistencia.setFocus(true);

	}

	@Listen("onClick = #btnBuscarCurso")
	public void mostrarCatalogoCurso() {
		final List<Curso> listCurso = servicioCurso.buscarTodos();
		catalogoCurso = new Catalogo<Curso>(divCatalogoCurso,
				"Catalogo de Cursos", listCurso,true,false,false, "Área", "Nombre", "Duración") {

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
				registros[2] = String.valueOf(mostrarDuracion(curso)) + " "
						+ curso.getMedidaDuracion();

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
		txtCursoControlAsistencia.setValue(curso.getNombre());
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

	@Listen("onClick = #btnLimpiar")
	public void limpiarCampos() {
		idCurso = 0;
		txtCursoControlAsistencia.setValue("");
		lsbEmpleado.setModel(new ListModelList<Empleado>());
		txtCursoControlAsistencia.setFocus(true);
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

		cerrarVentana1(wdwVControlAsistencia, "Control de Asistencia");
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
