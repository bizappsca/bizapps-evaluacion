package servicio.maestros;

import interfacedao.maestros.ICursoDAO;
import interfacedao.maestros.IAreaDAO;
import interfacedao.maestros.INombreCursoDAO;

import java.util.List;

import modelo.maestros.Area;
import modelo.maestros.Curso;
import modelo.maestros.Empleado;
import modelo.maestros.NombreCurso;
import modelo.seguridad.Grupo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SNombreCurso")
public class SNombreCurso {

	@Autowired
	private INombreCursoDAO nombreCursoDAO;
	

	/* Servicio que permite guardar los datos de un curso */
	public void guardar(NombreCurso curso) {
		nombreCursoDAO.save(curso);
	}

	/* Servicio que permite buscar un curso de acuerdo al id */
	public NombreCurso buscarCurso(int id) {
		return nombreCursoDAO.findOne(id);
	}

	/* Servicio que permite buscar un curso de acuerdo al nombre */
	public NombreCurso buscarPorNombre(String nombre) {
		NombreCurso curso;
		curso = nombreCursoDAO.findByNombreAllIgnoreCase(nombre);
		return curso;
	}
	
	/* Servicio que permite buscar un curso de acuerdo al area y al nombre */
	public NombreCurso buscarPorAreaYNombre(Area area, String nombre) {
		NombreCurso curso;
		curso = nombreCursoDAO.findByAreaAndNombreAllIgnoreCase(area,nombre);
		return curso;
	}

	/* Servicio que permite buscar todos las cursos */
	public List<NombreCurso> buscarTodos() {
		return nombreCursoDAO.findAll();
	}

	/* Servicio que permite eliminar una curso */
	public void eliminarUnCurso(int id) {
		nombreCursoDAO.delete(id);
	}

	/* Servicio que permite eliminar varios cursos */
	public void eliminarVariosCursos(List<NombreCurso> eliminar) {
		nombreCursoDAO.delete(eliminar);
	}
	
	//Buscar ultimo curso registrado
	public NombreCurso buscarUltimoCurso() {
		NombreCurso ultimoCurso;
		ultimoCurso = nombreCursoDAO.findOne(nombreCursoDAO.ultimoCursoRegistrado());
		return ultimoCurso;
	}
	

}

