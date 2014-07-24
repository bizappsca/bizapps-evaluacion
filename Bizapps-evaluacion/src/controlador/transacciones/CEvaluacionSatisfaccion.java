package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import modelo.maestros.Clase;
import modelo.maestros.Curso;
import modelo.maestros.Empleado;
import modelo.maestros.EmpleadoCurso;
import modelo.maestros.EmpleadoParametro;
import modelo.maestros.Parametro;

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

public class CEvaluacionSatisfaccion extends CGenerico {

	@Wire
	private Window wdwVEvaluacionSatisfaccion;
	@Wire
	private Button btnGuardar;
	@Wire
	private Button btnLimpiar;
	@Wire
	private Button btnSalir;
	@Wire
	private Textbox txtCursoEvaluacionSatisfaccion;
	@Wire
	private Datebox dbfecha;
	@Wire
	private Button btnBuscarCurso;
	@Wire
	private Listbox lsbParametroInformacionPrevia;
	@Wire
	private Listbox lsbParametroContenidoInformacion;
	@Wire
	private Listbox lsbParametroFacilitadorActividad;
	@Wire
	private Listbox lsbParametroEquipos;
	@Wire
	private Listbox lsbParametroResumen;
	@Wire
	private Div divCatalogoCurso;
	List<EmpleadoCurso> empleadosCurso = new ArrayList<EmpleadoCurso>();
	List<EmpleadoParametro> empleadoParametros = new ArrayList<EmpleadoParametro>();
	private int idCurso = 0;
	private Empleado empleado;

	Mensaje msj = new Mensaje();
	Catalogo<Curso> catalogoCurso;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub

