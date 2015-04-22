package controlador.transacciones;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Empleado;
import modelo.maestros.Empresa;
import modelo.maestros.Evaluacion;
import modelo.maestros.Gerencia;
import modelo.maestros.UnidadOrganizativa;
import modelo.maestros.Valoracion;
import modelo.seguridad.Usuario;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.CatalagoN;
import componentes.Catalogo;
import componentes.Mensaje;
import controlador.maestros.CGenerico;

public class CEstadoEvaluacion extends CGenerico {

	private static final long serialVersionUID = 1L;

	@Wire
	private Window winEvaluacionEmpleado;
	@Wire
	private Textbox txtEmpresa;
	@Wire
	private Button btnBuscarEmpresa;
	@Wire
	private Combobox cmbEstado;
	@Wire
	private Div divCatalogoEmpresa;
	@Wire
	private Div botoneraCalibracion;
	@Wire
	private Div catalogoEvaluaciones;
	@Wire
	private Button btnLimpiar;
	
	private int idGerencia = 0;
	private int idEmpresa = 0;
	private String ficha = "";
	private String nombre = "";
	
	
	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<UnidadOrganizativa> catalogo;
	Catalogo<Gerencia> catalogoGerencia;
	Catalogo<Empresa> catalogoEmpresa;
	Catalogo<Empleado> catalogoEvaluador;
	Catalogo<Empleado> catalogoTrabajador;
	Catalogo<Evaluacion> catalogoEvaluacion;
	List<Evaluacion> evaluaciones;
	
	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub
		
//		mostrarCatalogo();
	
