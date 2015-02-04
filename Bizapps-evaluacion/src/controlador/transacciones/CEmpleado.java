package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import modelo.maestros.Bitacora;
import modelo.maestros.Cargo;
import modelo.maestros.ConductaCompetencia;
import modelo.maestros.ConfiguracionGeneral;
import modelo.maestros.Empleado;
import modelo.maestros.Evaluacion;
import modelo.maestros.EvaluacionCapacitacion;
import modelo.maestros.EvaluacionCompetencia;
import modelo.maestros.EvaluacionConducta;
import modelo.maestros.EvaluacionIndicador;
import modelo.maestros.EvaluacionObjetivo;
import modelo.maestros.Revision;
import modelo.maestros.UnidadOrganizativa;
import modelo.seguridad.Arbol;
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;

import servicio.maestros.SEmpleado;
import servicio.seguridad.SUsuario;
import servicio.transacciones.SEvaluacion;

import arbol.MArbol;
import arbol.Nodos;

import controlador.maestros.CGenerico;
import componentes.Mensaje;
import componentes.Validador;

public class CEmpleado extends CGenerico {

	private static final long serialVersionUID = -5393608637902961029L;
	@Wire
	private Window winEvaluacionEmpleadoAgregar;
	@Wire
	private Tree arbolPersonal;
	@Wire
	private Include contenido;
	@Wire
	private Label etiqueta;
	private int idEva;
	private Evaluacion eva;
	public Revision revision;
	TreeModel _model;
	List<String> listmenu1 = new ArrayList<String>();
	@Wire
	private Tab tab;
	@Wire
	private Tabbox tabBox;
	@Wire
	private West west;
	@Wire
	private Window winListaPersonal;
	@Wire
	private Window winArbolPersonal;

	@Wire
	private Window winEvaluacionEmpleado;
	@Wire
	private Listbox lbxEvaluacion;
	@Wire
	private Listbox lbxPersonalCargo;
	@Wire
	private Listheader header;
	@Wire
	private Groupbox gpxListaPersonalCargo;
	@Wire
	private Button btnCopiar;
	@Wire
	private Button btnAgregar;
	@Wire
	private Button btnEliminar;
	private static int numeroEvaluacion;
	private static Empleado empleado;
	Evaluacion evaluacion = new Evaluacion();
	public Revision revisionActiva;
	private String fichaE;
	private boolean tienePersonal = false;
	List<Evaluacion> evaluacion1 = new ArrayList<Evaluacion>();
	String bandera;

	Mensaje msj = new Mensaje();

	@Override
	public void inicializar() throws IOException {
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
		arbolPersonal.setModel(getModel());

		revisionActiva = servicioRevision.buscarPorEstado("ACTIVO");
		
		if (!tienePersonal) {
			msj.mensajeAlerta(Mensaje.personalCargo);
			cerrarVentana(winArbolPersonal, titulo, tabs);
		}
		List<ConfiguracionGeneral> configuracion = servicioConfiguracionGeneral
				.buscar();
		bandera = configuracion.get(0).getBandera();
	}

	/* Permite asignarle los nodos cargados con el metodo getFooRoot() al arbol */
	public TreeModel getModel() {
		if (_model == null) {
			_model = new MArbol(getFooRoot());
		}
		return _model;
	}

	/*
	 * Permite obtener las funcionalidades asociadas al usuario en session y asi
	 * crear un arbol estructurado, segun la distribucion de las mismas
	 */
	private Nodos getFooRoot() {
		Nodos root = new Nodos(null, 0, "", "");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		List<Empleado> empleado = servicioEmpleado.BuscarPorSupervisor(u
				.getCedula());
		Empleado jefeSesion = servicioEmpleado.buscarPorFicha(u.getCedula());
		String erredora = jefeSesion.getFichaSupervisor();
		List<Empleado> arboles = new ArrayList<Empleado>();
		ArrayList<Integer> ids = new ArrayList<Integer>();
		List<Empleado> hijos = new ArrayList<Empleado>();
		for (int k = 0; k < empleado.size(); k++) {
			tienePersonal = true;
			if (!empleado.get(k).getFicha().equals(erredora)) {
				ids.add(Integer.valueOf(empleado.get(k).getFicha()));
				hijos = servicioEmpleado.BuscarPorSupervisor(empleado.get(k)
						.getFicha());
				for (int i = 0; i < hijos.size(); i++) {
					empleado.add(hijos.get(i));
				}
			}
		}
		Empleado jefe = servicioEmpleado.buscarPorFicha(u.getCedula());
		arboles.add(jefe);
		for (int t = 0; t < ids.size(); t++) {
			Empleado a;
			a = servicioEmpleado.buscarPorFicha(String.valueOf(ids.get(t)));
			arboles.add(a);
		}
		List<Nodos> nodos = new ArrayList<Nodos>();
		List<String> idsPadre = new ArrayList<String>();
		return crearArbol(root, nodos, arboles, 0, idsPadre, jefe.getFicha());

	}

