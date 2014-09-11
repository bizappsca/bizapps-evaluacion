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

public class CBuscarEvaluacionesCalibracion extends CGenerico {

	private static final long serialVersionUID = 1L;

	@Wire
	private Window winEvaluacionEmpleado;
	@Wire
	private Div botoneraUnidadOrganizativa;
	@Wire
	private Textbox txtValoracion;
	@Wire
	private Spinner txtGrado;
	@Wire
	private Textbox txtEmpresa;
	@Wire
	private Textbox txtGerencia;
	@Wire
	private Textbox txtTrabajador;
	@Wire
	private Textbox txtEvaluador;
	@Wire
	private Button btnBuscarGerencia;
	@Wire
	private Button btnBuscarEmpresa;
	@Wire
	private Button btnBuscarTrabajador;
	@Wire
	private Button btnBuscarEvaluador;
	@Wire
	private Button btnGuardar;
	@Wire
	private Div divCatalogoGerencia;
	@Wire
	private Div divCatalogoEmpresa;
	@Wire
	private Div divCatalogoTrabajador;
	@Wire
	private Div divCatalogoEvaluador;
	@Wire
	private Div botoneraCalibracion;
	@Wire
	private Div catalogoEvaluaciones;
	@Wire
	private Button btnBuscarValoracion;
	Catalogo<Valoracion> catalogoValoracion;
	@Wire
	private Div divCatalogoValoracion;
	@Wire
	private Button btnLimpiar;
	
	private int idGerencia = 0;
	private int idEmpresa = 0;
	private String ficha = "";
	private String nombre = "";
	
	CatalagoN<Evaluacion> catalogoEvaluacion;
	Mensaje msj = new Mensaje();
	Botonera botonera;
	Catalogo<UnidadOrganizativa> catalogo;
	Catalogo<Gerencia> catalogoGerencia;
	Catalogo<Empresa> catalogoEmpresa;
	Catalogo<Empleado> catalogoEvaluador;
	Catalogo<Empleado> catalogoTrabajador;
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
		};
