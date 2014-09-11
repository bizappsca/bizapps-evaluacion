package modelo.maestros;

import java.io.Serializable;
import javax.persistence.*;

import modelo.seguridad.Usuario;

import java.sql.Timestamp;
import java.util.List;

/**
 * The persistent class for the configuracion_general database table.
 * 
 */
@Entity
@Table(name = "configuracion_general")
public class ConfiguracionGeneral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id_configuracion")
	private int id;

	@Column(name = "bandera")
	private String bandera;


	public ConfiguracionGeneral() {
	}

	public ConfiguracionGeneral(int id, String bandera) {
		super();
		this.id = id;
		this.bandera = bandera;
		
	}

	public int getId() {
		return this.id;
	}

	public String getBandera() {
		return bandera;
	}

	public void setBandera(String bandera) {
		this.bandera = bandera;
	}

	public void setId(int id) {
		this.id = id;
	}

	

}