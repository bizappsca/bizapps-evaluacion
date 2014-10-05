package controlador.transacciones;

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
import modelo.maestros.EmpleadoCurso;
import modelo.maestros.NombreCurso;
import modelo.maestros.Periodo;

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
import controlador.maestros.CGenerico;

public class CConfigurarCurso extends CGenerico {

	@Wire
	private Window wdwVConfiguracionCurso;
	@Wire
	private Button btnGuardar;
	@Wire
	private Button btnLimpiar;
	@Wire
	private Button btnSalir;
	@Wire
	private Textbox txtPeriodoConfiguracionCurso;
	@Wire
	private Textbox txtCursoConfiguracionCurso;
	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Spinner spnDuracionCurso;
	@Wire
	private Textbox txtFacilitadorConfiguracionCurso;
	@Wire
	private Combobox cmbEstadoCurso;
	@Wire
	private Combobox cmbUnidadMedidaCurso;
	@Wire
	private Div divCatalogoCurso;
	@Wire
	private Div divCatalogoPeriodo;
	List<Curso> cursos = new ArrayList<Curso>();

	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd-MM-yyyy");
	private int idCurso = 0;
	private int idNombreCurso = 0;
	private int idPeriodo = 0;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<NombreCurso> catalogo;
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
		txtPeriodoConfiguracionCurso.setFocus(true);
		cmbUnidadMedidaCurso.setValue("HORAS");

	}

	@Listen("onClick = #btnBuscarCurso")
	public void mostrarCatalogoCurso() {
		final List<NombreCurso> listCurso = servicioNombreCurso.buscarTodos();
		catalogo = new Catalogo<NombreCurso>(divCatalogoCurso,
				"Catalogo de Cursos", listCurso, true, false, false, "Área",
				"Nombre") {

			@Override
			protected List<NombreCurso> buscar(List<String> valores) {
				List<NombreCurso> lista = new ArrayList<NombreCurso>();

				for (NombreCurso curso : listCurso) {
					if (curso.getArea().getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& curso.getNombre().toLowerCase()
									.contains(valores.get(1).toLowerCase())) {
						lista.add(curso);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(NombreCurso curso) {
				String[] registros = new String[2];
				registros[0] = curso.getArea().getDescripcion();
				registros[1] = curso.getNombre();

				return registros;
			}

		};

		catalogo.setClosable(true);
		catalogo.setWidth("80%");
		catalogo.setParent(divCatalogoCurso);
		catalogo.doModal();
	}

	@Listen("onSeleccion = #divCatalogoCurso")
	public void seleccionCurso() {
		NombreCurso curso = catalogo.objetoSeleccionadoDelCatalogo();
		idNombreCurso = curso.getId();
		txtCursoConfiguracionCurso.setValue(curso.getNombre());
		catalogo.setParent(null);

		if (idNombreCurso != 0 && idPeriodo != 0) {

			Periodo periodo = servicioPeriodo.buscarPeriodo(idPeriodo);
			Curso cursoPeriodo = servicioCurso.buscarPorCursoYPeriodo(curso,
					periodo);

			if (cursoPeriodo != null) {

				dtbFechaInicio.setValue(cursoPeriodo.getFechaInicio());
				dtbFechaFin.setValue(cursoPeriodo.getFechaFin());
				spnDuracionCurso.setValue((int) mostrarDuracion(cursoPeriodo));
				cmbUnidadMedidaCurso.setValue(cursoPeriodo.getMedidaDuracion());
				cmbEstadoCurso.setValue(cursoPeriodo.getEstado());
				txtFacilitadorConfiguracionCurso.setValue(cursoPeriodo
						.getFacilitador());

			}else{
				
				
				dtbFechaInicio.setValue(null);
				dtbFechaFin.setValue(null);
				spnDuracionCurso.setValue(null);
				cmbEstadoCurso.setValue("");
				txtFacilitadorConfiguracionCurso.setValue("");
				cmbUnidadMedidaCurso.setValue("HORAS");
				
			}

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
		catalogoPeriodo.doModal();

	}

	@Listen("onSeleccion = #divCatalogoPeriodo")
	public void seleccionPeriodo() {
		Periodo periodo = catalogoPeriodo.objetoSeleccionadoDelCatalogo();
		idPeriodo = periodo.getId();
		txtPeriodoConfiguracionCurso.setValue(periodo.getNombre());
		catalogoPeriodo.setParent(null);

		if (idNombreCurso != 0 && idPeriodo != 0) {

			NombreCurso nombreCurso = servicioNombreCurso.buscarCurso(idCurso);
			Curso curso = servicioCurso.buscarPorCursoYPeriodo(nombreCurso,
					periodo);

			if (curso != null) {

				dtbFechaInicio.setValue(curso.getFechaInicio());
				dtbFechaFin.setValue(curso.getFechaFin());
				spnDuracionCurso.setValue((int) mostrarDuracion(curso));
				cmbUnidadMedidaCurso.setValue(curso.getMedidaDuracion());
				cmbEstadoCurso.setValue(curso.getEstado());
				txtFacilitadorConfiguracionCurso.setValue(curso
						.getFacilitador());

			}else{
				
				dtbFechaInicio.setValue(null);
				dtbFechaFin.setValue(null);
				spnDuracionCurso.setValue(null);
				cmbEstadoCurso.setValue("");
				txtFacilitadorConfiguracionCurso.setValue("");
				cmbUnidadMedidaCurso.setValue("HORAS");
			}

		}

	}

	@Listen("onClick = #btnGuardar")
	public void guardar() {
		boolean guardar = true;
		guardar = validar();
		if (guardar) {

			NombreCurso nombreCurso = servicioNombreCurso
					.buscarCurso(idNombreCurso);
			Periodo periodo = servicioPeriodo.buscarPeriodo(idPeriodo);

			if (nombreCurso != null && periodo != null) {

				Timestamp fechaInicio = new Timestamp(dtbFechaInicio.getValue()
						.getTime());
				Timestamp fechaFin = new Timestamp(dtbFechaFin.getValue()
						.getTime());
				float duracion = transformarDuracion(spnDuracionCurso
						.getValue());
				String medidaDuracion = cmbUnidadMedidaCurso.getValue();
				String estado = cmbEstadoCurso.getValue();
				String facilitador = txtFacilitadorConfiguracionCurso
						.getValue();
				String usuario = nombreUsuarioSesion();
				Timestamp fechaAuditoria = new Timestamp(new Date().getTime());
				Curso curso = new Curso(idCurso, periodo, nombreCurso,
						fechaInicio, fechaFin, duracion, medidaDuracion,
						facilitador, estado, fechaAuditoria, horaAuditoria,
						usuario);
				servicioCurso.guardar(curso);
				msj.mensajeInformacion(Mensaje.guardado);
				limpiarCampos();

			} else {

				if (nombreCurso == null) {

					msj.mensajeAlerta(Mensaje.codigoCurso);
					txtCursoConfiguracionCurso.setFocus(true);

				}

				if (periodo == null) {

					msj.mensajeAlerta(Mensaje.codigoPeriodo);
					txtPeriodoConfiguracionCurso.setFocus(true);

				}
			}
		}
	}

	@Listen("onClick = #btnSalir")
	public void salir() {

		cerrarVentana(wdwVConfiguracionCurso, "Configuracion del Curso", tabs);
	}

	@Listen("onClick = #btnLimpiar")
	public void limpiarCampos() {
		idPeriodo = 0;
		idCurso = 0;
		txtPeriodoConfiguracionCurso.setValue("");
		txtCursoConfiguracionCurso.setValue("");
		dtbFechaInicio.setValue(null);
		dtbFechaFin.setValue(null);
		spnDuracionCurso.setValue(null);
		cmbEstadoCurso.setValue("");
		txtFacilitadorConfiguracionCurso.setValue("");
		cmbUnidadMedidaCurso.setValue("HORAS");
		txtPeriodoConfiguracionCurso.setFocus(true);

	}

	public boolean camposLLenos() {
		if (txtPeriodoConfiguracionCurso.getText().compareTo("") == 0
				|| txtCursoConfiguracionCurso.getText().compareTo("") == 0
				|| spnDuracionCurso.getText().compareTo("") == 0
				|| cmbUnidadMedidaCurso.getText().compareTo("") == 0
				|| dtbFechaInicio.getText().compareTo("") == 0
				|| dtbFechaFin.getText().compareTo("") == 0
				|| txtFacilitadorConfiguracionCurso.getText().compareTo("") == 0
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
