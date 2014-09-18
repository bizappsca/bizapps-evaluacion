package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Bitacora;
import modelo.maestros.Cargo;
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
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import servicio.maestros.SEmpleado;
import servicio.seguridad.SUsuario;
import servicio.transacciones.SEvaluacion;

import componentes.Mensaje;

import controlador.maestros.CGenerico;

public class CListaPersonal extends CGenerico {

	private static final long serialVersionUID = -5393608637902961029L;
	Mensaje msj = new Mensaje();

	private Button btnAgregar;
	@Wire
	private Button btnEliminar;
	@Wire
	private Button btnCopiar;
	@Wire
	private Window winListaPersonal;
	@Wire
	private Listbox lbxEvaluacion;
	@Wire
	private Listbox lbxUsuario;
	@Wire
	private Window winEvaluacionEmpleado;
	@Wire
	private Groupbox gpxListaPersonal;
	private  int idEva;
	private  String fichaE;
	private  Evaluacion eva;
	public  Revision revision;
	List<Evaluacion> evaluacion = new ArrayList<Evaluacion>();
	List<Usuario> usuarios = new ArrayList<Usuario>();
	Evaluacion evaluacionN;
	String bandera = "";


	@Override
	public void inicializar() throws IOException {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		String ficha = u.getCedula();
		fichaE = ficha;
		gpxListaPersonal.setTitle("(" + "  " + ficha + ")" + "   "
				+ u.getNombre() + "   " + u.getApellido());
		evaluacion = servicioEvaluacion.buscar(ficha);
		lbxEvaluacion.setModel(new ListModelList<Evaluacion>(evaluacion));
		revision = servicioRevision.buscarPorEstado("ACTIVO");
		idEva = servicioEvaluacion.buscarId() + 1;

		lbxEvaluacion.renderAll();
		for (int j = 0; j < lbxEvaluacion.getItems().size(); j++) {
			Listitem listItem = lbxEvaluacion.getItemAtIndex(j);
			List<Listitem> listItem2 = lbxEvaluacion.getItems();
			Evaluacion eva = listItem2.get(j).getValue();
			String fichaS = eva.getFichaEvaluador();
			Empleado empleado = servicioEmpleado.buscarPorFicha(fichaS);
			Usuario usuario =servicioUsuario.buscarId(eva.getIdUsuario());
			String fichaUsuario = usuario.getFicha();
			String nombre = usuario.getNombre().concat(" ").concat(usuario.getApellido());
			
			((Label) ((listItem.getChildren().get(4))).getFirstChild())
			.setValue(fichaUsuario);
			
			((Label) ((listItem.getChildren().get(5))).getFirstChild())
			.setValue(nombre);
		}
		List<ConfiguracionGeneral> configuracion = servicioConfiguracionGeneral.buscar();
		bandera = configuracion.get(0).getBandera();
	}

	@Listen("onClick = #btnSalir")
	public void salir() {
		cerrarVentana1(winListaPersonal, "Personal");
	}

