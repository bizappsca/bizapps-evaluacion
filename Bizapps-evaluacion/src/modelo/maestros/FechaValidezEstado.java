package modelo.maestros;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fecha_validez_estado")
public class FechaValidezEstado {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="id_estado")
	private int id;

	@Column(name="estado")
	private String estado;
	
	@Column(name="fecha_desde")
	private Timestamp fechaDesde;
	
	@Column(name="fecha_hasta")
	private Timestamp fechaHasta;

	@Column(name="fecha_auditoria")
	private Timestamp fechaAuditoria;

	@Column(name="hora_auditoria")
	private String horaAuditoria;
	
	@Column(name="usuario")
	private String usuario;
	
	@Column(name="grado")
	private Integer grado;

	public FechaValidezEstado() {
		super();
	}

	public FechaValidezEstado(int id, String estado, Timestamp fechaDesde,
			Timestamp fechaHasta, Timestamp fechaAuditoria,
			String horaAuditoria, String usuario, Integer grado) {
		super();
		this.id = id;
		this.estado = estado;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;
		this.fechaAuditoria = fechaAuditoria;
		this.horaAuditoria = horaAuditoria;
		this.usuario = usuario;
		this.grado = grado;
	}







	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Timestamp getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Timestamp fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Timestamp getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Timestamp fechaHasta) {
		this.fechaHasta = fechaHasta;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}







	public Integer getGrado() {
		return grado;
	}







	public void setGrado(Integer grado) {
		this.grado = grado;
	}
	
	
	

}
