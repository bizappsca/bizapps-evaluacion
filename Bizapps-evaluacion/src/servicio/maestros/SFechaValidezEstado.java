package servicio.maestros;

import interfacedao.maestros.IActividadDAO;
import interfacedao.maestros.IAreaDAO;
import interfacedao.maestros.IFechaValidezEstadoDAO;

import java.sql.Timestamp;
import java.util.List;

import modelo.maestros.Actividad;
import modelo.maestros.FechaValidezEstado;
import modelo.seguridad.Grupo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SFechaValidezEstado")
public class SFechaValidezEstado {

	@Autowired
	private IFechaValidezEstadoDAO fechaValidezEstadoDAO;

	/* Servicio que permite guardar los datos de una estado */
	public void guardar(FechaValidezEstado fechaValidezEstado) {
		fechaValidezEstadoDAO.save(fechaValidezEstado);
	}

	/* Servicio que permite buscar un estado de acuerdo al id */
	public FechaValidezEstado buscarActividad(int id) {
		return fechaValidezEstadoDAO.findOne(id);
	}

	/* Servicio que permite buscar un estado de acuerdo al nombre */
	public FechaValidezEstado buscarPorNombre(String descripcion) {
		FechaValidezEstado fechaValidezEstado;
		fechaValidezEstado = fechaValidezEstadoDAO.findByEstado(descripcion);
		return fechaValidezEstado;
	}
	
	/* Servicio que permite buscar todos los aliados*/
	public List<FechaValidezEstado> buscarTodas() {
		return fechaValidezEstadoDAO.findAll();
	}
	
	/* Servicio que permite buscar todos los aliados*/
	public FechaValidezEstado estadoPermitido(Timestamp fechaHoy, String estado,Integer grado) {
		return fechaValidezEstadoDAO.estadoPermitido(fechaHoy, estado,grado);
	}
	
	


}
