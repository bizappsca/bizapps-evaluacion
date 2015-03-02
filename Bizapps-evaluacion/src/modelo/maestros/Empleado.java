package modelo.maestros;

import java.io.Serializable;
import javax.persistence.*;

import modelo.seguridad.Usuario;

import java.sql.Timestamp;

/**
 * The persistent class for the empleado database table.
 * 
 */
@Entity
@Table(name = "empleado")
public class Empleado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id_empleado")
	private int id;

	@Column(name = "fecha_auditoria")
	private Timestamp fechaAuditoria;

	@Column(name = "ficha")
	private String ficha;

	@Column(name = "ficha_supervisor")
	private String fichaSupervisor;

	@Column(name = "grado_auxiliar")
	private int gradoAuxiliar;

	@Column(name = "nivel_academico")
	private String nivelAcademico;

	@Column(name = "especialidad")
	private String especialidad;

	@Column(name = "especializacion")
	private String especializacion;

	@Column(name = "hora_auditoria")
	private String horaAuditoria;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "usuario")
	private String usuario;
	
	@Column(name = "estado")
	private String estado;

	// bi-directional many-to-one association to Cargo
	@ManyToOne
	@JoinColumn(name = "id_cargo")
	private Cargo cargo;

	// bi-directional many-to-one association to Empresa
	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;

	// bi-directional many-to-one association to UnidadOrganizativa
	@ManyToOne
	@JoinColumn(name = "id_unidad_organizativa")
	private UnidadOrganizativa unidadOrganizativa;

	public Empleado() {
	}

	public Empleado(int id, Timestamp fechaAuditoria, String ficha,
			String fichaSupervisor, int gradoAuxiliar, String nivelAcademico,
			String especialidad, String especializacion, String horaAuditoria,
			String nombre, String usuario, Cargo cargo,
			Empresa empresa, UnidadOrganizativa unidadOrganizativa) {
		super();
		this.id = id;
		this.fechaAuditoria = fechaAuditoria;
		this.ficha = ficha;
		this.fichaSupervisor = fichaSupervisor;
		this.gradoAuxiliar = gradoAuxiliar;
		this.nivelAcademico = nivelAcademico;
		this.especialidad = especialidad;
		this.especializacion = especializacion;
		this.horaAuditoria = horaAuditoria;
		this.nombre = nombre;
		this.usuario = usuario;
		this.cargo = cargo;
		this.empresa = empresa;
		this.unidadOrganizativa = unidadOrganizativa;
	}




	public int getId() {
		return this.id;
	}

	public void setId(int idEmpleado) {
		this.id = idEmpleado;
	}

	public Timestamp getFechaAuditoria() {
		return this.fechaAuditoria;
	}

	public void setFechaAuditoria(Timestamp fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}

	public String getFicha() {
		return this.ficha;
	}

	public void setFicha(String ficha) {
		this.ficha = ficha;
	}

	public String getFichaSupervisor() {
		return this.fichaSupervisor;
	}

	public void setFichaSupervisor(String fichaSupervisor) {
		this.fichaSupervisor = fichaSupervisor;
	}

	public int getGradoAuxiliar() {
		return this.gradoAuxiliar;
	}

	public void setGradoAuxiliar(int gradoAuxiliar) {
		this.gradoAuxiliar = gradoAuxiliar;
	}

	public String getHoraAuditoria() {
		return this.horaAuditoria;
	}

	public void setHoraAuditoria(String horaAuditoria) {
		this.horaAuditoria = horaAuditoria;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Cargo getCargo() {
		return this.cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public UnidadOrganizativa getUnidadOrganizativa() {
		return this.unidadOrganizativa;
	}

	public void setUnidadOrganizativa(UnidadOrganizativa unidadOrganizativa) {
		this.unidadOrganizativa = unidadOrganizativa;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getNivelAcademico() {
		return nivelAcademico;
	}

	public void setNivelAcademico(String nivelAcademico) {
		this.nivelAcademico = nivelAcademico;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public String getEspecializacion() {
		return especializacion;
	}

	public void setEspecializacion(String especializacion) {
		this.especializacion = especializacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	
}