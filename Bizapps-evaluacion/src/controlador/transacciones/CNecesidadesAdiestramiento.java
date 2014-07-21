package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import modelo.maestros.Curso;
import modelo.maestros.Empleado;
import modelo.maestros.EmpleadoCurso;
import modelo.maestros.PerfilCargo;
import modelo.maestros.Periodo;

import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Catalogo;
import componentes.Mensaje;
import controlador.maestros.CGenerico;

public class CNecesidadesAdiestramiento extends CGenerico {

	@Wire
	private Window wdwVNecesidadesAdiestramiento;
	@Wire
	private Button btnBuscar;
	@Wire
	private Button btnGuardar;
	@Wire
	private Button btnLimpiar;
	@Wire
	private Button btnSalir;
	@Wire
	private Datebox dbfecha;
	@Wire
	private Textbox txtPeriodoNecesidadesAdiestramiento;
	@Wire
	private Textbox txtHorasAcumuladas;
	@Wire
	private Button btnBuscarPeriodo;
	@Wire
	private Listbox lsbEmpleadoDatos;
	@Wire
	private Listbox lsbEmpleadoFormacion;
	@Wire
	private Listbox lsbPerfilCargo;
	@Wire
	private Listbox lsbCursosRealizados;
	@Wire
	private Listbox lsbCursosDisponibles;
	@Wire
	private Div divCatalogoEmpleado;
	@Wire
	private Div divCatalogoPeriodo;
	List<Empleado> empleadoDatos = new ArrayList<Empleado>();
	List<Empleado> empleadoFormacion = new ArrayList<Empleado>();
	List<EmpleadoCurso> cursosEmpleado = new ArrayList<EmpleadoCurso>();
	List<Curso> cursos = new ArrayList<Curso>();
	List<Curso> cursosRealizados = new ArrayList<Curso>();
	List<Curso> cursosDisponibles = new ArrayList<Curso>();
	List<PerfilCargo> perfilCargo = new ArrayList<PerfilCargo>();
	private int idEmpleado = 0;
	private int idCurso = 0;
	private int idPeriodo = 0;

	Mensaje msj = new Mensaje();
	Catalogo<Empleado> catalogoEmpleado;
	Catalogo<Periodo> catalogoPeriodo;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub

