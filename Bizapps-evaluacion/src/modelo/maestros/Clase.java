package modelo.maestros;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the clase database table.
 * 
 */
@Entity
@Table(name = "clase")
public class Clase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id_clase")
	private int id;

	// bi-directional many-to-one association to Curso
	@ManyToOne
	@JoinColumn(name = "id_nombre_curso")
	private NombreCurso curso;

	@Column(name = "contenido")
	private String contenido;

	@Column(name = "objetivo")
	private String objetivo;

	@Column(name = "entidad_didactica")
	private String entidadDidactica;

	@Column(name = "fecha")
	private Timestamp fecha;

	@Column(name = "duracion")
	private float duracion;

	@Column(name = "medida_duracion")
	private String medidaDuracion;

	@Column(name = "lugar")
	private String lugar;

	@Column(name = "tipo_entrenamiento")
	private String tipoEntrenamiento;

	@Column(name = "modalidad")
	private String modalidad;

	@Column(name = "fecha_auditoria")
	private Timestamp fechaAuditoria;

	@Column(name = "hora_auditoria")
	private String horaAuditoria;

	@Column(name = "usuario")
	private String usuario;

	public Clase() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Clase(int id, NombreCurso curso, String contenido, String objetivo,
			String entidadDidactica, Timestamp fecha, float duracion,
			String medidaDuracion, String lugar, String tipoEntrenamiento,
			String modalidad, Timestamp fechaAuditoria, String horaAuditoria,
			String usuario) {
		super();
		this.id = id;
		this.curso = curso;
		this.contenido = contenido;
		this.objetivo = objetivo;
		this.entidadDidactica = entidadDidactica;
		this.fecha = fecha;
		this.duracion = duracion;
		this.medidaDuracion = medidaDuracion;
		this.lugar = lugar;
		this.tipoEntrenamiento = tipoEntrenamiento;
		this.modalidad = modalidad;
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

	public NombreCurso getCurso() {
		return curso;
	}

	public void setCurso(NombreCurso curso) {
		this.curso = curso;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getEntidadDidactica() {
		return entidadDidactica;
	}

	public void setEntidadDidactica(String entidadDidactica) {
		this.entidadDidactica = entidadDidactica;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
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

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public String getTipoEntrenamiento() {
		return tipoEntrenamiento;
	}

	public void setTipoEntrenamiento(String tipoEntrenamiento) {
		this.tipoEntrenamiento = tipoEntrenamiento;
	}

	public String getModalidad() {
		return modalidad;
	}

	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
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
