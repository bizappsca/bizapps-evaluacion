package interfacedao.transacciones.impl;


import interfacedao.transacciones.IUtilidadDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import modelo.beans.BeanCapacitacionRequerida;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
	
	
	@Transactional
	public List<BeanCapacitacionRequerida> getListaCapacitacionRequerida(Integer idRevision) {
		// TODO Auto-generated method stub

		List<BeanCapacitacionRequerida> listaBeanCapacitacion = new ArrayList<BeanCapacitacionRequerida>();
		String sentencia = "";
		String restricciones = "";
		String ordenamiento = "";
		String agrupamiento = "";

		
		 restricciones = "";
		
		restricciones += " WHERE e.id_revision='"+ idRevision +"'  ";
		
		sentencia = "SELECT e.ficha,em.nombre,ec.descripcion_formacion,ar.descripcion as ardescripcion,tf.descripcion as tfdescripcion ,ug.descripcion_urgencia,ge.descripcion as gedescripcion ,ca.descripcion as cadescripcion ,em.grado_auxiliar,rv.descripcion as rvdescripcion  FROM " + 
					"evaluacion e inner join evaluacion_capacitacion ec ON e.id_evaluacion= ec.id_evaluacion " +
					"INNER JOIN area ar on ar.id_area= ec.id_area " +
					"INNER JOIN tipo_formacion tf on tf.id_tipo_formacion=ec.id_tipo_formacion " + 
					"INNER JOIN urgencia ug ON ug.id_urgencia = ec.id_urgencia " +
					"INNER JOIN cargo ca on ca.id_cargo = e.id_cargo " +
					"INNER JOIN empleado em ON e.ficha = em.ficha " +
					"INNER JOIN revision rv ON rv.id_revision = e.id_revision " + 
					"INNER JOIN unidad_organizativa uo ON uo.id_unidad_organizativa=em.id_unidad_organizativa " + 
					"INNER JOIN gerencia ge ON ge.id_gerencia = uo.id_gerencia " ;
		
		agrupamiento = "  ";
		ordenamiento = "ORDER BY e.ficha ";

		Query qSentencia = entityManager.createNativeQuery(sentencia
				+ restricciones + agrupamiento + ordenamiento);
		
		@SuppressWarnings("unchecked")
		List<Object[]> results = qSentencia.getResultList();

		for (Object[] obj : results) {
			BeanCapacitacionRequerida beanCapacitacion = new BeanCapacitacionRequerida();
			
			beanCapacitacion.setFicha((String) obj[0]);
			beanCapacitacion.setNombre((String) obj[1]);
			beanCapacitacion.setCapacitacion((String) obj[2]);
			beanCapacitacion.setArea((String) obj[3]);
			beanCapacitacion.setTipoFormacion((String) obj[4]);
			beanCapacitacion.setUrgencia((String) obj[5]);
			beanCapacitacion.setGerencia((String) obj[6]);
			beanCapacitacion.setCargo((String) obj[7]);
			beanCapacitacion.setGrado(String.valueOf((Integer) obj[8]));
			beanCapacitacion.setRevision((String) obj[9]);
			

	
			listaBeanCapacitacion.add(beanCapacitacion);

		}

		return listaBeanCapacitacion;

	}
	

}
