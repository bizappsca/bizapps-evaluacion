package controlador.transacciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import modelo.seguridad.Arbol;
import modelo.seguridad.Usuario;
import modelos.Competencia;
import modelos.Empleado;
import modelos.Evaluacion;
import modelos.EvaluacionObjetivo;
import modelos.Perspectiva;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;
import sun.util.calendar.BaseCalendar.Date;

import arbol.MArbol;
import arbol.Nodos;

import controlador.maestros.CGenerico;
import componentes.Mensaje;
import componentes.Validador;

public class CObjetivos extends CGenerico {

	private static final long serialVersionUID = -5393608637902961029L;
	Mensaje msj = new Mensaje();

	@Wire
	private Textbox txtObjetivo;
	@Wire
	private Textbox txtCorresponsables;
	@Wire
	private Textbox txtPeso;
	@Wire
	private Textbox txtTotal;
	@Wire
	private Textbox txtResultados;
	@Wire
	private Button btnAgregar;
	@Wire
	private Button btnEliminar;
	@Wire
	private Button btnOk;
	@Wire
	private Button btnGuardar;
	@Wire
	private Listbox lbxEmpleado;
	@Wire
	private Listbox lbxObjetivos;
	@Wire
	private Listbox lbxObjetivosGuardados;
	@Wire
	private Label lblEvaluacion;
	@Wire
	private Label lblFechaCreacion;
	@Wire
	private Label lblRevision;
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
	private Combobox cmbPerspectiva;
	@Wire
	private Window window;
	@Wire
	private Groupbox gpxAgregar;
	@Wire
	private Groupbox gpxAgregados;
	
	List<EvaluacionObjetivo> objetivosG = new ArrayList<EvaluacionObjetivo>();
	ListModelList<Perspectiva> perspectiva;


	@Override
	public void inicializar() throws IOException {

		 List<Perspectiva> perspectiva = servicioPerspectiva.buscar();
		 cmbPerspectiva.setModel(new ListModelList<Perspectiva>(perspectiva));
		 
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		String ficha = u.getCedula();
		Integer numeroEvaluacion = servicioEvaluacion.buscar(ficha).size() + 1;
		lblEvaluacion.setValue(numeroEvaluacion.toString());
		lblFechaCreacion.setValue(fechaHora.toString());
		String nombreTrabajador = u.getNombre() + " " + u.getApellido();
		Empleado empleado = servicioEmpleado.buscarPorFicha(ficha);
		String cargo = empleado.getCargo().getDescripcion();
		String unidadOrganizativa = empleado.getUnidadOrganizativa()
				.getDescripcion();
		String gerenciaReporte = empleado.getUnidadOrganizativa().getGerencia()
				.getDescripcion();
		lblFicha.setValue(ficha);
		lblNombreTrabajador.setValue(nombreTrabajador);
		lblCargo.setValue(cargo);
		lblUnidadOrganizativa.setValue(unidadOrganizativa);
		lblGerencia.setValue(gerenciaReporte);
		gpxAgregar.setOpen(false);
		gpxAgregados.setOpen(false);
	}


	@Listen("onClick = #btnAgregar")
	public void AgregarObjetivo() {	
		gpxAgregar.setOpen(true);
	}
	
	@Listen("onClick = #btnOk")
	public void AgregarObjetivo2() {	
		 gpxAgregados.setOpen(true);
		 String perspectivaCombo= cmbPerspectiva.getSelectedItem().getContext();
		 System.out.println(perspectivaCombo);
		 Perspectiva perspectiva = servicioPerspectiva.buscarId(Integer.parseInt(perspectivaCombo));
		 String objetivo =txtObjetivo.getValue();
		 String corresponsables = txtCorresponsables.getValue();
		 Double peso = Double.parseDouble(txtPeso.getValue());
		 EvaluacionObjetivo objetivoLista = new EvaluacionObjetivo ();
		 Integer linea = objetivosG.size() + 1;
		 objetivoLista.setIdObjetivo(1);
		 objetivoLista.setDescripcionObjetivo(objetivo);
		 objetivoLista.setIdEvaluacion(1);
		 objetivoLista.setPerspectiva(perspectiva);	 
		 objetivoLista.setLinea(linea);
		 objetivoLista.setPeso(peso);
		 objetivoLista.setResultado(0);
		 objetivoLista.setTotalInd(0);
		 objetivoLista.setCorresponsables(corresponsables);
		 objetivosG.add(objetivoLista);
		 lbxObjetivosGuardados.setModel(new ListModelList<EvaluacionObjetivo>(objetivosG));
		 gpxAgregar.setOpen(false);
		 limpiar ();
	
	}
	
	public void limpiar() {
		 txtObjetivo.setValue("");
		 cmbPerspectiva.setValue(null);
		 txtCorresponsables.setValue("");
	}
	
	@Listen("onClick = #btnGuardar")
	public void Guardar() {	
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		String ficha = u.getCedula();
		Integer idUsuario = u.getIdUsuario();
		Integer evaluacion = Integer.parseInt(lblEvaluacion.getValue());
		Integer idEvaluacion = servicioEvaluacion.buscarId()+1; 
		Double peso = Double.parseDouble(txtPeso.getValue());
		Evaluacion evaluacionEmpleado = new Evaluacion();
		evaluacionEmpleado.setIdEvaluacion(idEvaluacion);
		evaluacionEmpleado.setEstadoEvaluacion("EN EDICION");
		evaluacionEmpleado.setFechaCreacion(fechaHora);
		evaluacionEmpleado.setFicha(ficha);
		evaluacionEmpleado.setIdEvaluacionSecundario(evaluacion);
		evaluacionEmpleado.setIdUsuario(idUsuario);
		evaluacionEmpleado.setPeso(peso);
		evaluacionEmpleado.setResultado(0);
		evaluacionEmpleado.setResultadoObjetivos(0);
		evaluacionEmpleado.setResultadoGeneral(0);
		evaluacionEmpleado.setResultadoFinal(0);

		
		if (objetivosG.size() == 0) {
			Messagebox.show("Debe agregar sus objetivos",
					"Advertencia", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
		else {
			EvaluacionObjetivo objetivo = new EvaluacionObjetivo ();
//			Integer revision = 1;
			objetivo.setIdEvaluacion(idEvaluacion);
			for (int j = 0; j < objetivosG.size(); j++) {
				Integer linea = objetivosG.get(j).getLinea();
				String corresponsables = objetivosG.get(j).getCorresponsables();
				String descripcionOb = objetivosG.get(j).getDescripcionObjetivo();
				Perspectiva perspectiva = objetivosG.get(j).getPerspectiva();
				Double peso1 = objetivosG.get(j).getPeso();
				Double resultado = objetivosG.get(j).getResultado();
				Double total = objetivosG.get(j).getTotalInd();
				objetivo.setCorresponsables(corresponsables);
				objetivo.setDescripcionObjetivo(descripcionOb);
				objetivo.setLinea(linea);
				objetivo.setPerspectiva(perspectiva);
				objetivo.setPeso(peso1);
				objetivo.setResultado(resultado);
				objetivo.setTotalInd(total);	
				servicioEvaluacionObjetivo.guardar(objetivo);
			}
			servicioEvaluacion.guardar(evaluacionEmpleado);
			Messagebox.show("Objetivos Guardados Exitosamente",
					"Informaci�n", Messagebox.OK,
					Messagebox.INFORMATION);
			
			limpiar ();
			objetivosG.clear();
		}
	}
}