package servicio.maestros;



import interfacedao.maestros.IEmpleadoDAO;

import java.util.List;

import modelo.maestros.Area;
import modelo.maestros.Cargo;
import modelo.maestros.Dominio;
import modelo.maestros.Empleado;
import modelo.seguridad.Arbol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SEmpleado")
public class SEmpleado {

	@Autowired
	private IEmpleadoDAO empleadoDAO;

	/* Servicio que permite guardar los datos de un empleado*/
	public void guardar(Empleado empleado) {
		empleadoDAO.save(empleado);
	}
	
	/* Servicio que permite buscar todos los empleados */
	public List<Empleado> buscarTodos() {
		return empleadoDAO.findAll();
	}
	
	public Empleado buscar(int id) {

		return empleadoDAO.findOne(id);
	}


	public Empleado buscarPorNombre(String nombre) {
		Empleado empleado;
		empleado = empleadoDAO.findByNombre(nombre);
		return empleado;
	}
	
	public Empleado buscarPorId(int integer) {

		Empleado empleado;
		empleado = empleadoDAO.findOne(integer);
		return empleado;
	}


	public Empleado buscarPorFicha(String ficha) {
		return empleadoDAO.findByFichaOrderByGradoAuxiliarDescNombreAsc(ficha);
	}
	
	public Empleado buscarEmpleadoPorFicha(String ficha) {
		return empleadoDAO.findByFicha(ficha);
	}


	public List<Empleado> BuscarPorSupervisor(String ficha) {
		return empleadoDAO.findByFichaSupervisorOrderByGradoAuxiliarDescNombreAsc(ficha);
	}
	
	
	/* Servicio que permite eliminar un empleado */
	public void eliminarUnEmpleado(int id) {
		empleadoDAO.delete(id);
	}
	
	/* Servicio que permite eliminar varios empleados */
	public void eliminarVariosEmpleados(List<Empleado> eliminar) {
		empleadoDAO.delete(eliminar);
	}
	
	/*
	 * Servicio que permite filtrar los empleados de una lista de acuerdo al
	 * id
	 */
	public List<Empleado> filtroId(String valor) {
		return empleadoDAO.findByIdStartingWithAllIgnoreCase(valor);
	}

	/*
	 * Servicio que permite filtrar los empleados de una lista de acuerdo a
	 * la empresa
	 */
	public List<Empleado> filtroEmpresa(String valor) {
		return empleadoDAO.findByEmpresaNombreStartingWithAllIgnoreCase(valor);
	}

	/*
	 * Servicio que permite filtrar los empleados de una lista de acuerdo al
	 * cargo
	 */
	public List<Empleado> filtroCargo(String valor) {
		return empleadoDAO.findByCargoDescripcionStartingWithAllIgnoreCase(valor);
	}
	
	/*
	 * Servicio que permite filtrar los empleados de una lista de acuerdo a la
	 * unidad organizativa
	 */
	public List<Empleado> filtroNombre(String valor) {
		return empleadoDAO.findByNombreStartingWithAllIgnoreCase(valor);
	}
	
	/*
	 * Servicio que permite filtrar los empleados de una lista de acuerdo al nombre
	 */
	public List<Empleado> filtroUnidadOrganizativa(String valor) {
		return empleadoDAO.findByUnidadOrganizativaDescripcionStartingWithAllIgnoreCase(valor);
	}
	
	
	/*
	 * Servicio que permite filtrar los empleados de una lista de acuerdo a la ficha
	 */
	public List<Empleado> filtroFicha(String valor) {
		return empleadoDAO.findByFichaStartingWithAllIgnoreCase(valor);
	}
	
	
	/*
	 * Servicio que permite filtrar los empleados de una lista de acuerdo al grado auxiliar
	 */
	public List<Empleado> filtroGradoAuxiliar(String valor) {
		return empleadoDAO.findByGradoAuxiliarStartingWithAllIgnoreCase(valor);
	}
	
	/*
	 * Servicio que permite filtrar los empleados de una lista de acuerdo a la ficha del supervisor
	 */
	public List<Empleado> filtroFichaSupervisor(String valor) {
		return empleadoDAO.findByFichaSupervisorStartingWithAllIgnoreCase(valor);
	}
	
	/* Servicio que permite buscar un cargo de acuerdo al nombre */
	public List<Empleado> buscarPorNombres(String descripcion) {
		List<Empleado> empleado;
		empleado = empleadoDAO.findByNombreAllIgnoreCase(descripcion);
		return empleado;
	}

	
		

}