	private Nodos crearArbol(Nodos roote, List<Nodos> nodos,
			List<Empleado> arboles, int i, List<String> idsPadre, String cedula) {
		for (int z = 0; z < arboles.size(); z++) {
			Nodos oneLevelNode = new Nodos(null, 0, "", "");
			Nodos two = new Nodos(null, 0, "", "");
			if (arboles.get(z).getFicha().equals(cedula)) {
				oneLevelNode = new Nodos(roote, z, +arboles.get(z)
						.getGradoAuxiliar()
						+ " "
						+ "("
						+ arboles.get(z).getFicha()
						+ ")"
						+ " "
						+ arboles.get(z).getNombre(), arboles.get(z).getFicha());
				roote.appendChild(oneLevelNode);
				idsPadre.add(arboles.get(z).getFicha());
				nodos.add(oneLevelNode);
			} else {
				for (int j = 0; j < idsPadre.size(); j++) {
					if (idsPadre.get(j).equals(
							arboles.get(z).getFichaSupervisor())) {
						oneLevelNode = nodos.get(j);
						two = new Nodos(oneLevelNode, z, +arboles.get(z)
								.getGradoAuxiliar()
								+ " "
								+ "("
								+ arboles.get(z).getFicha()
								+ ")"
								+ " "
								+ arboles.get(z).getNombre(), arboles.get(z)
								.getFicha());
						oneLevelNode.appendChild(two);
						idsPadre.add(arboles.get(z).getFicha());
						nodos.add(two);
					}
				}
			}
		}
		return roote;
	}

	@Listen("onClick = #arbolPersonal")
	public void selectedNode() {
		lbxEvaluacion.getItems().clear();
		gpxListaPersonalCargo.setTitle("");
		if (arbolPersonal.getSelectedItem() != null) {
			String item = String.valueOf(arbolPersonal.getSelectedItem()
					.getContext());
			boolean abrir = true;
			if (arbolPersonal.getSelectedItem().getLevel() > 0) {
				if (abrir) {

					List<Evaluacion> evaluacion = new ArrayList<Evaluacion>();
					evaluacion = servicioEvaluacion.buscar(item);

					if (evaluacion.isEmpty()) {
						Messagebox
								.show("El empleado no tiene evaluaciones registradas",
										"Error", Messagebox.OK,
										Messagebox.ERROR);
					} else {
						gpxListaPersonalCargo.setTitle(arbolPersonal
								.getSelectedItem().getLabel());
						lbxEvaluacion.setModel(new ListModelList<Evaluacion>(
								evaluacion));
						lbxEvaluacion.renderAll();
						for (int j = 0; j < lbxEvaluacion.getItems().size(); j++) {
							Listitem listItem = lbxEvaluacion.getItemAtIndex(j);
							List<Listitem> listItem2 = lbxEvaluacion.getItems();
							Evaluacion eva = listItem2.get(j).getValue();
							String fichaS = eva.getFichaEvaluador();
							Empleado empleado = servicioEmpleado
									.buscarPorFicha(fichaS);
							String nombre = empleado.getNombre();
							((Label) ((listItem.getChildren().get(5)))
									.getFirstChild()).setValue(nombre);
						}
					}

				}
			}
		}

	}

