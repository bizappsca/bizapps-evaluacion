package controlador.transacciones;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Actividad;
import modelo.maestros.ActividadCurso;
import modelo.maestros.Curso;
import modelo.maestros.Empleado;
import modelo.maestros.EmpleadoCurso;
import modelo.maestros.EmpleadoItem;
import modelo.maestros.ItemEvaluacion;
import modelo.maestros.Parametro;
import modelo.maestros.PerfilCargo;
import modelo.maestros.Periodo;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
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

public class CEvaluacionEficacia extends CGenerico {

	@Wire
	private Window wdwVEvaluacionEficacia;
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
	private Button btnCalcularPorcentaje;
	@Wire
	private Textbox txtCursoEvaluacionEficacia;
	@Wire
	private Textbox txtPorcentajeItem;
	@Wire
	private Button btnBuscarCurso;
	@Wire
	private Listbox lsbEmpleadoEvaluacionEficacia;
	@Wire
	private Listbox lsbItemEvaluacionCualitativa;
	@Wire
	private Listbox lsbItemEvaluacionCuantitativa;
	@Wire
	private Textbox txtPorcentajeEvaluacionEficacia;
	@Wire
	private Div divCatalogoEmpleado;
	@Wire
	private Div divCatalogoCurso;
	List<Empleado> empleadoDatos = new ArrayList<Empleado>();
	List<EmpleadoCurso> empleadosCurso = new ArrayList<EmpleadoCurso>();
	List<EmpleadoItem> empleadoItems = new ArrayList<EmpleadoItem>();
	List<ItemEvaluacion> itemPonderacionCualitativa = new ArrayList<ItemEvaluacion>();
	List<ItemEvaluacion> itemPonderacionCuantitativa = new ArrayList<ItemEvaluacion>();
	private int idCurso = 0;
	private int idEmpleado = 0;

	Mensaje msj = new Mensaje();
	Catalogo<Empleado> catalogoEmpleado;
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

