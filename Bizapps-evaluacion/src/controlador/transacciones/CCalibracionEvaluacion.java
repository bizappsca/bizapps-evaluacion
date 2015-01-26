package controlador.transacciones;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Bitacora;
import modelo.maestros.Empleado;
import modelo.maestros.Empresa;
import modelo.maestros.Evaluacion;
import modelo.maestros.Valoracion;

import modelo.maestros.Gerencia;
import modelo.maestros.UnidadOrganizativa;
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.CatalagoN;
import componentes.Catalogo;
import componentes.Mensaje;
import controlador.maestros.CGenerico;

public class CCalibracionEvaluacion extends CGenerico {

	private static final long serialVersionUID = 1L;

	@Wire
	private Div botoneraUnidadOrganizativa;
	@Wire
	private Textbox txtValoracion;
	@Wire
	private Textbox txtEmpresa;
	@Wire
	private Textbox txtGerencia;
	@Wire
	private Textbox txtTrabajador;
	@Wire
	private Textbox txtEvaluador;
	@Wire
	private Textbox txtValoracio;
	@Wire
	private Button btnBuscarGrado;
	@Wire
	private Button btnBuscarGerencia;
	@Wire
	private Button btnBuscarEmpresa;
	@Wire
	private Button btnBuscarTrabajador;
	@Wire
	private Button btnBuscarEvaluador;
	@Wire
	private Button btnBuscarValoracion;
	@Wire
	private Button btnGuardar;
	@Wire
	private Button btnLimpiar;
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
	private Div divCatalogoValoracion;
	@Wire
	private Div divCatalogoGrado;
	@Wire
	private Window winEvaluacionEmpleado;
	@Wire
	private Button btnVer;

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
	Catalogo<Valoracion> catalogoValoracion;
	Integer[] list = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25 };
	Catalogo<Integer> catalogoGrado;

	List<Evaluacion> evaluaciones;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub

		botonera = new Botonera() {
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
		// botonera.getChildren().get(1).setVisible(true);
		// botonera.getChildren().get(3).setVisible(true);
		// botoneraCalibracion.appendChild(botonera);
		
		
		//actualizarCatalogo();
	}

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(3).setVisible(!bol);

	}

	public void mostrarCatalogo() {
		//System.out.println("size"+catalogoEvaluaciones.getChildren().size());
		evaluaciones = servicioEvaluacion.buscarEvaluacionesRevision();

		catalogoEvaluacion = new CatalagoN<Evaluacion>(catalogoEvaluaciones,
				"Catalogo", evaluaciones, false, false, true, "Ficha",
				"Nombre Empleado", "Grado","Gerencia", "Estado Evaluacion",
				"Ficha Evaluador", "Nombre Evaluador", "Resultado Objetivos",
				"Resultado Competencias", "Resultado General", "Valoración",
				"Resultado Final") {

			@Override
			protected List<Evaluacion> buscar(List<String> valores) {

				List<Evaluacion> evaluacion = new ArrayList<Evaluacion>();

				for (Evaluacion evaluacionCampos : evaluaciones) {
					Empleado empleado = servicioEmpleado
							.buscarPorFicha(evaluacionCampos.getFicha());
					Empleado empleado1 = servicioEmpleado
							.buscarPorFicha(evaluacionCampos
									.getFichaEvaluador());

					if (evaluacionCampos.getFicha().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& String.valueOf(empleado.getNombre())
									.toLowerCase().contains(valores.get(1).toLowerCase())
							&& String
									.valueOf(
											empleado.getGradoAuxiliar())
									.toLowerCase().contains(valores.get(2).toLowerCase())
							&& String
									.valueOf(
											empleado.getUnidadOrganizativa().getGerencia().getDescripcion())
									.toLowerCase().contains(valores.get(3).toLowerCase())
							&& String
									.valueOf(
											evaluacionCampos
													.getEstadoEvaluacion())
									.toLowerCase().contains(valores.get(4).toLowerCase())
							&& evaluacionCampos.getFichaEvaluador()
									.toLowerCase().contains(valores.get(5).toLowerCase())
							&& String.valueOf(empleado1.getNombre())
									.toLowerCase().contains(valores.get(6).toLowerCase())
							&& String
									.valueOf(
											evaluacionCampos
													.getResultadoObjetivos())
									.toLowerCase().contains(valores.get(7).toLowerCase())
							&& String
									.valueOf(
											evaluacionCampos
													.getResultadoCompetencias())
									.toLowerCase().contains(valores.get(8).toLowerCase())
							&& String
									.valueOf(
											evaluacionCampos
													.getResultadoGeneral())
									.toLowerCase().contains(valores.get(9).toLowerCase())
							&& String.valueOf(evaluacionCampos.getValoracion())
									.toLowerCase().contains(valores.get(10).toLowerCase())
							&& String
									.valueOf(
											evaluacionCampos
													.getResultadoFinal())
									.toLowerCase().contains(valores.get(11).toLowerCase())) {
						evaluacion.add(evaluacionCampos);
					}
				}
				return evaluacion;
			}

			@Override
			protected String[] crearRegistros(Evaluacion evaluacionE) {
				Empleado empleado = servicioEmpleado.buscarPorFicha(evaluacionE
						.getFicha());
				Empleado empleado1 = servicioEmpleado
						.buscarPorFicha(evaluacionE.getFichaEvaluador());
				String[] registros = new String[12];
				registros[0] = evaluacionE.getFicha();
				registros[1] = empleado.getNombre();
				registros[2] = String.valueOf(empleado.getGradoAuxiliar());
				registros[3] = String.valueOf(empleado.getUnidadOrganizativa().getGerencia().getDescripcion());
				registros[4] = String
						.valueOf(evaluacionE.getEstadoEvaluacion());
				registros[5] = evaluacionE.getFichaEvaluador();
				registros[6] = empleado1.getNombre();
				registros[7] = String.valueOf(evaluacionE
						.getResultadoObjetivos());
				registros[8] = String.valueOf(evaluacionE
						.getResultadoCompetencias());
				registros[9] = String
						.valueOf(evaluacionE.getResultadoGeneral());
				registros[10] = String.valueOf(evaluacionE.getValoracion());
				registros[11] = (String
						.valueOf(evaluacionE.getResultadoFinal()));
				return registros;

			}

		};
		catalogoEvaluacion.setParent(catalogoEvaluaciones);
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

	@Listen("onClick = #btnBuscarGerencia")
	public void mostrarCatalogoGerencia() {
		final List<Gerencia> listGerencia = servicioGerencia.buscarTodas();
		catalogoGerencia = new Catalogo<Gerencia>(divCatalogoGerencia,
				"Catalogo de Gerencias", listGerencia,true,false,false, "Descripción") {

			@Override
			protected List<Gerencia> buscar(List<String> valores) {
				List<Gerencia> lista = new ArrayList<Gerencia>();

				for (Gerencia gerencia : listGerencia) {
					if (gerencia.getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())) {
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
		catalogoGerencia.setTitle("Catalogo de Gerencias");
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
							.contains(valores.get(0).toLowerCase())
							&& empleado.getNombre().toLowerCase()
									.contains(valores.get(1).toLowerCase())) {
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
		catalogoTrabajador.setTitle("Catalogo de Empleados");
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
				"Catalogo de Empleados", listEmpleado,true,false,false, "Ficha", "Nombre") {

			@Override
			protected List<Empleado> buscar(List<String> valores) {
				List<Empleado> lista = new ArrayList<Empleado>();

				for (Empleado empleado : listEmpleado) {
					if (empleado.getFicha().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& empleado.getNombre().toLowerCase()
									.contains(valores.get(1).toLowerCase())) {
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
		catalogoEvaluador.setTitle("Catalogo de Empleados");
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
							.contains(valores.get(0).toLowerCase())
							&& valoracion.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())) {
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
		catalogoValoracion.setTitle("Catalogo de Valoracion");
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

	@Listen("onClick = #btnBuscar")
	public void actualizarCatalogo() {
		if (catalogoEvaluaciones.getChildren().isEmpty())
			mostrarCatalogo();
		String empresa = txtEmpresa.getValue();
		String nombreE = txtTrabajador.getValue();
		String nombreEv = txtEvaluador.getValue();
		String gerencia = txtGerencia.getValue();
		String valoracion = txtValoracion.getValue();
		//Integer grado = txtGrado.getValue();
		
		String fichaAux = "";
		
		if (empresa.equals("") && nombreE.equals("") && nombreEv.equals("")
				&& gerencia.equals("") && valoracion.equals("") ) {
		
			evaluaciones = servicioEvaluacion.buscarEvaluacionesRevision();
		}
		
		else
		{
			
			if (nombreEv.trim().equals("")==false ) {
				Empleado eva = servicioEmpleado.buscarPorNombre(nombreEv);
				fichaAux = eva.getFicha();
			}
			
			evaluaciones= servicioEvaluacion.buscarEvaluacionCalibracion(empresa, nombreE, fichaAux, gerencia, valoracion, 0);
		}
		
		/*else if (nombreE.equals("") && nombreEv.equals("")
				&& gerencia.equals("") && valoracion.equals("") && grado == 0) {
			evaluaciones = servicioEvaluacion
					.buscarEvaluacionCalibracionEmpresa(empresa);
		} else if (empresa.equals("") && nombreEv.equals("")
				&& gerencia.equals("") && valoracion.equals("") && grado == 0) {
			evaluaciones = servicioEvaluacion
					.buscarEvaluacionCalibracionTrabajador(nombreE);
		} else if (empresa.equals("") && nombreE.equals("")
				&& gerencia.equals("") && valoracion.equals("") && grado == 0) {
			Empleado eva = servicioEmpleado.buscarPorNombre(nombreEv);
			

			fichaAux = eva.getFicha();
			
			evaluaciones = servicioEvaluacion
					.buscarEvaluacionCalibracionEvaluador(fichaAux);
		} else if (empresa.equals("") && nombreE.equals("")
				&& nombreEv.equals("") && valoracion.equals("") && grado == 0) {
			evaluaciones = servicioEvaluacion
					.buscarEvaluacionCalibracionGerencia(gerencia);
		} else if (empresa.equals("") && nombreE.equals("")
				&& nombreEv.equals("") && gerencia.equals("") && grado == 0) {
			evaluaciones = servicioEvaluacion
					.buscarEvaluacionCalibracionValoracion(valoracion);
		} else if (empresa.equals("") && nombreE.equals("")
				&& nombreEv.equals("") && gerencia.equals("")
				&& valoracion.equals("") && grado != 0) {
			evaluaciones = servicioEvaluacion
					.buscarEvaluacionCalibracionGrado(grado);
		} else {
			Empleado eva = servicioEmpleado.buscarPorNombre(nombreEv);
			if (eva != null){
			String ficha = eva.getFicha();
			evaluaciones = servicioEvaluacion.buscarEvaluacionCalibracion(
					empresa, nombreE, ficha, gerencia, valoracion);
		}
		}*/

		catalogoEvaluacion.actualizarLista(evaluaciones);
	}

	// @Listen("onSeleccion = #catalogoEvaluaciones")
	// public void seleccionEValoracion() {
	// Evaluacion e = catalogoEvaluacion.objetoSeleccionadoDelCatalagoN();
	// System.out.println(catalogoEvaluacion.obtenertext());
	// }

	@Listen("onClick = #btnGuardar")
	public void guardarValoracion() {
		String valoracion = "";
		try{
			
			List<Evaluacion> lista = new ArrayList<Evaluacion>();
			lista= catalogoEvaluacion.obtenerSeleccionados();
			
		if (lista.size() > 0) {
			
			
			for (Evaluacion eva : lista) {
				
				
				/*Evaluacion eva = catalogoEvaluacion
						.objetoSeleccionadoDelCatalagoN();*/
				//int idEvaluacion = eva.getIdEvaluacion();
				Evaluacion evaluacion =  eva; //servicioEvaluacion.buscarEvaluacion(idEvaluacion);
				Double resultado = evaluacion.getResultadoFinal();//Double.valueOf(catalogoEvaluacion.obtenertext());
	
				evaluacion.setResultadoFinal(resultado);
				evaluacion.setValoracion(valoracion);
				evaluacion.setValoracion(servicioUtilidad
						.obtenerValoracionFinalSimple(Double
								.parseDouble(resultado.toString())));
				evaluacion.setEstadoEvaluacion("FINALIZADA");
				servicioEvaluacion.guardar(evaluacion);
				Bitacora bitacora = new Bitacora();
				bitacora.setEstadoEvaluacion("PENDIENTE");
				bitacora.setEvaluacion(evaluacion);
				bitacora.setFechaAuditoria(fechaHora);
				bitacora.setHoraAuditoria(horaAuditoria);
				Authentication a = SecurityContextHolder.getContext()
						.getAuthentication();
				bitacora.setIdUsuario(servicioUsuario.buscarUsuarioPorNombre(a.getName()));
				servicioBitacora.guardar(bitacora);
				
				
				
			}
			
			actualizarCatalogo();
			Messagebox.show("Calibración Guardada exitosamente", "Información",
					Messagebox.OK, Messagebox.INFORMATION);
			
			
		}

		else {
			Messagebox.show("Debe seleccionar al menos una evaluación", "Alerta",
					Messagebox.OK, Messagebox.EXCLAMATION);
		}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

	}

	@Listen("onClick = #btnLimpiar")
	public void limpiar() {
		try{
		//catalogoEvaluaciones.detach();
		txtEmpresa.setValue("");
		txtEvaluador.setValue("");
		txtGerencia.setValue("");
		txtTrabajador.setValue("");
		txtValoracion.setValue("");
		catalogoEvaluacion.actualizarLista(new ArrayList<Evaluacion>());
		//txtGrado.setText("0");
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.toString());
	}
	}

	@Listen("onClick = #btnVer")
	public void mostrarEvaluacion() {
		try{
		if (catalogoEvaluacion.obtenerSeleccionados().size() == 1) {

			Evaluacion eva = catalogoEvaluacion
					.objetoSeleccionadoDelCatalagoN();

			Usuario usuario = servicioUsuario.buscarId(eva.getIdUsuario());
			String fichaUsuario = usuario.getFicha();

			if (fichaUsuario.compareTo("0") == 0) {
				Messagebox
						.show("La Evaluación no se puede visualizar ya que es solo un registro historico ( Valoracion Final )",
								"Alerta", Messagebox.OK, Messagebox.EXCLAMATION);
			} else {
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("modo", "EDITAR");
				map.put("id", eva.getIdEvaluacion());
				map.put("titulo", eva.getFicha());
				Sessions.getCurrent().setAttribute("itemsCatalogo", map);

				winEvaluacionEmpleado = (Window) Executions.createComponents(
						"/vistas/transacciones/VAgregarEvaluacion.zul", null,
						map);
				winEvaluacionEmpleado.doModal();
				// winListaPersonal.onClose();

			}

		} else {
			Messagebox.show("Debe seleccionar una evaluación", "Alerta",
					Messagebox.OK, Messagebox.EXCLAMATION);
		}

	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.toString());
	}
	}

}