//		botonera.getChildren().get(1).setVisible(true);
//		botonera.getChildren().get(3).setVisible(true);
//		botoneraCalibracion.appendChild(botonera);
		
		
		
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
							.startsWith(valores.get(0))) {
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
	
	@Listen("onClick = #btnBuscarGerencia")
	public void mostrarCatalogoGerencia() {
		final List<Gerencia> listGerencia = servicioGerencia.buscarTodas();
		catalogoGerencia = new Catalogo<Gerencia>(divCatalogoGerencia,
				"Catalogo de Gerencias", listGerencia, true,false,false, "Descripción") {

			@Override
			protected List<Gerencia> buscar(List<String> valores) {
				List<Gerencia> lista = new ArrayList<Gerencia>();

				for (Gerencia gerencia : listGerencia) {
					if (gerencia.getDescripcion().toLowerCase()
							.startsWith(valores.get(0))) {
						lista.add(gerencia);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Gerencia gerencia) {
				String[] registros = new String[1];
				registros[0] = gerencia.getDescripcion();

				return registros;
			}

		
		};

		catalogoGerencia.setClosable(true);
		catalogoGerencia.setWidth("80%");
		catalogoGerencia.setParent(divCatalogoGerencia);
		catalogoGerencia.doModal();
	}

	@Listen("onSeleccion = #divCatalogoGerencia")
	public void seleccionGerencia() {
		Gerencia gerencia = catalogoGerencia.objetoSeleccionadoDelCatalogo();
		idGerencia = gerencia.getId();
		txtGerencia.setValue(gerencia.getDescripcion());
		txtGerencia.setFocus(true);
		catalogoGerencia.setParent(null);
	}
	
	@Listen("onClick = #btnBuscarTrabajador")
	public void mostrarCatalogoTrabajador() {
		final List<Empleado> listEmpleado = servicioEmpleado.buscarTodos();
		catalogoTrabajador = new Catalogo<Empleado>(divCatalogoTrabajador,
				"Catalogo de Empleados", listEmpleado, true,false,false,"Ficha", "Nombre") {

			@Override
			protected List<Empleado> buscar(List<String> valores) {
				List<Empleado> lista = new ArrayList<Empleado>();

				for (Empleado empleado : listEmpleado) {
					if (empleado.getFicha().toLowerCase()
							.startsWith(valores.get(0))
							&& empleado.getNombre().toLowerCase()
									.startsWith(valores.get(1))) {
						lista.add(empleado);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Empleado empleado) {
				String[] registros = new String[2];
				registros[0] = empleado.getFicha();
				registros[1] = empleado.getNombre();

				return registros;
			}

		};

		catalogoTrabajador.setClosable(true);
		catalogoTrabajador.setWidth("80%");
		catalogoTrabajador.setParent(divCatalogoTrabajador);
		catalogoTrabajador.doModal();
	}

	@Listen("onSeleccion = #divCatalogoTrabajador")
	public void seleccionTrabajador() {
		Empleado empleado = catalogoTrabajador.objetoSeleccionadoDelCatalogo();
		nombre = empleado.getNombre();
		txtTrabajador.setValue(empleado.getNombre());
		txtTrabajador.setFocus(true);
		catalogoTrabajador.setParent(null);
	}
	
	@Listen("onClick = #btnBuscarEvaluador")
	public void mostrarCatalogoEvaluador() {
		final List<Empleado> listEmpleado = servicioEmpleado.buscarTodos();
		catalogoEvaluador = new Catalogo<Empleado>(divCatalogoEvaluador,
				"Catalogo de Empleados", listEmpleado,true,false,false, "Ficha","Nombre") {

			@Override
			protected List<Empleado> buscar(List<String> valores) {
				List<Empleado> lista = new ArrayList<Empleado>();

				for (Empleado empleado : listEmpleado) {
					if (empleado.getFicha().toLowerCase()
							.startsWith(valores.get(0))
							&& empleado.getNombre().toLowerCase()
							.startsWith(valores.get(1))) {
						lista.add(empleado);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Empleado empleado) {
				String[] registros = new String[2];
				registros[0] = empleado.getFicha();
				registros[1] = empleado.getNombre();

				return registros;
			}


		};

		catalogoEvaluador.setClosable(true);
		catalogoEvaluador.setWidth("80%");
		catalogoEvaluador.setParent(divCatalogoEvaluador);
		catalogoEvaluador.doModal();
	}

	@Listen("onSeleccion = #divCatalogoEvaluador")
	public void seleccionEvaluador() {
		Empleado empleado = catalogoEvaluador.objetoSeleccionadoDelCatalogo();
		ficha = empleado.getFicha();
		txtEvaluador.setValue(empleado.getNombre());
		txtEvaluador.setFocus(true);
		catalogoEvaluador.setParent(null);
	}
	
	@Listen("onClick = #btnBuscarValoracion")
	public void mostrarCatalogoValoracion() {
		final List<Valoracion> listValoracion = servicioValoracion
				.buscarTodas();
		catalogoValoracion = new Catalogo<Valoracion>(divCatalogoValoracion,
				"Catalogo de Valoracion", listValoracion,true,false,false, "Nombre",
				"Descripción") {

			@Override
			protected List<Valoracion> buscar(List<String> valores) {
				List<Valoracion> lista = new ArrayList<Valoracion>();

				for (Valoracion valoracion : listValoracion) {
					if (valoracion.getNombre().toLowerCase()
							.startsWith(valores.get(0))
							&& valoracion.getDescripcion().toLowerCase()
									.startsWith(valores.get(1))) {
						lista.add(valoracion);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Valoracion valoracion) {
				String[] registros = new String[2];
				registros[0] = valoracion.getNombre();
				registros[1] = valoracion.getDescripcion();

				return registros;
			}

		};

		catalogoValoracion.setClosable(true);
		catalogoValoracion.setWidth("80%");
		catalogoValoracion.setParent(divCatalogoValoracion);
		catalogoValoracion.doModal();
	}

	@Listen("onSeleccion = #divCatalogoValoracion")
	public void seleccionValoracion() {
		Valoracion valoracion = catalogoValoracion
				.objetoSeleccionadoDelCatalogo();
		txtValoracion.setValue(valoracion.getNombre());
		txtValoracion.setFocus(true);
		catalogoValoracion.setParent(null);
	}
	
	
	
	public void mostrarCatalogo() {
		
		evaluaciones = servicioEvaluacion.buscarEvaluacionesRevision();
		catalogoEvaluacion = new CatalagoN <Evaluacion> (catalogoEvaluaciones, "Catalogo", evaluaciones,
				false, false, true,"Ficha", "Nombre Empleado", "Fecha Revisión","Estado Evaluacion","Ficha Evaluador", 
				"Nombre Evaluador","Resultado Objetivos", "Resultado Competencias", "Resultado General", "Valoración", "Resultado Final") {
			

			@Override
			protected List<Evaluacion> buscar(List<String> valores) {

				List<Evaluacion> evaluacion = new ArrayList<Evaluacion>();

				for (Evaluacion evaluacionCampos : evaluaciones) {
					Empleado empleado = servicioEmpleado.buscarPorFicha(evaluacionCampos.getFicha());
					Empleado empleado1 = servicioEmpleado.buscarPorFicha(evaluacionCampos.getFichaEvaluador());
					if ( evaluacionCampos.getFicha().toLowerCase()
									.startsWith(valores.get(0))
							&& String.valueOf(empleado.getNombre()).toLowerCase()
									.startsWith(valores.get(1))
							&& String.valueOf(evaluacionCampos.getFechaRevision()).toLowerCase()
									.startsWith(valores.get(2))
							&& String.valueOf(evaluacionCampos.getEstadoEvaluacion()).toLowerCase()
									.startsWith(valores.get(3))
							&& evaluacionCampos.getFichaEvaluador().toLowerCase()
									.startsWith(valores.get(4))
							&& String.valueOf(empleado1.getNombre()).toLowerCase()
									.startsWith(valores.get(5))
							&& String.valueOf(evaluacionCampos.getResultadoObjetivos()).toLowerCase()
									.startsWith(valores.get(6))
							&& String.valueOf(evaluacionCampos.getResultadoCompetencias()).toLowerCase()
									.startsWith(valores.get(7))
							&& String.valueOf(evaluacionCampos.getResultadoGeneral()).toLowerCase()
									.startsWith(valores.get(8))
							&& String.valueOf(evaluacionCampos.getValoracion()).toLowerCase()
									.startsWith(valores.get(9))
							&& String.valueOf(evaluacionCampos.getResultadoFinal()).toLowerCase()
									.startsWith(valores.get(10))){
						evaluacion.add(evaluacionCampos);
					}
				}
				return evaluacion;
			}

			@Override
			protected String[] crearRegistros(Evaluacion evaluacionE) {
				Empleado empleado = servicioEmpleado.buscarPorFicha(evaluacionE.getFicha());
				Empleado empleado1 = servicioEmpleado.buscarPorFicha(evaluacionE.getFichaEvaluador());
				String[] registros = new String[11];
				registros[0] = evaluacionE.getFicha();
				registros[1] = empleado.getNombre();
				registros[2] = String.valueOf(evaluacionE.getFechaRevision());
				registros[3] = String.valueOf(evaluacionE.getEstadoEvaluacion());
				registros[4] = evaluacionE.getFichaEvaluador();
				registros[5] = empleado1.getNombre();
				registros[6] = String.valueOf(evaluacionE.getResultadoObjetivos());
				registros[7] = String.valueOf(evaluacionE.getResultadoCompetencias());
				registros[8] = String.valueOf(evaluacionE.getResultadoGeneral());
				registros[9] =String.valueOf(evaluacionE.getValoracion());
				registros[10] =String.valueOf(evaluacionE.getResultadoFinal());
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
		String nombreE = txtTrabajador.getValue();
		String nombreEv = txtEvaluador.getValue();
		String gerencia = txtGerencia.getValue();
		String valoracion = txtValoracion.getValue();
		Integer grado = txtGrado.getValue();
		System.out.println(empresa);
		System.out.println(nombreE+ nombreEv+gerencia+valoracion);
		if (empresa.equals("") && nombreE.equals("") && nombreEv.equals("")
				&& gerencia.equals("") && valoracion.equals("") && grado == 0) {
			System.out.println("entrooooooo");
			evaluaciones = servicioEvaluacion.buscarEvaluacionesRevision();
		} 
		else if (nombreE.equals("") && nombreEv.equals("")
				&& gerencia.equals("") && valoracion.equals("") && grado == 0) {
			evaluaciones = servicioEvaluacion.buscarEvaluacionCalibracionEmpresa(empresa);
		}
		else if (empresa.equals("") && nombreEv.equals("")
				&& gerencia.equals("") && valoracion.equals("") && grado == 0) {
			evaluaciones = servicioEvaluacion.buscarEvaluacionCalibracionTrabajador(nombreE);
		}
		else if (empresa.equals("") && nombreE.equals("")
				&& gerencia.equals("") && valoracion.equals("") && grado  == 0) {
			Empleado eva = servicioEmpleado.buscarPorNombre(nombreEv);
			System.out.println(nombreEv);
			
			String ficha = eva.getFicha();
			System.out.println(ficha);
			evaluaciones = servicioEvaluacion.buscarEvaluacionCalibracionEvaluador(ficha);
		}
		else if (empresa.equals("") && nombreE.equals("") && nombreEv.equals("")
				 && valoracion.equals("") && grado  == 0) {
			evaluaciones = servicioEvaluacion.buscarEvaluacionCalibracionGerencia(gerencia);
		} 
		else if (empresa.equals("") && nombreE.equals("") && nombreEv.equals("")
				&& gerencia.equals("")  && grado  == 0) {
			evaluaciones = servicioEvaluacion.buscarEvaluacionCalibracionValoracion(valoracion);
		} 
		else if (empresa.equals("") && nombreE.equals("") && nombreEv.equals("")
				&& gerencia.equals("") && valoracion.equals("") && grado != 0) {
			evaluaciones = servicioEvaluacion.buscarEvaluacionCalibracionGrado(grado);
		} 
		else {
			Empleado eva = servicioEmpleado.buscarPorNombre(nombreEv);
			String ficha = eva.getFicha();
			evaluaciones = servicioEvaluacion.buscarEvaluacionCalibracion(
					empresa, nombreE, ficha, gerencia, valoracion);
		}
		
//		try {
//			catalogoEvaluacion.setParent(catalogoEvaluaciones);
			catalogoEvaluacion.actualizarLista(evaluaciones);
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			catalogoEvaluacion.actualizarLista(evaluaciones);	
//		}
		
		
	
		
	//catalogoEvaluacion.actualizarLista(evaluaciones);
		
		
	}
	
	
	
	@Listen("onClick = #btnGuardar")
	public void mostrarEvaluacion() {

		if (catalogoEvaluacion.obtenerSeleccionados().size() == 1){
			
			Evaluacion eva = catalogoEvaluacion.objetoSeleccionadoDelCatalagoN();
			
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

		}
	
	@Listen("onClick = #btnLimpiar")
	public void limpiar() {
		txtEmpresa.setValue("");
		txtEvaluador.setValue("");
		txtGerencia.setValue("");
		txtTrabajador.setValue("");
		txtValoracion.setValue("");
		catalogoEvaluacion.actualizarLista(new ArrayList<Evaluacion>());
		txtGrado.setText("0");
	}

	}
	
