package modelo.maestros;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the curso database table.
 * 
 */
@Entity
@Table(name = "curso")
public class Curso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id_curso")
	private int id;

	@ManyToOne
	@JoinColumn(name = "id_periodo")
	private Periodo periodo;

	@ManyToOne
	@JoinColumn(name = "id_nombre_curso")
	private NombreCurso nombreCurso;

	@Column(name = "fecha_inicio")
	private Timestamp fechaInicio;

	@Column(name = "fecha_fin")
	private Timestamp fechaFin;

	@Column(name = "duracion")
	private float duracion;

	@Column(name = "medida_duracion")
	private String medidaDuracion;
	
	@Column(name = "facilitador")
	private String facilitador;

	@Column(name = "estado")
	private String estado;

	@Column(name = "fecha_auditoria")
	private Timestamp fechaAuditoria;

	@Column(name = "hora_auditoria")
	private String horaAuditoria;

	@Column(name = "usuario")
	private String usuario;

	public Curso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Curso(int id, Periodo periodo, NombreCurso nombreCurso,
			Timestamp fechaInicio, Timestamp fechaFin, float duracion,
			String medidaDuracion, String facilitador, String estado,
			Timestamp fechaAuditoria, String horaAuditoria, String usuario) {
		super();
		this.id = id;
		this.periodo = periodo;
		this.nombreCurso = nombreCurso;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.duracion = duracion;
		this.medidaDuracion = medidaDuracion;
		this.facilitador = facilitador;
		this.estado = estado;
		this.fechaAuditoria = fechaAuditoria;
		this.horaAuditoria = horaAuditoria;
		this.usuario = usuario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public NombreCurso getNombreCurso() {
		return nombreCurso;
	}

	public void setNombreCurso(NombreCurso nombreCurso) {
		this.nombreCurso = nombreCurso;
	}

	public Timestamp getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Timestamp fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Timestamp getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Timestamp fechaFin) {
		this.fechaFin = fechaFin;
	}

	public float getDuracion() {
		return duracion;
	}

	public void setDuracion(float duracion) {
		this.duracion = duracion;
	}

	public String getMedidaDuracion() {
		return medidaDuracion;
	}

	public void setMedidaDuracion(String medidaDuracion) {
		this.medidaDuracion = medidaDuracion;
	}

	public String getFacilitador() {
		return facilitador;
	}

	public void setFacilitador(String facilitador) {
		this.facilitador = facilitador;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Timestamp getFechaAuditoria() {
		return fechaAuditoria;
	}

	public void setFechaAuditoria(Timestamp fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}

	public String getHoraAuditoria() {
		return horaAuditoria;
	}

	public void setHoraAuditoria(String horaAuditoria) {
		this.horaAuditoria = horaAuditoria;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}