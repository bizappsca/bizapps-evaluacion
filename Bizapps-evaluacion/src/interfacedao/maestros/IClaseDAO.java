package interfacedao.maestros;


import java.util.List;

import modelo.maestros.Clase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IClaseDAO extends JpaRepository<Clase, Integer> {

	public List<Clase> findByCursoNombreStartingWithAllIgnoreCase(String valor);

	public List<Clase> findByContenidoStartingWithAllIgnoreCase(String valor);

	public List<Clase> findByObjetivoStartingWithAllIgnoreCase(String valor);

	public List<Clase> findByEntidadDidacticaStartingWithAllIgnoreCase(
			String valor);

	public List<Clase> findByFechaStartingWithAllIgnoreCase(String valor);

	public List<Clase> findByDuracionStartingWithAllIgnoreCase(String valor);

	public List<Clase> findByLugarStartingWithAllIgnoreCase(String valor);

	public List<Clase> findByTipoEntrenamientoStartingWithAllIgnoreCase(
			String valor);

	public List<Clase> findByModalidadStartingWithAllIgnoreCase(String valor);

	public List<Clase> findByContenidoAllIgnoreCase(String contenido);



}