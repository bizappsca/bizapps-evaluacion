package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Actividad;
import modelo.maestros.ActividadCurso;
import modelo.maestros.Curso;
import modelo.maestros.Empleado;
import modelo.maestros.EmpleadoCurso;
import modelo.maestros.PerfilCargo;
import modelo.maestros.Periodo;
import modelo.seguridad.Arbol;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import arbol.CArbol;

import componentes.Catalogo;
import componentes.Mensaje;
import controlador.maestros.CGenerico;

public class CBuscarEvaluacionSatisfaccion extends CGenerico {

	@Wire
	private Window wdwVBuscarEvaluacionsatisfaccion;
	@Wire
	private Groupbox gpxFiltros;
	@Wire
	private Button btnBuscar;
	@Wire
	private Button btnCargarEvaluacion;
	@Wire
	private Button btnLimpiar;
	@Wire
	private Button btnSalir;
	@Wire
	private Textbox txtTrabajador;
	@Wire
	private Button btnBuscarTrabajador;
	@Wire
	private Listbox lsbCursosRealizados;
	@Wire
	private Div divCatalogoTrabajador;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Div divCatalogoCurso;
	@Wire
	private Div divCatalogoEmpleado;
	private CArbol cArbol = new CArbol();
	List<EmpleadoCurso> empleadoCurso = new ArrayList<EmpleadoCurso>();
	List<Curso> cursos = new ArrayList<Curso>();

	private int idEmpleado = 0;
	private int idCurso = 0;

	Mensaje msj = new Mensaje();
	Catalogo<Empleado> catalogoEmpleado;
	Catalogo<Curso> catalogoCurso;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub

		contenido = (Include) wdwVBuscarEvaluacionsatisfaccion.getParent();
		Tabbox tabox = (Tabbox) wdwVBuscarEvaluacionsatisfaccion.getParent()
				.getParent().getParent().getParent();
		tabBox = tabox;
		tab = (Tab) tabox.getTabs().getLastChild();

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

		txtTrabajador.setFocus(true);

	}

	@Listen("onClick = #btnCargarEvaluacion")
	public void cargarEvaluacion() {

		if (idEmpleado != 0) {

			Empleado empleado = servicioEmpleado.buscar(idEmpleado);

			if (empleado != null) {

				if (obtenerSeleccionados().size() != 0) {

					if (obtenerSeleccionados().size() == 1) {

						Curso curso = lsbCursosRealizados.getSelectedItem()
								.getValue();

						final HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("id", "consulta");
						map.put("idCurso", curso.getId());
						map.put("idEmpleado", idEmpleado);
						Sessions.getCurrent()
								.setAttribute("itemsCatalogo", map);
						List<Arbol> arboles = servicioArbol
								.buscarPorArbolPorNombre("Evaluacion de Satisfacion");
						if (!arboles.isEmpty()) {
							Arbol arbolItem = arboles.get(0);
							cArbol.abrirVentanas(arbolItem, tabBox, contenido,
									tab, tabs);
						}

						limpiarCampos();

					} else {

						msj.mensajeAlerta(Mensaje.editarSoloUno);

					}

				} else {

					msj.mensajeAlerta(Mensaje.noSeleccionoCurso);
				}

			} else {

				msj.mensajeAlerta(Mensaje.codigoEmpleado);
				txtTrabajador.setFocus(true);

			}

		} else {

			Messagebox.show("Debe selecionar el empleado", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);

		}

	}

	@Listen("onClick = #btnBuscarTrabajador")
	public void mostrarCatalogoEmpleado() {
		final List<Empleado> listEmpleado = servicioEmpleado.buscarTodos();
		catalogoEmpleado = new Catalogo<Empleado>(divCatalogoEmpleado,
				"Catalogo de Empleados", listEmpleado, true, false, false,
				"Empresa", "Cargo", "Unidad Organizativa", "Nombre", "Ficha",
				"Ficha Supervisor", "Grado Auxiliar") {

			@Override
			protected List<Empleado> buscar(List<String> valores) {
				List<Empleado> lista = new ArrayList<Empleado>();

				for (Empleado empleado : listEmpleado) {
					if (empleado.getEmpresa().getNombre().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& empleado.getCargo().getDescripcion()
									.toLowerCase()
									.contains(valores.get(1).toLowerCase())
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
									.toLowerCase()
									.contains(valores.get(6).toLowerCase())) {
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
		catalogoEmpleado.setTitle("Catalogo de Empleados");
		catalogoEmpleado.doModal();
	}

	@Listen("onSeleccion = #divCatalogoEmpleado")
	public void seleccionEmpleado() {
		Empleado empleado = catalogoEmpleado.objetoSeleccionadoDelCatalogo();
		txtTrabajador.setValue(empleado.getNombre());
		idEmpleado = empleado.getId();
		catalogoEmpleado.setParent(null);

		empleadoCurso = servicioEmpleadoCurso.buscarCursos(empleado);

		for (int i = 0; i < empleadoCurso.size(); i++) {

			if (empleadoCurso.get(i).getEstadoCurso().equals("APROBADO")
					|| empleadoCurso.get(i).getEstadoCurso()
							.equals("REPROBADO")) {

				Curso curso = empleadoCurso.get(i).getCurso();
				cursos.add(curso);

			}

		}

		lsbCursosRealizados.setModel(new ListModelList<Curso>(cursos));
		lsbCursosRealizados.renderAll();
		lsbCursosRealizados.setMultiple(false);
		lsbCursosRealizados.setCheckmark(false);
		lsbCursosRealizados.setMultiple(true);
		lsbCursosRealizados.setCheckmark(true);

	}

	@Listen("onClick = #btnLimpiar")
	public void limpiarCampos() {

		idEmpleado = 0;
		idCurso = 0;
		txtTrabajador.setValue("");
		lsbCursosRealizados.setModel(new ListModelList<Curso>());
		txtTrabajador.setFocus(true);

	}

	@Listen("onClick = #btnSalir")
	public void salir() {
		cerrarVentana(wdwVBuscarEvaluacionsatisfaccion,
				titulo, tabs);
	}

	public List<Curso> obtenerSeleccionados() {
		List<Curso> valores = new ArrayList<Curso>();
		boolean entro = false;
		if (lsbCursosRealizados.getItemCount() != 0) {
			final List<Listitem> list1 = lsbCursosRealizados.getItems();
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
