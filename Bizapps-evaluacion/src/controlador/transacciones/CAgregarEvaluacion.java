package controlador.transacciones;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Area;
import modelo.maestros.Bitacora;
import modelo.maestros.Competencia;
import modelo.maestros.Distribucion;
import modelo.maestros.Dominio;
import modelo.maestros.Empleado;
import modelo.maestros.Evaluacion;
import modelo.maestros.EvaluacionCapacitacion;
import modelo.maestros.EvaluacionCompetencia;
import modelo.maestros.EvaluacionConducta;
import modelo.maestros.EvaluacionIndicador;
import modelo.maestros.EvaluacionObjetivo;
import modelo.maestros.Medicion;
import modelo.maestros.NivelCompetenciaCargo;
import modelo.maestros.Perspectiva;
import modelo.maestros.Revision;
import modelo.maestros.TipoFormacion;
import modelo.maestros.UnidadMedida;
import modelo.maestros.Urgencia;
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Mensaje;

import controlador.maestros.CGenerico;

public class CAgregarEvaluacion extends CGenerico {

	private static final long serialVersionUID = -5393608637902961029L;
	Mensaje msj = new Mensaje();
	@Wire
	private Label txttotalIndicador;
	@Wire
	private Label txttotalObjetivos;
	@Wire
	private Label txttotalPesoObjetivos;
	@Wire
	private Textbox txtFortalezas;
	@Wire
	private Textbox txtOportunidades;
	@Wire
	private Textbox txtResumen;
	@Wire
	private Textbox txtCompromisos;
	@Wire
	private Textbox txtComentarios;
	@Wire
	private Textbox txtIndicador;
	@Wire
	private Spinner txtPeso1;
	@Wire
	private Doublespinner txtValorMeta;
	@Wire
	private Button btnCalcular;
	@Wire
	private Button btnAgregarCapacitacion;
	@Wire
	private Button btnAgregarAcciones;
	@Wire
	private Button btnEliminarAcciones;
	@Wire
	private Doublespinner txtValorResultado;
	@Wire
	private Doublespinner txtResFy;
	@Wire
	private Spinner txtResultadoPorc;
	@Wire
	private Spinner txtPesoPorc;
	@Wire
	private Button btnGuardar;
	@Wire
	private Button btnGuardarObjetivo;
	@Wire
	private Button btnImprimir;
	@Wire
	private Button btnOk;
	@Wire
	private Button btnCancelarO;
	@Wire
	private Button btnOk2;
	@Wire
	private Button btnEliminar;
	@Wire
	private Button btnEliminar2;
	@Wire
	private Button btnEliminarIndicador;
	@Wire
	private Button btnAgregarIndicador;
	@Wire
	private Button btnGuardarIndicador;
	@Wire
	private Button btnIr;
	@Wire
	private Listbox lbxIndicadoresAgregados;
	@Wire
	private Listbox lbxObjetivosGuardados;
	@Wire
	private Listbox lbxAgregarIndicador;
	@Wire
	private Listbox lbxAccionesGuardadas;
	@Wire
	private Listbox lbxCompetenciaEspecifica;
	@Wire
	private Combobox cmbObjetivos;
	@Wire
	private Window window;
	@Wire
	private Groupbox gpxAgregar;
	@Wire
	private Groupbox gpxAgregados;
	@Wire
	private Groupbox gpxAgregarCapacitacion;
	@Wire
	private Groupbox gpxObjetivosAgregados;
	@Wire
	private Label lblEvaluacion;
	@Wire
	private Label lblFechaCreacion;
	@Wire
	private Label lblRevision;
	@Wire
	private Textbox txtObjetivo;
	@Wire
	private Textbox txtCorresponsables;
	@Wire
	private Spinner txtPeso;
	@Wire
	private Textbox txtTotal;
	@Wire
	private Textbox txtResultados;
	@Wire
	private Textbox txtDescFormacion;
	@Wire
	private Button btnAgregar;
	@Wire
	private Button btnCancelar;
	@Wire
	private Button btnCancelarI;
	@Wire
	private Button btnCalculo;
	@Wire
	private Button btnCambiarEstado;
	@Wire
	private Button btnAgregarF;
	@Wire
	private Listbox lbxCompetenciaRectora;
	@Wire
	private Listbox lbxObjetivos;
	@Wire
	private Label lblFicha;
	@Wire
	private Label lblNombreTrabajador;
	@Wire
	private Label lblCargo;
	@Wire
	private Label lblUnidadOrganizativa;
	@Wire
	private Label lblGerencia;
	@Wire
	private Combobox cmbNivelRequerido;
	@Wire
	private Window wdwConductasRectoras;
	@Wire
	private Window wdwConductasEspecificas;
	@Wire
	private Window winEvaluacionEmpleado;
	@Wire
	private Groupbox gpxListaPersonal;
	@Wire
	private Groupbox gpxAgregarIndicador;
	@Wire
	private Combobox cmbPerspectiva;
	@Wire
	private Combobox cmbUnidad;
	@Wire
	private Combobox cmbMedicion;
	@Wire
	private Button btnCancelarIndicador;
	@Wire
	private Button btnSalirCompetenciaR;
	@Wire
	private Button btnSalirCompetenciaE;
	@Wire
	private Button btnCancelarEvaluacion;
	@Wire
	private Panel panBotones;
	@Wire
	private Tab tbIndicadores;
	@Wire
	private Tab tbEvaluacionObjetivos;
	@Wire
	private Button btnEnEdicion;
	@Wire
	private Button btnPendiente;
	@Wire
	private Button btnRevisada;
	@Wire
	private Button btnAprobada;
	@Wire
	private Button btnCalibrada;
	@Wire
	private Button btnFinalizada;
	@Wire
	private Label lblResultado1;
	@Wire
	private Label lblResultado;
	@Wire
	private Label lblDistribucion;
	@Wire
	private Label lblDistribucion1;
	@Wire
	private Label lblResultadoPeso;
	@Wire
	private Label lblResultadoPeso1;
	@Wire
	private Label txtResultadoFinal;
	@Wire
	private Label txttotalCompetencia1;
	@Wire
	private Label txttotalCompetencia2;
	
	String content="";
	/*
	 * @Wire private Label txtValoracionFinal;
	 */
	@Wire
	private Combobox cmbUrgencia;
	@Wire
	private Combobox cmbTipoFormacion;
	@Wire
	private Combobox cmbArea;
	
	String tipo = "EVIDENCIADO";

	ListModelList<Dominio> dominio;
	ListModelList<Perspectiva> perspectiva;
	List<EvaluacionCapacitacion> listCapacitacion = new ArrayList<EvaluacionCapacitacion>();
	List<EvaluacionObjetivo> objetivosG = new ArrayList<EvaluacionObjetivo>();
	List<EvaluacionIndicador> indicadores = new ArrayList<EvaluacionIndicador>();
	List<EvaluacionObjetivo> evaluacionObjetivoIndicadores = new ArrayList<EvaluacionObjetivo>();
	List<EvaluacionConducta> evaluacionconductas = new ArrayList<EvaluacionConducta>();
	List<NivelCompetenciaCargo> nivelCompetencia = new ArrayList<NivelCompetenciaCargo>();
	List<NivelCompetenciaCargo> nivelCompetencia1 = new ArrayList<NivelCompetenciaCargo>();

	private int num;
	private Integer idEva;
	private boolean bool = false;
	private boolean validar = false;
	private boolean bool1 = false;
	private int idObjetivo;
	private int idCapacitacion;
	private int idIndicador;
	private String pers;
	private String unid;
	private String medic;
	public Revision revision;
	private String fichaE;
	public Integer numero = 0;
	public Integer idO = 0;
	public EvaluacionObjetivo ev;
	public EvaluacionIndicador evi;
	private Double valor = 0.0;
	private Double total = 0.0;
	private Double totalObjetivo = 0.0;
	private Double totalInd = 0.0;
	private Empleado evaluador = null;
	Usuario u;
	private int idIndicadorE;
	private int idObjetivoE;
	private Empleado empleado;
	private double tind = 0.0;
	public Double totalCompetencia = 0.0;
	public Double totalConducta = 0.0;
	public Double calculo = 0.0;
	public Double porcentaje = 0.0;
	public Double totalCompetencia1 = 0.0;
	public Double totalConducta1 = 0.0;
	public Double calculo1 = 0.0;
	public Double porcentaje1 = 0.0;
	public Double resultadoCompetencia = 0.0;
	public Double resultadoPesoCompetencia = 0.0;
	public Double resultadoPesoObjetivo = 0.0;
	
	private Listbox listbox;
	private String item;
	private boolean agregarW=false;
	private boolean agregarW1=false;
	private CListaPersonal controlador = new CListaPersonal();
	private CEmpleado controlador1 = new CEmpleado();

