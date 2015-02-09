package modelo.maestros;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the actividad database table.
 * 
 */
@Entity
@Table(name="error_interfaz")
public class ErrorInterfaz implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="id_error")
	private int id;

	@Column(name="nombre")
	private String nombre;
	
	@Column(name="error")
	private String error;

	@Column(name="fecha")
	private Timestamp fecha;

	@Column(name="hora")
	private String hora;
	
	@Column(name="ficha")
	private String ficha;

	public ErrorInterfaz() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getFicha() {
		return ficha;
	}

	public void setFicha(String ficha) {
		this.ficha = ficha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ErrorInterfaz(int id, String nombre, String error, Timestamp fecha,
			String hora, String ficha) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.error = error;
		this.fecha = fecha;
		this.hora = hora;
		this.ficha = ficha;
	}


	
}