		txtPeriodoNecesidadesAdiestramiento.setFocus(true);

	}

	@Listen("onClick = #btnBuscarPeriodo")
	public void mostrarCatalogoPeriodo() {
		final List<Periodo> listPeriodo = servicioPeriodo.buscarTodos();
		catalogoPeriodo = new Catalogo<Periodo>(divCatalogoPeriodo,
				"Catalogo de Periodos", listPeriodo, "Nombre", "Descripci�n",
				"Fecha Inicio", "Fecha Fin", "Estado") {

			@Override
			protected List<Periodo> buscarCampos(List<String> valores) {
				List<Periodo> lista = new ArrayList<Periodo>();

				for (Periodo periodo : listPeriodo) {
					if (periodo.getNombre().toLowerCase()
							.startsWith(valores.get(0))
							&& periodo.getDescripcion().toLowerCase()
									.startsWith(valores.get(1))
							&& String
									.valueOf(
											formatoFecha.format(periodo
													.getFechaInicio()))
									.toLowerCase().startsWith(valores.get(2))
							&& String
									.valueOf(
											formatoFecha.format(periodo
													.getFechaFin()))
									.toLowerCase().startsWith(valores.get(3))
							&& periodo.getEstadoPeriodo().toLowerCase()
									.startsWith(valores.get(4))) {
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

			@Override
			protected List<Periodo> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("Nombre"))
					return servicioPeriodo.filtroNombre(valor);
				else if (combo.equals("Descripci�n"))
					return servicioPeriodo.filtroDescripcion(valor);
				else if (combo.equals("Fecha Inicio"))
					return servicioPeriodo.filtroFechaInicio(valor);
				else if (combo.equals("Fecha Fin"))
					return servicioPeriodo.filtroFechaFin(valor);
				else if (combo.equals("Estado"))
					return servicioPeriodo.filtroEstado(valor);
				else
					return servicioPeriodo.buscarTodos();
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
		txtPeriodoNecesidadesAdiestramiento.setValue(periodo.getNombre());
		catalogoPeriodo.setParent(null);
	}

	@Listen("onChange = #txtPeriodoNecesidadesAdiestramiento")
	public void buscarPeriodo() {
		List<Periodo> periodos = servicioPeriodo
				.buscarPorNombres(txtPeriodoNecesidadesAdiestramiento
						.getValue());

		if (periodos.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoPeriodo);
			txtPeriodoNecesidadesAdiestramiento.setFocus(true);
		} else {
			idPeriodo = periodos.get(0).getId();
		}

	}

	@Listen("onClick = #btnBuscar")
	public void mostrarCatalogoEmpleado() {
		final List<Empleado> listEmpleado = servicioEmpleado.buscarTodos();
		catalogoEmpleado = new Catalogo<Empleado>(divCatalogoEmpleado,
				"Catalogo de Empleados", listEmpleado, "Empresa", "Cargo",
				"Unidad Organizativa", "Nombre", "Ficha", "Ficha Supervisor",
				"Grado Auxiliar") {

			@Override
			protected List<Empleado> buscarCampos(List<String> valores) {
				List<Empleado> lista = new ArrayList<Empleado>();

				for (Empleado empleado : listEmpleado) {
					if (empleado.getEmpresa().getNombre().toLowerCase()
							.startsWith(valores.get(0))
							&& empleado.getCargo().getDescripcion()
									.toLowerCase().startsWith(valores.get(1))
							&& empleado.getUnidadOrganizativa()
									.getDescripcion().toLowerCase()
									.startsWith(valores.get(2))
							&& empleado.getNombre().toLowerCase()
									.startsWith(valores.get(3))
							&& empleado.getFicha().toLowerCase()
									.startsWith(valores.get(4))
							&& empleado.getFichaSupervisor().toLowerCase()
									.startsWith(valores.get(5))
							&& String.valueOf(empleado.getGradoAuxiliar())
									.toLowerCase().startsWith(valores.get(6))) {
						lista.add(empleado);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Empleado empleado) {
				String[] registros = new String[8];
				registros[0] = empleado.getEmpresa().getNombre();
				registros[1] = empleado.getCargo().getDescripcion();
				registros[2] = empleado.getUnidadOrganizativa()
						.getDescripcion();
				registros[3] = empleado.getNombre();
				registros[4] = empleado.getFicha();
				registros[5] = empleado.getFichaSupervisor();
				registros[6] = String.valueOf(empleado.getGradoAuxiliar());

				return registros;
			}

			@Override
			protected List<Empleado> buscar(String valor, String combo) {
				// TODO Auto-generated method stub
				if (combo.equals("Empresa"))
					return servicioEmpleado.filtroEmpresa(valor);
				else if (combo.equals("Cargo"))
					return servicioEmpleado.filtroCargo(valor);
				else if (combo.equals("Unidad Organizativa"))
					return servicioEmpleado.filtroUnidadOrganizativa(valor);
				else if (combo.equals("Nombre"))
					return servicioEmpleado.filtroNombre(valor);
				else if (combo.equals("Ficha"))
					return servicioEmpleado.filtroFicha(valor);
				else if (combo.equals("Ficha Supervisor"))
					return servicioEmpleado.filtroFichaSupervisor(valor);
				else if (combo.equals("Grado Auxiliar"))
					return servicioEmpleado.filtroGradoAuxiliar(valor);
				else
					return servicioEmpleado.buscarTodos();
			}

		};

		catalogoEmpleado.setClosable(true);
		catalogoEmpleado.setWidth("80%");
		catalogoEmpleado.setParent(divCatalogoEmpleado);
		catalogoEmpleado.doModal();
	}

	@Listen("onSeleccion = #divCatalogoEmpleado")
	public void seleccionEmpleado() {
		Empleado empleado = catalogoEmpleado.objetoSeleccionadoDelCatalogo();
		idEmpleado = empleado.getId();
		catalogoEmpleado.setParent(null);
		llenarLista();
	}

	@Listen("onClick = #btnLimpiar")
	public void limpiarCampos() {

		idEmpleado = 0;
		idPeriodo = 0;
		idCurso = 0;
		txtPeriodoNecesidadesAdiestramiento.setValue("");
		txtHorasAcumuladas.setValue("");
		lsbEmpleadoDatos.setModel(new ListModelList<Empleado>());
		lsbEmpleadoFormacion.setModel(new ListModelList<Empleado>());
		lsbPerfilCargo.setModel(new ListModelList<PerfilCargo>());
		lsbCursosDisponibles.setModel(new ListModelList<Curso>());
		lsbCursosRealizados.setModel(new ListModelList<Curso>());
		txtPeriodoNecesidadesAdiestramiento.setFocus(true);
	}

	public boolean camposLLenos() {
		if (txtPeriodoNecesidadesAdiestramiento.getText().compareTo("") == 0) {
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

		cerrarVentana1(wdwVNecesidadesAdiestramiento,
				"Deteccion de Necesidades de Adiestramiento");
	}

	public void llenarLista() {
		empleadoDatos = new ArrayList<Empleado>();
		empleadoFormacion = new ArrayList<Empleado>();
		perfilCargo = new ArrayList<PerfilCargo>();
		cursosEmpleado = new ArrayList<EmpleadoCurso>();
		cursos = new ArrayList<Curso>();
		cursosRealizados = new ArrayList<Curso>();
		cursosDisponibles = new ArrayList<Curso>();
		lsbCursosDisponibles.setModel(new ListModelList<Curso>());
		lsbCursosRealizados.setModel(new ListModelList<Curso>());
		float horasAcumuladas = 0;

		Empleado empleado = servicioEmpleado.buscar(idEmpleado);
		empleadoDatos.add(empleado);
		lsbEmpleadoDatos.setModel(new ListModelList<Empleado>(empleadoDatos));
		empleadoFormacion.add(empleado);
		lsbEmpleadoFormacion.setModel(new ListModelList<Empleado>(
				empleadoFormacion));
		PerfilCargo perfil = servicioPerfilCargo.buscarPorCargo(empleado
				.getCargo());
		perfilCargo.add(perfil);
		lsbPerfilCargo.setModel(new ListModelList<PerfilCargo>(perfilCargo));

		cursos = servicioCurso.buscarTodos();
		cursosEmpleado = servicioEmpleadoCurso.buscarCursos(empleado);

		if (cursosEmpleado.size() != 0) {

			if (cursosEmpleado.get(0).getPeriodo() != null)
				txtPeriodoNecesidadesAdiestramiento.setValue(cursosEmpleado
						.get(0).getPeriodo().getNombre());
			idPeriodo = cursosEmpleado.get(0).getPeriodo().getId();

			for (int i = 0; i < cursosEmpleado.size(); i++) {

				if (cursosEmpleado.get(i).getEstadoCurso().equals("APROBADO")) {

					Curso curso = cursosEmpleado.get(i).getCurso();
					horasAcumuladas = horasAcumuladas
							+ cursosEmpleado.get(i).getCurso().getDuracion();
					cursosRealizados.add(curso);
				}

			}

			txtHorasAcumuladas.setValue(String.valueOf(horasAcumuladas));
			lsbCursosRealizados.setModel(new ListModelList<Curso>(
					cursosRealizados));

		} else {

			lsbCursosDisponibles.setModel(new ListModelList<Curso>(cursos));
			lsbCursosDisponibles.setMultiple(false);
			lsbCursosDisponibles.setCheckmark(false);
			lsbCursosDisponibles.setMultiple(true);
			lsbCursosDisponibles.setCheckmark(true);
		}

		if (cursosEmpleado.size() != 0 && cursos.size() != 0) {

			for (int i = 0; i < cursos.size(); i++) {

				for (int j = 0; j < cursosEmpleado.size(); j++) {

					if (cursos.get(i).getId() == cursosEmpleado.get(j)
							.getCurso().getId()
							&& cursosEmpleado.get(j).getEstadoCurso()
									.equals("APROBADO")) {

						cursos.remove(i);

					}

				}

			}

			lsbCursosDisponibles.setModel(new ListModelList<Curso>(cursos));
			lsbCursosDisponibles.renderAll();
			lsbCursosDisponibles.setMultiple(false);
			lsbCursosDisponibles.setCheckmark(false);
			lsbCursosDisponibles.setMultiple(true);
			lsbCursosDisponibles.setCheckmark(true);

			for (int i = 0; i < lsbCursosDisponibles.getItems().size(); i++) {

				Listitem listItem = lsbCursosDisponibles.getItemAtIndex(i);
				Curso curso = listItem.getValue();
				EmpleadoCurso cursosSeleccionados = servicioEmpleadoCurso
						.buscarPorempleadoYCurso(empleado, curso);
				if (cursosSeleccionados != null) {

					if (cursosSeleccionados.getEstadoCurso().equals(
							"EN TRAMITE")) {

						listItem.setSelected(true);

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

			System.out.println(idPeriodo);
			Periodo periodo = servicioPeriodo.buscarPeriodo(idPeriodo);
			Empleado empleado = servicioEmpleado.buscar(idEmpleado);

			if (periodo != null) {

				if (obtenerSeleccionados().size() != 0) {

					if (obtenerSeleccionados().size() <= 3) {

						if (lsbCursosDisponibles.getItemCount() != 0) {

							for (int j = 0; j < lsbCursosDisponibles
									.getItemCount(); j++) {

								Listitem listItem = lsbCursosDisponibles
										.getItemAtIndex(j);

								Curso cursoRegistrado = listItem.getValue();
								EmpleadoCurso cursosSeleccionados = servicioEmpleadoCurso
										.buscarPorempleadoYCurso(empleado,
												cursoRegistrado);
								if (cursosSeleccionados != null) {

									if (cursosSeleccionados.getEstadoCurso()
											.equals("EN TRAMITE")) {

										servicioEmpleadoCurso
												.eliminar(cursosSeleccionados);

									}
								}
							}

							for (int i = 0; i < lsbCursosDisponibles
									.getItemCount(); i++) {

								Listitem listItem = lsbCursosDisponibles
										.getItemAtIndex(i);

								if (lsbCursosDisponibles.getItems().get(i)
										.isSelected()) {

									Curso curso = listItem.getValue();
									String estadoCurso = "EN TRAMITE";

									EmpleadoCurso cursosEmpleado = new EmpleadoCurso(
											curso, empleado, periodo,
											estadoCurso);
									servicioEmpleadoCurso
											.guardar(cursosEmpleado);

								}

							}

							msj.mensajeInformacion(Mensaje.guardado);
							limpiarCampos();

						}

					} else {

						msj.mensajeAlerta(Mensaje.limiteCurso);

					}

				} else {

					msj.mensajeAlerta(Mensaje.noSeleccionoCurso);
				}

			} else {

				msj.mensajeAlerta(Mensaje.codigoPeriodo);
				txtPeriodoNecesidadesAdiestramiento.setFocus(true);

			}

		}

	}

	public List<Curso> obtenerSeleccionados() {
		List<Curso> valores = new ArrayList<Curso>();
		boolean entro = false;
		if (lsbCursosDisponibles.getItemCount() != 0) {
			final List<Listitem> list1 = lsbCursosDisponibles.getItems();
			for (int i = 0; i < list1.size(); i++) {
				if (list1.get(i).isSelected()) {
					Curso curso = list1.get(i).getValue();
					entro = true;
					valores.add(curso);
				}
			}
			if (!entro) {
				valores.clear();
				return valores;
			}
			return valores;
		} else
			return null;
	}

}
