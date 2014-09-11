package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import modelo.maestros.Actividad;
import modelo.maestros.ActividadCurso;
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
	private Datebox dbfecha;
	@Wire
	private Button btnBuscarCurso;
	@Wire
	private Listbox lsbActividad;
	@Wire
	private Div divCatalogoCurso;
	List<ActividadCurso> actividadesCurso = new ArrayList<ActividadCurso>();
	private int idCurso = 0;

	Mensaje msj = new Mensaje();
	Catalogo<Curso> catalogoCurso;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub

		txtCursoActividadCurso.setFocus(true);

	}

	@Listen("onClick = #btnBuscarCurso")
	public void mostrarCatalogoCurso() {
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
		txtCursoActividadCurso.setValue(curso.getNombre());
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

	@Listen("onClick = #btnLimpiar")
	public void limpiarCampos() {
		idCurso = 0;
		txtCursoActividadCurso.setValue("");
		lsbActividad.setModel(new ListModelList<Actividad>());
		txtCursoActividadCurso.setFocus(true);
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

		cerrarVentana1(wdwVActividadCurso, "Check List de Actividades");
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

							((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).setChecked(true);

						}

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

			Curso curso = servicioCurso.buscarCurso(idCurso);

			if (curso != null) {

				if (lsbActividad.getItemCount() != 0) {

					for (int i = 0; i < lsbActividad.getItemCount(); i++) {

						Listitem listItem = lsbActividad.getItemAtIndex(i);

						int codigoActividad = ((Intbox) ((listItem.getChildren()
								.get(0))).getFirstChild()).getValue();

						String valor = null;

						if (((Checkbox) ((listItem.getChildren().get(2)))
								.getFirstChild()).isChecked()) {
							valor = "REVISADO";

						} else {

							valor = "SIN REVISAR";
						}

						Actividad actividad = servicioActividad.buscarActividad(codigoActividad);
						ActividadCurso actividadCurso = new ActividadCurso(actividad, curso, valor);
						servicioActividadCurso.guardar(actividadCurso);
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
