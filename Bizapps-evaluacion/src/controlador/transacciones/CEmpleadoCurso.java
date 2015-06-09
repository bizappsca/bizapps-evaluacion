package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Empleado;
import modelo.maestros.PerfilCargo;
import modelo.maestros.Periodo;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Catalogo;
import componentes.Mensaje;

import controlador.maestros.CGenerico;

public class CEmpleadoCurso extends CGenerico {

	@Wire
	private Window wdwVEmpleadoCurso;
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
	private Textbox txtPeriodoEmpleadoCurso;
	@Wire
	private Button btnBuscarPeriodo;
	@Wire
	private Listbox lsbEmpleadoDatos;
	@Wire
	private Listbox lsbEmpleadoFormacion;
	@Wire
	private Listbox lsbPerfilCargo;
	@Wire
	private Div divCatalogoEmpleado;
	@Wire
	private Div divCatalogoPeriodo;
	List<Empleado> empleadoDatos = new ArrayList<Empleado>();
	List<Empleado> empleadoFormacion = new ArrayList<Empleado>();
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
		txtPeriodoEmpleadoCurso.setFocus(true);

	}

	@Listen("onClick = #btnBuscarPeriodo")
	public void mostrarCatalogoPeriodo() {
		final List<Periodo> listPeriodo = servicioPeriodo.buscarTodos();
		catalogoPeriodo = new Catalogo<Periodo>(divCatalogoPeriodo,
				"Catalogo de Periodos", listPeriodo,true,false,false, "Nombre", "Descripci�n",
				"Fecha Inicio", "Fecha Fin", "Estado") {

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
									.toLowerCase().contains(valores.get(2).toLowerCase())
							&& String
									.valueOf(
											formatoFecha.format(periodo
													.getFechaFin()))
									.toLowerCase().contains(valores.get(3).toLowerCase())
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
		catalogoPeriodo.setTitle("Catalogo de Periodos");
		catalogoPeriodo.doModal();
	}

	@Listen("onSeleccion = #divCatalogoPeriodo")
	public void seleccionPeriodo() {
		Periodo periodo = catalogoPeriodo.objetoSeleccionadoDelCatalogo();
		idPeriodo = periodo.getId();
		txtPeriodoEmpleadoCurso.setValue(periodo.getDescripcion());
		catalogoPeriodo.setParent(null);
	}

	@Listen("onChange = #txtPeriodoEmpleadoCurso")
	public void buscarPeriodo() {
		List<Periodo> periodos = servicioPeriodo
				.buscarPorNombres(txtPeriodoEmpleadoCurso.getValue());

		if (periodos.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoPeriodo);
			txtPeriodoEmpleadoCurso.setFocus(true);
		} else {
			idPeriodo = periodos.get(0).getId();
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
									.contains(valores.get(4))
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
		catalogoEmpleado.setTitle("Catalogo de Empleados");
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
		txtPeriodoEmpleadoCurso.setValue("");
		lsbEmpleadoDatos.setModel(new ListModelList<Empleado>());
		lsbEmpleadoFormacion.setModel(new ListModelList<Empleado>());
		lsbPerfilCargo.setModel(new ListModelList<PerfilCargo>());
		txtPeriodoEmpleadoCurso.setFocus(true);
	}

	public boolean camposLLenos() {
		if (txtPeriodoEmpleadoCurso.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	protected boolean validar() {

		if (!camposLLenos()) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else
			return true;

	}

	@Listen("onClick = #btnSalir")
	public void salir() {

		cerrarVentana(wdwVEmpleadoCurso,titulo, tabs);
	}

	public void llenarLista() {
		empleadoDatos = new ArrayList<Empleado>();
		empleadoFormacion = new ArrayList<Empleado>();
		perfilCargo = new ArrayList<PerfilCargo>();

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
	}


}
