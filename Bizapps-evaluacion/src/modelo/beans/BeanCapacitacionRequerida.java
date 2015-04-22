package modelo.beans;

public class BeanCapacitacionRequerida {
	
	String ficha;
	String nombre;
	String capacitacion;
	String area;
	String tipoFormacion;
	String urgencia;
	String gerencia;
	String cargo;
	String grado;
	String revision;
	
	
	
	public String getFicha() {
		return ficha;
	}
	public void setFicha(String ficha) {
		this.ficha = ficha;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCapacitacion() {
		return capacitacion;
	}
	public void setCapacitacion(String capacitacion) {
		this.capacitacion = capacitacion;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getTipoFormacion() {
		return tipoFormacion;
	}
	public void setTipoFormacion(String tipoFormacion) {
		this.tipoFormacion = tipoFormacion;
	}
	public String getUrgencia() {
		return urgencia;
	}
	public void setUrgencia(String urgencia) {
		this.urgencia = urgencia;
	}
	public String getGerencia() {
		return gerencia;
	}
	public void setGerencia(String gerencia) {
		this.gerencia = gerencia;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getGrado() {
		return grado;
	}
	public void setGrado(String grado) {
		this.grado = grado;
	}
	
	public BeanCapacitacionRequerida(String ficha, String nombre,
			String capacitacion, String area, String tipoFormacion,
			String urgencia, String gerencia, String cargo, String grado,
			String revision) {
		super();
		this.ficha = ficha;
		this.nombre = nombre;
		this.capacitacion = capacitacion;
		this.area = area;
		this.tipoFormacion = tipoFormacion;
		this.urgencia = urgencia;
		this.gerencia = gerencia;
		this.cargo = cargo;
		this.grado = grado;
		this.revision = revision;
	}
	public BeanCapacitacionRequerida() {
		super();
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	
	

}
