package servicio.maestros;

import interfacedao.maestros.IAreaDAO;

import java.util.List;

import modelo.maestros.Area;
import modelo.maestros.Empleado;
import modelo.maestros.TipoFormacion;
import modelo.seguridad.Grupo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SArea")
public class SArea {

	@Autowired
	private IAreaDAO areaDAO;

	/* Servicio que permite guardar los datos de un area */
	public void guardar(Area area) {
		areaDAO.save(area);
	}

	/* Servicio que permite buscar un area de acuerdo al id */
	public Area buscarArea(int id) {
		return areaDAO.findOne(id);
	}

	/* Servicio que permite buscar un area de acuerdo al nombre */
	public Area buscarPorNombre(String descripcion) {
		Area area;
		area = areaDAO.findByDescripcion(descripcion);
		return area;
	}

	/* Servicio que permite buscar todos las areas */
	public List<Area> buscarTodas() {
		return areaDAO.findAll();
	}

	/* Servicio que permite eliminar un area */
	public void eliminarUnArea(int id) {
		areaDAO.delete(id);
	}

	/* Servicio que permite eliminar varias areas */
	public void eliminarVariasAreas(List<Area> eliminar) {
		areaDAO.delete(eliminar);
	}

	/*
	 * Servicio que permite filtrar las areas de una lista de acuerdo a la
	 * descripcion
	 */
	public List<Area> filtroNombre(String valor) {
		return areaDAO.findByDescripcionStartingWithAllIgnoreCase(valor);
	}

	/*
	 * Servicio que permite filtrar las areas de una lista de acuerdo al tipo de
	 * formacion
	 */
	public List<Area> filtroTipoFormacion(String valor) {
		return areaDAO.findByTipoFormacionDescripcionStartingWithAllIgnoreCase(valor);
	}

	/*
	 * Servicio que permite filtrar las areas de una lista de acuerdo al id
	 */
	public List<Area> filtroId(String valor) {
		return areaDAO.findByIdStartingWithAllIgnoreCase(valor);
	}

	/* Servicio que permite buscar una lista de areas de acuerdo al nombre */
	public List<Area> buscarPorNombres(String descripcion) {
		List<Area> area;
		area = areaDAO.findByDescripcionAllIgnoreCase(descripcion);
		return area;
	}
	
	/* Servicio que permite buscar un area de acuerdo al nombre */
	public Area buscarPorNombresTipoFormacion(String descripcion,String tipoFormacion) {
		Area area;
		area = areaDAO.findByDescripcionAndTipoFormacionDescripcion(descripcion, tipoFormacion);
		return area;
	}
	

	public List<Area> buscarPorFormacion(int tipo) {
		// TODO Auto-generated method stub
		return areaDAO.findByTipoFormacionId(tipo);
	}


}
