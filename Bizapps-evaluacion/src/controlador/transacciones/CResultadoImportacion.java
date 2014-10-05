package controlador.transacciones;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;

import controlador.maestros.CGenerico;


/**
 * Controlador que permite realizar las operaciones basicas (CRUD) sobre la
 * entidad Actividad
 */
@Controller
public class CResultadoImportacion extends CGenerico {

	private static final long serialVersionUID = -7144250834458342918L;
	private long id = 0;
	@Wire
	private Window wdwVResultadoImportacion;
	@Wire
	private Label lblEncabezado;
	@Wire
	private Label lblResultado;
	@Wire
	private Label lblLineasEvaluadas;
	@Wire
	private Label lblLineasValidas;
	@Wire
	private Label lblLineasInvalidas;
	
	int lineasEvaluadas;
	int lineasValidas;
	int lineasInvalidas;
	String archivo;
	
	

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub
		
		
		contenido = (Include) wdwVResultadoImportacion.getParent();
		Tabbox tabox = (Tabbox) wdwVResultadoImportacion.getParent().getParent()
				.getParent().getParent();
		tabBox = tabox;
		tab = (Tab) tabox.getTabs().getLastChild();
		HashMap<String, Object> mapa = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (mapa != null) {
			if (mapa.get("tabsGenerales") != null) {
				tabs = (List<Tab>) mapa.get("tabsGenerales");
				mapa.clear();
				mapa = null;
			}
		}
		
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		if (map != null) {
			if (map.get("id") != null) {
				lineasEvaluadas = (Integer) map.get("lineasEvaluadas");
				lineasValidas = (Integer) map.get("lineasValidas");
				lineasInvalidas = (Integer) map.get("lineasInvalidas");
				archivo = (String) map.get("archivo");
				map.clear();
				map = null;
			}
		}
		
		
		lblEncabezado.setValue("Resultado del Proceso de Validacion del Archivo:" + " " + archivo);
		
		if(lineasInvalidas == 0){
			
			lblResultado.setValue("Los registros han sido importados exitosamente!");
			
		}else{
			
			lblResultado.setValue("Los registros no fueron importados");
			
		}
		
		
		lblLineasEvaluadas.setValue("Cantidad de Lineas Evaluadas:" + " " + lineasEvaluadas);
		lblLineasValidas.setValue("Cantidad de Lineas Validas:" + " " + lineasValidas);
		lblLineasInvalidas.setValue("Cantidad de Lineas Invalidas:" + " " + lineasInvalidas);
		
		
	}


}


