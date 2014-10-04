package modelo.maestros;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;

import modelo.pk.ActividadCursoPK;

/**
 * The persistent class for the nivel_competencia_cargo database table.
 * 
 */
@Entity
@Table(name = "actividad_curso")
@IdClass(ActividadCursoPK.class)
public class ActividadCurso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_actividad", referencedColumnName = "id_actividad")
	private Actividad actividad;

	@Id
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_curso", referencedColumnName = "id_curso")
	private Curso curso;

	@Column(name = "valor")
	private String valor;
	
	@Column(name="fecha")
	private Timestamp fecha;

	public ActividadCurso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ActividadCurso(Actividad actividad, Curso curso, String valor,
			Timestamp fecha) {
		super();
		this.actividad = actividad;
		this.curso = curso;
		this.valor = valor;
		this.fecha = fecha;
	}

	public Actividad getActividad() {
		return actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}
	
}