package interfacedao.transacciones;

import java.util.List;
import java.util.Map;

import modelo.beans.BeanCapacitacionRequerida;

public interface IUtilidadDAO {
	
	public void eliminarConductaPorCompetencia(Integer eva, Integer com);
	public void eliminarConductaPorEvaluacion(Integer eva);
	public String obtenerValoracionFinal(double resultado);
	public String obtenerValoracionFinalSimple(double resultado);
	public void eliminarCompetenciaPorEvaluacion(Integer eva);
	public void eliminarCapacitacionPorEvaluacion(Integer eva);
	public List<BeanCapacitacionRequerida> getListaCapacitacionRequerida(Integer idPeriodo);

}
