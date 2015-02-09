package controlador.transacciones;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Bitacora;
import modelo.maestros.Cargo;
import modelo.maestros.ConfiguracionGeneral;
import modelo.maestros.Empleado;
import modelo.maestros.ErrorInterfaz;
import modelo.maestros.Evaluacion;
import modelo.maestros.EvaluacionCapacitacion;
import modelo.maestros.EvaluacionCompetencia;
import modelo.maestros.EvaluacionConducta;
import modelo.maestros.EvaluacionIndicador;
import modelo.maestros.EvaluacionObjetivo;
import modelo.maestros.Periodo;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

import servicio.maestros.SEmpleado;
import servicio.maestros.SRevision;
import servicio.seguridad.SUsuario;
import servicio.transacciones.SEvaluacion;

import componentes.Mensaje;

import controlador.maestros.CGenerico;

public class CResultadoInterfaz extends CGenerico {

	private static final long serialVersionUID = -5393608637902961029L;
	Mensaje msj = new Mensaje();

	
	@Wire
	private Window winListaPersonal;
	@Wire
	private Listbox lbxInterfaz;
	@Wire
	private Window winEvaluacionEmpleado;
	@Wire
	private Groupbox gpxListaPersonal;
	@Wire
	private Label fecha;
	
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
		
		List<ErrorInterfaz> lista = servicioErrorInterfaz.buscar();
		
		lbxInterfaz.setModel(new ListModelList<ErrorInterfaz>(lista));
		lbxInterfaz.renderAll();
		
		if (lista.size()>0)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			ErrorInterfaz error= lista.get(0);
			String dateValue = sdf.format(error.getFecha());
			
			fecha.setValue(dateValue + " " + error.getHora());
		}
		
		
		

	}
	
	@Listen("onClick = #btnSalir")
	public void salir() {
		cerrarVentana(winListaPersonal, titulo, tabs);
	}

	
}