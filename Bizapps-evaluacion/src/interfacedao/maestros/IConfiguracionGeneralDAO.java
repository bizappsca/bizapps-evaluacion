package interfacedao.maestros;
import modelo.maestros.ConfiguracionGeneral;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IConfiguracionGeneralDAO extends JpaRepository<ConfiguracionGeneral, Integer> {

	ConfiguracionGeneral findByBandera(String bandera);
	
}
