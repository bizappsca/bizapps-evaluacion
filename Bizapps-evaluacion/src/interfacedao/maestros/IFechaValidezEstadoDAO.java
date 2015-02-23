package interfacedao.maestros;


import java.sql.Timestamp;
import java.util.List;

import modelo.maestros.Actividad;
import modelo.maestros.FechaValidezEstado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IFechaValidezEstadoDAO extends JpaRepository<FechaValidezEstado, Integer> {

	public FechaValidezEstado findByEstado(String estado);
	public List<FechaValidezEstado> findAll();
	
	@Query("SELECT fve FROM FechaValidezEstado fve  WHERE ?1>= fve.fechaDesde  AND  ?1<= fve.fechaHasta  and estado = ?2 and ?3>=grado")
	public FechaValidezEstado estadoPermitido (Timestamp fechaActual, String estado, Integer grado );
	
	@Query("SELECT fve FROM FechaValidezEstado fve  WHERE ?1>= fve.fechaDesde  AND  ?1<= fve.fechaHasta")
	public FechaValidezEstado estadoActual (Timestamp fechaActual );

	

}