package interfacedao.maestros;
import java.util.List;

import modelo.maestros.ErrorInterfaz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IErrorInterfazDAO extends JpaRepository<ErrorInterfaz, Integer> {

	 @Query("select e from ErrorInterfaz e ")
		public List<ErrorInterfaz> buscar();
	 
	
}