	@Listen("onClick = #btnAgregar")
	public void AgregarEvaluacion() {
		if (bandera.equals("false")){
		//winListaPersonal.onClose();
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		String ficha = u.getCedula();
		Integer idUsuario = u.getIdUsuario();
		Integer numeroEvaluacion =0;
		try {
            numeroEvaluacion= servicioEvaluacion.buscarIdSecundario(ficha) + 1;
		} catch (Exception e) {
			// TODO: handle exception
			numeroEvaluacion=1;
		}
		
		
		
		
		idEva = servicioEvaluacion.buscarId() + 1;
		Empleado empleado = servicioEmpleado.buscarPorFicha(ficha);
		String fichaEvaluador = empleado.getFichaSupervisor();
		Cargo cargo = empleado.getCargo();
		UnidadOrganizativa unidadOrganizativa = empleado.getUnidadOrganizativa();
		Evaluacion evaluacion = new Evaluacion();
		evaluacion.setIdEvaluacion(idEva);
		evaluacion.setEstadoEvaluacion("EN EDICION");
		evaluacion.setFechaCreacion(fechaHora);
		evaluacion.setFechaAuditoria(fechaHora);
		evaluacion.setFicha(ficha);
		evaluacion.setRevision(revision);
		evaluacion.setIdEvaluacionSecundario(numeroEvaluacion);
		evaluacion.setIdUsuario(idUsuario);
		evaluacion.setFichaEvaluador(fichaEvaluador);
		evaluacion.setPeso(0);
		evaluacion.setResultado(0);
		evaluacion.setResultadoObjetivos(0);
		evaluacion.setResultadoGeneral(0);
		evaluacion.setCargo(cargo);
		evaluacion.setUnidadOrganizativa(unidadOrganizativa);
		evaluacion.setHoraAuditoria(horaAuditoria);
		Bitacora bitacora = new Bitacora();

		bitacora.setEvaluacion(evaluacion);
		bitacora.setIdUsuario(u);
		bitacora.setFechaAuditoria(fechaHora);
		bitacora.setHoraAuditoria(horaAuditoria);
		bitacora.setEstadoEvaluacion("EN EDICION");
		System.out.println(bitacora.getEvaluacion().getIdEvaluacion());
		System.out.println(evaluacion + " " + u + " " + fechaHora
				+ horaAuditoria);

		servicioEvaluacion.guardar(evaluacion);
		String item = ficha;
		servicioBitacora.guardar(bitacora);
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("modo", "AGREGAR");
		map.put("idEva", idEva);
		map.put("listbox", lbxEvaluacion);
		map.put("ficha", item);
		System.out.println("va" + idEva);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		winEvaluacionEmpleado = (Window) Executions.createComponents(
				"/vistas/transacciones/VAgregarEvaluacion.zul", null, map);
		winEvaluacionEmpleado.doModal();
		
	}
		else{
			msj.mensajeError(Mensaje.noSePuedeCrear);
		}
	}
	@Listen("onClick = #btnEliminar")
	public void eliminar() {
		
		if (lbxEvaluacion.getItemCount() != 0) {

			Listitem listItem = lbxEvaluacion.getSelectedItem();
			if (listItem != null) {

				Evaluacion evaluacionE = (Evaluacion) listItem.getValue();
				eva = evaluacionE;
				Integer id = evaluacionE.getIdEvaluacion();
				idEva = id;
				Messagebox.show(Mensaje.deseaEliminar, "Alerta", Messagebox.OK
						| Messagebox.CANCEL, Messagebox.QUESTION,
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
										servicioUtilidad.eliminarConductaPorEvaluacion(idEva);
										servicioUtilidad.eliminarCompetenciaPorEvaluacion(idEva);
										servicioUtilidad.eliminarCapacitacionPorEvaluacion(idEva);
										servicioEvaluacion.eliminarUno(idEva);
									
										msj.mensajeInformacion(Mensaje.eliminado);
										lbxEvaluacion.getItems().clear();
										evaluacion = servicioEvaluacion
												.buscar(fichaE);
										lbxEvaluacion
												.setModel(new ListModelList<Evaluacion>(
														evaluacion));
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
	}
	
	
	
	
	@Listen("onClick = #btnCopiar")
	public void copiar() {
		if (bandera.equals("false")){
		if (lbxEvaluacion.getItemCount() != 0) {

			Listitem listItem = lbxEvaluacion.getSelectedItem();
			if (listItem != null) {

				Evaluacion evaluacionE = (Evaluacion) listItem.getValue();
				eva = evaluacionE;
				Integer id = evaluacionE.getIdEvaluacion();
				idEva = id;
				Messagebox.show(Mensaje.deseaCopiar, "Alerta", Messagebox.YES
						| Messagebox.NO, Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener<Event>() {
							public void onEvent(Event evt)
									throws InterruptedException {

								if (evt.getName().equals("onYes")) {					
									revision = servicioRevision.buscarPorEstado("ACTIVO");
									Authentication auth = SecurityContextHolder.getContext()
											.getAuthentication();
									Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
									String ficha = u.getCedula();
									Integer idUsuario = u.getIdUsuario();
									Integer numeroEvaluacion = servicioEvaluacion.buscarIdSecundario(eva.getFicha()) + 1;
									Integer numeroEvaluacionPrimaria = servicioEvaluacion.buscarId() + 1;
									Empleado empleado = servicioEmpleado.buscarPorFicha(ficha);
									String fichaEvaluador = empleado.getFichaSupervisor();
									Cargo cargo = empleado.getCargo();
									UnidadOrganizativa unidadOrganizativa = empleado.getUnidadOrganizativa();
									
									
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
									System.out.println(bitacora.getEvaluacion().getIdEvaluacion());
									System.out.println(evaluacion + " " + u + " " + fechaHora
											+ horaAuditoria);

									servicioEvaluacion.guardar(eva);
									servicioBitacora.guardar(bitacora);
									
									Integer idEvaluacionObjetivoOriginal=0;
									
									List<EvaluacionObjetivo> listaEvaluacionObjetivo = servicioEvaluacionObjetivo
											.buscarObjetivosEvaluar(idEva);
									for (int i = 0; i < listaEvaluacionObjetivo
											.size(); i++) {
										
										EvaluacionObjetivo evaluacionObjetivo=listaEvaluacionObjetivo
												.get(i);
										idEvaluacionObjetivoOriginal=evaluacionObjetivo.getIdObjetivo();
										evaluacionObjetivo.setIdObjetivo(0);
										evaluacionObjetivo.setIdEvaluacion(numeroEvaluacionPrimaria);
										servicioEvaluacionObjetivo.guardar(evaluacionObjetivo);
										
										List<EvaluacionObjetivo> listaEvaluacionObjetivoGuardado = servicioEvaluacionObjetivo
												.buscarObjetivosEvaluar(numeroEvaluacionPrimaria);
										
										Integer idObjetivoGuardado=0;
										for (int x = 0; x < listaEvaluacionObjetivoGuardado
												.size(); x++) {
											
											EvaluacionObjetivo evaluacionObjetivoGuardardo=listaEvaluacionObjetivoGuardado
													.get(x);
											idObjetivoGuardado= evaluacionObjetivoGuardardo.getIdObjetivo();
											
										}
										
										List<EvaluacionIndicador> listaEvaluacionIndicador = servicioEvaluacionIndicador
												.buscarIndicadores(idEvaluacionObjetivoOriginal);
										for (int x = 0; x < listaEvaluacionIndicador
												.size(); x++) {
											
											EvaluacionIndicador evaluacionIndicador=listaEvaluacionIndicador
													.get(x);
											evaluacionIndicador.setIdIndicador(0);
											evaluacionIndicador.setIdObjetivo(idObjetivoGuardado);
											servicioEvaluacionIndicador.guardar(evaluacionIndicador);
										}
									
									}
									
									
									Evaluacion evaluacionAuxiliar = servicioEvaluacion.buscarEvaluacion(idEva);
									List<EvaluacionCompetencia> listaEvaluacionCompetencias = servicioEvaluacionCompetencia.buscar(evaluacionAuxiliar);
									
									for (int i = 0; i < listaEvaluacionCompetencias
											.size(); i++) {
										
										EvaluacionCompetencia evaluacionCompetencia=listaEvaluacionCompetencias
												.get(i);
										evaluacionCompetencia.setEvaluacion(eva);
										servicioEvaluacionCompetencia.guardar(evaluacionCompetencia);

									}
											
									
									List<EvaluacionConducta> listaEvaluacionConductas = servicioEvaluacionConducta.buscar(evaluacionAuxiliar);
									
									for (int i = 0; i < listaEvaluacionConductas
											.size(); i++) {
										
										EvaluacionConducta evaluacionConducta=listaEvaluacionConductas
												.get(i);
										evaluacionConducta.setEvaluacion(eva);
										servicioEvaluacionConducta.guardar(evaluacionConducta);

									}
									
									
									List<EvaluacionCapacitacion> listaEvaluacionCapacitacion = servicioEvaluacionCapacitacion.buscarPorEvaluacion(idEva);
									
									for (int i = 0; i < listaEvaluacionCapacitacion
											.size(); i++) {
										
										EvaluacionCapacitacion evaluacionCapacitacion=listaEvaluacionCapacitacion
												.get(i);
										evaluacionCapacitacion.setIdEvaluacion(numeroEvaluacionPrimaria);
										servicioEvaluacionCapacitacion.guardar(evaluacionCapacitacion);

									}
									
									
									lbxEvaluacion.getItems().clear();
									evaluacion = servicioEvaluacion
											.buscar(fichaE);
									lbxEvaluacion
											.setModel(new ListModelList<Evaluacion>(
													evaluacion));
									lbxEvaluacion.renderAll();
									for (int j = 0; j < lbxEvaluacion.getItems().size(); j++) {
										Listitem listItem = lbxEvaluacion.getItemAtIndex(j);
										List<Listitem> listItem2 = lbxEvaluacion.getItems();
										Evaluacion eva = listItem2.get(j).getValue();
										String fichaS = eva.getFichaEvaluador();
										empleado = servicioEmpleado.buscarPorFicha(fichaS);
										String nombre = empleado.getNombre();
										((Label) ((listItem.getChildren().get(5))).getFirstChild())
												.setValue(nombre);
									}
											


								}
							}
						});
			} else
				msj.mensajeAlerta(Mensaje.noSeleccionoRegistro);

		}
		}
		else {
			msj.mensajeError(Mensaje.noSePuedeCopiar);
		}
	}
	

	@Listen("onDoubleClick = #lbxEvaluacion")
	public void mostrarEvaluacion() {

		if (lbxEvaluacion.getItemCount() != 0) {

			Listitem listItem = lbxEvaluacion.getSelectedItem();
			if (listItem != null) {
				
				Evaluacion evaluacion = (Evaluacion) listItem.getValue();
				
				Usuario usuario =servicioUsuario.buscarId(evaluacion.getIdUsuario());
				String fichaUsuario = usuario.getFicha();

				if (fichaUsuario.compareTo("0")==0)
				{
					Messagebox
					.show("La Evaluación no se puede visualizar ya que es solo un registro historico ( Valoracion Final )",
							"Alerta",
							Messagebox.OK,
							Messagebox.EXCLAMATION);
				}
				else
				{				
					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("modo", "EDITAR");
					map.put("id", evaluacion.getIdEvaluacion());
					map.put("titulo", evaluacion.getFicha());
					map.put("listbox", lbxEvaluacion);
					map.put("lista", evaluacion);
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);
					
					
					winEvaluacionEmpleado = (Window) Executions.createComponents(
							"/vistas/transacciones/VAgregarEvaluacion.zul", null,
							map);
					winEvaluacionEmpleado.doModal();
					//winListaPersonal.onClose();
					
				}
				
				
			}

		}

	}

	public void actualizame(Listbox listbox, SUsuario servicio, SEvaluacion servicioE, SEmpleado servicioEm) {
		lbxEvaluacion = listbox;
		servicioUsuario = servicio;
		servicioEvaluacion = servicioE;
		servicioEmpleado = servicioEm;
		fichaE = new String();
		evaluacion = new ArrayList<Evaluacion>();
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		String ficha = u.getCedula();
		fichaE = ficha;
		evaluacion = servicioEvaluacion.buscar(ficha);
		lbxEvaluacion.setModel(new ListModelList<Evaluacion>(evaluacion));
		lbxEvaluacion.renderAll();
		for (int j = 0; j < lbxEvaluacion.getItems().size(); j++) {
			Listitem listItem = lbxEvaluacion.getItemAtIndex(j);
			List<Listitem> listItem2 = lbxEvaluacion.getItems();
			Evaluacion eva = listItem2.get(j).getValue();
			String fichaS = eva.getFichaEvaluador();
			Empleado empleado = servicioEmpleado.buscarPorFicha(fichaS);
			Usuario usuario =servicioUsuario.buscarId(eva.getIdUsuario());
			String fichaUsuario = usuario.getFicha();
			String nombre = usuario.getNombre().concat(" ").concat(usuario.getApellido());
			
			((Label) ((listItem.getChildren().get(4))).getFirstChild())
			.setValue(fichaUsuario);
			
			((Label) ((listItem.getChildren().get(5))).getFirstChild())
			.setValue(nombre);
	}
	}
}