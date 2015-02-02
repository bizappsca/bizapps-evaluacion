package modelo.maestros;

import java.io.Serializable;
import javax.persistence.*;

import modelo.seguridad.Usuario;

import java.sql.Timestamp;


/**
 * The persistent class for the competencia database table.
 * 
 */
@Entity
@Table(name="competencia")
public class Competencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="id_competencia")
	private int id;

	@Column(name="comentario")
	private String comentario;

	@Column(name="descripcion")
	private String descripcion;

	@Column(name="fecha_auditoria")
	private Timestamp fechaAuditoria;

	@Column(name="hora_auditoria")
	private String horaAuditoria;

	@Column(name="nivel")
	private String nivel;
	
	@Column(name="usuario")
	private String usuario;
	
	@Column(name="id_empresa")
	private int idEmpresa;

	public Competencia() {
	}
	
	public Competencia(int idCompetencia, String comentario,
			String descripcion, Timestamp fechaAuditoria, String horaAuditoria,
			String nivel, String usuario) {
		super();
		this.id = idCompetencia;
		this.comentario = comentario;
		this.descripcion = descripcion;
		this.fechaAuditoria = fechaAuditoria;
		this.horaAuditoria = horaAuditoria;
		this.nivel = nivel;
		this.usuario = usuario;
	}



	public int getId() {
		return this.id;
	}

	public void setId(int idCompetencia) {
		this.id = idCompetencia;
	}

	public String getComentario() {
		return this.comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public String getNivel() {
		return this.nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	

}