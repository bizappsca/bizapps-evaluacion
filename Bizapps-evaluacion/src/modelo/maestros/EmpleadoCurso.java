package modelo.maestros;

import java.io.Serializable;
import javax.persistence.*;

import modelo.pk.EmpleadoCursoPK;

/**
 * The persistent class for the nivel_competencia_cargo database table.
 * 
 */
@Entity
@Table(name = "empleado_curso")
@IdClass(EmpleadoCursoPK.class)
public class EmpleadoCurso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_curso", referencedColumnName = "id_curso")
	private Curso curso;

	@Id
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
	private Empleado empleado;

	@Column(name = "estado_curso")
	private String estadoCurso;

	public EmpleadoCurso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmpleadoCurso(Curso curso, Empleado empleado, String estadoCurso) {
		super();
		this.curso = curso;
		this.empleado = empleado;
		this.estadoCurso = estadoCurso;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public String getEstadoCurso() {
		return estadoCurso;
	}

	public void setEstadoCurso(String estadoCurso) {
		this.estadoCurso = estadoCurso;
	}

}