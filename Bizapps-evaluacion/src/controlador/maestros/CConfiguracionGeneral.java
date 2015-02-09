package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Cargo;
import modelo.maestros.Clase;
import modelo.maestros.ConfiguracionGeneral;
import modelo.maestros.Curso;
import modelo.maestros.Empleado;

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

public class CConfiguracionGeneral extends CGenerico {

	@Wire
	private Window wdwVConfiguracion;
	@Wire
	private Div botoneraConfiguracionGeneral;
	@Wire
	private Combobox cmbConfiguracion;
	@Wire
	private Combobox cmbInterfaz;

	Mensaje msj = new Mensaje();
	Botonera botonera;
	
	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				titulo = (String) map.get("titulo");
				map.clear();
				map = null;
			}
		}
		List<ConfiguracionGeneral> conf = servicioConfiguracionGeneral.buscar();
		String valorConf = conf.get(0).getBandera();
		String valorInterfaz = conf.get(0).getInterfaz();
		
		if (valorConf.compareTo("true")==0)
		{
			valorConf="SI";
		}
		else
		{
			valorConf="NO";
		}
		
		cmbConfiguracion.setValue(valorConf);
		cmbInterfaz.setValue(valorInterfaz);
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				

			}

			@Override
			public void guardar() {
				// TODO Auto-generated method stub
				if (validar()) {
		
						String bandera = cmbConfiguracion
								.getValue();
						
						if (bandera.compareTo("SI")==0)
						{
							bandera="true";
						}
						else
						{

							bandera="false";
						}
						ConfiguracionGeneral configuracionGeneral = servicioConfiguracionGeneral.buscarBandera(1);
						configuracionGeneral.setBandera(bandera);
						configuracionGeneral.setInterfaz(cmbInterfaz.getValue());
						servicioConfiguracionGeneral.guardar(configuracionGeneral);
						msj.mensajeInformacion(Mensaje.guardado);
						limpiar();

					}
			}

			@Override
			public void limpiar() {
				// TODO Auto-generated method stub
				mostrarBotones(false);
				limpiarCampos();
			}

			@Override
			public void salir() {
				// TODO Auto-generated method stub
				cerrarVentana2(wdwVConfiguracion, titulo,tabs);
			}

			@Override
			public void eliminar() {

			}

			@Override
			public void buscar() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void annadir() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void ayuda() {
				// TODO Auto-generated method stub
				
			}

		};
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(5).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botoneraConfiguracionGeneral.appendChild(botonera);

	}

	public void limpiarCampos() {
		//cmbConfiguracion.setValue("");

	}


	public boolean camposLLenos() {
		if (cmbConfiguracion.getText().compareTo("") == 0) {
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

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(3).setVisible(!bol);
		botonera.getChildren().get(5).setVisible(!bol);
		botonera.getChildren().get(2).setVisible(bol);
		botonera.getChildren().get(4).setVisible(bol);
		botonera.getChildren().get(8).setVisible(false);
		

	}



}
