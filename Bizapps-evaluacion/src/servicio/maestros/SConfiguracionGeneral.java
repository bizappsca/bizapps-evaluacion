package servicio.maestros;


import interfacedao.maestros.IConfiguracionGeneralDAO;

import modelo.maestros.ConfiguracionGeneral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SConfiguracionGeneral")
public class SConfiguracionGeneral {

	@Autowired
	private IConfiguracionGeneralDAO configuracionGeneralDAO;


	/* Servicio que permite buscar un area de acuerdo al nombre */
	public ConfiguracionGeneral buscar(String bandera) {
		ConfiguracionGeneral configuracionGeneral;
		configuracionGeneral = configuracionGeneralDAO.findByBandera(bandera);
		return configuracionGeneral;
	}
}
