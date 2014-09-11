package interfacedao.transacciones;

public interface IUtilidadDAO {
	
	public void eliminarConductaPorCompetencia(Integer eva, Integer com);
	public void eliminarConductaPorEvaluacion(Integer eva);
	public String obtenerValoracionFinal(double resultado);
	public String obtenerValoracionFinalSimple(double resultado);
	public void eliminarCompetenciaPorEvaluacion(Integer eva);
	public void eliminarCapacitacionPorEvaluacion(Integer eva);

}
