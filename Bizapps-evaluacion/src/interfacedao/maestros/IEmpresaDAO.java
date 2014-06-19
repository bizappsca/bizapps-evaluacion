package interfacedao.maestros;


import java.util.List;

import modelos.Empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IEmpresaDAO extends JpaRepository<Empresa, Integer> {

	Empresa findByNombre(String nombre);


	
}