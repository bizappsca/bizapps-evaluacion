package modelo.maestros;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the nombre_curso database table.
 * 
 */
@Entity
@Table(name = "nombre_curso")
public class NombreCurso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id_nombre_curso")
	private int id;

	// bi-directional many-to-one association to Area
	@ManyToOne
	@JoinColumn(name = "id_area")
	private Area area;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "fecha_auditoria")
	private Timestamp fechaAuditoria;

	@Column(name = "hora_auditoria")
	private String horaAuditoria;

	@Column(name = "usuario")
	private String usuario;
	

	public NombreCurso() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NombreCurso(int id, Area area, String nombre,
			Timestamp fechaAuditoria, String horaAuditoria, String usuario) {
		super();
		this.id = id;
		this.area = area;
		this.nombre = nombre;
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

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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