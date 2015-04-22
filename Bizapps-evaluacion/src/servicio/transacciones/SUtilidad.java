package servicio.transacciones;

import java.util.List;
import java.util.Map;

import interfacedao.reportes.IReporteDAO;
import interfacedao.transacciones.IUtilidadDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.chart.model.CategoryModel;
import modelo.beans.BeanCapacitacionRequerida;

@Service
public class SUtilidad {

	@Autowired
	private IUtilidadDAO servicioUtilidadDAO;
	
	@SuppressWarnings("unchecked")
	public void eliminarConductaPorCompetencia(Integer eva, Integer com)  {

		servicioUtilidadDAO.eliminarConductaPorCompetencia(eva, com);
		
	}
	
	@SuppressWarnings("unchecked")
	public String obtenerValoracionFinal(double resultado) {

		return servicioUtilidadDAO.obtenerValoracionFinal(resultado);
		
	}
	
	@SuppressWarnings("unchecked")
	public String obtenerValoracionFinalSimple(double resultado) {
		return servicioUtilidadDAO.obtenerValoracionFinalSimple(resultado);
	}
	
	@SuppressWarnings("unchecked")
	public void eliminarConductaPorEvaluacion(Integer eva)  {
		servicioUtilidadDAO.eliminarConductaPorEvaluacion(eva);
	}
	
	@SuppressWarnings("unchecked")
	public void eliminarCompetenciaPorEvaluacion(Integer eva)  {
		servicioUtilidadDAO.eliminarCompetenciaPorEvaluacion(eva);
	}
	
	@SuppressWarnings("unchecked")
	public void eliminarCapacitacionPorEvaluacion(Integer eva)  {
		servicioUtilidadDAO.eliminarCapacitacionPorEvaluacion(eva);
	}
	
	@SuppressWarnings("unchecked")
	public List<BeanCapacitacionRequerida> getListaCapacitacionRequerida(Integer idPeriodo)  {
		return servicioUtilidadDAO.getListaCapacitacionRequerida(idPeriodo);
	}
	
	
	
}