		txtCursoEvaluacionEficacia.setFocus(true);

	}

	public List<Curso> cursosEmpleado() {

		List<Curso> cursos = new ArrayList<Curso>();
		empleadosCurso = servicioEmpleadoCurso.buscarCursos(empleadoDatos
				.get(0));

		for (int i = 0; i < empleadosCurso.size(); i++) {

			cursos.add(empleadosCurso.get(i).getCurso());
		}

		return cursos;
	}

	@Listen("onClick = #btnBuscarCurso")
	public void mostrarCatalogoCurso() {

		if (empleadoDatos.size() != 0) {

			final List<Curso> listCurso = cursosEmpleado();
			catalogoCurso = new Catalogo<Curso>(divCatalogoCurso,
					"Catalogo de Cursos", listCurso,true,false,false, "Área", "Nombre",
					"Duración") {

				@Override
				protected List<Curso> buscar(List<String> valores) {
					List<Curso> lista = new ArrayList<Curso>();

					for (Curso curso : listCurso) {
						if (curso.getNombreCurso().getArea().getDescripcion().toLowerCase()
								.contains(valores.get(0).toLowerCase())
								&& curso.getNombreCurso().getNombre().toLowerCase()
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
					registros[0] = curso.getNombreCurso().getArea().getDescripcion();
					registros[1] = curso.getNombreCurso().getNombre();
					registros[2] = String.valueOf(mostrarDuracion(curso)) + " "
							+ curso.getMedidaDuracion();

					return registros;
				}


			};

			catalogoCurso.setClosable(true);
			catalogoCurso.setWidth("80%");
			catalogoCurso.setParent(divCatalogoCurso);
			catalogoCurso.doModal();
		} else {

			Messagebox.show("Debe seleccionar un empleado", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);

		}

	}

	@Listen("onSeleccion = #divCatalogoCurso")
	public void seleccionCurso() {
		Curso curso = catalogoCurso.objetoSeleccionadoDelCatalogo();
		idCurso = curso.getId();
		catalogoCurso.setParent(null);

		EmpleadoCurso cursoEmpleado = servicioEmpleadoCurso
				.buscarPorempleadoYCurso(empleadoDatos.get(0), curso);

		if (cursoEmpleado.getEstadoCurso().equals("APROBADO")
				|| cursoEmpleado.getEstadoCurso().equals("REPROBADO")) {

			txtCursoEvaluacionEficacia.setValue(curso.getNombreCurso().getNombre());
			llenarLista();

		} else {

			Messagebox
					.show("El curso debe estar finalizado para poder realizar la evaluacion de eficacia",
							"Advertencia", Messagebox.OK,
							Messagebox.EXCLAMATION);
		}

	}

	@Listen("onChange = #txtCursoEvaluacionEficacia")
	public void buscarCurso() {
		List<EmpleadoCurso> cursos = servicioEmpleadoCurso.buscar(servicioCurso
				.buscarPorNombre(txtCursoEvaluacionEficacia.getValue()));

		if (cursos.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoCurso);
			txtCursoEvaluacionEficacia.setFocus(true);
		} else {
			idCurso = cursos.get(0).getCurso().getId();
			llenarLista();
		}

	}

	@Listen("onClick = #btnBuscar")
	public void mostrarCatalogoEmpleado() {
		final List<Empleado> listEmpleado = servicioEmpleado.buscarTodos();
		catalogoEmpleado = new Catalogo<Empleado>(divCatalogoEmpleado,
				"Catalogo de Empleados", listEmpleado, true,false,false,"Empresa", "Cargo",
				"Unidad Organizativa", "Nombre", "Ficha", "Ficha Supervisor",
				"Grado Auxiliar") {

			@Override
			protected List<Empleado> buscar(List<String> valores) {
				List<Empleado> lista = new ArrayList<Empleado>();

				for (Empleado empleado : listEmpleado) {
					if (empleado.getEmpresa().getNombre().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& empleado.getCargo().getDescripcion()
									.toLowerCase().contains(valores.get(1).toLowerCase())
							&& empleado.getUnidadOrganizativa()
									.getDescripcion().toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& empleado.getNombre().toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& empleado.getFicha().toLowerCase()
									.contains(valores.get(4).toLowerCase())
							&& empleado.getFichaSupervisor().toLowerCase()
									.contains(valores.get(5).toLowerCase())
							&& String.valueOf(empleado.getGradoAuxiliar())
									.toLowerCase().contains(valores.get(6).toLowerCase())) {
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
		empleadoDatos.add(empleado);
		lsbEmpleadoEvaluacionEficacia.setModel(new ListModelList<Empleado>(
				empleadoDatos));

	}

	@Listen("onClick = #btnLimpiar")
	public void limpiarCampos() {

		idEmpleado = 0;
		idCurso = 0;
		empleadoDatos = new ArrayList<Empleado>();
		txtCursoEvaluacionEficacia.setValue("");
		lsbEmpleadoEvaluacionEficacia.setModel(new ListModelList<Empleado>());
		lsbItemEvaluacionCualitativa
				.setModel(new ListModelList<ItemEvaluacion>());
		lsbItemEvaluacionCuantitativa
				.setModel(new ListModelList<ItemEvaluacion>());
		txtPorcentajeEvaluacionEficacia.setValue("");
		txtCursoEvaluacionEficacia.setFocus(true);
	}

	@Listen("onClick = #btnCalcularPorcentaje")
	public void calcularPorcentaje() {

		Curso curso = servicioCurso.buscarCurso(idCurso);
		int porcentaje = 0;

		if (empleadoDatos.size() != 0) {

			if (curso != null) {

				for (int i = 0; i < lsbItemEvaluacionCuantitativa.getItems()
						.size(); i++) {

					Listitem listItem = lsbItemEvaluacionCuantitativa
							.getItemAtIndex(i);

					if (((Textbox) ((listItem.getChildren().get(2)))
							.getFirstChild()).equals("")) {

						porcentaje = porcentaje + 0;

					} else {

						porcentaje = porcentaje
								+ Integer
										.parseInt(((Textbox) ((listItem
												.getChildren().get(2)))
												.getFirstChild()).getValue());
					}

				}

				porcentaje = porcentaje
						/ lsbItemEvaluacionCuantitativa.getItems().size();

				txtPorcentajeEvaluacionEficacia.setValue(String
						.valueOf(porcentaje));

			} else {

				Messagebox.show("Debe seleccionar un curso", "Advertencia",
						Messagebox.OK, Messagebox.EXCLAMATION);

			}

		} else {

			Messagebox.show("Debe seleccionar un empleado", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);
		}

	}

	public boolean camposLLenos() {
		if (txtCursoEvaluacionEficacia.getText().compareTo("") == 0) {
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
		
		cerrarVentana(wdwVEvaluacionEficacia, "Evaluacion de Eficacia",tabs);

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

	public void llenarLista() {

		Curso curso = servicioCurso.buscarCurso(idCurso);

		itemPonderacionCualitativa = servicioItemEvaluacion
				.buscarPorTipoPonderacion("CUALITATIVA");
		lsbItemEvaluacionCualitativa
				.setModel(new ListModelList<ItemEvaluacion>(
						itemPonderacionCualitativa));

		itemPonderacionCuantitativa = servicioItemEvaluacion
				.buscarPorTipoPonderacion("CUANTITATIVA");
		lsbItemEvaluacionCuantitativa
				.setModel(new ListModelList<ItemEvaluacion>(
						itemPonderacionCuantitativa));

		empleadoItems = servicioEmpleadoItem.buscarItems(empleadoDatos.get(0),
				curso);

		lsbItemEvaluacionCualitativa.renderAll();
		lsbItemEvaluacionCuantitativa.renderAll();

		if (empleadoItems.size() != 0) {

			for (int i = 0; i < lsbItemEvaluacionCualitativa.getItems().size(); i++) {

				for (int j = 0; j < empleadoItems.size(); j++) {

					if (empleadoItems.get(j).getItem().getId() == itemPonderacionCualitativa
							.get(i).getId()) {

						Listitem listItem = lsbItemEvaluacionCualitativa
								.getItemAtIndex(i);

						if (empleadoItems.get(j).getValorEvaluacion()
								.equals("SI")) {

							((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).setChecked(true);

						} else if (empleadoItems.get(j).getValorEvaluacion()
								.equals("NO")) {

							((Checkbox) ((listItem.getChildren().get(3)))
									.getFirstChild()).setChecked(true);

						}

					}

				}

			}

			for (int i = 0; i < lsbItemEvaluacionCuantitativa.getItems().size(); i++) {

				for (int j = 0; j < empleadoItems.size(); j++) {

					if (empleadoItems.get(j).getItem().getId() == itemPonderacionCuantitativa
							.get(i).getId()) {

						Listitem listItem = lsbItemEvaluacionCuantitativa
								.getItemAtIndex(i);

						String valor = empleadoItems.get(j)
								.getValorEvaluacion();

						((Textbox) ((listItem.getChildren().get(2)))
								.getFirstChild()).setValue(valor);

					}

				}

			}

		}

	}

	@Listen("onClick = #btnGuardar")
	public void guardar() {

		boolean guardar = true;
		boolean error = false;
		boolean datosGuardados = false;
		guardar = validar();
		if (guardar) {

			Curso curso = servicioCurso.buscarCurso(idCurso);
			Timestamp fecha = new Timestamp(new Date().getTime());

			if (curso != null) {

				if (lsbItemEvaluacionCualitativa.getItemCount() != 0) {

					for (int i = 0; i < lsbItemEvaluacionCualitativa
							.getItemCount(); i++) {

						Listitem listItem = lsbItemEvaluacionCualitativa
								.getItemAtIndex(i);

						int codigoItem = ((Intbox) ((listItem.getChildren()
								.get(0))).getFirstChild()).getValue();

						String valorEvaluacion = null;

						if (((Checkbox) ((listItem.getChildren().get(2)))
								.getFirstChild()).isChecked()
								&& ((Checkbox) ((listItem.getChildren().get(3)))
										.getFirstChild()).isChecked()) {
							error = true;
							Messagebox
									.show("Debe seleccionar una sola respuesta por cada item de evaluacion",
											"Advertencia", Messagebox.OK,
											Messagebox.EXCLAMATION);

						} else {

							if (((Checkbox) ((listItem.getChildren().get(2)))
									.getFirstChild()).isChecked()) {
								valorEvaluacion = "SI";

							}

							if (((Checkbox) ((listItem.getChildren().get(3)))
									.getFirstChild()).isChecked()) {
								valorEvaluacion = "NO";

							}

						}

						if (!error) {

							ItemEvaluacion item = servicioItemEvaluacion
									.buscarItem(codigoItem);
							EmpleadoItem empleadoItem = new EmpleadoItem(
									empleadoDatos.get(0), item, curso, fecha,
									valorEvaluacion);
							servicioEmpleadoItem.guardar(empleadoItem);
							datosGuardados = true;

						}

					}

				} else {

					Messagebox
							.show("Actualmente no existen items de evaluacion registrados",
									"Advertencia", Messagebox.OK,
									Messagebox.EXCLAMATION);

				}

				if (!error) {

					if (lsbItemEvaluacionCuantitativa.getItemCount() != 0) {

						for (int i = 0; i < lsbItemEvaluacionCuantitativa
								.getItemCount(); i++) {

							Listitem listItem = lsbItemEvaluacionCuantitativa
									.getItemAtIndex(i);

							int codigoItem = ((Intbox) ((listItem.getChildren()
									.get(0))).getFirstChild()).getValue();

							String valorEvaluacion = null;

							valorEvaluacion = ((Textbox) ((listItem
									.getChildren().get(2))).getFirstChild())
									.getValue();

							ItemEvaluacion item = servicioItemEvaluacion
									.buscarItem(codigoItem);
							EmpleadoItem empleadoItem = new EmpleadoItem(
									empleadoDatos.get(0), item, curso, fecha,
									valorEvaluacion);
							servicioEmpleadoItem.guardar(empleadoItem);
							datosGuardados = true;

						}

						if (datosGuardados) {

							msj.mensajeInformacion(Mensaje.guardado);
							limpiarCampos();

						}

					} else {

						Messagebox
								.show("Actualmente no existen items de evaluacion registrados",
										"Advertencia", Messagebox.OK,
										Messagebox.EXCLAMATION);

					}

				}

			} else {

				msj.mensajeAlerta(Mensaje.codigoCurso);
				txtCursoEvaluacionEficacia.setFocus(true);

			}

		}

	}

}
