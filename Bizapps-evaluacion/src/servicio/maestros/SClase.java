package servicio.maestros;

import interfacedao.maestros.IClaseDAO;
import interfacedao.maestros.IAreaDAO;

import java.util.List;

import modelo.maestros.Clase;
import modelo.maestros.Empleado;
import modelo.seguridad.Grupo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SClase")
public class SClase {

	@Autowired
	private IClaseDAO claseDAO;

	/* Servicio que permite guardar los datos de una clase */
	public void guardar(Clase clase) {
		claseDAO.save(clase);
	}

	/* Servicio que permite buscar una clase de acuerdo al id */
	public Clase buscarClase(int id) {
		return claseDAO.findOne(id);
	}

	/* Servicio que permite buscar todos las clases */
	public List<Clase> buscarTodas() {
		return claseDAO.findAll();
	}

	/* Servicio que permite eliminar una clase */
	public void eliminarUnaClase(int id) {
		claseDAO.delete(id);
	}

	/* Servicio que permite eliminar varias clases */
	public void eliminarVariasClases(List<Clase> eliminar) {
		claseDAO.delete(eliminar);
	}

	/*
	 * Servicio que permite filtrar las clases de una lista de acuerdo al curso
	 */
	public List<Clase> filtroCurso(String valor) {
		return claseDAO.findByCursoNombreStartingWithAllIgnoreCase(valor);
	}

	/*
	 * Servicio que permite filtrar las clases de una lista de acuerdo al
	 * contenido o tema
	 */
	public List<Clase> filtroContenido(String valor) {
		return claseDAO.findByContenidoStartingWithAllIgnoreCase(valor);
	}

	/*
	 * Servicio que permite filtrar las clases de una lista de acuerdo al
	 * objetivo
	 */
	public List<Clase> filtroObjetivo(String valor) {
		return claseDAO.findByObjetivoStartingWithAllIgnoreCase(valor);
	}
	
	
	/*
	 * Servicio que permite filtrar las clases de una lista de acuerdo a la
	 * entidad didactica
	 */
	public List<Clase> filtroEntidadDidactica(String valor) {
		return claseDAO.findByEntidadDidacticaStartingWithAllIgnoreCase(valor);
	}
	
	
	/*
	 * Servicio que permite filtrar las clases de una lista de acuerdo a la
	 * fecha
	 */
	public List<Clase> filtroFecha(String valor) {
		return claseDAO.findByFechaStartingWithAllIgnoreCase(valor);
	}
	
	
	/*
	 * Servicio que permite filtrar las clases de una lista de acuerdo a la
	 * duracion
	 */
	public List<Clase> filtroDuracion(String valor) {
		return claseDAO.findByDuracionStartingWithAllIgnoreCase(valor);
	}
	
	
	/*
	 * Servicio que permite filtrar las clases de una lista de acuerdo a la
	 * duracion
	 */
	public List<Clase> filtroLugar(String valor) {
		return claseDAO.findByLugarStartingWithAllIgnoreCase(valor);
	}
	
	/*
	 * Servicio que permite filtrar las clases de una lista de acuerdo al
	 * tipo de entrenamiento
	 */
	public List<Clase> filtroTipoEntrenamiento(String valor) {
		return claseDAO.findByTipoEntrenamientoStartingWithAllIgnoreCase(valor);
	}
	
	
	/*
	 * Servicio que permite filtrar las clases de una lista de acuerdo a la
	 * modalidad
	 */
	public List<Clase> filtroModalidad(String valor) {
		return claseDAO.findByModalidadStartingWithAllIgnoreCase(valor);
	}
	
	
	/* Servicio que permite buscar una clase de acuerdo al nombre */
	public List<Clase> buscarPorContenidos(String contenido) {
		List<Clase> clases;
		clases = claseDAO.findByContenidoAllIgnoreCase(contenido);
		return clases;
	}
	
	
}
