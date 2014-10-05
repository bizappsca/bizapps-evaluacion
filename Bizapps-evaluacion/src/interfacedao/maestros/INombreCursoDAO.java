package interfacedao.maestros;

import java.util.List;

import modelo.maestros.Area;
import modelo.maestros.Curso;
import modelo.maestros.NombreCurso;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface INombreCursoDAO extends JpaRepository<NombreCurso, Integer> {

	NombreCurso findByNombreAllIgnoreCase(String nombre);

	NombreCurso findByAreaAndNombreAllIgnoreCase(Area area, String nombre);
	
	/* Busca ultimo curso registrado */
	@Query("select max(c.id) from NombreCurso c")
	public int ultimoCursoRegistrado();



}