	@Override
	public void inicializar() throws IOException {

		
		
		// ------------Codigo de Capacitacion------------
		gpxAgregarCapacitacion.setOpen(false);

		// COMPARTIDO PARA AMBOS COMPORTAMIENTOS

		List<Perspectiva> perspectiva = servicioPerspectiva.buscar();
		cmbPerspectiva.setModel(new ListModelList<Perspectiva>(perspectiva));
		// cmbPerspectiva.setValue(perspectiva.get(0).getDescripcion());

		List<Medicion> medicion = servicioMedicion.buscar();
		cmbMedicion.setModel(new ListModelList<Medicion>(medicion));

		List<UnidadMedida> unidad = servicioUnidadMedida.buscar();
		cmbUnidad.setModel(new ListModelList<UnidadMedida>(unidad));

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {

			if (map.get("modo") != null) {

				if (map.get("modo").equals("AGREGAR")) {
					agregarW = true;
					listbox =  (Listbox) map.get("listbox");
					item = (String) map.get("ficha");
					revision = servicioRevision.buscarPorEstado("ACTIVO");
					
					Authentication auth = SecurityContextHolder.getContext()
							.getAuthentication();
					u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
					String ficha = u.getCedula();
					if (!item.equals(ficha)){
						agregarW1= true;
					}
					/*
					 * HashMap<String, Object> map = (HashMap<String, Object>)
					 * Sessions .getCurrent().getAttribFFute("itemsCatalogo");
					 */

					if (map != null) {
						if (map.get("idEva") != null) {
							Integer idEvaluacion = (Integer) map.get("idEva");
							idEva = idEvaluacion;

							System.out.println("viene" + idEva);

							fichaE = ficha;
							Integer numeroEvaluacion = servicioEvaluacion
									.buscarIdSecundario(ficha);
							numero = numeroEvaluacion;
							lblEvaluacion.setValue(numeroEvaluacion.toString());
							lblFechaCreacion.setValue(formatoFecha
									.format(fechaHora));
							lblRevision.setValue(revision.getDescripcion());
							String nombreTrabajador = u.getNombre() + " "
									+ u.getApellido();
							empleado = servicioEmpleado.buscarPorFicha(ficha);
							String cargo = empleado.getCargo().getDescripcion();
							String unidadOrganizativa = empleado
									.getUnidadOrganizativa().getDescripcion();
							String gerenciaReporte = empleado
									.getUnidadOrganizativa().getGerencia()
									.getDescripcion();

							evaluador = servicioEmpleado
									.buscarPorFicha(empleado
											.getFichaSupervisor());
							lblFicha.setValue(ficha);
							lblNombreTrabajador.setValue(nombreTrabajador);
							lblCargo.setValue(cargo);
							lblUnidadOrganizativa.setValue(unidadOrganizativa);
							lblGerencia.setValue(gerenciaReporte);
							lblFicha.setValue(ficha);
							lblNombreTrabajador.setValue(nombreTrabajador);
							lblCargo.setValue(cargo);
							lblUnidadOrganizativa.setValue(unidadOrganizativa);
							lblGerencia.setValue(gerenciaReporte);
							lblEvaluacion.setValue(numeroEvaluacion.toString());
							lblFechaCreacion.setValue(formatoFecha
									.format(fechaHora));

							listCapacitacion = servicioEvaluacionCapacitacion
									.buscarPorEvaluacion(idEva);
							lbxAccionesGuardadas
									.setModel(new ListModelList<EvaluacionCapacitacion>(
											listCapacitacion));
						}

						else {

							HashMap<String, Object> map1 = (HashMap<String, Object>) Sessions
									.getCurrent().getAttribute("itemsCatalogo");
							if (map1 != null) {
								if (map1.get("ficha") != null) {
									String ficha1 = (String) map1.get("ficha");
									Integer idEvaluacion1 = (Integer) map1
											.get("id");
									fichaE = ficha1;
									idEva = idEvaluacion1;
									/*
									 * List<Medicion> medicion =
									 * servicioMedicion.buscar();
									 * cmbMedicion.setModel(new
									 * ListModelList<Medicion>( medicion));
									 * 
									 * List<UnidadMedida> unidad =
									 * servicioUnidadMedida .buscar();
									 * cmbUnidad.setModel(new
									 * ListModelList<UnidadMedida>( unidad));
									 */
									empleado = servicioEmpleado
											.buscarPorFicha(ficha1);
									evaluador = servicioEmpleado
											.buscarPorFicha(empleado
													.getFichaSupervisor());
									String cargo = empleado.getCargo()
											.getDescripcion();
									String unidadOrganizativa = empleado
											.getUnidadOrganizativa()
											.getDescripcion();
									String gerenciaReporte = empleado
											.getUnidadOrganizativa()
											.getGerencia().getDescripcion();
									String nombreTrabajador = empleado
											.getNombre();
									Integer numeroEvaluacion = servicioEvaluacion
											.buscarIdSecundario(fichaE);
									numero = numeroEvaluacion;
									System.out.println(numeroEvaluacion);
									lblFicha.setValue(ficha1);
									lblNombreTrabajador
											.setValue(nombreTrabajador);
									lblCargo.setValue(cargo);
									lblUnidadOrganizativa
											.setValue(unidadOrganizativa);
									lblGerencia.setValue(gerenciaReporte);
									lblEvaluacion.setValue(numeroEvaluacion
											.toString());
									lblFechaCreacion.setValue(formatoFecha
											.format(fechaHora));
									lblRevision.setValue(revision
											.getDescripcion());

								}
							}

						}
						List<NivelCompetenciaCargo> nivel = new ArrayList<NivelCompetenciaCargo>();
						List<NivelCompetenciaCargo> nivel2 = new ArrayList<NivelCompetenciaCargo>();
						List<NivelCompetenciaCargo> nivel3 = new ArrayList<NivelCompetenciaCargo>();
						List<NivelCompetenciaCargo> nivel4 = new ArrayList<NivelCompetenciaCargo>();
						NivelCompetenciaCargo nivelRectoras = new NivelCompetenciaCargo();
						NivelCompetenciaCargo nivelEspecificas = new NivelCompetenciaCargo();

						nivel = servicioNivelCompetenciaCargo.buscar(empleado
								.getCargo());
						for (int j = 0; j < nivel.size(); j++) {
							if (nivel.get(j).getCompetencia().getNivel()
									.equals("RECTORAS")) {
								nivelRectoras = nivel.get(j);
								nivel2.add(nivelRectoras);
								nivelCompetencia = nivel2;
							} else {
								nivel.remove(j);
							}
							lbxCompetenciaRectora
									.setModel(new ListModelList<NivelCompetenciaCargo>(
											nivel2));
						}
						nivel4 = servicioNivelCompetenciaCargo.buscar(empleado
								.getCargo());
						for (int j = 0; j < nivel4.size(); j++) {
							if (nivel4.get(j).getCompetencia().getNivel()
									.equals("ESPECIFICAS")) {
								nivelEspecificas = nivel4.get(j);
								nivel3.add(nivelEspecificas);
								nivelCompetencia1 = nivel3;
							} else {
								nivel4.remove(j);
							}
							lbxCompetenciaEspecifica
									.setModel(new ListModelList<NivelCompetenciaCargo>(
											nivel3));
						}

						gpxAgregar.setOpen(false);
						gpxAgregarIndicador.setOpen(false);
						// txttotalIndicador.setValue(String.valueOf(tind));

						txttotalObjetivos.setValue("0.0");
						txttotalPesoObjetivos.setValue("0.0");

						listCapacitacion = servicioEvaluacionCapacitacion
								.buscarPorEvaluacion(idEva);
						lbxAccionesGuardadas
								.setModel(new ListModelList<EvaluacionCapacitacion>(
										listCapacitacion));

					}

					// TERMINA MODO AGREGAR

				} else {
					// COMIENZA MODO EDITAR

					if (map != null) {
						if (map.get("id") != null) {
							Integer idEvaluacion = (Integer) map.get("id");
							idEva = idEvaluacion;
							String fichaMap = (String) map.get("titulo");
							fichaE = fichaMap;

							Evaluacion evaluacion = servicioEvaluacion
									.buscarEvaluacion(idEvaluacion);
							

							if (evaluacion.getEstadoEvaluacion().equals(
									"EN EDICION")) {
								btnPendiente.setVisible(true);
								btnCambiarEstado.setVisible(false);
							} else if (evaluacion.getEstadoEvaluacion().equals(
									"PENDIENTE")) {
								btnEnEdicion.setVisible(true);
								btnRevisada.setVisible(true);
							} else if (evaluacion.getEstadoEvaluacion().equals(
									"REVISADA")) {
								btnPendiente.setVisible(true);
								btnAprobada.setVisible(true);
							} else if (evaluacion.getEstadoEvaluacion().equals(
									"APROBADA")) {
								btnRevisada.setVisible(true);
								btnCalibrada.setVisible(true);
							} else if (evaluacion.getEstadoEvaluacion().equals(
									"CALIBRADA")) {
								btnAprobada.setVisible(true);
								btnFinalizada.setVisible(true);
							} else if (evaluacion.getEstadoEvaluacion().equals(
									"FINALIZADA")) {
								btnAgregar.setVisible(false);
								btnEliminar.setVisible(false);
								btnAgregarIndicador.setVisible(false);
								btnCambiarEstado.setVisible(false);
							}
							txtCompromisos
									.setValue(evaluacion.getCompromisos());
							txtFortalezas.setValue(evaluacion.getFortalezas());
							txtOportunidades.setValue(evaluacion
									.getOportunidades());
							txtResumen.setValue(evaluacion.getResumen());
							txtComentarios.setValue(evaluacion.getComentario());
							Authentication auth = SecurityContextHolder
									.getContext().getAuthentication();
							u = servicioUsuario.buscarUsuarioPorNombre(auth
									.getName());
							String ficha = evaluacion.getFicha();
							Integer numeroEvaluacion = evaluacion
									.getIdEvaluacionSecundario();
							num = numeroEvaluacion;
							numero = numeroEvaluacion;
							empleado = servicioEmpleado.buscarPorFicha(ficha);
							evaluador = servicioEmpleado
									.buscarPorFicha(empleado
											.getFichaSupervisor());
							// btnCambiarEstado
							// String cargo =
							// empleado.getCargo().getDescripcion();

							// Permite saber el grado de la persona que se esta
							// logueando
							Empleado empleadoGrado = servicioEmpleado
									.buscarPorFicha(u.getFicha());
							Integer grado = empleadoGrado.getGradoAuxiliar();

							String cargo = evaluacion.getCargo()
									.getDescripcion();
							String unidadOrganizativa = empleado
									.getUnidadOrganizativa().getDescripcion();
							String gerenciaReporte = empleado
									.getUnidadOrganizativa().getGerencia()
									.getDescripcion();
							String nombreTrabajador = empleado.getNombre();
							lblRevision.setValue(evaluacion.getRevision()
									.getDescripcion());
							// Combo de Objetivos
							List<EvaluacionObjetivo> evaluacionObjetivo = servicioEvaluacionObjetivo
									.buscarObjetivos(ficha, numeroEvaluacion);
							cmbObjetivos
									.setModel(new ListModelList<EvaluacionObjetivo>(
											evaluacionObjetivo));

							// Listbox que contiene los objetivos
							objetivosG = servicioEvaluacionObjetivo
									.buscarObjetivosEvaluar(idEvaluacion);
							lbxObjetivosGuardados
									.setModel(new ListModelList<EvaluacionObjetivo>(
											objetivosG));

							// Listbox que contine los indicadores
							List<EvaluacionObjetivo> evaluacionObjetivoIndicadores = servicioEvaluacionObjetivo
									.buscarObjetivosEvaluar(idEvaluacion);
							cmbObjetivos
									.setModel(new ListModelList<EvaluacionObjetivo>(
											evaluacionObjetivoIndicadores));

							List<NivelCompetenciaCargo> nivel = new ArrayList<NivelCompetenciaCargo>();
							List<NivelCompetenciaCargo> nivel2 = new ArrayList<NivelCompetenciaCargo>();
							List<NivelCompetenciaCargo> nivel3 = new ArrayList<NivelCompetenciaCargo>();
							List<NivelCompetenciaCargo> nivel4 = new ArrayList<NivelCompetenciaCargo>();
							NivelCompetenciaCargo nivelRectoras = new NivelCompetenciaCargo();
							NivelCompetenciaCargo nivelEspecificas = new NivelCompetenciaCargo();

							nivel = servicioNivelCompetenciaCargo
									.buscar(evaluacion.getCargo());
							for (int j = 0; j < nivel.size(); j++) {
								if (nivel.get(j).getCompetencia().getNivel()
										.equals("RECTORAS")) {
									nivelRectoras = nivel.get(j);
									nivel2.add(nivelRectoras);
									nivelCompetencia = nivel2;
								} else {
									nivel.remove(j);
								}
								lbxCompetenciaRectora
										.setModel(new ListModelList<NivelCompetenciaCargo>(
												nivel2));
							}
							nivel4 = servicioNivelCompetenciaCargo
									.buscar(evaluacion.getCargo());
							for (int j = 0; j < nivel4.size(); j++) {
								if (nivel4.get(j).getCompetencia().getNivel()
										.equals("ESPECIFICAS")) {
									nivelEspecificas = nivel4.get(j);
									nivel3.add(nivelEspecificas);
									nivelCompetencia1 = nivel3;
								} else {
									nivel4.remove(j);
								}
								lbxCompetenciaEspecifica
										.setModel(new ListModelList<NivelCompetenciaCargo>(
												nivel3));
							}

							lblFicha.setValue(ficha);
							lblNombreTrabajador.setValue(nombreTrabajador);
							lblCargo.setValue(cargo);
							lblUnidadOrganizativa.setValue(unidadOrganizativa);
							lblGerencia.setValue(gerenciaReporte);
							lblEvaluacion.setValue(numeroEvaluacion.toString());
							lblFechaCreacion.setValue(formatoFecha
									.format(fechaHora));

							if (evaluacion.getRevision().getEstadoRevision()
									.compareTo("ACTIVO") == 0) {
								if (evaluacion.getEstadoEvaluacion().equals(
										"EN EDICION")) {
									btnAgregar.setVisible(true);
									btnEliminar.setVisible(true);
									btnAgregarIndicador.setVisible(true);
									btnEliminarIndicador.setVisible(true);
									btnAgregarAcciones.setVisible(true);
									btnEliminarAcciones.setVisible(true);
									btnAgregarCapacitacion.setVisible(true);
									btnOk.setVisible(true);
									btnOk2.setVisible(true);
								} else {

									if (evaluacion.getEstadoEvaluacion()
											.equals("PENDIENTE")) {

										if (grado >= 19) {
											btnAgregar.setVisible(true);
											btnEliminar.setVisible(true);
											btnAgregarIndicador
													.setVisible(true);
											btnEliminarIndicador
													.setVisible(true);
											btnAgregarAcciones.setVisible(true);
											btnEliminarAcciones
													.setVisible(true);
											btnAgregarCapacitacion
													.setVisible(true);
											btnOk.setVisible(true);
											btnOk2.setVisible(true);
										} else {
											btnAgregar.setVisible(false);
											btnEliminar.setVisible(false);
											btnOk.setVisible(false);
											btnOk2.setVisible(false);
											btnAgregarIndicador
													.setVisible(false);
											btnEliminarIndicador
													.setVisible(false);
											btnAgregarAcciones
													.setVisible(false);
											btnEliminarAcciones
													.setVisible(false);
											btnCambiarEstado.setVisible(false);
											btnCancelar.setVisible(true);
											btnAgregarCapacitacion
													.setVisible(false);

										}

									} else {
										btnAgregar.setVisible(false);
										btnEliminar.setVisible(false);
										btnOk.setVisible(false);
										btnOk2.setVisible(false);
										btnAgregarIndicador.setVisible(false);
										btnEliminarIndicador.setVisible(false);
										btnAgregarAcciones.setVisible(false);
										btnEliminarAcciones.setVisible(false);
										btnCambiarEstado.setVisible(false);
										btnCancelar.setVisible(true);
										btnAgregarCapacitacion
												.setVisible(false);
									}

									/*
									 * Messagebox .show(
									 * "Esta evaluacion se encuentra en un estado distinto a 'EN EDICION' !, por tal razon no podra guardar ningun cambio !"
									 * , "Información", Messagebox.OK,
									 * Messagebox.INFORMATION);
									 */
								}
							} else {
								btnAgregar.setVisible(false);
								btnEliminar.setVisible(false);
								btnOk.setVisible(false);
								btnOk2.setVisible(false);
								btnAgregarIndicador.setVisible(false);
								btnEliminarIndicador.setVisible(false);
								btnAgregarAcciones.setVisible(false);
								btnEliminarAcciones.setVisible(false);
								btnCambiarEstado.setVisible(false);
								btnCancelar.setVisible(true);
								btnAgregarCapacitacion.setVisible(false);

								/*
								 * Messagebox .show(
								 * "Esta evaluacion pertenece a una revision inactiva !, por tal razon no podra guardar ningun cambio !"
								 * , "Información", Messagebox.OK,
								 * Messagebox.INFORMATION);
								 */

							}


						}
					}
					gpxAgregar.setOpen(false);
					gpxObjetivosAgregados.setOpen(true);
					gpxAgregarIndicador.setOpen(false);
					gpxAgregados.setOpen(false);
					evaluacionconductas = servicioEvaluacionConducta
							.buscarConductas(idEva);
					System.out.println(evaluacionconductas);
					/*
					 * if (evaluacionconductas.size() != 0) { }
					 */
					mostrarDominioRectora();
					mostrarDominioEspecifica();

					// evaluarIndicadores();
					txttotalIndicador.setValue(String.valueOf(tind));

					listCapacitacion = servicioEvaluacionCapacitacion
							.buscarPorEvaluacion(idEva);
					lbxAccionesGuardadas
							.setModel(new ListModelList<EvaluacionCapacitacion>(
									listCapacitacion));

					refrescarCalculosEvaluacion();
				}

			}
		}
		
		/*for(Evaluacion evaluacionAuxiliar:servicioEvaluacion.buscarEvaluacionesRevision() )
		{
			calcularResultadoFinal(evaluacionAuxiliar);
		}*/
		
	}

	@Listen("onClick = #btnPendiente")
	public void pasarPendiente() {
		cambiarEstado();
		if (validar == false){
			String estado = "PENDIENTE";
			revision = servicioRevision.buscarPorEstado("ACTIVO");
			List<Evaluacion> evaluacion = new ArrayList<Evaluacion>();
			evaluacion = servicioEvaluacion.buscarRevision(fichaE, revision.getId(), estado);
			if (evaluacion.size() == 0){
		Evaluacion eva = servicioEvaluacion.buscarEvaluacion(idEva);
		eva.setEstadoEvaluacion("PENDIENTE");
		servicioEvaluacion.guardar(eva);
		Bitacora bitacora = new Bitacora();
		bitacora.setEstadoEvaluacion("PENDIENTE");
		bitacora.setEvaluacion(eva);
		bitacora.setFechaAuditoria(fechaHora);
		bitacora.setHoraAuditoria(horaAuditoria);
		bitacora.setIdUsuario(u);
		servicioBitacora.guardar(bitacora);
		msj.mensajeInformacion(Mensaje.pendiente);
		btnEnEdicion.setVisible(true);
		btnRevisada.setVisible(true);
		btnPendiente.setVisible(false);
		btnCalibrada.setVisible(false);
		btnFinalizada.setVisible(false);
		btnAprobada.setVisible(false);
		}
			else {
				msj.mensajeError(Mensaje.yaExistenPendiente);
			}
		}
		
	}
	