		txtCursoEvaluacionSatisfaccion.setFocus(true);
		String nombreUsuario = nombreUsuarioSesion();
		empleado = servicioEmpleado.buscarPorFicha(servicioUsuario
				.buscarUsuarioPorNombre(nombreUsuario).getFicha());

	}

	public List<Curso> cursosEmpleado() {

		List<Curso> cursos = new ArrayList<Curso>();
		empleadosCurso = servicioEmpleadoCurso.buscarCursos(empleado);

		for (int i = 0; i < empleadosCurso.size(); i++) {

			cursos.add(empleadosCurso.get(i).getCurso());
		}

		return cursos;
	}

	@Listen("onClick = #btnBuscarCurso")
	public void mostrarCatalogoCurso() {
		final List<Curso> listCurso = cursosEmpleado();
		catalogoCurso = new Catalogo<Curso>(divCatalogoCurso,
				"Catalogo de Cursos", listCurso, "�rea", "Nombre", "Duraci�n") {

			@Override
			protected List<Curso> buscarCampos(List<String> valores) {
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

			@Override
			protected List<Curso> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("�rea"))
					return servicioCurso.filtroArea(valor);
				else if (combo.equals("Nombre"))
					return servicioCurso.filtroNombre(valor);
				else if (combo.equals("Duraci�n"))
					return servicioCurso.filtroDuracion(valor);
				else
					return servicioCurso.buscarTodos();
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
		txtCursoEvaluacionSatisfaccion.setValue(curso.getNombre());
		catalogoCurso.setParent(null);
		llenarLista();
	}

	@Listen("onChange = #txtCursoEvaluacionSatisfaccion")
	public void buscarCurso() {
		List<EmpleadoCurso> cursos = servicioEmpleadoCurso.buscar(servicioCurso
				.buscarPorNombre(txtCursoEvaluacionSatisfaccion.getValue()));

		if (cursos.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoCurso);
			txtCursoEvaluacionSatisfaccion.setFocus(true);
		} else {
			idCurso = cursos.get(0).getCurso().getId();
			llenarLista();
		}

	}

	@Listen("onClick = #btnLimpiar")
	public void limpiarCampos() {
		idCurso = 0;
		txtCursoEvaluacionSatisfaccion.setValue("");
		lsbParametroInformacionPrevia.setModel(new ListModelList<Parametro>());
		lsbParametroContenidoInformacion
				.setModel(new ListModelList<Parametro>());
		lsbParametroFacilitadorActividad
				.setModel(new ListModelList<Parametro>());
		lsbParametroEquipos.setModel(new ListModelList<Parametro>());
		lsbParametroResumen.setModel(new ListModelList<Parametro>());
		txtCursoEvaluacionSatisfaccion.setFocus(true);
	}

	public boolean camposLLenos() {
		if (txtCursoEvaluacionSatisfaccion.getText().compareTo("") == 0) {
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

		cerrarVentana1(wdwVEvaluacionSatisfaccion, "Evaluacion de Satisfaccion");
	}

	public void llenarLista() {

		Curso curso = servicioCurso.buscarCurso(idCurso);
		empleadoParametros = servicioEmpleadoParametro
				.buscarParametros(empleado, curso);

		List<Parametro> parametrosTipo1 = new ArrayList<Parametro>();
		parametrosTipo1 = servicioParametro
				.buscarPorTipo("EVALUACION INFORMACION PREVIA");

		List<Parametro> parametrosTipo2 = new ArrayList<Parametro>();
		parametrosTipo2 = servicioParametro
				.buscarPorTipo("EVALUACION CONTENIDO");

		List<Parametro> parametrosTipo3 = new ArrayList<Parametro>();
		parametrosTipo3 = servicioParametro
				.buscarPorTipo("EVALUACION FACILITADOR");

		List<Parametro> parametrosTipo4 = new ArrayList<Parametro>();
		parametrosTipo4 = servicioParametro
				.buscarPorTipo("EVALUACION EQUIPOS E INFRAESTRUCTURA");

		List<Parametro> parametrosTipo5 = new ArrayList<Parametro>();
		parametrosTipo5 = servicioParametro.buscarPorTipo("EVALUACION RESUMEN");

		lsbParametroInformacionPrevia.setModel(new ListModelList<Parametro>(
				parametrosTipo1));
		lsbParametroContenidoInformacion.setModel(new ListModelList<Parametro>(
				parametrosTipo2));
		lsbParametroFacilitadorActividad.setModel(new ListModelList<Parametro>(
				parametrosTipo3));
		lsbParametroEquipos.setModel(new ListModelList<Parametro>(
				parametrosTipo4));
		lsbParametroResumen.setModel(new ListModelList<Parametro>(
				parametrosTipo5));

		lsbParametroInformacionPrevia.renderAll();
		lsbParametroContenidoInformacion.renderAll();
		lsbParametroFacilitadorActividad.renderAll();
		lsbParametroEquipos.renderAll();
		lsbParametroResumen.renderAll();

		if (empleadoParametros.size() != 0) {

			for (int i = 0; i < lsbParametroInformacionPrevia.getItems().size(); i++) {

				for (int j = 0; j < empleadoParametros.size(); j++) {

					if (empleadoParametros.get(j).getParametro().getId() == parametrosTipo1
							.get(i).getId()) {

						Listitem listItem = lsbParametroInformacionPrevia
								.getItemAtIndex(i);

						if (empleadoParametros.get(j).getValorEvaluacion()
								.equals("MUY BUENO")) {

							((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("BUENO")) {

							((Checkbox) ((listItem.getChildren().get(3)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("REGULAR")) {

							((Checkbox) ((listItem.getChildren().get(4)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("MALO")) {

							((Checkbox) ((listItem.getChildren().get(5)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("MUY MALO")) {

							((Checkbox) ((listItem.getChildren().get(6)))
									.getFirstChild()).setChecked(true);

						}

					}

				}

			}

			for (int i = 0; i < lsbParametroContenidoInformacion.getItems()
					.size(); i++) {

				for (int j = 0; j < empleadoParametros.size(); j++) {

					if (empleadoParametros.get(j).getParametro().getId() == parametrosTipo2
							.get(i).getId()) {

						Listitem listItem = lsbParametroContenidoInformacion
								.getItemAtIndex(i);

						if (empleadoParametros.get(j).getValorEvaluacion()
								.equals("MUY BUENO")) {

							((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("BUENO")) {

							((Checkbox) ((listItem.getChildren().get(3)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("REGULAR")) {

							((Checkbox) ((listItem.getChildren().get(4)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("MALO")) {

							((Checkbox) ((listItem.getChildren().get(5)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("MUY MALO")) {

							((Checkbox) ((listItem.getChildren().get(6)))
									.getFirstChild()).setChecked(true);

						}

					}

				}

			}

			for (int i = 0; i < lsbParametroFacilitadorActividad.getItems()
					.size(); i++) {

				for (int j = 0; j < empleadoParametros.size(); j++) {

					if (empleadoParametros.get(j).getParametro().getId() == parametrosTipo3
							.get(i).getId()) {

						Listitem listItem = lsbParametroFacilitadorActividad
								.getItemAtIndex(i);

						if (empleadoParametros.get(j).getValorEvaluacion()
								.equals("MUY BUENO")) {

							((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("BUENO")) {

							((Checkbox) ((listItem.getChildren().get(3)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("REGULAR")) {

							((Checkbox) ((listItem.getChildren().get(4)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("MALO")) {

							((Checkbox) ((listItem.getChildren().get(5)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("MUY MALO")) {

							((Checkbox) ((listItem.getChildren().get(6)))
									.getFirstChild()).setChecked(true);

						}

					}

				}

			}

			for (int i = 0; i < lsbParametroEquipos.getItems().size(); i++) {

				for (int j = 0; j < empleadoParametros.size(); j++) {

					if (empleadoParametros.get(j).getParametro().getId() == parametrosTipo4
							.get(i).getId()) {

						Listitem listItem = lsbParametroEquipos
								.getItemAtIndex(i);

						if (empleadoParametros.get(j).getValorEvaluacion()
								.equals("MUY BUENO")) {

							((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("BUENO")) {

							((Checkbox) ((listItem.getChildren().get(3)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("REGULAR")) {

							((Checkbox) ((listItem.getChildren().get(4)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("MALO")) {

							((Checkbox) ((listItem.getChildren().get(5)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("MUY MALO")) {

							((Checkbox) ((listItem.getChildren().get(6)))
									.getFirstChild()).setChecked(true);

						}

					}

				}

			}
			
			for (int i = 0; i < lsbParametroResumen.getItems().size(); i++) {

				for (int j = 0; j < empleadoParametros.size(); j++) {

					if (empleadoParametros.get(j).getParametro().getId() == parametrosTipo5
							.get(i).getId()) {

						Listitem listItem = lsbParametroResumen
								.getItemAtIndex(i);

						if (empleadoParametros.get(j).getValorEvaluacion()
								.equals("MUY BUENO")) {

							((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("BUENO")) {

							((Checkbox) ((listItem.getChildren().get(3)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("REGULAR")) {

							((Checkbox) ((listItem.getChildren().get(4)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("MALO")) {

							((Checkbox) ((listItem.getChildren().get(5)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoParametros.get(j)
								.getValorEvaluacion().equals("MUY MALO")) {

							((Checkbox) ((listItem.getChildren().get(6)))
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
		boolean parametrosGuardados = false;
		guardar = validar();
		if (guardar) {

			Curso curso = servicioCurso.buscarCurso(idCurso);
			List<EmpleadoCurso> cursoEmpleado = servicioEmpleadoCurso
					.buscar(curso);

			if (cursoEmpleado.size() != 0) {

				if (lsbParametroInformacionPrevia.getItemCount() != 0) {

					for (int i = 0; i < lsbParametroInformacionPrevia
							.getItemCount(); i++) {

						Listitem listItem = lsbParametroInformacionPrevia
								.getItemAtIndex(i);

						if (((Checkbox) ((listItem.getChildren().get(2)))
								.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(3)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(4)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(5)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(6)))
										.getFirstChild()).isChecked()) {

							parametrosGuardados = true;

							int codigoParametro1 = ((Intbox) ((listItem
									.getChildren().get(0))).getFirstChild())
									.getValue();

							String valorEvaluacion = null;

							if (((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).isChecked()) {

								valorEvaluacion = "MUY BUENO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(3))).getFirstChild()).isChecked()) {

								valorEvaluacion = "BUENO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(4))).getFirstChild()).isChecked()) {

								valorEvaluacion = "REGULAR";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(5))).getFirstChild()).isChecked()) {

								valorEvaluacion = "MALO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(6))).getFirstChild()).isChecked()) {

								valorEvaluacion = "MUY MALO";

							}

							Parametro parametro1 = servicioParametro
									.buscarParametro(codigoParametro1);
							EmpleadoParametro empleadoParametro1 = new EmpleadoParametro(
									empleado, parametro1, curso,
									valorEvaluacion);

							servicioEmpleadoParametro
									.guardar(empleadoParametro1);

						}

					}

				}

				if (lsbParametroContenidoInformacion.getItemCount() != 0) {

					for (int i = 0; i < lsbParametroContenidoInformacion
							.getItemCount(); i++) {

						Listitem listItem = lsbParametroContenidoInformacion
								.getItemAtIndex(i);

						if (((Checkbox) ((listItem.getChildren().get(2)))
								.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(3)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(4)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(5)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(6)))
										.getFirstChild()).isChecked()) {

							parametrosGuardados = true;

							int codigoParametro2 = ((Intbox) ((listItem
									.getChildren().get(0))).getFirstChild())
									.getValue();

							String valorEvaluacion = null;

							if (((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).isChecked()) {

								valorEvaluacion = "MUY BUENO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(3))).getFirstChild()).isChecked()) {

								valorEvaluacion = "BUENO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(4))).getFirstChild()).isChecked()) {

								valorEvaluacion = "REGULAR";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(5))).getFirstChild()).isChecked()) {

								valorEvaluacion = "MALO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(6))).getFirstChild()).isChecked()) {

								valorEvaluacion = "MUY MALO";

							}

							Parametro parametro2 = servicioParametro
									.buscarParametro(codigoParametro2);
							EmpleadoParametro empleadoParametro2 = new EmpleadoParametro(
									empleado, parametro2, curso,
									valorEvaluacion);

							servicioEmpleadoParametro
									.guardar(empleadoParametro2);

						}
					}

				}

				if (lsbParametroFacilitadorActividad.getItemCount() != 0) {

					for (int i = 0; i < lsbParametroFacilitadorActividad
							.getItemCount(); i++) {

						Listitem listItem = lsbParametroFacilitadorActividad
								.getItemAtIndex(i);

						if (((Checkbox) ((listItem.getChildren().get(2)))
								.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(3)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(4)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(5)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(6)))
										.getFirstChild()).isChecked()) {

							parametrosGuardados = true;

							int codigoParametro3 = ((Intbox) ((listItem
									.getChildren().get(0))).getFirstChild())
									.getValue();

							String valorEvaluacion = null;

							if (((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).isChecked()) {

								valorEvaluacion = "MUY BUENO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(3))).getFirstChild()).isChecked()) {

								valorEvaluacion = "BUENO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(4))).getFirstChild()).isChecked()) {

								valorEvaluacion = "REGULAR";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(5))).getFirstChild()).isChecked()) {

								valorEvaluacion = "MALO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(6))).getFirstChild()).isChecked()) {

								valorEvaluacion = "MUY MALO";

							}

							Parametro parametro3 = servicioParametro
									.buscarParametro(codigoParametro3);
							EmpleadoParametro empleadoParametro3 = new EmpleadoParametro(
									empleado, parametro3, curso,
									valorEvaluacion);

							servicioEmpleadoParametro
									.guardar(empleadoParametro3);

						}

					}

				}

				if (lsbParametroEquipos.getItemCount() != 0) {

					for (int i = 0; i < lsbParametroEquipos.getItemCount(); i++) {

						Listitem listItem = lsbParametroEquipos
								.getItemAtIndex(i);

						if (((Checkbox) ((listItem.getChildren().get(2)))
								.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(3)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(4)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(5)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(6)))
										.getFirstChild()).isChecked()) {

							parametrosGuardados = true;

							int codigoParametro4 = ((Intbox) ((listItem
									.getChildren().get(0))).getFirstChild())
									.getValue();

							String valorEvaluacion = null;

							if (((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).isChecked()) {

								valorEvaluacion = "MUY BUENO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(3))).getFirstChild()).isChecked()) {

								valorEvaluacion = "BUENO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(4))).getFirstChild()).isChecked()) {

								valorEvaluacion = "REGULAR";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(5))).getFirstChild()).isChecked()) {

								valorEvaluacion = "MALO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(6))).getFirstChild()).isChecked()) {

								valorEvaluacion = "MUY MALO";

							}

							Parametro parametro4 = servicioParametro
									.buscarParametro(codigoParametro4);
							EmpleadoParametro empleadoParametro4 = new EmpleadoParametro(
									empleado, parametro4, curso,
									valorEvaluacion);

							servicioEmpleadoParametro
									.guardar(empleadoParametro4);

						}
					}

				}

				if (lsbParametroResumen.getItemCount() != 0) {

					for (int i = 0; i < lsbParametroResumen.getItemCount(); i++) {

						Listitem listItem = lsbParametroResumen
								.getItemAtIndex(i);

						if (((Checkbox) ((listItem.getChildren().get(2)))
								.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(3)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(4)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(5)))
										.getFirstChild()).isChecked()
								|| ((Checkbox) ((listItem.getChildren().get(6)))
										.getFirstChild()).isChecked()) {

							parametrosGuardados = true;

							int codigoParametro5 = ((Intbox) ((listItem
									.getChildren().get(0))).getFirstChild())
									.getValue();

							String valorEvaluacion = null;

							if (((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).isChecked()) {

								valorEvaluacion = "MUY BUENO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(3))).getFirstChild()).isChecked()) {

								valorEvaluacion = "BUENO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(4))).getFirstChild()).isChecked()) {

								valorEvaluacion = "REGULAR";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(5))).getFirstChild()).isChecked()) {

								valorEvaluacion = "MALO";

							} else if (((Checkbox) ((listItem.getChildren()
									.get(6))).getFirstChild()).isChecked()) {

								valorEvaluacion = "MUY MALO";

							}

							Parametro parametro5 = servicioParametro
									.buscarParametro(codigoParametro5);
							EmpleadoParametro empleadoParametro5 = new EmpleadoParametro(
									empleado, parametro5, curso,
									valorEvaluacion);

							servicioEmpleadoParametro
									.guardar(empleadoParametro5);

						}

					}

				}

				if (parametrosGuardados) {

					msj.mensajeInformacion(Mensaje.guardado);
					limpiarCampos();

				} else {

				}

			} else {

				msj.mensajeAlerta(Mensaje.codigoCurso);
				txtCursoEvaluacionSatisfaccion.setFocus(true);

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