	@Listen("onClick = #btnEliminar")
	public void eliminar() {

		idEva = 0;
		if (bandera.equals("false")) {
			if (lbxEvaluacion.getItemCount() != 0) {

				Listitem listItem = lbxEvaluacion.getSelectedItem();
				if (listItem != null) {

					Evaluacion evaluacionE = (Evaluacion) listItem.getValue();
					eva = evaluacionE;
					Integer id = evaluacionE.getIdEvaluacion();
					idEva = id;
					Messagebox.show(Mensaje.deseaEliminar, "Alerta",
							Messagebox.OK | Messagebox.CANCEL,
							Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener<Event>() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onOK")) {
										if (eva.getEstadoEvaluacion().equals(
												"EN EDICION")) {
											List<EvaluacionObjetivo> evaluacionObjetivo = servicioEvaluacionObjetivo
													.buscarObjetivosEvaluar(idEva);
											for (int i = 0; i < evaluacionObjetivo
													.size(); i++) {
												Integer idObjetivo = evaluacionObjetivo
														.get(i).getIdObjetivo();
												List<EvaluacionIndicador> evaluacionIndicador = servicioEvaluacionIndicador
														.buscarIndicadores(idObjetivo);
												servicioEvaluacionIndicador
														.eliminarVarios(evaluacionIndicador);
											}
											servicioEvaluacionObjetivo
													.eliminarVarios(evaluacionObjetivo);
											servicioUtilidad
													.eliminarConductaPorEvaluacion(idEva);
											servicioUtilidad
													.eliminarCompetenciaPorEvaluacion(idEva);
											servicioUtilidad
													.eliminarCapacitacionPorEvaluacion(idEva);
											servicioEvaluacion
													.eliminarUno(idEva);

											msj.mensajeInformacion(Mensaje.eliminado);
											lbxEvaluacion.getItems().clear();

											if (arbolPersonal.getSelectedItem() != null) {
												String item = String
														.valueOf(arbolPersonal
																.getSelectedItem()
																.getContext());

												boolean abrir = true;
												if (arbolPersonal
														.getSelectedItem()
														.getLevel() > 0) {
													if (abrir) {

														List<Evaluacion> evaluacion = new ArrayList<Evaluacion>();
														evaluacion = servicioEvaluacion
																.buscarEstado(item);

														if (evaluacion
																.isEmpty()) {
															Messagebox
																	.show("El empleado no tiene evaluaciones registradas",
																			"Error",
																			Messagebox.OK,
																			Messagebox.ERROR);
														} else {
															gpxListaPersonalCargo
																	.setTitle("("
																			+ "  "
																			+ item
																			+ ")"
																			+ "   "
																			+ arbolPersonal
																					.getSelectedItem()
																					.getLabel());
															lbxEvaluacion
																	.setModel(new ListModelList<Evaluacion>(
																			evaluacion));
															lbxEvaluacion
																	.renderAll();
															for (int j = 0; j < lbxEvaluacion
																	.getItems()
																	.size(); j++) {
																Listitem listItem = lbxEvaluacion
																		.getItemAtIndex(j);
																List<Listitem> listItem2 = lbxEvaluacion
																		.getItems();
																Evaluacion eva = listItem2
																		.get(j)
																		.getValue();
																String fichaS = eva
																		.getFichaEvaluador();
																Empleado empleado = servicioEmpleado
																		.buscarPorFicha(fichaS);
																String nombre = empleado
																		.getNombre();
																((Label) ((listItem
																		.getChildren()
																		.get(5)))
																		.getFirstChild())
																		.setValue(nombre);
															}
														}

													}
												}
											}

										} else {
											Messagebox
													.show("No puede Eliminar la Evaluación",
															"Alerta",
															Messagebox.OK,
															Messagebox.EXCLAMATION);
										}

									}
								}
							});
				} else
					msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);

			}
		} else {
			msj.mensajeError(Mensaje.noSePuedeEliminar);
		}
	}

	@Listen("onClick = #btnCopiar")
	public void copiar() {
		if (bandera.equals("false")) {
			if (lbxEvaluacion.getItemCount() != 0) {

				Listitem listItem = lbxEvaluacion.getSelectedItem();
				if (listItem != null) {

					Evaluacion evaluacionE = (Evaluacion) listItem.getValue();
					eva = evaluacionE;
					Integer id = evaluacionE.getIdEvaluacion();
					idEva = id;
					Messagebox.show(Mensaje.deseaCopiar, "Alerta",
							Messagebox.YES | Messagebox.NO,
							Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener<Event>() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onYes")) {

										revision = servicioRevision
												.buscarPorEstado("ACTIVO");

										Usuario u = servicioUsuario
												.buscarPorCedula(eva.getFicha());
										String ficha = eva.getFicha();
										Integer idUsuario = u.getIdUsuario();

										if (servicioEvaluacion
												.cantidadEvaluaciones(ficha,
														revision.getId()) <= 1) {

											Integer numeroEvaluacion = servicioEvaluacion
													.buscarIdSecundario(eva
															.getFicha()) + 1;
											Integer numeroEvaluacionPrimaria = servicioEvaluacion
													.buscarId() + 1;
											Empleado empleado = servicioEmpleado
													.buscarPorFicha(ficha);
											String fichaEvaluador = empleado
													.getFichaSupervisor();
											Cargo cargo = empleado.getCargo();
											UnidadOrganizativa unidadOrganizativa = empleado
													.getUnidadOrganizativa();

											eva.setIdEvaluacion(numeroEvaluacionPrimaria);
											eva.setIdEvaluacionSecundario(numeroEvaluacion);
											eva.setCargo(cargo);
											eva.setUnidadOrganizativa(unidadOrganizativa);
											eva.setEstadoEvaluacion("EN EDICION");
											eva.setFechaCreacion(fechaHora);
											eva.setFechaAuditoria(fechaHora);
											eva.setRevision(revision);
											eva.setIdUsuario(idUsuario);
											eva.setFichaEvaluador(fichaEvaluador);
											eva.setPeso(0);
											eva.setResultado(0);
											eva.setResultadoObjetivos(0);
											eva.setResultadoGeneral(0);
											eva.setHoraAuditoria(horaAuditoria);

											Bitacora bitacora = new Bitacora();

											bitacora.setEvaluacion(eva);
											bitacora.setIdUsuario(u);
											bitacora.setFechaAuditoria(fechaHora);
											bitacora.setHoraAuditoria(horaAuditoria);
											bitacora.setEstadoEvaluacion("EN EDICION");
											System.out.println(bitacora
													.getEvaluacion()
													.getIdEvaluacion());
											System.out.println(evaluacion + " "
													+ u + " " + fechaHora
													+ horaAuditoria);

											servicioEvaluacion.guardar(eva);
											servicioBitacora.guardar(bitacora);

											Integer idEvaluacionObjetivoOriginal = 0;

											List<EvaluacionObjetivo> listaEvaluacionObjetivo = servicioEvaluacionObjetivo
													.buscarObjetivosEvaluar(idEva);
											for (int i = 0; i < listaEvaluacionObjetivo
													.size(); i++) {

												EvaluacionObjetivo evaluacionObjetivo = listaEvaluacionObjetivo
														.get(i);
												idEvaluacionObjetivoOriginal = evaluacionObjetivo
														.getIdObjetivo();
												evaluacionObjetivo
														.setIdObjetivo(0);
												evaluacionObjetivo
														.setIdEvaluacion(numeroEvaluacionPrimaria);
												servicioEvaluacionObjetivo
														.guardar(evaluacionObjetivo);

												List<EvaluacionObjetivo> listaEvaluacionObjetivoGuardado = servicioEvaluacionObjetivo
														.buscarObjetivosEvaluar(numeroEvaluacionPrimaria);

												Integer idObjetivoGuardado = 0;
												for (int x = 0; x < listaEvaluacionObjetivoGuardado
														.size(); x++) {

													EvaluacionObjetivo evaluacionObjetivoGuardardo = listaEvaluacionObjetivoGuardado
															.get(x);
													idObjetivoGuardado = evaluacionObjetivoGuardardo
															.getIdObjetivo();

												}

												List<EvaluacionIndicador> listaEvaluacionIndicador = servicioEvaluacionIndicador
														.buscarIndicadores(idEvaluacionObjetivoOriginal);
												for (int x = 0; x < listaEvaluacionIndicador
														.size(); x++) {

													EvaluacionIndicador evaluacionIndicador = listaEvaluacionIndicador
															.get(x);
													evaluacionIndicador
															.setIdIndicador(0);
													evaluacionIndicador
															.setIdObjetivo(idObjetivoGuardado);
													servicioEvaluacionIndicador
															.guardar(evaluacionIndicador);
												}

											}

											Evaluacion evaluacionAuxiliar = servicioEvaluacion
													.buscarEvaluacion(idEva);
											List<EvaluacionCompetencia> listaEvaluacionCompetencias = servicioEvaluacionCompetencia
													.buscar(evaluacionAuxiliar);

											for (int i = 0; i < listaEvaluacionCompetencias
													.size(); i++) {

												EvaluacionCompetencia evaluacionCompetencia = listaEvaluacionCompetencias
														.get(i);
												evaluacionCompetencia
														.setEvaluacion(eva);
												servicioEvaluacionCompetencia
														.guardar(evaluacionCompetencia);

											}

											List<EvaluacionConducta> listaEvaluacionConductas = servicioEvaluacionConducta
													.buscar(evaluacionAuxiliar);

											for (int i = 0; i < listaEvaluacionConductas
													.size(); i++) {

												EvaluacionConducta evaluacionConducta = listaEvaluacionConductas
														.get(i);
												evaluacionConducta
														.setEvaluacion(eva);
												servicioEvaluacionConducta
														.guardar(evaluacionConducta);

											}

											List<EvaluacionCapacitacion> listaEvaluacionCapacitacion = servicioEvaluacionCapacitacion
													.buscarPorEvaluacion(idEva);

											for (int i = 0; i < listaEvaluacionCapacitacion
													.size(); i++) {

												EvaluacionCapacitacion evaluacionCapacitacion = listaEvaluacionCapacitacion
														.get(i);
												evaluacionCapacitacion
														.setIdEvaluacion(numeroEvaluacionPrimaria);
												servicioEvaluacionCapacitacion
														.guardar(evaluacionCapacitacion);

											}

											if (arbolPersonal.getSelectedItem() != null) {
												String item = String
														.valueOf(arbolPersonal
																.getSelectedItem()
																.getContext());
												System.out.println(item);
												boolean abrir = true;
												if (arbolPersonal
														.getSelectedItem()
														.getLevel() > 0) {
													if (abrir) {

														List<Evaluacion> evaluacion = new ArrayList<Evaluacion>();
														evaluacion = servicioEvaluacion
																.buscarEstado(item);

														if (evaluacion
																.isEmpty()) {
															Messagebox
																	.show("El empleado no tiene evaluaciones registradas",
																			"Error",
																			Messagebox.OK,
																			Messagebox.ERROR);
														} else {
															gpxListaPersonalCargo
																	.setTitle("("
																			+ "  "
																			+ item
																			+ ")"
																			+ "   "
																			+ arbolPersonal
																					.getSelectedItem()
																					.getLabel());
															lbxEvaluacion
																	.setModel(new ListModelList<Evaluacion>(
																			evaluacion));
															lbxEvaluacion
																	.renderAll();
															for (int j = 0; j < lbxEvaluacion
																	.getItems()
																	.size(); j++) {
																Listitem listItem = lbxEvaluacion
																		.getItemAtIndex(j);
																List<Listitem> listItem2 = lbxEvaluacion
																		.getItems();
																Evaluacion eva = listItem2
																		.get(j)
																		.getValue();
																String fichaS = eva
																		.getFichaEvaluador();
																empleado = servicioEmpleado
																		.buscarPorFicha(fichaS);
																String nombre = empleado
																		.getNombre();
																((Label) ((listItem
																		.getChildren()
																		.get(5)))
																		.getFirstChild())
																		.setValue(nombre);
															}
														}

													}
												}
											}

										} else {
											msj.mensajeError(Mensaje.soloUnaEvaluacion);
										}

									}
								}
							});
				} else
					msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);

			}
		} else {
			msj.mensajeError(Mensaje.noSePuedeCopiar);
		}
	}

	@Listen("onDoubleClick = #lbxEvaluacion")
	public void mostrarEvaluacion() {

		if (lbxEvaluacion.getItemCount() != 0) {

			Listitem listItem = lbxEvaluacion.getSelectedItem();
			if (listItem != null) {

				Evaluacion evaluacion = (Evaluacion) listItem.getValue();

				Usuario usuario = servicioUsuario.buscarId(evaluacion
						.getIdUsuario());
				String fichaUsuario = usuario.getFicha();

				if (fichaUsuario.compareTo("0") == 0) {
					Messagebox
							.show("La Evaluación no se puede visualizar ya que es solo un registro historico ( Valoracion Final )",
									"Alerta", Messagebox.OK,
									Messagebox.EXCLAMATION);
				} else {

					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("modo", "EDITAR");
					map.put("id", evaluacion.getIdEvaluacion());
					map.put("titulo", evaluacion.getFicha());
					map.put("listbox", lbxEvaluacion);
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);
					winEvaluacionEmpleado = (Window) Executions
							.createComponents(
									"/vistas/transacciones/VAgregarEvaluacion.zul",
									null, map);
					winEvaluacionEmpleado.doModal();
					winEvaluacionEmpleado.setClosable(true);

				}

			}

		}

	}

	@Listen("onClick = #btnAgregar")
	public void selectedNodeA() {
		if (bandera.equals("false")) {
			if (arbolPersonal.getSelectedItem() != null) {
				String ficha = String.valueOf(arbolPersonal.getSelectedItem()
						.getContext());
				Authentication auth = SecurityContextHolder.getContext()
						.getAuthentication();
				Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth
						.getName());
				
				Integer idUsuario = u.getIdUsuario();
				Integer numeroEvaluacion;
				if (servicioEvaluacion.buscarIdSecundario(ficha) != null) {
					numeroEvaluacion = servicioEvaluacion
							.buscarIdSecundario(ficha) + 1;
				} else {
					numeroEvaluacion = 1;
				}

				if (servicioEvaluacion.cantidadEvaluaciones(ficha,
						revisionActiva.getId()) <=1) {

					idEva = servicioEvaluacion.buscarId() + 1;
					Empleado empleado = servicioEmpleado.buscarPorFicha(ficha);
					String fichaEvaluador = empleado.getFichaSupervisor();
					Cargo cargo = empleado.getCargo();
					Evaluacion evaluacion = new Evaluacion();
					evaluacion.setIdEvaluacion(idEva);
					evaluacion.setEstadoEvaluacion("EN EDICION");
					evaluacion.setFechaCreacion(fechaHora);
					evaluacion.setFechaAuditoria(fechaHora);
					evaluacion.setFicha(ficha);
					evaluacion.setRevision(revisionActiva);
					evaluacion.setIdEvaluacionSecundario(numeroEvaluacion);
					evaluacion.setIdUsuario(idUsuario);
					evaluacion.setFichaEvaluador(fichaEvaluador);
					evaluacion.setPeso(0);
					evaluacion.setResultado(0);
					evaluacion.setResultadoObjetivos(0);
					evaluacion.setResultadoGeneral(0);
					evaluacion.setCargo(cargo);
					evaluacion.setHoraAuditoria(horaAuditoria);

					Bitacora bitacora = new Bitacora();

					bitacora.setEvaluacion(evaluacion);
					bitacora.setIdUsuario(u);
					bitacora.setFechaAuditoria(fechaHora);
					bitacora.setHoraAuditoria(horaAuditoria);
					bitacora.setEstadoEvaluacion("EN EDICION");
					System.out.println(bitacora.getEvaluacion()
							.getIdEvaluacion());
					System.out.println(evaluacion + " " + u + " " + fechaHora
							+ horaAuditoria);
					servicioEvaluacion.guardar(evaluacion);
					servicioBitacora.guardar(bitacora);

					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("modo", "AGREGAR");
					map.put("ficha", ficha);
					map.put("id", idEva);
					map.put("numero", numeroEvaluacion);
					map.put("listbox", lbxEvaluacion);
					map.put("empleado", "true");
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);
					if (winEvaluacionEmpleadoAgregar != null) {
						winEvaluacionEmpleadoAgregar.detach();
						winEvaluacionEmpleadoAgregar = null;
					} else {
						winEvaluacionEmpleadoAgregar = (Window) Executions
								.createComponents(
										"/vistas/transacciones/VAgregarEvaluacion.zul",
										null, map);
						winEvaluacionEmpleadoAgregar.doModal();
						winEvaluacionEmpleadoAgregar.setClosable(true);

					}
				} else {
					msj.mensajeError(Mensaje.soloUnaEvaluacion);
				}

			}
		} else {
			msj.mensajeError(Mensaje.noSePuedeCrear);
		}

	}

	public void actualizame1(Listbox listbox, SUsuario servicio,
			SEvaluacion servicioE, SEmpleado servicioEm, String item) {
		lbxEvaluacion = listbox;
		servicioUsuario = servicio;
		servicioEvaluacion = servicioE;
		servicioEmpleado = servicioEm;
		fichaE = new String();
		fichaE = item;
		System.out.println("ojo" + item);
		evaluacion1 = new ArrayList<Evaluacion>();
		evaluacion1 = servicioEvaluacion.buscar(item);
		System.out.println("eva" + evaluacion1);
		lbxEvaluacion.setModel(new ListModelList<Evaluacion>(evaluacion1));
		lbxEvaluacion.renderAll();
		for (int j = 0; j < lbxEvaluacion.getItems().size(); j++) {
			Listitem listItem = lbxEvaluacion.getItemAtIndex(j);
			List<Listitem> listItem2 = lbxEvaluacion.getItems();
			Evaluacion eva = listItem2.get(j).getValue();
			String fichaS = eva.getFichaEvaluador();
			Empleado empleado = servicioEmpleado.buscarPorFicha(fichaS);
			String nombre = empleado.getNombre();
			((Label) ((listItem.getChildren().get(5))).getFirstChild())
					.setValue(nombre);
		}
	}
}