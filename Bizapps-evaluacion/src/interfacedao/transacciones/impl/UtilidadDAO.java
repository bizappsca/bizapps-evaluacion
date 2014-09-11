package interfacedao.transacciones.impl;


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import modelo.maestros.ConductaCompetencia;
import modelo.maestros.Evaluacion;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;

import interfacedao.transacciones.IUtilidadDAO;

@Repository
public class UtilidadDAO implements IUtilidadDAO {
	
	protected EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public UtilidadDAO() {
		super();
	}

	@Transactional
	public void eliminarConductaPorCompetencia(Integer eva, Integer com) {
		// TODO Auto-generated method stub

		String sentencia = "";
		sentencia = " delete from EvaluacionConducta as ec where ec.evaluacion.idEvaluacion = "+ eva +" and ec.competencia.id= "+ com +"";	
		int resultado=getEntityManager().createQuery(sentencia).executeUpdate();
		
	}
	
	
	@Transactional
	public String obtenerValoracionFinal(double resultado) {
		// TODO Auto-generated method stub

		String sentencia = "";
		String aux="";
		
		sentencia = " SELECT     nombre , descripcion  FROM         valoracion WHERE     ("+ resultado +" BETWEEN rango_inferior AND rango_superior)";
		
		
		Query qSentencia = getEntityManager().createNativeQuery(
				sentencia );
		
		@SuppressWarnings("unchecked")
		List<String[]> results = qSentencia.getResultList();

		
		for (Object[] obj : results) {
			aux =((String) obj[0]).concat(" - ").concat((String) obj[1]) ; 
		}

	
		return aux;
		
	}

	@Transactional
	public void eliminarConductaPorEvaluacion(Integer eva) {
		// TODO Auto-generated method stub
		String sentencia = "";
		sentencia = " delete from EvaluacionConducta as ec where ec.evaluacion.idEvaluacion = "+ eva +"";	
		int resultado=getEntityManager().createQuery(sentencia).executeUpdate();
	}
	
	
	@Transactional
	public void eliminarCompetenciaPorEvaluacion(Integer eva) {
		// TODO Auto-generated method stub
		String sentencia = "";
		sentencia = " delete from EvaluacionCompetencia as ec where ec.evaluacion.idEvaluacion = "+ eva +"";	
		int resultado=getEntityManager().createQuery(sentencia).executeUpdate();
	}

	@Transactional
	public String obtenerValoracionFinalSimple(double resultado) {
		// TODO Auto-generated method stub
		String sentencia = "";
		String aux="";
		
		sentencia = " SELECT     nombre , descripcion  FROM         valoracion WHERE     ("+ resultado +" BETWEEN rango_inferior AND rango_superior)";
		
		
		Query qSentencia = getEntityManager().createNativeQuery(
				sentencia );
		
		@SuppressWarnings("unchecked")
		List<String[]> results = qSentencia.getResultList();

		
		for (Object[] obj : results) {
			aux =((String) obj[0]) ; 
		}

	
		return aux;
		
	}

	@Transactional
	public void eliminarCapacitacionPorEvaluacion(Integer eva) {
		// TODO Auto-generated method stub
		
		String sentencia = "";
		sentencia = " delete from EvaluacionCapacitacion as ec where ec.idEvaluacion = "+ eva +"";	
		int resultado=getEntityManager().createQuery(sentencia).executeUpdate();
		
	}
	

}
