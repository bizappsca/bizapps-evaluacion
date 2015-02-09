package servicio.maestros;


import java.util.ArrayList;
import java.util.List;

import interfacedao.maestros.IConfiguracionGeneralDAO;
import interfacedao.maestros.IErrorInterfazDAO;

import modelo.maestros.Actividad;
import modelo.maestros.Area;
import modelo.maestros.Clase;
import modelo.maestros.ConfiguracionGeneral;
import modelo.maestros.ErrorInterfaz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SResultadoInterfaz")
public class SResultadoInterfaz {

	@Autowired
	private IErrorInterfazDAO errorInterfazDAO;


	/* Servicio que permite buscar un area de acuerdo al nombre */
	public List<ErrorInterfaz> buscar() {
		return errorInterfazDAO.buscar();
	}
	
	
}