	@Listen("onClick = #btnEnEdicion")
	public void pasarEnEdicion() {
		Evaluacion eva = servicioEvaluacion.buscarEvaluacion(idEva);
		eva.setEstadoEvaluacion("EN EDICION");
		servicioEvaluacion.guardar(eva);
		Bitacora bitacora = new Bitacora();
		bitacora.setEstadoEvaluacion("EN EDICION");
		bitacora.setEvaluacion(eva);
		bitacora.setFechaAuditoria(fechaHora);
		bitacora.setHoraAuditoria(horaAuditoria);
		bitacora.setIdUsuario(u);
		servicioBitacora.guardar(bitacora);
		msj.mensajeInformacion(Mensaje.edicion);
		btnEnEdicion.setVisible(false);
		btnPendiente.setVisible(true);
		btnRevisada.setVisible(false);
		btnCalibrada.setVisible(false);
		btnFinalizada.setVisible(false);
		btnAprobada.setVisible(false);
	}
	
	
	@Listen("onClick = #btnRevisada")
	public void pasarRevisada() {
		Evaluacion eva = servicioEvaluacion.buscarEvaluacion(idEva);
		eva.setEstadoEvaluacion("REVISADA");
		servicioEvaluacion.guardar(eva);
		Bitacora bitacora = new Bitacora();
		bitacora.setEstadoEvaluacion("REVISADA");
		bitacora.setEvaluacion(eva);
		bitacora.setFechaAuditoria(fechaHora);
		bitacora.setHoraAuditoria(horaAuditoria);
		bitacora.setIdUsuario(u);
		servicioBitacora.guardar(bitacora);
		msj.mensajeInformacion(Mensaje.revisada);
		btnPendiente.setVisible(true);
		btnRevisada.setVisible(false);
		btnAprobada.setVisible(true);
		btnEnEdicion.setVisible(false);
		btnCalibrada.setVisible(false);
		btnFinalizada.setVisible(false);
	
	}
	
	@Listen("onClick = #btnAprobada")
	public void PasarAprobada() {
		Evaluacion eva = servicioEvaluacion.buscarEvaluacion(idEva);
		eva.setEstadoEvaluacion("APROBADA");
		servicioEvaluacion.guardar(eva);
		Bitacora bitacora = new Bitacora();
		bitacora.setEstadoEvaluacion("APROBADA");
		bitacora.setEvaluacion(eva);
		bitacora.setFechaAuditoria(fechaHora);
		bitacora.setHoraAuditoria(horaAuditoria);
		bitacora.setIdUsuario(u);
		servicioBitacora.guardar(bitacora);
		msj.mensajeInformacion(Mensaje.aprobada);
		btnAprobada.setVisible(false);
		btnCalibrada.setVisible(true);
		btnRevisada.setVisible(true);
		btnEnEdicion.setVisible(false);
		btnPendiente.setVisible(false);
		btnFinalizada.setVisible(false);
		
	}
	
	@Listen("onClick = #btnCalibrada")
	public void pasarCalibrada() {
		Evaluacion eva = servicioEvaluacion.buscarEvaluacion(idEva);
		eva.setEstadoEvaluacion("CALIBRADA");
		servicioEvaluacion.guardar(eva);
		Bitacora bitacora = new Bitacora();
		bitacora.setEstadoEvaluacion("CALIBRADA");
		bitacora.setEvaluacion(eva);
		bitacora.setFechaAuditoria(fechaHora);
		bitacora.setHoraAuditoria(horaAuditoria);
		bitacora.setIdUsuario(u);
		servicioBitacora.guardar(bitacora);
		msj.mensajeInformacion(Mensaje.calibrada);
		btnCalibrada.setVisible(false);
		btnFinalizada.setVisible(true);
		btnAprobada.setVisible(true);
		btnEnEdicion.setVisible(false);
		btnPendiente.setVisible(false);
		btnRevisada.setVisible(false);
		
	}
	
	@Listen("onClick = #btnFinalizada")
	public void pasarFinalizada() {
		Evaluacion eva = servicioEvaluacion.buscarEvaluacion(idEva);
		eva.setEstadoEvaluacion("FINALIZADA");
		servicioEvaluacion.guardar(eva);
		Bitacora bitacora = new Bitacora();
		bitacora.setEstadoEvaluacion("FINALIZADA");
		bitacora.setEvaluacion(eva);
		bitacora.setFechaAuditoria(fechaHora);
		bitacora.setHoraAuditoria(horaAuditoria);
		bitacora.setIdUsuario(u);
		servicioBitacora.guardar(bitacora);
		msj.mensajeInformacion(Mensaje.finalizada);
		btnFinalizada.setVisible(false);
		btnCalibrada.setVisible(true);
		btnEnEdicion.setVisible(false);
		btnPendiente.setVisible(false);
		btnRevisada.setVisible(false);
		btnAprobada.setVisible(false);
		
	}

	public ListModelList<Dominio> getDominio() {
		dominio = new ListModelList<Dominio>(
				servicioDominio.buscarPorTipo(tipo));
		return dominio;
	}

	@Listen("onClick = #btnEliminarIndicador")
	public void eliminarI() {
		eliminarIndicador();
	}

	@Listen("onClick = #btnCancelarO")
	public void cerrarPanel() {
		gpxAgregar.setOpen(false);

		btnAgregar.setDisabled(false);
		btnEliminar.setDisabled(false);
		btnIr.setDisabled(false);

	}

	@Listen("onClick = #btnCancelarI")
	public void cerrarPanelI() {
		idIndicador = 0;
		limpiarI();
		gpxAgregarIndicador.setOpen(false);
		btnAgregarIndicador.setDisabled(false);
		btnEliminarIndicador.setDisabled(false);
	}

	@Listen("onClick = #btnEliminar")
	public void eliminarObj() {
		eliminarObjetivo();

	}

	@Listen("onClick = #tbIndicadores")
	public void mostrarObjetivos() {

		List<EvaluacionObjetivo> evaluacionObjetivo = servicioEvaluacionObjetivo
				.buscarObjetivos(fichaE, numero);
		cmbObjetivos.setModel(new ListModelList<EvaluacionObjetivo>(
				evaluacionObjetivo));

		lbxIndicadoresAgregados
				.setModel(new ListModelList<EvaluacionIndicador>());
		txttotalIndicador.setValue("0.0");

		bool1 = true;

	}

	@Listen("onClick = #tbEvaluacionObjetivos")
	public void ir() {
		cmbObjetivos.setValue(null);
		txtIndicador.setValue("0.0");
	}

	@Listen("onSelect = #cmbObjetivos")
	public void mostrarIndicadores() {
		tind = 0;
		// txttotalIndicador.setValue("0.0");
		gpxAgregados.setOpen(true);
		lbxIndicadoresAgregados.getItems().clear();
		List<EvaluacionIndicador> evaluacionObjetivoIndicador = new ArrayList<EvaluacionIndicador>();
		Integer idObjetivo = Integer.parseInt(cmbObjetivos.getSelectedItem()
				.getContext());
		evaluacionObjetivoIndicador = servicioEvaluacionIndicador
				.buscarIndicadores(idObjetivo);
		lbxIndicadoresAgregados
				.setModel(new ListModelList<EvaluacionIndicador>(
						evaluacionObjetivoIndicador));

		EvaluacionObjetivo eo = servicioEvaluacionObjetivo
				.buscarObjetivosId(idObjetivo);

		txttotalIndicador.setValue(String
				.valueOf(calcularTotalPesoIndicadores(idObjetivo)));
	}

	@Listen("onClick = #btnCancelar")
	public void salir() {
		if(agregarW && !agregarW1){
		
			System.out.println("ojo");
			System.out.println(agregarW +""+ agregarW1);
			controlador.actualizame(listbox, servicioUsuario, servicioEvaluacion, servicioEmpleado);
			agregarW = false;
		}
		if (agregarW1 && agregarW){
			System.out.println("ojo1");
			System.out.println(agregarW +""+ agregarW1);
			controlador1.actualizame1(listbox, servicioUsuario, servicioEvaluacion, servicioEmpleado,item);
			agregarW1= false;
		}
		cerrarVentana1(winEvaluacionEmpleado, "Personal");
	}

	@Listen("onClick = #btnAgregar")
	public void AgregarObjetivo() {
		limpiar();
		idObjetivo = 0;
		gpxAgregar.setOpen(true);
		List<Perspectiva> perspectiva = servicioPerspectiva.buscar();
		cmbPerspectiva.setModel(new ListModelList<Perspectiva>(perspectiva));
		// cmbPerspectiva.setValue(perspectiva.get(0).getDescripcion());

		btnAgregar.setDisabled(true);
		btnEliminar.setDisabled(true);
		btnIr.setDisabled(true);

	}

	@Listen("onClick = #btnOk")
	public void AgregarObjetivo2() {
		gpxAgregados.setOpen(true);

		boolean campoBlanco = false;

		if (txtObjetivo.getValue().trim().equals("")
				|| txtCorresponsables.getValue().trim().equals("")
				|| cmbPerspectiva.getSelectedItem() == null) {
			campoBlanco = true;
		}

		if (campoBlanco) {
			Messagebox.show("Debe llenar todos los campos de Objetivo",
					"Error", Messagebox.OK, Messagebox.EXCLAMATION);

		} else {

			if (idObjetivo != 0) {
				EvaluacionObjetivoActualizar();
			} else {

				String perspectivaCombo = cmbPerspectiva.getSelectedItem()
						.getContext();

				Perspectiva perspectiva = servicioPerspectiva.buscarId(Integer
						.parseInt(perspectivaCombo));
				String objetivo = txtObjetivo.getValue();
				String corresponsables = txtCorresponsables.getValue();
				Double peso = Double.valueOf(txtPeso.getValue());
				EvaluacionObjetivo objetivoLista = new EvaluacionObjetivo();
				Integer linea = objetivosG.size() + 1;
				objetivoLista.setIdEvaluacion(idEva);
				objetivoLista.setDescripcionObjetivo(objetivo);
				objetivoLista.setPerspectiva(perspectiva);
				objetivoLista.setLinea(linea);
				objetivoLista.setPeso(peso);
				objetivoLista.setResultado(0);
				objetivoLista.setTotalInd(0);
				objetivoLista.setCorresponsables(corresponsables);
				ev = objetivoLista;
				objetivosG.add(objetivoLista);
				validarAgregarObjetivo();
				objetivosG = servicioEvaluacionObjetivo
						.buscarObjetivosEvaluar(idEva);
				lbxObjetivosGuardados
						.setModel(new ListModelList<EvaluacionObjetivo>(
								objetivosG));
			}

			refrescarCalculosEvaluacion();

			idObjetivo = 0;

			btnAgregar.setDisabled(false);
			btnEliminar.setDisabled(false);
			btnIr.setDisabled(false);

		}

	}

	public void limpiar() {
		txtObjetivo.setValue("");
		cmbPerspectiva.setValue(null);
		txtCorresponsables.setValue("");
		txtPeso.setValue(null);

	}

	public void limpiarI() {
		txtIndicador.setValue("");
		txtPesoPorc.setValue(0);
		txtResFy.setValue(0.0);
		txtResultadoPorc.setValue(0);
		txtValorMeta.setValue(null);
		txtValorResultado.setValue(null);
		cmbMedicion.setValue(null);
		cmbUnidad.setValue(null);
		txtPeso1.setValue(null);
		idIndicador = 0;
	}

