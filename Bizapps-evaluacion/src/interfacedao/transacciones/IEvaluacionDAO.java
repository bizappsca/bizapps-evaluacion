package interfacedao.transacciones;

import java.util.List;

import modelo.maestros.Evaluacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IEvaluacionDAO extends JpaRepository<Evaluacion, Integer> {
	
	
	public List<Evaluacion> findByFichaOrderByRevisionIdDescIdEvaluacionSecundarioDesc(String ficha);
	
	//@Query("select e from Evaluacion e where e.estadoEvaluacion <> 'EN EDICION' and e.ficha = ?1")
	@Query("select e from Evaluacion e where  e.ficha = ?1 order by e.idEvaluacion desc")
	public List<Evaluacion> buscarEstado(String ficha);
	
	@Query("select e from Evaluacion e where  e.ficha = ?1 and e.revision.id = ?2 " +
			"and e.estadoEvaluacion = ?3  and e.idEvaluacion<>?4")
	public List<Evaluacion> buscarRevision(String ficha, Integer revision, String estado, Integer id);
	
	
	@Query("select count(idEvaluacion) from Evaluacion e where  e.ficha = ?1 and e.revision.id = ?2 " )
	public Long cantidadEvaluaciones(String ficha, Integer revision);
	
	public List<Evaluacion> findByFichaAndEstadoEvaluacion (String ficha, String estado);
	
	@Query("select max(idEvaluacion) from Evaluacion")
	public Integer buscar();
	
	@Query("select max(idEvaluacionSecundario) from Evaluacion e where e.ficha = ?1")
	public Integer buscarIdSecundario(String ficha);

	public Evaluacion findByIdEvaluacionSecundarioAndFicha (Integer id, String ficha); 
	
	public Evaluacion findByIdEvaluacion(Integer idEvaluacion);

	@Query("select  distinct(ev) from Empleado em, Evaluacion ev, Empresa e, Revision r , UnidadOrganizativa uo, Gerencia g " +
			"where " +
			"em.ficha = ev.ficha and " +
			"r.id = ev.revision.id " +
			"and r.estadoRevision = 'ACTIVO' AND " +
			"e.nombre = ?1 AND  " +
			"em.nombre = ?2  " +
			"AND em.fichaSupervisor = ?3 and " +
			"em.unidadOrganizativa.id = uo.id and " +
			"uo.gerencia.id = g.id and " +
			"g.descripcion = ?4 and ev.valoracion = ?5 ")
	public List<Evaluacion> buscarEvaluacionCalibracion(String empresa, String nombreE, String fichaE, String gerencia, String valoracion);

	@Query("select  distinct(ev) from Empleado em, Evaluacion ev, Empresa e, Revision r , UnidadOrganizativa uo, Gerencia g " +
			"where " +
			"em.empresa.id = e.id and " +
			"em.ficha = ev.ficha and " +
			"r.id = ev.revision.id " +
			"and r.estadoRevision = 'ACTIVO' AND " +
			"e.nombre = ?1 AND  " +
			"em.unidadOrganizativa.id = uo.id and " +
			"uo.gerencia.id = g.id")
	public List<Evaluacion> buscarEvaluacionCalibracionEmpresa(String empresa);
	
	@Query("select  distinct(ev) from Empleado em, Evaluacion ev, Empresa e, Revision r , UnidadOrganizativa uo, Gerencia g " +
			"where " +
			"em.ficha = ev.ficha and " +
			"r.id = ev.revision.id " +
			"and r.estadoRevision = 'ACTIVO' AND " +
			"em.nombre = ?1 and " +
			"em.unidadOrganizativa.id = uo.id and " +
			"uo.gerencia.id = g.id")
	public List<Evaluacion> buscarEvaluacionCalibracionTrabajador(String nombreE);
	
	@Query("select  distinct(ev) from Empleado em, Evaluacion ev, Empresa e, Revision r , UnidadOrganizativa uo, Gerencia g " +
			"where " +
			"em.ficha = ev.ficha and " +
			"r.id = ev.revision.id " +
			"and r.estadoRevision = 'ACTIVO' AND " +
			" em.fichaSupervisor = ?1 and " +
			"em.unidadOrganizativa.id = uo.id and " +
			"uo.gerencia.id = g.id" )
	public List<Evaluacion> buscarEvaluacionCalibracionEvaluador(String fichaE);

	@Query("select distinct(ev) from Empleado em, Evaluacion ev, Empresa e, Revision r , UnidadOrganizativa uo, Gerencia g " +
			"where " +
			"em.ficha = ev.ficha and " +
			"r.id = ev.revision.id " +
			"and r.estadoRevision = 'ACTIVO' AND " +
			"em.unidadOrganizativa.id = uo.id and " +
			"uo.gerencia.id = g.id and " +
			"g.descripcion = ?1 ")
	public List<Evaluacion> buscarEvaluacionCalibracionGerencia(String gerencia);
	
	@Query("select distinct(ev) from Empleado em, Evaluacion ev, Empresa e, Revision r , UnidadOrganizativa uo, Gerencia g " +
			"where " +
			"em.ficha = ev.ficha and " +
			"r.id = ev.revision.id " +
			"and r.estadoRevision = 'ACTIVO' AND " +
			"em.unidadOrganizativa.id = uo.id and " +
			"uo.gerencia.id = g.id and " +
			"ev.valoracion = ?1 ")
	public List<Evaluacion> buscarEvaluacionCalibracionValoracion(String valoracion);
	
	@Query("select distinct(ev) from Empleado em, Evaluacion ev, Empresa e, Revision r , UnidadOrganizativa uo, Gerencia g " +
			"where " +
			"em.ficha = ev.ficha and " +
			"r.id = ev.revision.id " +
			"and r.estadoRevision = 'ACTIVO' AND " +
			"em.unidadOrganizativa.id = uo.id and " +
			"uo.gerencia.id = g.id and " +
			"em.gradoAuxiliar = ?1 ")
	
	public List<Evaluacion> buscarEvaluacionCalibracionGrado(Integer Grado);
	
	@Query("select e from Evaluacion e, Revision r , Empleado em " +
			"where r.id = e.revision.id and em.ficha=e.ficha and  " +
			" r.estadoRevision = 'ACTIVO' order by em.gradoAuxiliar, em.nombre")
	public List<Evaluacion> buscarEvaluacionesRevision();
	
	
	
	@Query("select ev from Empleado em, Evaluacion ev, Empresa e, Revision r , UnidadOrganizativa uo, Gerencia g " +
			"where " +
			"em.ficha = ev.ficha and " +
			"r.id = ev.revision.id " +
			"and r.estadoRevision = 'ACTIVO' AND " +
			"em.unidadOrganizativa.id = uo.id and " +
			"uo.gerencia.id = g.id and " +
			"e.id = em.empresa.id and " +
			"e.nombre like  CONCAT('%' , ?1 , '%') AND  " +
			"em.nombre like CONCAT('%' , ?2 , '%') and " +
			"em.fichaSupervisor like CONCAT('%' , ?3 , '%') and " +
			"g.descripcion like CONCAT('%' , ?4 , '%') and " +
			"ev.valoracion like CONCAT('%' , ?5 , '%') and "+ 
			"em.gradoAuxiliar >= ?6 order by em.gradoAuxiliar,em.nombre ")
	public List<Evaluacion> buscarEvaluacionCalibracion(String empresa,String nombreE,String fichaE,String gerencia,String valoracion,Integer Grado);
	
}
