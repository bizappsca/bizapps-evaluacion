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

	Mensaje msj = new Mensaje();
	Botonera botonera;
	
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
		botonera = new Botonera() {

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				

			}

			@Override
			public void guardar() {
				// TODO Auto-generated method stub

				boolean guardar = true;
				guardar = validar();
				if (guardar) {
		
						String bandera = cmbConfiguracion
								.getValue();
						ConfiguracionGeneral configuracionGeneral = servicioConfiguracionGeneral.buscarBandera(1);
						configuracionGeneral.setBandera(bandera);
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
				cerrarVentana(wdwVConfiguracion, "Configuracion General",tabs);
			}

			@Override
			public void eliminar() {

			}

		};
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botoneraConfiguracionGeneral.appendChild(botonera);

	}

	public void limpiarCampos() {
		cmbConfiguracion.setValue("");

	}


	public boolean camposLLenos() {
		if (cmbConfiguracion.getText().compareTo("") == 0) {
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

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(3).setVisible(!bol);

	}


}
