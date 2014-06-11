package modelos;

import java.io.Serializable;
import javax.persistence.*;

import modelo.pk.NivelCompetenciaCargoPK;


/**
 * The persistent class for the nivel_competencia_cargo database table.
 * 
 */
@Entity
@Table(name="nivel_competencia_cargo")
public class NivelCompetenciaCargo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private NivelCompetenciaCargoPK id;

	@Column(name="id_dominio")
	private int idDominio;

	public NivelCompetenciaCargo() {
	}

	public NivelCompetenciaCargoPK getId() {
		return this.id;
	}

	public void setId(NivelCompetenciaCargoPK id) {
		this.id = id;
	}

	public int getIdDominio() {
		return this.idDominio;
	}

	public void setIdDominio(int idDominio) {
		this.idDominio = idDominio;
	}

}