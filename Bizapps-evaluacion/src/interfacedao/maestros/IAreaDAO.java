package interfacedao.maestros;


import java.util.List;

import modelo.maestros.Area;
import modelo.maestros.NombreCurso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IAreaDAO extends JpaRepository<Area, Integer> {

	Area findByDescripcion(String descripcion);
	
	Area findByDescripcionAndTipoFormacionDescripcion(String descripcion,String tipoFormacion);
	
	

	public List<Area> findByDescripcionStartingWithAllIgnoreCase(String valor);

	public List<Area> findByIdStartingWithAllIgnoreCase(String valor);

	public List<Area> findByDescripcionAllIgnoreCase(String descripcion);

	public List<Area> findByTipoFormacionDescripcionStartingWithAllIgnoreCase(
			String valor);

	List<Area> findByTipoFormacionId(int tipo);



	
}
