package servicio.transacciones;



import interfacedao.transacciones.IBitacoraDAO;
import interfacedao.transacciones.IEvaluacionDAO;

import java.util.ArrayList;
import java.util.List;

import modelo.maestros.Empleado;
import modelo.maestros.Evaluacion;
import modelo.maestros.EvaluacionObjetivo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SEvaluacion")
public class SEvaluacion {

	@Autowired
	private IEvaluacionDAO evaluacionDAO;
	
	@Autowired
	private IBitacoraDAO bitacoraDAO;

	public List<Evaluacion> buscar(String ficha) {
		return evaluacionDAO.findByFichaOrderByRevisionIdDescIdEvaluacionSecundarioDesc(ficha);
	}
	
	public List<Evaluacion> buscarEvaluacionesActivas(String ficha, Integer revision)
	{
		return evaluacionDAO.buscarEvaluacionesActivas(ficha, revision);
	}
	
	public List<Evaluacion> buscarEvaluacionesInactivas(String ficha, Integer revision)
	{
		return evaluacionDAO.buscarEvaluacionesInactivas(ficha, revision);
	}
	
	public List<Evaluacion> buscarEstado(String ficha) {
		return evaluacionDAO.buscarEstado(ficha);
	}
	
	public List<Evaluacion> buscarRevision(String ficha, Integer revision, String estado, Integer id) {
		return evaluacionDAO.buscarRevision(ficha, revision, estado,id);
	}
	
	public List<Evaluacion> buscarPorEstado(String ficha, String estado) {
		return evaluacionDAO.findByFichaAndEstadoEvaluacion(ficha, estado);
	}
	
	/* Servicio que permite guardar los datos de la evaluacion de un trabajador*/
	public void guardar(Evaluacion evaluacion) {
		evaluacionDAO.save(evaluacion);
	}
	
	// Servicio que busca el maximo id
	public Integer buscarId() {
		return evaluacionDAO.buscar();
	}
	
	// Servicio que busca el maximo id Secundario	
		public Integer buscarIdSecundario(String ficha) {
			return evaluacionDAO.buscarIdSecundario(ficha);
		}
	
		// Servicio que busca el maximo id Secundario	
		public Long cantidadEvaluaciones(String ficha, Integer revision) {
			return evaluacionDAO.cantidadEvaluaciones(ficha,revision);
		}
		
		
	// Servicio que busca el id de una evaluacion
	public Evaluacion buscarIdEvaluacion(Integer id, String ficha) {
		return evaluacionDAO.findByIdEvaluacionSecundarioAndFicha(id, ficha);
	}
	
	public Evaluacion buscarEvaluacion (Integer idEvaluacion) {
		return evaluacionDAO.findByIdEvaluacion(idEvaluacion);
	}
	
	public List<Evaluacion> buscarEvaluacionCalibracion (String empresa, String nombreE, String fichaE, String gerencia, String valoracion) {
		return evaluacionDAO.buscarEvaluacionCalibracion(empresa, nombreE, fichaE, gerencia, valoracion);
	}
	
	public List<Evaluacion> buscarEvaluacionCalibracionEmpresa (String empresa) {
		return evaluacionDAO.buscarEvaluacionCalibracionEmpresa(empresa);
	}
	
	public List<Evaluacion> buscarEvaluacionCalibracionTrabajador (String nombreE) {
		return evaluacionDAO.buscarEvaluacionCalibracionTrabajador(nombreE);
	}
	
	public List<Evaluacion> buscarEvaluacionCalibracionEvaluador (String fichaE) {
		return evaluacionDAO.buscarEvaluacionCalibracionEvaluador(fichaE);
	}
	
	public List<Evaluacion> buscarEvaluacionCalibracionGerencia (String gerencia) {
		return evaluacionDAO.buscarEvaluacionCalibracionGerencia(gerencia);
	}
	
	public List<Evaluacion> buscarEvaluacionCalibracionValoracion (String valoracion) {
		return evaluacionDAO.buscarEvaluacionCalibracionValoracion(valoracion);
	}
	
	public List<Evaluacion> buscarEvaluacionCalibracionGrado (Integer grado) {
		return evaluacionDAO.buscarEvaluacionCalibracionGrado(grado);
	}
	
	public List<Evaluacion> buscarEvaluacionCalibracion (String empresa, String nombreE, String fichaE, String gerencia, String valoracion,Integer grado) {
		return evaluacionDAO.buscarEvaluacionCalibracion(empresa, nombreE, fichaE, gerencia, valoracion,grado);
	}
	
	public List<Evaluacion> buscarEvaluacionRevision (String empresa, String nombreE, String fichaE, String gerencia, String valoracion,Integer grado) {
		return evaluacionDAO.buscarEvaluacionRevision(empresa, nombreE, fichaE, gerencia, valoracion,grado);
	}
	
	
	public void eliminarUno(Integer id) {
		evaluacionDAO.delete(id);
	}

	public List<Evaluacion> buscarEvaluacionesRevision() {
		return evaluacionDAO.buscarEvaluacionesRevision();
	}
	
	public int actualizarEstado(String estado, Integer revision) {
		
		for( Evaluacion evaluacion : evaluacionDAO.buscarTodasEvaluacionesActivas(revision))
		{
			evaluacion.setEstadoEvaluacion(estado);
			evaluacionDAO.save(evaluacion);
		}
		
		return 1;
	}
	
	public List<Evaluacion> buscarPersonalConEvaluacion(String empresa) {
		return evaluacionDAO.buscarPersonalConEvaluacion(empresa);
	}
	
	public List<Evaluacion> buscarPersonalSinEvaluacion(String empresa) {
		
		List<Empleado> listaEmpleado=evaluacionDAO.buscarPersonalSinEvaluacion(empresa);
		List<Evaluacion> listaEvaluaciones=new ArrayList<Evaluacion>();
		for (Empleado empleado:listaEmpleado)
		{
			Evaluacion evaluacion = new Evaluacion();
			evaluacion.setFicha(empleado.getFicha());
			evaluacion.setFichaEvaluador(empleado.getFichaSupervisor());
			evaluacion.setEstadoEvaluacion("");
			evaluacion.setResultadoObjetivos(0);
			evaluacion.setResultadoCompetencias(0);
			evaluacion.setResultadoGeneral(0);
			evaluacion.setValoracion("");
			evaluacion.setResultadoFinal(0);
			listaEvaluaciones.add(evaluacion);
		}
		
		
		return listaEvaluaciones;
	}
	
	
	
	
}
