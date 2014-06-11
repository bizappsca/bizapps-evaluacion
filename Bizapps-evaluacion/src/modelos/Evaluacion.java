package modelos;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the evaluacion database table.
 * 
 */
@Entity
@Table(name="evaluacion")
public class Evaluacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_evaluacion")
	private int idEvaluacion;

	private String comentario;

	private String compromisos;

	@Column(name="estado_evaluacion")
	private String estadoEvaluacion;

	@Column(name="fecha_aprobacion")
	private Timestamp fechaAprobacion;

	@Column(name="fecha_auditoria")
	private Timestamp fechaAuditoria;

	@Column(name="fecha_calibracion")
	private Timestamp fechaCalibracion;

	@Column(name="fecha_creacion")
	private Timestamp fechaCreacion;

	@Column(name="fecha_impresion")
	private Timestamp fechaImpresion;

	@Column(name="fecha_revision")
	private Timestamp fechaRevision;

	private String ficha;

	@Column(name="ficha_evaluador")
	private String fichaEvaluador;

	private String fortalezas;

	@Column(name="hora_auditoria")
	private String horaAuditoria;

	@Column(name="id_evaluacion_secundario")
	private int idEvaluacionSecundario;

	@Column(name="id_revision")
	private int idRevision;

	@Column(name="id_usuario")
	private int idUsuario;

	private String oportunidades;

	private double peso;

	private double resultado;

	@Column(name="resultado_competencias")
	private int resultadoCompetencias;

	@Column(name="resultado_final")
	private int resultadoFinal;

	@Column(name="resultado_general")
	private int resultadoGeneral;

	@Column(name="resultado_objetivos")
	private int resultadoObjetivos;

	private String resumen;

	private String valoracion;

	public Evaluacion() {
	}

	public int getIdEvaluacion() {
		return this.idEvaluacion;
	}

	public void setIdEvaluacion(int idEvaluacion) {
		this.idEvaluacion = idEvaluacion;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getCompromisos() {
		return this.compromisos;
	}

	public void setCompromisos(String compromisos) {
		this.compromisos = compromisos;
	}

	public String getEstadoEvaluacion() {
		return this.estadoEvaluacion;
	}

	public void setEstadoEvaluacion(String estadoEvaluacion) {
		this.estadoEvaluacion = estadoEvaluacion;
	}

	public Timestamp getFechaAprobacion() {
		return this.fechaAprobacion;
	}

	public void setFechaAprobacion(Timestamp fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public Timestamp getFechaAuditoria() {
		return this.fechaAuditoria;
	}

	public void setFechaAuditoria(Timestamp fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}

	public Timestamp getFechaCalibracion() {
		return this.fechaCalibracion;
	}

	public void setFechaCalibracion(Timestamp fechaCalibracion) {
		this.fechaCalibracion = fechaCalibracion;
	}

	public Timestamp getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Timestamp getFechaImpresion() {
		return this.fechaImpresion;
	}

	public void setFechaImpresion(Timestamp fechaImpresion) {
		this.fechaImpresion = fechaImpresion;
	}

	public Timestamp getFechaRevision() {
		return this.fechaRevision;
	}

	public void setFechaRevision(Timestamp fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public String getFicha() {
		return this.ficha;
	}

	public void setFicha(String ficha) {
		this.ficha = ficha;
	}

	public String getFichaEvaluador() {
		return this.fichaEvaluador;
	}

	public void setFichaEvaluador(String fichaEvaluador) {
		this.fichaEvaluador = fichaEvaluador;
	}

	public String getFortalezas() {
		return this.fortalezas;
	}

	public void setFortalezas(String fortalezas) {
		this.fortalezas = fortalezas;
	}

	public String getHoraAuditoria() {
		return this.horaAuditoria;
	}

	public void setHoraAuditoria(String horaAuditoria) {
		this.horaAuditoria = horaAuditoria;
	}

	public int getIdEvaluacionSecundario() {
		return this.idEvaluacionSecundario;
	}

	public void setIdEvaluacionSecundario(int idEvaluacionSecundario) {
		this.idEvaluacionSecundario = idEvaluacionSecundario;
	}

	public int getIdRevision() {
		return this.idRevision;
	}

	public void setIdRevision(int idRevision) {
		this.idRevision = idRevision;
	}

	public int getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getOportunidades() {
		return this.oportunidades;
	}

	public void setOportunidades(String oportunidades) {
		this.oportunidades = oportunidades;
	}

	public double getPeso() {
		return this.peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	public double getResultado() {
		return this.resultado;
	}

	public void setResultado(double resultado) {
		this.resultado = resultado;
	}

	public int getResultadoCompetencias() {
		return this.resultadoCompetencias;
	}

	public void setResultadoCompetencias(int resultadoCompetencias) {
		this.resultadoCompetencias = resultadoCompetencias;
	}

	public int getResultadoFinal() {
		return this.resultadoFinal;
	}

	public void setResultadoFinal(int resultadoFinal) {
		this.resultadoFinal = resultadoFinal;
	}

	public int getResultadoGeneral() {
		return this.resultadoGeneral;
	}

	public void setResultadoGeneral(int resultadoGeneral) {
		this.resultadoGeneral = resultadoGeneral;
	}

	public int getResultadoObjetivos() {
		return this.resultadoObjetivos;
	}

	public void setResultadoObjetivos(int resultadoObjetivos) {
		this.resultadoObjetivos = resultadoObjetivos;
	}

	public String getResumen() {
		return this.resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public String getValoracion() {
		return this.valoracion;
	}

	public void setValoracion(String valoracion) {
		this.valoracion = valoracion;
	}

}