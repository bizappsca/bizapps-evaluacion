package interfacedao.maestros;


import java.util.List;

import modelo.maestros.Medicion;
import modelo.maestros.Perspectiva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IMedicionDAO extends JpaRepository<Medicion, Integer> {

	Medicion findByDescripcionMedicion(String descripcion);

	 public List<Medicion> findAll();

	 Medicion findById (Integer value);
}