	@Listen("onClick = #btnAgregarIndicador")
	public void AgregarIndicador() {

		if (cmbObjetivos.getText().compareTo("") == 0) {
			Messagebox.show(
					"Debe seleccionar un objetivo para agregar un indicador",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			idIndicador = 0;
			limpiarI();
			gpxAgregarIndicador.setOpen(true);
			btnAgregarIndicador.setDisabled(true);
			btnEliminarIndicador.setDisabled(true);
		}

	}

	@Listen("onClick = #btnOk2")
	public void AgregarIndicador1() {
		String idObjetivo = "0";

		idObjetivo = cmbObjetivos.getSelectedItem().getContext();

		if (cmbObjetivos.getText().compareTo("") == 0
				|| cmbUnidad.getText().compareTo("") == 0
				|| cmbMedicion.getText().compareTo("") == 0
				|| txtPeso1.getText().compareTo("") == 0
				// || txtValorMeta.getText().compareTo("") == 0
				|| txtIndicador.getText().trim().compareTo("") == 0
				// || txtResultadoPorc.getText().compareTo("") == 0
				// || txtPesoPorc.getText().compareTo("") == 0
				|| txtValorMeta.getText().compareTo("") == 0)

		{
			Messagebox.show("Debe llenar todos los campos de Indicador",
					"Advertencia", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			if (idIndicador != 0) {
				EvaluacionIndicadorActualizar();
			} else {
				gpxAgregados.setOpen(true);
				String indicador = txtIndicador.getValue();
				String unidadCombo = cmbUnidad.getSelectedItem().getContext();
				UnidadMedida unidad = servicioUnidadMedida.buscarUnidad(Integer
						.parseInt(unidadCombo));
				String medicionCombo = cmbMedicion.getSelectedItem()
						.getContext();
				Medicion medicion = servicioMedicion.buscarMedicion(Integer
						.parseInt(medicionCombo));

				if (bool1 == false) {
					idObjetivo = String.valueOf(idO);
				} else {
					idObjetivo = cmbObjetivos.getSelectedItem().getContext();
				}
				Double peso = Double.valueOf(txtPeso1.getValue());
				Double valorMeta = Double.valueOf(txtValorMeta.getValue());
				Double valorResultado = Double.valueOf(txtValorResultado
						.getValue());
				Double resFy = Double.valueOf(txtResFy.getValue());
				Double resultadoPorc = Double.valueOf(txtResultadoPorc
						.getValue());
				Double pesoPorc = Double.valueOf(txtPesoPorc.getValue());
				Integer linea = indicadores.size() + 1;

				EvaluacionIndicador indicadorLista = new EvaluacionIndicador();
				indicadorLista.setIdIndicador(servicioEvaluacionIndicador
						.buscarId() + 1);
				indicadorLista.setIdObjetivo(Integer.parseInt(idObjetivo));
				indicadorLista.setDescripcionIndicador(indicador);
				indicadorLista.setMedicion(medicion);
				indicadorLista.setUnidadMedida(unidad);
				indicadorLista.setLinea(linea);
				indicadorLista.setPeso(peso);
				indicadorLista.setResultadoFyAnterior(resFy);
				indicadorLista.setResultadoPeso(pesoPorc);
				indicadorLista.setResultadoPorc(resultadoPorc);
				indicadorLista.setValorMeta(valorMeta);
				indicadorLista.setValorResultado(valorResultado);
				indicadorLista.setTotal(0);
				evi = indicadorLista;
				indicadores.add(indicadorLista);
				validarAgregarIndicador(Integer.parseInt(idObjetivo), peso);
				indicadores.remove(indicadorLista);
				indicadores = servicioEvaluacionIndicador
						.buscarIndicadores(Integer.parseInt(idObjetivo));
				for (int i = 0; i < indicadores.size(); i++) {
					int id = indicadores.get(i).getIdObjetivo();

					if (Integer.valueOf(idObjetivo) == id) {
						lbxIndicadoresAgregados
								.setModel(new ListModelList<EvaluacionIndicador>(
										indicadores));
					} else {
						indicadores.remove(indicadorLista);
					}
				}
			}

			idIndicador = 0;
			btnAgregarIndicador.setDisabled(false);
			btnEliminarIndicador.setDisabled(false);

		}
		refrescarCalculosEvaluacion();
		txttotalIndicador.setValue(String
				.valueOf(calcularTotalPesoIndicadores(Integer
						.parseInt(idObjetivo))));
	}

	// indicadores.add(indicadorLista);
	// lbxIndicadoresAgregados
	// .setModel(new ListModelList<EvaluacionIndicador>(
	// indicadores));
	// servicioEvaluacionIndicador.guardar(indicadorLista);
	// Messagebox.show("Indicador para el objetivo" + " "
	// + cmbObjetivos.getValue() + " "
	// + "ha sido guardado exitosamente", "Información",
	// Messagebox.OK, Messagebox.INFORMATION);
	// gpxAgregarIndicador.setOpen(false);
	// idIndicador=0;
	// limpiarI();
	// }
	// }
	// evaluarIndicadores();
	// }

	@Listen("onSelect = #cmbMedicion")
	public void mostrarResAnterior() {

		if (cmbMedicion.getValue().equals("CONTINUA")) {
			txtResFy.setDisabled(false);
			txtResFy.setValue(null);
		} else {
			txtResFy.setDisabled(true);
			txtResFy.setValue(0.0);
		}

	}

	public void validar() {
		Evaluacion evaluacion = servicioEvaluacion.buscarEvaluacion(idEva);
		String ficha = evaluacion.getFicha();
		Integer numeroEvaluacion = servicioEvaluacion.buscar(ficha).size();
		List<EvaluacionObjetivo> evaluacionObjetivoIndicadores = servicioEvaluacionObjetivo
				.buscarObjetivosEvaluar(idEva);
		List<EvaluacionIndicador> evaluacionObjetivoIndicador = new ArrayList<EvaluacionIndicador>();
		if (evaluacionObjetivoIndicadores != null) {
			for (int i = 0; i < evaluacionObjetivoIndicadores.size(); i++) {
				int idObjetivo = evaluacionObjetivoIndicadores.get(i)
						.getIdObjetivo();
				evaluacionObjetivoIndicador = servicioEvaluacionIndicador
						.buscarIndicadores(idObjetivo);
				Double sumaPeso = (double) 0;
				for (int j = 0; j < evaluacionObjetivoIndicador.size(); j++) {
					Double peso = evaluacionObjetivoIndicador.get(j).getPeso();
					sumaPeso = peso + sumaPeso;
				}

				if (sumaPeso != 100) {
					bool = true;
					i = 1000000000;
				}
			}

		}

	}

	// @Listen("onClick = #btnCambiarEstado")
	public void cambiarEstado() {
		Evaluacion evaluacion = servicioEvaluacion.buscarEvaluacion(idEva);
		validar();
		Double sumaPeso = (double) 0;
		for (int j = 0; j < objetivosG.size(); j++) {
			Double peso = objetivosG.get(j).getPeso();
			sumaPeso = sumaPeso + peso;
		}
		if (sumaPeso != 100) {
			Messagebox
					.show("La suma de los pesos de los objetivos debe ser igual a 100",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
			validar = true;

		}

		else if (bool == true) {
			Messagebox
					.show("La suma de los pesos de los indicadores debe ser igual a 100",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
			validar = true;

		} else {

			// String estado = "PENDIENTE";
			// evaluacion.setEstadoEvaluacion(estado);
			// servicioEvaluacion.guardar(evaluacion);
	
//			Messagebox.show("Evaluación guardada con exito", "Información",
//					Messagebox.OK, Messagebox.INFORMATION);
		}

	}

	@Listen("onDoubleClick = #lbxObjetivosGuardados")
	public void mostrarEvaluacion() {
		gpxAgregar.setOpen(true);
		if (lbxObjetivosGuardados.getItemCount() != 0) {

			Listitem listItem = lbxObjetivosGuardados.getSelectedItem();
			if (listItem != null) {
				EvaluacionObjetivo evaluacionObjetivo = (EvaluacionObjetivo) listItem
						.getValue();
				idObjetivo = evaluacionObjetivo.getIdObjetivo();
				Integer linea = evaluacionObjetivo.getLinea();
				String objetivo = evaluacionObjetivo.getDescripcionObjetivo();
				String corresponsables = evaluacionObjetivo
						.getCorresponsables();
				Double peso = evaluacionObjetivo.getPeso();
				Perspectiva perspectiva = evaluacionObjetivo.getPerspectiva();
				txtObjetivo.setValue(objetivo);
				txtCorresponsables.setValue(corresponsables);
				int peso1 = peso.intValue();
				txtPeso.setValue(peso1);
				cmbPerspectiva.setValue(perspectiva.getDescripcion());
				pers = perspectiva.getDescripcion();
				// cmbPerspectiva.setDisabled(true);
			}
		}
	}

	@Listen("onClick = #btnIr")
	public void mostrarPestannaIndicadores() {
		mostrarObjetivos();
		tbIndicadores.setSelected(true);
		gpxAgregados.setOpen(true);
		if (lbxObjetivosGuardados.getItemCount() != 0) {
			Listitem listItem = lbxObjetivosGuardados.getSelectedItem();
			if (listItem != null) {
				EvaluacionObjetivo evaluacionObjetivo = (EvaluacionObjetivo) listItem
						.getValue();
				idObjetivo = evaluacionObjetivo.getIdObjetivo();
				List<EvaluacionObjetivo> evaluacionObjetivo1 = servicioEvaluacionObjetivo
						.buscarObjetivos(fichaE, numero);
				evaluacionObjetivoIndicadores = servicioEvaluacionObjetivo
						.buscarObjetivosEvaluar(idEva);
				System.out.println("objeto" + evaluacionObjetivoIndicadores);
				if (evaluacionObjetivo1.size() == 1) {
					cmbObjetivos.setValue(evaluacionObjetivoIndicadores.get(0)
							.getDescripcionObjetivo());
					idO = evaluacionObjetivoIndicadores.get(0).getIdObjetivo();

				}
				for (int i = 0; i < evaluacionObjetivoIndicadores.size(); i++) {
					Integer idOb = evaluacionObjetivoIndicadores.get(i)
							.getIdObjetivo();
					if (idOb == idObjetivo) {
						cmbObjetivos.setValue(evaluacionObjetivo1.get(i)
								.getDescripcionObjetivo());
						idO = evaluacionObjetivoIndicadores.get(i)
								.getIdObjetivo();
					}
				}
			}
			indicadores = servicioEvaluacionIndicador
					.buscarIndicadores(idObjetivo);
			lbxIndicadoresAgregados
					.setModel(new ListModelList<EvaluacionIndicador>(
							indicadores));
		}
		idObjetivo = 0;

		refrescarCalculosEvaluacion();

	}

	@Listen("onDoubleClick = #lbxIndicadoresAgregados")
	public void mostrarEvaluacionIndicadores() {
		gpxAgregarIndicador.setOpen(true);
		if (lbxIndicadoresAgregados.getItemCount() != 0) {

			Listitem listItem = lbxIndicadoresAgregados.getSelectedItem();
			if (listItem != null) {
				EvaluacionIndicador evaluacionIndicador = (EvaluacionIndicador) listItem
						.getValue();
				idObjetivo = evaluacionIndicador.getIdObjetivo();
				idIndicador = evaluacionIndicador.getIdIndicador();
				Integer linea = evaluacionIndicador.getLinea();
				String indicador = evaluacionIndicador
						.getDescripcionIndicador();
				String unidad = evaluacionIndicador.getUnidadMedida()
						.getDescripcion();
				unid = unidad;
				String medicion = evaluacionIndicador.getMedicion()
						.getDescripcionMedicion();
				medic = medicion;
				Double peso = evaluacionIndicador.getPeso();
				Double valorMeta = evaluacionIndicador.getValorMeta();
				Double valorResultado = evaluacionIndicador.getValorResultado();
				Double fyAnterior = evaluacionIndicador
						.getResultadoFyAnterior();
				Double resultadoPorc = evaluacionIndicador.getResultadoPorc();
				Double resultadoPeso = evaluacionIndicador.getResultadoPeso();
				Double total = evaluacionIndicador.getTotal();
				txtIndicador.setValue(indicador);
				cmbMedicion.setValue(medicion);
				cmbUnidad.setValue(unidad);
				int peso1 = peso.intValue();
				// int valor = valorMeta.intValue();
				// int valorR = valorResultado.intValue();
				// int fyAnt = fyAnterior.intValue();
				int resulPorc = resultadoPorc.intValue();
				int resulP = resultadoPeso.intValue();
				int tot = total.intValue();
				txtPeso1.setValue(peso1);
				txtResFy.setValue(fyAnterior);
				txtValorMeta.setValue(valorMeta);
				txtValorResultado.setValue(valorResultado);
				txtResultadoPorc.setValue(resulPorc);
				txtPesoPorc.setValue(resulP);
				// cmbMedicion.setDisabled(true);
				// cmbUnidad.setDisabled(true);

				if (cmbMedicion.getValue().equals("CONTINUA")) {
					txtResFy.setDisabled(false);
				} else {
					txtResFy.setDisabled(true);
				}

			}
		}
	}

	private void EvaluacionObjetivoActualizar() {

		List<EvaluacionObjetivo> listaEvaluacionObjetivos = servicioEvaluacionObjetivo
				.buscarObjetivosEvaluar(idEva);

		Double sumaPeso = (double) 0;
		for (int j = 0; j < listaEvaluacionObjetivos.size(); j++) {

			Double peso = 0.0;

			if (listaEvaluacionObjetivos.get(j).getIdObjetivo() != idObjetivo) {
				peso = listaEvaluacionObjetivos.get(j).getPeso();
			}

			sumaPeso = sumaPeso + peso;
		}

		String objetivo = txtObjetivo.getValue();
		String corresponsables = txtCorresponsables.getValue();
		Double peso = Double.valueOf(txtPeso.getValue());
		String perspectivaCombo = cmbPerspectiva.getSelectedItem().getContext();
		Perspectiva perspectiva = servicioPerspectiva.buscarId(Integer
				.parseInt(perspectivaCombo));
		EvaluacionObjetivo objetivoLista = servicioEvaluacionObjetivo
				.buscarObjetivosId(idObjetivo);
		objetivoLista.setDescripcionObjetivo(objetivo);
		objetivoLista.setPerspectiva(perspectiva);
		objetivoLista.setPeso(peso);
		objetivoLista.setCorresponsables(corresponsables);

		sumaPeso = sumaPeso + peso;

		if (sumaPeso > 100) {
			Messagebox
					.show("La suma de los pesos de los objetivos no debe ser mayor a 100",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
		} else {
			servicioEvaluacionObjetivo.guardar(objetivoLista);
			List<EvaluacionObjetivo> evaluacionObje = servicioEvaluacionObjetivo
					.buscarObjetivosEvaluar(idEva);
			lbxObjetivosGuardados.getItems().clear();
			lbxObjetivosGuardados
					.setModel(new ListModelList<EvaluacionObjetivo>(
							evaluacionObje));
			gpxAgregar.setOpen(false);
			limpiar();
			idObjetivo = 0;

		}

	}

	private void EvaluacionIndicadorActualizar() {

		List<EvaluacionIndicador> listaEvaluacionIndicadores = servicioEvaluacionIndicador
				.buscarIndicadores(idObjetivo);

		Double sumaPeso = (double) 0;
		for (int j = 0; j < listaEvaluacionIndicadores.size(); j++) {

			Double peso = 0.0;

			if (listaEvaluacionIndicadores.get(j).getIdIndicador() != idIndicador) {
				peso = listaEvaluacionIndicadores.get(j).getPeso();
			}

			sumaPeso = sumaPeso + peso;
		}

		String UnidadCombo = cmbUnidad.getSelectedItem().getContext();
		UnidadMedida unidad = servicioUnidadMedida.buscarUnidad(Integer
				.parseInt(UnidadCombo));
		String medicionCombo = cmbMedicion.getSelectedItem().getContext();
		Medicion medicion = servicioMedicion.buscarMedicion(Integer
				.parseInt(medicionCombo));

		EvaluacionIndicador indicador = servicioEvaluacionIndicador
				.buscarIndicadorId(idIndicador);
		indicador.setDescripcionIndicador(txtIndicador.getValue());
		Integer peso = txtPeso1.getValue();
		indicador.setPeso(txtPeso1.getValue());
		indicador.setValorMeta(txtValorMeta.getValue());
		indicador.setResultadoFyAnterior(txtResFy.getValue());
		Double valorResultado = Double.valueOf(txtValorResultado.getValue());
		indicador.setValorResultado(valorResultado);
		indicador.setUnidadMedida(unidad);
		indicador.setMedicion(medicion);

		sumaPeso = sumaPeso + peso;

		if (sumaPeso > 100) {
			Messagebox
					.show("La suma de los pesos de los indicadores no debe ser mayor a 100",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
		} else {
			servicioEvaluacionIndicador.guardar(indicador);
			List<EvaluacionIndicador> evaluacionInd = servicioEvaluacionIndicador
					.buscarIndicadores(idObjetivo);
			lbxIndicadoresAgregados.getItems().clear();
			lbxIndicadoresAgregados
					.setModel(new ListModelList<EvaluacionIndicador>(
							evaluacionInd));
			gpxAgregarIndicador.setOpen(false);
		}

	}

	@Listen("onDoubleClick = #lbxCompetenciaEspecifica")
	public void mostrarDatosCatalogo() {

		String periodoActivo = "NO";

		if (lbxCompetenciaEspecifica.getItemCount() != 0) {

			Listitem listItem = lbxCompetenciaEspecifica.getSelectedItem();
			if (listItem != null) {
				if (((Combobox) ((listItem.getChildren().get(2)))
						.getFirstChild()).getValue() == "") {
					Messagebox
							.show("Debe Seleccionar un nivel de dominio para ver sus conductas",
									"Error", Messagebox.OK, Messagebox.ERROR);
				} else {
					String nivel = ((Combobox) ((listItem.getChildren().get(2)))
							.getFirstChild()).getSelectedItem().getContext();

					NivelCompetenciaCargo competencia = (NivelCompetenciaCargo) listItem
							.getValue();

					Evaluacion evaluacion = servicioEvaluacion
							.buscarEvaluacion(idEva);

					if (evaluacion.getRevision().getEstadoRevision()
							.compareTo("ACTIVO") == 0) {
						periodoActivo = "SI";
					}

					final HashMap<String, Object> map = new HashMap<String, Object>();
					String titulo = "Competencias Especificas";
					map.put("idEva", idEva);
					map.put("id", competencia.getCompetencia().getId());
					map.put("idnivel", nivel);
					map.put("titulo", titulo);
					map.put("periodoActivo", periodoActivo);

					evaluacionconductas = servicioEvaluacionConducta
							.buscarConductas(idEva);
					map.put("conductas", evaluacionconductas);
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);

					wdwConductasEspecificas = (Window) Executions
							.createComponents(
									"/vistas/transacciones/VEvaluacionConductas.zul",
									null, map);
					wdwConductasEspecificas.doModal();

				}

			}
		}
	}

	@Listen("onDoubleClick = #lbxCompetenciaRectora")
	public void mostrarDatosCatalogo1() {

		String periodoActivo = "NO";

		if (lbxCompetenciaRectora.getItemCount() != 0) {

			Listitem listItem = lbxCompetenciaRectora.getSelectedItem();
			if (listItem != null) {
				if (((Combobox) ((listItem.getChildren().get(2)))
						.getFirstChild()).getValue() == "") {
					Messagebox.show("Debe Seleccionar un nivel de dominio",
							"Error", Messagebox.OK, Messagebox.ERROR);
				} else {
					String nivel = ((Combobox) ((listItem.getChildren().get(2)))
							.getFirstChild()).getSelectedItem().getContext();

					NivelCompetenciaCargo competencia = (NivelCompetenciaCargo) listItem
							.getValue();

					Evaluacion evaluacion = servicioEvaluacion
							.buscarEvaluacion(idEva);

					if (evaluacion.getRevision().getEstadoRevision()
							.compareTo("ACTIVO") == 0) {
						periodoActivo = "SI";
					}

					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("idEva", idEva);
					map.put("id", competencia.getCompetencia().getId());
					map.put("idnivel", nivel);
					map.put("periodoActivo", periodoActivo);
					String titulo = "Competencias Rectoras";
					map.put("titulo", titulo);
					evaluacionconductas = servicioEvaluacionConducta
							.buscarConductas(idEva);
					map.put("conductas", evaluacionconductas);
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);
					wdwConductasRectoras = (Window) Executions
							.createComponents(
									"/vistas/transacciones/VEvaluacionConductas.zul",
									null, map);
					wdwConductasRectoras.doModal();
				}
			}

		}

	}

	@Listen("onClick = #btnCambiarEstado")
	public void eventoGuardarEvaluacion() {

		guardarEvaluacion(true);

	}

	private void guardarEvaluacion(boolean mostrarMensaje) {
		guardarCompetenciasEspecificas();
		guardarCompetenciasRectoras();
		refrescarCalculosEvaluacion();

		try {

			String fortalezas = txtFortalezas.getValue();
			String oportunidades = txtOportunidades.getValue();
			String resumen = txtResumen.getValue();
			String compromisos = txtCompromisos.getValue();
			String comentario = txtComentarios.getValue();
			Integer idEvaluacion = Integer.parseInt(lblEvaluacion.getValue());
			Evaluacion evaluacionEmpleado = new Evaluacion();
			evaluacionEmpleado = servicioEvaluacion.buscarIdEvaluacion(
					idEvaluacion, fichaE);
			evaluacionEmpleado.setCompromisos(compromisos);
			evaluacionEmpleado.setFortalezas(fortalezas);
			evaluacionEmpleado.setOportunidades(oportunidades);
			evaluacionEmpleado.setResumen(resumen);
			evaluacionEmpleado.setComentario(comentario);
			evaluacionEmpleado.setResultadoObjetivos(Integer
					.parseInt(lblResultadoPeso.getValue()));
			evaluacionEmpleado.setResultadoCompetencias(Integer
					.parseInt(lblResultadoPeso1.getValue()));
			evaluacionEmpleado.setResultadoGeneral(Integer
					.parseInt(txtResultadoFinal.getValue()));
			evaluacionEmpleado.setResultadoFinal(Integer
					.parseInt(txtResultadoFinal.getValue()));
			evaluacionEmpleado.setValoracion(servicioUtilidad
					.obtenerValoracionFinalSimple(Integer
							.parseInt(txtResultadoFinal.getValue())));
			evaluacionEmpleado.setPeso(Double.parseDouble(txttotalPesoObjetivos
					.getValue()));
			evaluacionEmpleado.setResultado(Double
					.parseDouble(txttotalObjetivos.getValue()));

			servicioEvaluacion.guardar(evaluacionEmpleado);

			if (mostrarMensaje) {
				Messagebox.show("La Evaluacion ha sido guardada Exitosamente",
						"Información", Messagebox.OK, Messagebox.INFORMATION);
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

	}

	private void eliminarIndicador() {
		if (lbxIndicadoresAgregados.getItemCount() != 0) {
			Listitem listItem = lbxIndicadoresAgregados.getSelectedItem();
			if (listItem != null) {
				EvaluacionIndicador evaluacionIndicador = (EvaluacionIndicador) listItem
						.getValue();
				idIndicadorE = evaluacionIndicador.getIdIndicador();
				idObjetivoE = evaluacionIndicador.getIdObjetivo();
				Messagebox.show("Desea Eliminar el Indicador", "Alerta",
						Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener<Event>() {

							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onOK")) {
									servicioEvaluacionIndicador
											.eliminarUno(idIndicadorE);
									msj.mensajeInformacion(Mensaje.eliminado);
									lbxIndicadoresAgregados.getItems().clear();
									indicadores = servicioEvaluacionIndicador
											.buscarIndicadores(idObjetivoE);
									lbxIndicadoresAgregados
											.setModel(new ListModelList<EvaluacionIndicador>(
													indicadores));

								}
								refrescarCalculosEvaluacion();

							}
						});
			} else
				msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);
		}

	}

	public void eliminarObjetivo() {
		if (lbxObjetivosGuardados.getItemCount() != 0) {
			Listitem listItem = lbxObjetivosGuardados.getSelectedItem();
			if (listItem != null) {
				EvaluacionObjetivo evaluacionObjetivo = (EvaluacionObjetivo) listItem
						.getValue();
				evaluacionObjetivo = listItem.getValue();

				idObjetivoE = evaluacionObjetivo.getIdObjetivo();
				System.out.println("idObjetivoE" + idObjetivoE);
				List<EvaluacionIndicador> evaluacionIndicador = servicioEvaluacionIndicador
						.buscarIndicadores(idObjetivoE);
				if (evaluacionIndicador.size() != 0) {
					Messagebox
							.show("El objetivo tiene indicadores asocioados desea eliminarlo",
									"Alerta",
									Messagebox.OK | Messagebox.CANCEL,
									Messagebox.QUESTION,
									new org.zkoss.zk.ui.event.EventListener<Event>() {
										public void onEvent(Event evt)
												throws InterruptedException {
											if (evt.getName().equals("onOK")) {
												List<EvaluacionIndicador> evaluacionIndicador = servicioEvaluacionIndicador
														.buscarIndicadores(idObjetivoE);
												servicioEvaluacionIndicador
														.eliminarVarios(evaluacionIndicador);
												servicioEvaluacionObjetivo
														.eliminarUno(idObjetivoE);
												msj.mensajeInformacion(Mensaje.eliminado);
												lbxObjetivosGuardados
														.getItems().clear();
												objetivosG = servicioEvaluacionObjetivo
														.buscarObjetivosEvaluar(idEva);
												lbxObjetivosGuardados
														.setModel(new ListModelList<EvaluacionObjetivo>(
																objetivosG));

												refrescarCalculosEvaluacion();
											}

										}
									});
				} else {

					idObjetivoE = evaluacionObjetivo.getIdObjetivo();
					Messagebox.show("Desea Eliminar el Objetivo", "Alerta",
							Messagebox.OK | Messagebox.CANCEL,
							Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener<Event>() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onOK")) {

										servicioEvaluacionObjetivo
												.eliminarUno(idObjetivoE);
										msj.mensajeInformacion(Mensaje.eliminado);
										lbxObjetivosGuardados.getItems()
												.clear();
										objetivosG = servicioEvaluacionObjetivo
												.buscarObjetivosEvaluar(idEva);
										lbxObjetivosGuardados
												.setModel(new ListModelList<EvaluacionObjetivo>(
														objetivosG));

										txttotalObjetivos.setValue(String
												.valueOf(calcularTotalResultadoObjetivos()));
										txttotalPesoObjetivos.setValue(String
												.valueOf(calcularTotalPesoObjetivos()));

										refrescarCalculosEvaluacion();

									}

								}
							});

				}
			} else
				msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);

		}

	}

	public void validar1() {
		System.out
				.println("enttroooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
		Evaluacion evaluacion = servicioEvaluacion.buscarEvaluacion(idEva);
		String ficha = evaluacion.getFicha();
		Integer numeroEvaluacion = servicioEvaluacion.buscar(ficha).size();
		List<EvaluacionObjetivo> evaluacionObjetivoIndicadores = servicioEvaluacionObjetivo
				.buscarObjetivosEvaluar(idEva);
		List<EvaluacionIndicador> evaluacionObjetivoIndicador = new ArrayList<EvaluacionIndicador>();
		if (evaluacionObjetivoIndicadores != null) {
			for (int i = 0; i < evaluacionObjetivoIndicadores.size(); i++) {
				int idObjetivo = evaluacionObjetivoIndicadores.get(i)
						.getIdObjetivo();
				evaluacionObjetivoIndicador = servicioEvaluacionIndicador
						.buscarIndicadores(idObjetivo);
				Double sumaPeso = 0.0;
				for (int j = 0; j < indicadores.size(); j++) {
					System.out.println("ojoooooooooo");
					Double peso = indicadores.get(j).getPeso();
					System.out.println("pesooo" + peso);
					sumaPeso = peso + sumaPeso;
					System.out.println("sumapeso" + sumaPeso);
				}

				if (sumaPeso > 100) {
					bool = true;
					i = 1000000000;
				}
			}

		}

	}

	public Double obtenerPesoIndicadorObjetivo(Integer idObjetivo,
			Double pesoAgregar) {
		Double sumaPeso = 0.0;

		List<EvaluacionIndicador> evaluacionObjetivoIndicador = new ArrayList<EvaluacionIndicador>();
		evaluacionObjetivoIndicador = servicioEvaluacionIndicador
				.buscarIndicadores(idObjetivo);
		for (int j = 0; j < evaluacionObjetivoIndicador.size(); j++) {
			Double peso = evaluacionObjetivoIndicador.get(j).getPeso();
			sumaPeso = peso + sumaPeso;
		}

		return sumaPeso + pesoAgregar;
	}

	public boolean validarAgregarObjetivo() {
		boolean valor = true;

		Evaluacion evaluacion = servicioEvaluacion.buscarEvaluacion(idEva);
		Double sumaPeso = (double) 0;
		for (int j = 0; j < objetivosG.size(); j++) {
			Double peso = objetivosG.get(j).getPeso();
			sumaPeso = sumaPeso + peso;
			System.out.println("SUMA" + sumaPeso);
		}
		if (sumaPeso > 100) {
			Messagebox
					.show("La suma de los pesos de los objetivos no debe ser mayor a 100",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
			objetivosG.remove(ev);
		} else {

			servicioEvaluacionObjetivo.guardar(ev);
			Messagebox.show("Guardado Exitosamente", "Información",
					Messagebox.OK, Messagebox.INFORMATION);
			gpxAgregar.setOpen(false);
			limpiar();

		}

		return valor;
	}

	public boolean validarAgregarIndicador(Integer idObjetivo,
			Double pesoAgregar) {
		boolean valor = true;

		if (obtenerPesoIndicadorObjetivo(idObjetivo, pesoAgregar) > 100) {
			Messagebox
					.show("La suma de los pesos de los indicadores no debe ser mayor a 100",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
			indicadores.remove(evi);

		} else {
			if (evi != null) {
				servicioEvaluacionIndicador.guardar(evi);
				gpxAgregarIndicador.setOpen(false);
				limpiarI();
				Messagebox.show("Guardado Exitosamente", "Información",
						Messagebox.OK, Messagebox.INFORMATION);
			} else {
				gpxAgregarIndicador.setOpen(false);
			}

		}

		return valor;
	}

	public void cambiarEstado1() {
		System.out.println("METODO");
		Evaluacion evaluacion = servicioEvaluacion.buscarEvaluacion(idEva);
		validar1();
		Double sumaPeso = (double) 0;
		for (int j = 0; j < objetivosG.size(); j++) {
			Double peso = objetivosG.get(j).getPeso();
			sumaPeso = sumaPeso + peso;
			System.out.println("SUMA" + sumaPeso);
		}
		if (sumaPeso > 100) {
			Messagebox
					.show("La suma de los pesos de los objetivos no debe ser mayor a 100",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
			objetivosG.remove(ev);

		}

		else if (bool == true) {
			Messagebox
					.show("La suma de los pesos de los indicadores no debe ser mayor a 100",
							"Información", Messagebox.OK,
							Messagebox.INFORMATION);
			indicadores.remove(evi);

		} else {
			if (evi != null) {
				servicioEvaluacionIndicador.guardar(evi);
				gpxAgregarIndicador.setOpen(false);
				limpiarI();
				Messagebox.show("Guardado Exitosamente", "Información",
						Messagebox.OK, Messagebox.INFORMATION);
			} else {
				System.out.println("ojooooooooooooooooo");
				servicioEvaluacionObjetivo.guardar(ev);
				Messagebox.show("Guardado Exitosamente", "Información",
						Messagebox.OK, Messagebox.INFORMATION);
				gpxAgregar.setOpen(false);
				limpiar();

			}
		}
		bool = false;
	}

	@Listen("onClick = #btnGuardarObjetivo")
	public void guardarObjetivos() {

		try {

			Double acumulador = 0.0;

			boolean campoBlanco = false;

			for (int i = 0; i < lbxObjetivosGuardados.getItems().size(); i++) {
				List<Listitem> listItem2 = lbxObjetivosGuardados.getItems();

				Listitem listItem = lbxObjetivosGuardados.getItemAtIndex(i);
				if (((Textbox) ((listItem.getChildren().get(5)))
						.getFirstChild()).getValue().equals("")
						&& ((Textbox) ((listItem.getChildren().get(6)))
								.getFirstChild()).getValue().equals("")) {
					campoBlanco = true;
				}
			}

			if (campoBlanco == true) {
				Messagebox.show("Debe ingresar la Evaluacion de los Objetivos",
						"Error", Messagebox.OK, Messagebox.ERROR);

			} else {

				List<EvaluacionObjetivo> evaluacionesO = new ArrayList<EvaluacionObjetivo>();

				for (int i = 0; i < lbxObjetivosGuardados.getItems().size(); i++) {
					List<Listitem> listItem2 = lbxObjetivosGuardados.getItems();
					EvaluacionObjetivo EvaluacionO = listItem2.get(i)
							.getValue();

					Integer idObjetivo = EvaluacionO.getIdObjetivo();
					Listitem listItem = lbxObjetivosGuardados.getItemAtIndex(i);
					EvaluacionObjetivo objetivos = listItem2.get(i).getValue();
					String totalIndicador = ((Textbox) ((listItem.getChildren()
							.get(5))).getFirstChild()).getValue();

					String resultado = ((Textbox) ((listItem.getChildren()
							.get(5))).getFirstChild()).getValue();

					EvaluacionObjetivo evaluacion = servicioEvaluacionObjetivo
							.buscarObjetivosId(idObjetivo);

					evaluacion.setTotalInd(0);
					evaluacion.setResultado(0);
					servicioEvaluacionObjetivo.guardar(evaluacion);

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

	}

	@Listen("onClick = #btnImprimir")
	public void imprimirEvaluacion() {

		try {

			Evaluacion evaluacion = servicioEvaluacion.buscarEvaluacion(idEva);

			if (evaluacion.getRevision().getEstadoRevision()
					.compareTo("ACTIVO") == 0) {
				guardarEvaluacion(false);
			}

			Executions.getCurrent().sendRedirect(
					"http://www.dusanet.com:8029/evaluacion/Impresion?par1="
							+ idEva + "&par2=" + evaluador.getNombre() + "",
					"_blank");

			/*
			 * Executions.getCurrent().sendRedirect(
			 * "http://localhost:8080/Bizapps-evaluacion/Impresion?par1=" +
			 * idEva + "&par2="+ evaluador.getNombre() +"", "_blank");
			 */

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

	}

	private double calcularTotalPesoObjetivos() {
		Double acumulador = 0.0;

		for (int i = 0; i < objetivosG.size(); i++) {
			EvaluacionObjetivo EvaluacionO = objetivosG.get(i);
			acumulador = acumulador + EvaluacionO.getPeso();
		}
		return acumulador;
	}

	private double calcularTotalPesoIndicadores(Integer idObjetivo) {
		Double acumulador = 0.0;

		try {

			List<EvaluacionIndicador> lista = servicioEvaluacionIndicador
					.buscarIndicadores(idObjetivo);

			for (int k = 0; k < lista.size(); k++) {
				EvaluacionIndicador evaluacionIndicador = lista.get(k);
				acumulador = acumulador
						+ evaluacionIndicador.getResultadoPeso();
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		return acumulador;
	}

	private double calcularTotalResultadoObjetivos() {
		Double acumulador = 0.0;

		for (int i = 0; i < objetivosG.size(); i++) {
			EvaluacionObjetivo EvaluacionO = objetivosG.get(i);
			acumulador = acumulador + EvaluacionO.getResultado();
		}

		return acumulador;
	}
	
	
	private double calcularTotalResultadoObjetivos(Evaluacion evaluacionAuxiliar) {
		Double acumulador = 0.0;
		
		List<EvaluacionObjetivo>objetivos = servicioEvaluacionObjetivo.buscarObjetivosEvaluar(evaluacionAuxiliar.getIdEvaluacion());

		for (int i = 0; i < objetivos.size(); i++) {
			EvaluacionObjetivo EvaluacionO = objetivos.get(i);
			acumulador = acumulador + EvaluacionO.getResultado();
		}

		return acumulador;
	}
	

	@Listen("onClick = #btnCalcular")
	public void prueba() {
		// evaluarIndicadores();
	}

	@Listen("onClick = #btnGuardarIndicador")
	public void guardarIndicadores() {

		boolean campoBlanco = false;

		for (int i = 0; i < lbxIndicadoresAgregados.getItems().size(); i++) {
			List<Listitem> listItem2 = lbxIndicadoresAgregados.getItems();

			Listitem listItem = lbxIndicadoresAgregados.getItemAtIndex(i);
			if (((Doublespinner) ((listItem.getChildren().get(6)))
					.getFirstChild()).getValue().equals("")
					&& ((Doublespinner) ((listItem.getChildren().get(7)))
							.getFirstChild()).getValue().equals("")
					&& ((Textbox) ((listItem.getChildren().get(8)))
							.getFirstChild()).getValue().equals("")
					&& ((Textbox) ((listItem.getChildren().get(9)))
							.getFirstChild()).getValue().equals("")) {
				campoBlanco = true;
			}
		}

		if (campoBlanco == true) {
			Messagebox.show("Debe llenar todos los campos de los Indicadores",
					"Error", Messagebox.OK, Messagebox.ERROR);

		} else {
			List<EvaluacionIndicador> evaluacionesI = new ArrayList<EvaluacionIndicador>();

			for (int i = 0; i < lbxIndicadoresAgregados.getItems().size(); i++) {
				List<Listitem> listItem2 = lbxIndicadoresAgregados.getItems();
				EvaluacionIndicador EvaluacionI = listItem2.get(i).getValue();
				Integer idIndicador = EvaluacionI.getIdIndicador();
				Listitem listItem = lbxIndicadoresAgregados.getItemAtIndex(i);
				EvaluacionIndicador indicadores = listItem2.get(i).getValue();
				Double valorResultado = ((Doublespinner) ((listItem
						.getChildren().get(6))).getFirstChild()).getValue();
				Double resultadoFyAnterior = ((Doublespinner) ((listItem
						.getChildren().get(7))).getFirstChild()).getValue();
				String resultado = ((Textbox) ((listItem.getChildren().get(8)))
						.getFirstChild()).getValue();
				String resultadoPeso = ((Textbox) ((listItem.getChildren()
						.get(9))).getFirstChild()).getValue();

				EvaluacionIndicador indicador = servicioEvaluacionIndicador
						.buscarIndicadorId(idIndicador);

				indicador.setValorResultado(valorResultado);
				indicador.setResultadoFyAnterior(resultadoFyAnterior);
				indicador.setResultadoPorc(Double.parseDouble(resultado));
				indicador.setResultadoPeso(Double.parseDouble(resultadoPeso));
				indicador.setTotal(total);
				servicioEvaluacionIndicador.guardar(indicador);

			}

			// guardarObjetivos();
			// guardarEvaluacion();

		}

	}

	public void refrescarCalculosEvaluacion() {
		evaluarIndicadores();
		evaluarObjetivos();

		lbxObjetivosGuardados.getItems().clear();
		objetivosG = servicioEvaluacionObjetivo.buscarObjetivosEvaluar(idEva);
		lbxObjetivosGuardados.setModel(new ListModelList<EvaluacionObjetivo>(
				objetivosG));

		txttotalObjetivos.setValue(String
				.valueOf(calcularTotalResultadoObjetivos()));
		txttotalPesoObjetivos.setValue(String
				.valueOf(calcularTotalPesoObjetivos()));
		Evaluacion evalua = servicioEvaluacion.buscarEvaluacion(idEva);
		calcularResultadoFinal(evalua);
	}

	public void evaluarObjetivos() {

		try {

			List<EvaluacionObjetivo> evaluacionObjetivo = servicioEvaluacionObjetivo
					.buscarObjetivos(fichaE, numero);

			for (int j = 0; j < evaluacionObjetivo.size(); j++) {

				EvaluacionObjetivo EvaluacionO = evaluacionObjetivo.get(j);
				Double peso = EvaluacionO.getPeso();

				List<EvaluacionIndicador> lista = servicioEvaluacionIndicador
						.buscarIndicadores(EvaluacionO.getIdObjetivo());

				Double acumulador = 0.0;

				for (int k = 0; k < lista.size(); k++) {

					EvaluacionIndicador evaluacionIndicador = lista.get(k);
					acumulador = acumulador
							+ evaluacionIndicador.getResultadoPeso();

				}

				Double resultado = (acumulador * peso) / 100;
				EvaluacionO.setTotalInd(acumulador);
				EvaluacionO.setResultado(resultado);
				servicioEvaluacionObjetivo.guardar(EvaluacionO);

			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

	}

	public void evaluarIndicadores() {

		try {

			Integer resultado1 = 0;
			lbxIndicadoresAgregados.renderAll();
			for (int i = 0; i < lbxIndicadoresAgregados.getItems().size(); i++) {
				List<Listitem> listItem2 = lbxIndicadoresAgregados.getItems();
				EvaluacionIndicador EvaluacionI = listItem2.get(i).getValue();
				Listitem listItem = lbxIndicadoresAgregados.getItemAtIndex(i);
				Double valorResultado = ((Doublespinner) ((listItem
						.getChildren().get(6))).getFirstChild()).getValue();
				Double resultadoFyAnterior = ((Doublespinner) ((listItem
						.getChildren().get(7))).getFirstChild()).getValue();
				String resultado = ((Textbox) ((listItem.getChildren().get(8)))
						.getFirstChild()).getValue();
				String resultadoPeso = ((Textbox) ((listItem.getChildren()
						.get(9))).getFirstChild()).getValue();
				if (EvaluacionI.getMedicion().getDescripcionMedicion()
						.equals("CONTINUA")) {
					if ((valorResultado == 0) && (resultadoFyAnterior == 0)) {
						resultado = "0";
					} else {
						if ((valorResultado) < EvaluacionI.getValorMeta()) {
							if ((valorResultado) >= (resultadoFyAnterior)) {
								resultado = "85";
							} else {
								resultado = "0";
							}
						} else {
							if ((valorResultado) > EvaluacionI.getValorMeta()) {
								resultado = "115";
							} else {
								if ((valorResultado) == EvaluacionI
										.getValorMeta()) {
									resultado = "100";
								}
							}
						}

					}

				} else {
					((Doublespinner) ((listItem.getChildren().get(7)))
							.getFirstChild()).setDisabled(true);
					valor = ((valorResultado / EvaluacionI.getValorMeta()) * 100);
					System.out.println("valor" + valor);
					Integer valor1 = valor.intValue();
					System.out.println("valor1" + valor1);
					resultado = calcularPorcentajeMetaIndicado(valor1)
							.toString();
					resultado1 = calcularPorcentajeMetaIndicado(valor1);
					System.out.println("resultado" + resultado);
				}

				resultadoPeso = String.valueOf((Double.parseDouble(resultado)
						* (EvaluacionI.getPeso()) / 100));
				double resul = ((Double.parseDouble(resultado)
						* (EvaluacionI.getPeso()) / 100));
				((Textbox) ((listItem.getChildren().get(8))).getFirstChild())
						.setValue((resultado));
				((Textbox) ((listItem.getChildren().get(9))).getFirstChild())
						.setValue((resultadoPeso));
				total = total + Double.parseDouble(resultadoPeso);

				for (int j = 0; j < lbxObjetivosGuardados.getItems().size(); j++) {
					List<Listitem> listItem3 = lbxObjetivosGuardados.getItems();
					EvaluacionObjetivo EvaluacionO = listItem3.get(j)
							.getValue();
					Double peso = EvaluacionO.getPeso();
					Integer idObjetivo = EvaluacionO.getIdObjetivo();
					Integer idObjetivo1 = EvaluacionI.getIdObjetivo();
					if (idObjetivo.equals(idObjetivo1)) {
						totalInd = 0.0;
						totalInd = (total * peso) / 100;
						((Textbox) ((listItem3.get(j).getChildren().get(5)))
								.getFirstChild()).setValue(total.toString());
					}
				}

			}

			guardarIndicadores();
			totalObjetivo = 0.0;
			tind = total;
			totalInd = 0.0;
			total = 0.0;

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

	}

	private double calcularTotalObjetivo() {
		Double acumulador = 0.0;

		for (int j = 0; j < lbxObjetivosGuardados.getItems().size(); j++) {
			List<Listitem> listItem3 = lbxObjetivosGuardados.getItems();
			EvaluacionObjetivo EvaluacionO = listItem3.get(j).getValue();

			Double resultado = EvaluacionO.getResultado();
		}

		return acumulador;
	}

	public Integer calcularPorcentajeMetaIndicado(Integer valor) {
		if (valor < 0)
			valor = 0;
		if (valor >= 0 && valor <= 24)
			valor = 0;
		if (valor >= 25 && valor <= 49)
			valor = 25;
		if (valor >= 50 && valor <= 84)
			valor = 50;
		if (valor >= 85 && valor <= 94)
			valor = 85;
		if (valor >= 95 && valor <= 100)
			valor = 100;
		if (valor >= 101)
			valor = 115;
		System.out.println(valor);
		return valor;
	}

	/*
	 * public void guardarEvaluacion() { System.out.println("entrooo");
	 * Evaluacion evalua = servicioEvaluacion.buscarEvaluacion(idEva);
	 * System.out.println(evalua); System.out.println(idEva);
	 * evalua.setFechaRevision(fechaHora);
	 * evalua.setFichaEvaluador(u.getCedula());
	 * evalua.setIdUsuario(u.getIdUsuario());
	 * evalua.setResultadoObjetivos(totalObjetivo.intValue());
	 * servicioEvaluacion.guardar(evalua); }
	 */

	@Listen("onClick = #btnCancelar,  #btnCancelarIndicador, #btnSalirCompetenciaR, #btnSalirCompetenciaE, #btnCancelarEvaluacion")
	public void salir1() {
		winEvaluacionEmpleado.onClose();
	}

	private void mostrarDominioRectora() {
		lbxCompetenciaRectora.renderAll();
		for (int i = 0; i < lbxCompetenciaRectora.getItemCount(); i++) {
			Listitem listItem2 = lbxCompetenciaRectora.getItemAtIndex(i);
			NivelCompetenciaCargo nivel = (NivelCompetenciaCargo) listItem2
					.getValue();
			List<Listitem> listItem5 = lbxCompetenciaRectora.getItems();
			Integer idCompetencia = nivel.getCompetencia().getId();
			Evaluacion eva = servicioEvaluacion.buscarEvaluacion(idEva);
			Competencia competencia = servicioCompetencia
					.buscarCompetencia(idCompetencia);
			EvaluacionCompetencia evaluacion = servicioEvaluacionCompetencia
					.buscar(eva, competencia);
			System.out.println(evaluacion);
			if (evaluacion != null) {
				Integer dominio = evaluacion.getIdDominio();
				Dominio dom = servicioDominio.buscarDominio(dominio);
				String descripcionDominio = dom.getDescripcionDominio();
				((Combobox) ((listItem5.get(i).getChildren().get(2)))
						.getFirstChild()).setValue(descripcionDominio);
			}

		}

	}

	private void mostrarDominioEspecifica() {
		lbxCompetenciaEspecifica.renderAll();
		for (int i = 0; i < lbxCompetenciaEspecifica.getItemCount(); i++) {
			Listitem listItem2 = lbxCompetenciaEspecifica.getItemAtIndex(i);
			NivelCompetenciaCargo nivel = (NivelCompetenciaCargo) listItem2
					.getValue();
			List<Listitem> listItem5 = lbxCompetenciaEspecifica.getItems();
			Integer idCompetencia = nivel.getCompetencia().getId();
			Evaluacion eva = servicioEvaluacion.buscarEvaluacion(idEva);
			Competencia competencia = servicioCompetencia
					.buscarCompetencia(idCompetencia);
			EvaluacionCompetencia evaluacion = servicioEvaluacionCompetencia
					.buscar(eva, competencia);
			if (evaluacion != null) {
				Integer dominio = evaluacion.getIdDominio();
				Dominio dom = servicioDominio.buscarDominio(dominio);
				String descripcionDominio = dom.getDescripcionDominio();
				((Combobox) ((listItem5.get(i).getChildren().get(2)))
						.getFirstChild()).setValue(descripcionDominio);
				System.out.println(descripcionDominio);
			}

		}

	}

	private void calcularResultadoFinalVieja(Evaluacion evaluacionAux) {

		double ndr = 0;
		double nde = 0;
		Competencia competenciaEvidenciada;
		Dominio dominioEvidenciado;

		try {

			List<NivelCompetenciaCargo> listaNivelCompetenciaCargo = new ArrayList<NivelCompetenciaCargo>();
			listaNivelCompetenciaCargo = servicioNivelCompetenciaCargo
					.buscar(evaluacionAux.getCargo());

			for (int i = 0; i < listaNivelCompetenciaCargo.size(); i++) {

				Competencia competenciaRequerida = servicioCompetencia
						.buscarCompetencia(listaNivelCompetenciaCargo.get(i)
								.getCompetencia().getId());

				try {

					competenciaEvidenciada = servicioCompetencia
							.buscarCompetencia(servicioEvaluacionCompetencia
									.buscar(evaluacionAux, competenciaRequerida)
									.getCompetencia().getId());

					dominioEvidenciado = servicioDominio
							.buscarDominio((servicioEvaluacionCompetencia
									.buscar(evaluacionAux, competenciaRequerida)
									.getIdDominio()));

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.toString());

					competenciaEvidenciada = null;
					dominioEvidenciado = null;
				}

				Dominio dominioRequerido = servicioDominio
						.buscarDominio(listaNivelCompetenciaCargo.get(i)
								.getDominio().getIdDominioRelacionado());

				if (dominioRequerido != null) {

					List<EvaluacionConducta> listaConductasEvidenciadas = new ArrayList<EvaluacionConducta>();

					if (competenciaEvidenciada != null) {
						listaConductasEvidenciadas = servicioEvaluacionConducta
								.buscar(evaluacionAux, competenciaEvidenciada);
					}

					double pesoConducta = 0;
					try {
						pesoConducta = 100 / listaConductasEvidenciadas.size();
					} catch (Exception e) {
						// TODO: handle exception
						pesoConducta = 0;
					}

					double acumuladorPesoConducta = 0;

					for (int j = 0; j < listaConductasEvidenciadas.size(); j++) {
						EvaluacionConducta evaluacionConducta = listaConductasEvidenciadas
								.get(j);
						if (evaluacionConducta != null) {
							if (evaluacionConducta.getValor()) {
								acumuladorPesoConducta = acumuladorPesoConducta
										+ pesoConducta;
							}
						}

					}

					if (dominioEvidenciado != null) {
						nde = nde
								+ (dominioEvidenciado.getPeso()
										* acumuladorPesoConducta / 100);
					}

					ndr = ndr + dominioRequerido.getPeso();

				}

			}

			// -------------------------------------------------------------------------------------------

			resultadoCompetencia = (nde / ndr) * 100;

		} catch (Exception e) {
			System.out.println(e.toString());
			resultadoCompetencia = 0.0;
		}

		try {

			DecimalFormat f = new DecimalFormat("##.0");

			int id = 1;
			int id2 = 2;
			Distribucion dis = servicioDistribucion.buscarDistribucion(id);
			Distribucion dist = servicioDistribucion.buscarDistribucion(id2);
			int distO = dis.getPorcentaje();
			int distC = dist.getPorcentaje();
			String dO = String.valueOf(distO);
			String dC = String.valueOf(distC);

			String r = f.format(resultadoCompetencia);

			txttotalCompetencia1.setValue(r);
			txttotalCompetencia2.setValue(r);

			String o = String.valueOf(calcularTotalResultadoObjetivos());
			String ro = f.format(o);
			Double resul1 = Double.parseDouble(ro);
			
			lblResultado.setValue(ro);
			lblResultado1.setValue(r);
			lblDistribucion.setValue(dO);
			lblDistribucion1.setValue(dC);
			resultadoPesoCompetencia =  ((distC * resultadoCompetencia) / 100);
			resultadoPesoObjetivo =  ((distO * resul1) / 100);
			lblResultadoPeso.setValue(resultadoPesoObjetivo.toString());
			lblResultadoPeso1.setValue(resultadoPesoCompetencia.toString());
			double resultadoFinal = resultadoPesoObjetivo
					+ resultadoPesoCompetencia;
			String rf = String.valueOf(resultadoFinal);
			txtResultadoFinal.setValue(rf);
			/*
			 * txtValoracionFinal.setValue(servicioUtilidad
			 * .obtenerValoracionFinal((int) resultadoFinal));
			 */

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}

	private void calcularResultadoFinal(Evaluacion evaluacionAux) {

		double acumuladorPesoCompetencia = 0.0;
		double ndr = 0;
		double nde = 0;
		Competencia competenciaEvidenciada;
		Dominio dominioEvidenciado;

		try {

			List<NivelCompetenciaCargo> listaNivelCompetenciaCargo = new ArrayList<NivelCompetenciaCargo>();
			listaNivelCompetenciaCargo = servicioNivelCompetenciaCargo
					.buscar(evaluacionAux.getCargo());

			Integer contadorCompetencias = 0;

			for (int i = 0; i < listaNivelCompetenciaCargo.size(); i++) {

				try {

					Dominio dominioRequerido = servicioDominio
							.buscarDominio(listaNivelCompetenciaCargo.get(i)
									.getDominio().getIdDominioRelacionado());

					if (dominioRequerido.getDescripcionDominio().compareTo(
							"No Aplica") == 0) {

					} else {
						contadorCompetencias++;
					}

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.toString());

				}

			}
			
			
			List<EvaluacionCompetencia> listaCompetenciasEvidenciadas= servicioEvaluacionCompetencia.buscar(evaluacionAux);
			Integer numeroCompetenciasEvidenciadas = 0;
			
			for (EvaluacionCompetencia evaluacionCompetencia : listaCompetenciasEvidenciadas) {
				
				if (evaluacionCompetencia.getIdDominio()!=1)
				{
					numeroCompetenciasEvidenciadas++;
				}
				
			}
			
			if (numeroCompetenciasEvidenciadas>contadorCompetencias)
			{
				contadorCompetencias= numeroCompetenciasEvidenciadas;
			}
			

			double pesoCompetencia = 0.0;
			try {
				pesoCompetencia = (double) 100 / contadorCompetencias;
			} catch (Exception e) {
				// TODO: handle exception
				pesoCompetencia = 0;
			}

			acumuladorPesoCompetencia = 0.0;

			for (int i = 0; i < listaNivelCompetenciaCargo.size(); i++) {

				Competencia competenciaRequerida = servicioCompetencia
						.buscarCompetencia(listaNivelCompetenciaCargo.get(i)
								.getCompetencia().getId());

				try {

					competenciaEvidenciada = servicioCompetencia
							.buscarCompetencia(servicioEvaluacionCompetencia
									.buscar(evaluacionAux, competenciaRequerida)
									.getCompetencia().getId());

					dominioEvidenciado = servicioDominio
							.buscarDominio((servicioEvaluacionCompetencia
									.buscar(evaluacionAux, competenciaRequerida)
									.getIdDominio()));

				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.toString());

					competenciaEvidenciada = null;
					dominioEvidenciado = null;
				}

				Dominio dominioRequerido = servicioDominio
						.buscarDominio(listaNivelCompetenciaCargo.get(i)
								.getDominio().getIdDominioRelacionado());

				Integer pesoDominioRequerido = 0;
				if (dominioRequerido != null) {

					switch (dominioRequerido.getDescripcionDominio()) {
					case "Basico":
						pesoDominioRequerido = 1;
						break;
					case "Intermedio":
						pesoDominioRequerido = 2;
						break;
					case "Avanzado":
						pesoDominioRequerido = 3;
						break;
					case "Especializado":
						pesoDominioRequerido = 4;
						break;
					default:
						break;
					}

				}

				Integer pesoDominioEvidenciado = 0;

				if (dominioEvidenciado != null) {

					switch (dominioEvidenciado.getDescripcionDominio()) {
					case "Basico":
						pesoDominioEvidenciado = 1;
						break;
					case "Intermedio":
						pesoDominioEvidenciado = 2;
						break;
					case "Avanzado":
						pesoDominioEvidenciado = 3;
						break;
					case "Especializado":
						pesoDominioEvidenciado = 4;
						break;
					default:
						break;
					}

				}

				List<EvaluacionConducta> listaConductasEvidenciadas = new ArrayList<EvaluacionConducta>();

				if (competenciaEvidenciada != null) {
					listaConductasEvidenciadas = servicioEvaluacionConducta
							.buscar(evaluacionAux, competenciaEvidenciada);
				}

				double acumuladorPesoConducta = 0;
				Integer contadorConductas = 0;
				for (int j = 0; j < listaConductasEvidenciadas.size(); j++) {
					EvaluacionConducta evaluacionConducta = listaConductasEvidenciadas
							.get(j);
					contadorConductas++;

					if (evaluacionConducta != null) {
						if (evaluacionConducta.getValor()) {
							acumuladorPesoConducta = acumuladorPesoConducta
									+ pesoDominioEvidenciado;
						}
					}

				}

				Integer pesoTotalDominioRequerido = 0;

				try {
					pesoTotalDominioRequerido = pesoDominioRequerido
							* contadorConductas;

				} catch (Exception ex) {
					pesoTotalDominioRequerido = 0;

				}

				Integer pesoTotalDominioEvidenciado = 0;
				if (pesoTotalDominioRequerido == 0) {

					try {

						pesoTotalDominioEvidenciado = pesoDominioEvidenciado
								* contadorConductas;
					} catch (Exception ex) {

						pesoTotalDominioEvidenciado = 0;
					}
				}

				if (dominioEvidenciado != null) {

					if (pesoTotalDominioRequerido != 0) {
						acumuladorPesoCompetencia = acumuladorPesoCompetencia
								+ ((acumuladorPesoConducta / pesoTotalDominioRequerido) * pesoCompetencia);
					} else {
						if (pesoTotalDominioEvidenciado != 0) {
							acumuladorPesoCompetencia = acumuladorPesoCompetencia
									+ ((acumuladorPesoConducta / pesoTotalDominioEvidenciado) * pesoCompetencia);
						}

					}
					
					Empleado empleado1 = servicioEmpleado.buscarPorFicha(evaluacionAux.getFicha());
					String cargo1 = empleado1.getCargo().getDescripcion();
					String unidadOrganizativa1 = empleado1
							.getUnidadOrganizativa().getDescripcion();
					String gerenciaReporte1 = empleado1
							.getUnidadOrganizativa().getGerencia()
							.getDescripcion();
					
					String dominioAuxR="";
					
					try
					{
						dominioAuxR= dominioRequerido.getDescripcionDominio();	
					}
					catch (Exception ex)
					{
						dominioAuxR="No APlica";
					}
					
							
					
					/*String eol = System.getProperty("line.separator"); 
					content = content  + evaluacionAux.getIdEvaluacionSecundario()+ ";" + evaluacionAux.getFicha() + ";"+ empleado1.getNombre()  +";" + cargo1 +";" + unidadOrganizativa1 +";" + gerenciaReporte1 +";" + competenciaRequerida.getDescripcion() +";" + dominioAuxR +";" + dominioEvidenciado.getDescripcionDominio() +";" + pesoCompetencia +";" + pesoDominioRequerido +";" + pesoDominioEvidenciado +";" +  pesoTotalDominioRequerido+";" + acumuladorPesoConducta + ";"  + acumuladorPesoCompetencia + ";"  + eol   ;*/
					
				}

			}

			// -------------------------------------------------------------------------------------------

			resultadoCompetencia = acumuladorPesoCompetencia; // (nde / ndr) *
																// 100;

			if (resultadoCompetencia > 115) {
				resultadoCompetencia = 115.0;
			}

		} catch (Exception e) {
			System.out.println(e.toString());
			resultadoCompetencia = 0.0;
		}
		
		
		/*File file = new File("c:/evaluacion/Datos.txt");
		try (FileOutputStream fop = new FileOutputStream(file)) {
 
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("Done");
			
		
 
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		

		try {

			DecimalFormat decimalFormat = new DecimalFormat("##.00");

			int id = 1;
			int id2 = 2;
			Distribucion dis = servicioDistribucion.buscarDistribucion(id);
			Distribucion dist = servicioDistribucion.buscarDistribucion(id2);
			double distO = dis.getPorcentaje();
			double distC = dist.getPorcentaje();
			String dO = String.valueOf(distO);
			String dC = String.valueOf(distC);
			
			
			
			DecimalFormat df=new DecimalFormat("0.00");
			
			try {
				resultadoCompetencia = (Double)df.parse(df.format(resultadoCompetencia)) ;
			} catch (Exception e) {
				// TODO: handle exception
				
			}
		
			String r = decimalFormat.format(resultadoCompetencia);

			txttotalCompetencia1.setValue(r);
			txttotalCompetencia2.setValue(r);
			
			
			Double resul1=0.0;
			try {
				resul1 = (Double)df.parse(df.format(calcularTotalResultadoObjetivos(evaluacionAux))) ;	
			} catch (Exception e) {
				// TODO: handle exception
				resul1 = calcularTotalResultadoObjetivos(evaluacionAux) ;
			}
			
			String o = String.valueOf(resul1);

			lblResultado.setValue(o);
			lblResultado1.setValue(r);
			lblDistribucion.setValue(dO);
			lblDistribucion1.setValue(dC);

			try {
				resultadoPesoCompetencia = (Double)df.parse(df.format(((distC * resultadoCompetencia) / 100))) ; //((distC * resultadoCompetencia) / 100); 
			} catch (Exception e) {
				// TODO: handle exception
				resultadoPesoCompetencia = (double)Math.round(((distC * resultadoCompetencia) / 100)) ; //((distC * resultadoCompetencia) / 100);
			}
			
			
			try {
				resultadoPesoObjetivo =  (Double)df.parse(df.format(((distO * resul1) / 100))) ; 
			} catch (Exception e) {
				// TODO: handle exception
				resultadoPesoObjetivo =  (double)Math.round((distO * resul1) / 100) ; 
			}
			
			lblResultadoPeso.setValue(resultadoPesoObjetivo.toString());
			lblResultadoPeso1.setValue(resultadoPesoCompetencia.toString());
				
			double resultadoFinal = 0.0;
			
			try {
				resultadoFinal =  (Double)df.parse(df.format((resultadoPesoObjetivo 	+ resultadoPesoCompetencia))) ;  //    resultadoPesoObjetivo 	+ resultadoPesoCompetencia;
			} catch (Exception e) {
				// TODO: handle exception
				resultadoFinal =  resultadoPesoObjetivo 	+ resultadoPesoCompetencia ;  //    resultadoPesoObjetivo 	+ resultadoPesoCompetencia;
			}
			
			String rf = String.valueOf(resultadoFinal);
			txtResultadoFinal.setValue(rf);
			
			evaluacionAux.setResultadoObjetivos(resultadoPesoObjetivo);
			evaluacionAux.setResultadoCompetencias(resultadoPesoCompetencia);
			evaluacionAux.setResultadoGeneral(resultadoFinal);
			evaluacionAux.setResultadoFinal(resultadoFinal);
			evaluacionAux.setValoracion(servicioUtilidad.obtenerValoracionFinalSimple(resultadoFinal));
			evaluacionAux.setResultado(0.0);
			
			servicioEvaluacion.guardar(evaluacionAux);
			
			/*
			 * txtValoracionFinal.setValue(servicioUtilidad
			 * .obtenerValoracionFinal((int) resultadoFinal));
			 */

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}

	private void guardarCompetenciasEspecificas() {

		try {

			String idDominio = "0";
			NivelCompetenciaCargo nivelCompetenciaCargo = new NivelCompetenciaCargo();
			Competencia competencia = new Competencia();
			Dominio dominio = new Dominio();

			for (int i = 0; i < lbxCompetenciaEspecifica.getItems().size(); i++) {

				Listitem listItem = lbxCompetenciaEspecifica.getItemAtIndex(i);

				if (listItem != null) {

					if (((Combobox) ((listItem.getChildren().get(2)))
							.getFirstChild()).getValue() == "") {
						/*
						 * Messagebox .show(
						 * "Debe Seleccionar un nivel de dominio para cada competencia"
						 * , "Error", Messagebox.OK, Messagebox.ERROR);
						 */
					} else {

						idDominio = ((Combobox) ((listItem.getChildren().get(2)))
								.getFirstChild()).getSelectedItem()
								.getContext();

						nivelCompetenciaCargo = (NivelCompetenciaCargo) listItem
								.getValue();

						competencia = servicioCompetencia
								.buscarCompetencia(nivelCompetenciaCargo
										.getCompetencia().getId());

						dominio = servicioDominio.buscarDominio(Integer
								.parseInt(idDominio));

						if (dominio.getDescripcionDominio().compareTo(
								"No Posee") == 0) {
							servicioUtilidad.eliminarConductaPorCompetencia(
									idEva, competencia.getId());
						}

					}
					EvaluacionCompetencia evaluacionCompetencia = new EvaluacionCompetencia();
					Evaluacion evaluacion = servicioEvaluacion
							.buscarEvaluacion(idEva);

					evaluacionCompetencia.setCompetencia(competencia);
					evaluacionCompetencia.setEvaluacion(evaluacion);
					evaluacionCompetencia.setIdDominio(Integer
							.parseInt(idDominio));
					servicioEvaluacionCompetencia
							.guardar(evaluacionCompetencia);

				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

	}

	private void guardarCompetenciasRectoras() {

		try {

			String idDominio = "0";
			NivelCompetenciaCargo nivelCompetenciaCargo = new NivelCompetenciaCargo();
			Competencia competencia = new Competencia();
			Dominio dominio = new Dominio();

			for (int i = 0; i < lbxCompetenciaRectora.getItems().size(); i++) {

				Listitem listItem = lbxCompetenciaRectora.getItemAtIndex(i);

				if (listItem != null) {

					if (((Combobox) ((listItem.getChildren().get(2)))
							.getFirstChild()).getValue() == "") {
						/*
						 * Messagebox .show(
						 * "Debe Seleccionar un nivel de dominio para cada competencia"
						 * , "Error", Messagebox.OK, Messagebox.ERROR);
						 */
					} else {

						idDominio = ((Combobox) ((listItem.getChildren().get(2)))
								.getFirstChild()).getSelectedItem()
								.getContext();

						nivelCompetenciaCargo = (NivelCompetenciaCargo) listItem
								.getValue();

						competencia = servicioCompetencia
								.buscarCompetencia(nivelCompetenciaCargo
										.getCompetencia().getId());

						dominio = servicioDominio.buscarDominio(Integer
								.parseInt(idDominio));

						if (dominio.getDescripcionDominio().compareTo(
								"No Posee") == 0) {
							servicioUtilidad.eliminarConductaPorCompetencia(
									idEva, competencia.getId());
						}

					}
					EvaluacionCompetencia evaluacionCompetencia = new EvaluacionCompetencia();
					Evaluacion evaluacion = servicioEvaluacion
							.buscarEvaluacion(idEva);

					evaluacionCompetencia.setCompetencia(competencia);
					evaluacionCompetencia.setEvaluacion(evaluacion);
					evaluacionCompetencia.setIdDominio(Integer
							.parseInt(idDominio));
					servicioEvaluacionCompetencia
							.guardar(evaluacionCompetencia);

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

	}

	public void llenarCombosPestanna5() {
		List<TipoFormacion> formacion = servicioTipoFormacion.buscarTodos();
		cmbTipoFormacion.setModel(new ListModelList<TipoFormacion>(formacion));

		List<Urgencia> urgencia = servicioUrgencia.buscarTodas();
		cmbUrgencia.setModel(new ListModelList<Urgencia>(urgencia));
	}

	@Listen("onSelect = #cmbTipoFormacion")
	public void filtrarArea() {
		cmbArea.setValue(null);
		cmbArea.setPlaceholder("Area / Campo");
	}

	@Listen("onOpen = #cmbArea")
	public void abrirArea() {
		if (cmbTipoFormacion.getValue().trim().equals(""))
			Messagebox.show("Debe Seleccionar un Tipo de Formacion", "Error",
					Messagebox.OK, Messagebox.ERROR);
		else {
			String idTipo = cmbTipoFormacion.getSelectedItem().getContext();
			List<Area> area = servicioArea.buscarPorFormacion(Integer
					.parseInt(idTipo));
			cmbArea.setModel(new ListModelList<Area>(area));
		}
	}

	@Listen("onClick = #btnAgregarAcciones")
	public void AgregarAccion() {
		// limpiar();
		listCapacitacion = servicioEvaluacionCapacitacion
				.buscarPorEvaluacion(idEva);
		if (listCapacitacion.size() == 3)
			Messagebox
					.show("Ha Superado el limite de Acciones a Registrar, puede Eliminar o Modificar las Existentes",
							"Alerta", Messagebox.OK, Messagebox.EXCLAMATION);
		else {
			gpxAgregarCapacitacion.setOpen(true);
			llenarCombosPestanna5();
			btnAgregarAcciones.setDisabled(true);
			btnEliminarAcciones.setDisabled(true);
		}
	}

	@Listen("onClick = #btnAgregarCapacitacion")
	public void AgregarAcciones() {

		if (txtDescFormacion.getValue().trim().equals("")
				|| cmbArea.getValue().trim().equals("")
				|| cmbUrgencia.getValue().trim().equals("")
				|| cmbTipoFormacion.getValue().trim().equals("")) {
			Messagebox.show("Debe llenar todos los campos", "Error",
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			String idArea = cmbArea.getSelectedItem().getContext();
			Area area = servicioArea.buscarArea(Integer.parseInt(idArea));
			String idTipo = cmbTipoFormacion.getSelectedItem().getContext();
			TipoFormacion tipo = servicioTipoFormacion
					.buscarTipoFormacion(Integer.parseInt(idTipo));
			String idUrgencia = cmbUrgencia.getSelectedItem().getContext();
			Urgencia urgencia = servicioUrgencia.buscarUrgencia(Integer
					.parseInt(idUrgencia));
			String descripcion = txtDescFormacion.getValue();

			EvaluacionCapacitacion capacitacion = new EvaluacionCapacitacion();

			if (idCapacitacion != 0)
				capacitacion.setIdCapacitacion(idCapacitacion);
			else
				capacitacion.setIdCapacitacion(0);
			capacitacion.setIdEvaluacion(idEva);
			capacitacion.setDescripcionFormacion(descripcion);
			capacitacion.setArea(area);
			capacitacion.setTipoFormacion(tipo);
			capacitacion.setUrgencia(urgencia);
			servicioEvaluacionCapacitacion.guardar(capacitacion);

			listCapacitacion = servicioEvaluacionCapacitacion
					.buscarPorEvaluacion(idEva);
			lbxAccionesGuardadas
					.setModel(new ListModelList<EvaluacionCapacitacion>(
							listCapacitacion));
			msj.mensajeInformacion(Mensaje.guardado);
			idCapacitacion = 0;
			btnAgregarAcciones.setDisabled(false);
			btnEliminarAcciones.setDisabled(false);
			gpxAgregarCapacitacion.setOpen(false);
			limpiarPestanna5();
		}

	}

	public void limpiarPestanna5() {
		txtDescFormacion.setValue("");
		cmbArea.setValue(null);
		cmbArea.setPlaceholder("Area / Campo");
		cmbTipoFormacion.setValue(null);
		cmbTipoFormacion.setPlaceholder("Tipo de Formacion");
		cmbUrgencia.setValue(null);
		cmbUrgencia.setPlaceholder("Urgencia");

	}

	@Listen("onClick = #btnQuitarCapacitacion")
	public void cerrarListaCapacitacion() {
		gpxAgregarCapacitacion.setOpen(false);
		btnAgregarAcciones.setDisabled(false);
		btnEliminarAcciones.setDisabled(false);
		limpiarPestanna5();
		idCapacitacion = 0;
	}

	@Listen("onClick = #btnEliminarAcciones")
	public void eliminarCapacitacion() {
		if (lbxAccionesGuardadas.getItemCount() != 0) {
			Listitem listItem = lbxAccionesGuardadas.getSelectedItem();
			if (listItem != null) {
				EvaluacionCapacitacion evaluacionCapacitacion = (EvaluacionCapacitacion) listItem
						.getValue();
				evaluacionCapacitacion = listItem.getValue();
				final int idCapacitacionEliminar = evaluacionCapacitacion
						.getIdCapacitacion();
				Messagebox.show("¿Esta Seguro de Eliminar la Accion?",
						"Alerta", Messagebox.OK | Messagebox.CANCEL,
						Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener<Event>() {
							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onOK")) {

									servicioEvaluacionCapacitacion
											.eliminarUno(idCapacitacionEliminar);
									msj.mensajeInformacion(Mensaje.eliminado);
									lbxAccionesGuardadas.getItems().clear();
									listCapacitacion = servicioEvaluacionCapacitacion
											.buscarPorEvaluacion(idEva);
									lbxAccionesGuardadas
											.setModel(new ListModelList<EvaluacionCapacitacion>(
													listCapacitacion));

								}

							}
						});

			}
		} else
			msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);
	}

	@Listen("onDoubleClick = #lbxAccionesGuardadas")
	public void editarAccion() {
		gpxAgregarCapacitacion.setOpen(true);
		if (lbxAccionesGuardadas.getItemCount() != 0) {
			Listitem listItem = lbxAccionesGuardadas.getSelectedItem();
			if (listItem != null) {
				EvaluacionCapacitacion evaluacionCapacitacion = (EvaluacionCapacitacion) listItem
						.getValue();
				idCapacitacion = evaluacionCapacitacion.getIdCapacitacion();
				txtDescFormacion.setValue(evaluacionCapacitacion
						.getDescripcionFormacion());
				cmbArea.setValue(evaluacionCapacitacion.getArea()
						.getDescripcion());
				cmbTipoFormacion.setValue(evaluacionCapacitacion
						.getTipoFormacion().getDescripcion());
				cmbUrgencia.setValue(evaluacionCapacitacion.getUrgencia()
						.getDescripcionUrgencia());
			}
		}
	}

}