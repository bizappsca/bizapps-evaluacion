package servicio.maestros;


import interfacedao.maestros.IConfiguracionGeneralDAO;

import java.util.List;

import modelo.maestros.ConfiguracionGeneral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SConfiguracionGeneral")
public class SConfiguracionGeneral {

	@Autowired
	private IConfiguracionGeneralDAO configuracionGeneralDAO;


	/* Servicio que permite buscar un area de acuerdo al nombre */
	public List<ConfiguracionGeneral> buscar() {
		return configuracionGeneralDAO.findAll();
	}
	
	public void guardar(ConfiguracionGeneral configuracionGeneral) {
		configuracionGeneralDAO.save(configuracionGeneral);
	}
	
	public ConfiguracionGeneral buscarBandera(int id) {
		return configuracionGeneralDAO.findById(id);
	}
}