		botonera = new Botonera(){
			@Override
			public void guardar() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void limpiar() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void salir() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void eliminar() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void buscar() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void annadir() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void ayuda() {
				// TODO Auto-generated method stub
				
			}
		};

			
		
	}


	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(3).setVisible(!bol);

	}
	

	@Listen("onClick = #btnBuscarEmpresa")
	public void mostrarCatalogoEmpresa() {
		final List<Empresa> listEmpresa = servicioEmpresa.buscarTodas();
		catalogoEmpresa = new Catalogo<Empresa>(divCatalogoEmpresa,
				"Catalogo de Empresas", listEmpresa,true,false,false, "Descripción") {

			@Override
			protected List<Empresa> buscar(List<String> valores) {
				List<Empresa> lista = new ArrayList<Empresa>();

				for (Empresa empresa : listEmpresa) {
					if (empresa.getNombre().toLowerCase()
							.contains(valores.get(0).toLowerCase())) {
						lista.add(empresa);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Empresa empresa) {
				String[] registros = new String[1];
				registros[0] = empresa.getNombre();

				return registros;
			}

		};

		catalogoEmpresa.setClosable(true);
		catalogoEmpresa.setWidth("80%");
		catalogoEmpresa.setParent(divCatalogoEmpresa);
		catalogoEmpresa.setTitle("Catalogo de Empresas");
		catalogoEmpresa.doModal();
	}

	@Listen("onSeleccion = #divCatalogoEmpresa")
	public void seleccionEmpresa() {
		Empresa empresa = catalogoEmpresa.objetoSeleccionadoDelCatalogo();
		idEmpresa = empresa.getId();
		txtEmpresa.setValue(empresa.getNombre());
		txtEmpresa.setFocus(true);
		catalogoEmpresa.setParent(null);
	}
	


	
	
	
	
	
	
	
	public void mostrarCatalogo() {
		
		evaluaciones = servicioEvaluacion.buscarEvaluacionesRevision();
		catalogoEvaluacion = new Catalogo <Evaluacion> (catalogoEvaluaciones, "Catalogo", evaluaciones,
				false, false, true,"Ficha", "Nombre Empleado","Estado Evaluacion","Ficha Evaluador", 
				"Nombre Evaluador","Resultado Objetivos", "Resultado Competencias", "Resultado General", "Valoración", "Resultado Final") {
			

			@Override
			protected List<Evaluacion> buscar(List<String> valores) {

				List<Evaluacion> evaluacion = new ArrayList<Evaluacion>();

				for (Evaluacion evaluacionCampos : evaluaciones) {
					Empleado empleado = servicioEmpleado.buscarPorFicha(evaluacionCampos.getFicha());
					Empleado empleado1 = servicioEmpleado.buscarPorFicha(evaluacionCampos.getFichaEvaluador());
					if ( evaluacionCampos.getFicha().toLowerCase()
									.contains(valores.get(0).toLowerCase())
							&& String.valueOf(empleado.getNombre()).toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& String.valueOf(evaluacionCampos.getEstadoEvaluacion()).toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& evaluacionCampos.getFichaEvaluador().toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& String.valueOf(empleado1.getNombre()).toLowerCase()
									.contains(valores.get(4).toLowerCase())
							&& String.valueOf(evaluacionCampos.getResultadoObjetivos()).toLowerCase()
									.contains(valores.get(5).toLowerCase())
							&& String.valueOf(evaluacionCampos.getResultadoCompetencias()).toLowerCase()
									.contains(valores.get(6).toLowerCase())
							&& String.valueOf(evaluacionCampos.getResultadoGeneral()).toLowerCase()
									.contains(valores.get(7).toLowerCase())
							&& String.valueOf(evaluacionCampos.getValoracion()).toLowerCase()
									.contains(valores.get(8).toLowerCase())
							&& String.valueOf(evaluacionCampos.getResultadoFinal()).toLowerCase()
									.contains(valores.get(9).toLowerCase())
							){
						evaluacion.add(evaluacionCampos);
					}
				}
				return evaluacion;
			}

			@Override
			protected String[] crearRegistros(Evaluacion evaluacionE) {
				Empleado empleado = servicioEmpleado.buscarPorFicha(evaluacionE.getFicha());
				Empleado empleado1 = servicioEmpleado.buscarPorFicha(evaluacionE.getFichaEvaluador());
				String[] registros = new String[10];
				registros[0] = evaluacionE.getFicha();
				registros[1] = empleado.getNombre();
				registros[2] = String.valueOf(evaluacionE.getEstadoEvaluacion());
				registros[3] = evaluacionE.getFichaEvaluador();
				registros[4] = empleado1.getNombre();
				registros[5] = String.valueOf(evaluacionE.getResultadoObjetivos());
				registros[6] = String.valueOf(evaluacionE.getResultadoCompetencias());
				registros[7] = String.valueOf(evaluacionE.getResultadoGeneral());
				registros[8] =String.valueOf(evaluacionE.getValoracion());
				registros[9] =String.valueOf(evaluacionE.getResultadoFinal());

				return registros;
				
			}

		};
		
		
		catalogoEvaluacion.setParent(catalogoEvaluaciones);
		// catalogo.doModal();
}
	
	
	
	@Listen("onClick = #btnBuscar")
	public void actualizarCatalogo() {
		if (catalogoEvaluaciones.getChildren().isEmpty())
			mostrarCatalogo();
		String empresa = txtEmpresa.getValue();
		empresa= "%"+empresa+"%";
		System.out.println(empresa);
		
		if (cmbEstado.getSelectedItem().getLabel().compareTo("CON EVALUACIONES CREADAS")==0)
		{
			evaluaciones = servicioEvaluacion.buscarPersonalConEvaluacion(empresa);
		}
		else
		{
			evaluaciones= servicioEvaluacion.buscarPersonalSinEvaluacion(empresa);
		}
		
		catalogoEvaluacion.actualizarLista(evaluaciones);
		
		
		
	}
	
	
	
	@Listen("onClick = #btnGuardar")
	public void mostrarEvaluacion() {
		try{
		if (catalogoEvaluacion.obtenerSeleccionados().size() == 1){
			
			Evaluacion eva = catalogoEvaluacion.objetoSeleccionadoDelCatalogo();
			
				Usuario usuario =servicioUsuario.buscarId(eva.getIdUsuario());
				String fichaUsuario = usuario.getFicha();

				if (fichaUsuario.compareTo("0")==0)
				{
					Messagebox
					.show("La Evaluación no se puede visualizar ya que es solo un registro historico ( Valoracion Final )",
							"Alerta",
							Messagebox.OK,
							Messagebox.EXCLAMATION);
				}
				else
				{
					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("modo", "EDITAR");
					map.put("id", eva.getIdEvaluacion());
					map.put("titulo", eva.getFicha());
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);
					
					
					winEvaluacionEmpleado = (Window) Executions.createComponents(
							"/vistas/transacciones/VAgregarEvaluacion.zul", null,
							map);
					winEvaluacionEmpleado.doModal();
					//winListaPersonal.onClose();
					
				}
				
				
			}
		else {
			Messagebox
			.show("Debe seleccionar una evaluación",
					"Alerta",
					Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

		}
	
	@Listen("onClick = #btnLimpiar")
	public void limpiar() {
		try{
		txtEmpresa.setValue("");
		catalogoEvaluacion.actualizarLista(new ArrayList<Evaluacion>());
		
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.toString());
	}
	}
	

	}
	
