package modelo.maestros;

import java.io.Serializable;
import javax.persistence.*;

import modelo.seguridad.Usuario;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the area database table.
 * 
 */
@Entity
@Table(name = "area")
public class Area implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id_area")
	private int id;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "usuario")
	private String usuario;

	@Column(name = "fecha_auditoria")
	private Timestamp fechaAuditoria;

	@Column(name = "hora_auditoria")
	private String horaAuditoria;

	// bi-directional many-to-one association to TipoFormacion
	@ManyToOne
	@JoinColumn(name = "id_tipo_formacion")
	private TipoFormacion tipoFormacion;

	// bi-directional many-to-one association to Curso
	@OneToMany(mappedBy = "area")
	private List<NombreCurso> cursos;

	public Area() {
	}

	public Area(int id, String descripcion, String usuario,
			Timestamp fechaAuditoria, String horaAuditoria,
			TipoFormacion tipoFormacion) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.usuario = usuario;
		this.fechaAuditoria = fechaAuditoria;
		this.horaAuditoria = horaAuditoria;
		this.tipoFormacion = tipoFormacion;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int idArea) {
		this.id = idArea;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoFormacion getTipoFormacion() {
		return tipoFormacion;
	}

	public void setTipoFormacion(TipoFormacion tipoFormacion) {
		this.tipoFormacion = tipoFormacion;
	}

	public Timestamp getFechaAuditoria() {
		return this.fechaAuditoria;
	}

	public void setFechaAuditoria(Timestamp fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}

	public String getHoraAuditoria() {
		return this.horaAuditoria;
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

	public List<NombreCurso> getCursos() {
		return cursos;
	}

	public void setCursos(List<NombreCurso> cursos) {
		this.cursos = cursos;
	}
	
	
	

}