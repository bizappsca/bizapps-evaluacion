package interfacedao.maestros;

import java.util.List;

import modelo.maestros.Curso;
import modelo.maestros.NombreCurso;
import modelo.maestros.Periodo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICursoDAO extends JpaRepository<Curso, Integer> {


	public List<Curso> findByDuracionStartingWithAllIgnoreCase(String valor);

	public List<Curso> findByEstadoStartingWithAllIgnoreCase(String valor);

	public List<Curso> findByEstadoOrderByNombreCursoAreaAsc(String string);

	public List<Curso> findByPeriodo(Periodo periodo);

	public List<Curso> findByNombreCursoNombreStartingWithAllIgnoreCase(String valor);

	public List<Curso> findByNombreCursoNombreAllIgnoreCase(String nombre);

	Curso findByNombreCursoNombre(String nombre);

	public Curso findByNombreCursoAndPeriodo(NombreCurso curso, Periodo periodo);
	
	/* Busca ultimo curso registrado */
	@Query("select max(c.id) from Curso c")
	public int ultimoCursoRegistrado();


}