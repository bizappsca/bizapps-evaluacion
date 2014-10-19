package controlador.transacciones;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import java.io.File;

import jxl.*;
import jxl.read.biff.BiffException;

import modelo.maestros.Actividad;
import modelo.maestros.ActividadCurso;
import modelo.maestros.Area;
import modelo.maestros.Curso;
import modelo.maestros.Empleado;
import modelo.maestros.EmpleadoCurso;
import modelo.maestros.NombreCurso;
import modelo.maestros.Periodo;
import modelo.seguridad.Arbol;

import org.springframework.data.jpa.repository.Query;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;

import arbol.CArbol;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;
import controlador.maestros.CGenerico;

public class CGestionarAdiestramiento extends CGenerico {

	private static final long serialVersionUID = 84966503677381446L;

	@Wire
	private Window wdwVGestionarAdiestramiento;
	@Wire
	private Button btnSeleccionarArchivo;
	@Wire
	private Textbox txtArchivoAdiestramiento;
	@Wire
	private Button btnSubirArchivo;
	@Wire
	private Div botoneraAdiestramiento;
	@Wire
	private Media mediaAdiestramiento;
	@Wire
	private Textbox txtPeriodoGestionarAdiestramiento;
	@Wire
	private Button btnBuscarPeriodo;
	@Wire
	private Radiogroup rdgGestionarAdiestramiento;
	@Wire
	private Radio rbExportar;
	@Wire
	private Radio rbImportar;
	@Wire
	private Groupbox gbImportar;
	@Wire
	private Groupbox gbExportar;
	@Wire
	private Listbox lsbAdiestramientos;
	@Wire
	private Div divCatalogoPeriodo;

	private static SimpleDateFormat formatoFecha = new SimpleDateFormat(
			"dd/MM/yyyy");
	DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
	DateFormat df1 = DateFormat.getDateInstance(DateFormat.MEDIUM);
	List<String> erroresGenerados = new ArrayList<String>();
	List<Curso> cursos = new ArrayList<Curso>();
	List<EmpleadoCurso> empleadoCursos = new ArrayList<EmpleadoCurso>();
	List<EmpleadoCurso> cursosPeriodo = new ArrayList<EmpleadoCurso>();
	private int idRow = 0;
	private int idNombreCurso = 0;
	private int idCurso = 0;
	private int idPeriodo = 0;
	Curso curso = new Curso();
	NombreCurso nombreCurso = new NombreCurso();
	Periodo periodo = new Periodo();
	Empleado empleado = new Empleado();
	Area area = new Area();
	File archivoPlanificacion;
	int fila = 1;
	int filaEvaluada = 0;
	int filaInvalida = 0;
	int lineasEvaluadas = 0;
	int lineasValidas = 0;
	int lineasInvalidas = 0;
	int errores = 0;
	int errorLinea = 0;
	boolean parar = false;

	Catalogo<Periodo> catalogoPeriodo;

	private CArbol cArbol = new CArbol();

	@Override
	public void inicializar() throws IOException {

		contenido = (Include) wdwVGestionarAdiestramiento.getParent();
		Tabbox tabox = (Tabbox) wdwVGestionarAdiestramiento.getParent()
				.getParent().getParent().getParent();
		tabBox = tabox;
		tab = (Tab) tabox.getTabs().getLastChild();
		HashMap<String, Object> mapa = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (mapa != null) {
			if (mapa.get("tabsGenerales") != null) {
				tabs = (List<Tab>) mapa.get("tabsGenerales");
				mapa.clear();
				mapa = null;
			}
		}

		txtArchivoAdiestramiento.setPlaceholder("Ningún archivo seleccionado");
		txtArchivoAdiestramiento.setStyle("color:black !important;");
		Botonera botonera = new Botonera() {

			@Override
			public void salir() {

				cerrarVista();

			}

			@Override
			public void limpiar() {
				limpiarCampos();
			}

			@Override
			public void guardar() {

			}

			@Override
			public void eliminar() {

			}

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub

			}
		};
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botoneraAdiestramiento.appendChild(botonera);

	}

	/**
	 * Metodo que permite validar que el documento posee una extension adecuada
	 * asi como tambien permite subirlo a la vista
	 * 
	 * @param event
	 *            archivo subido al sistema por el usuario
	 * @throws IOException
	 */
	@Listen("onUpload = #btnSeleccionarArchivo")
	public void seleccionarArchivo(UploadEvent event) throws IOException {

		mediaAdiestramiento = event.getMedia();
		if (mediaAdiestramiento != null) {

			if (mediaAdiestramiento.getContentType().equals(
					"application/vnd.ms-excel"))

			{
				txtArchivoAdiestramiento
						.setValue(mediaAdiestramiento.getName());

			} else {
				Messagebox
						.show(mediaAdiestramiento.getName()
								+ " No es un tipo de archivo valido!, el archivo debe tener la extensión .xls",
								"Error", Messagebox.OK, Messagebox.ERROR);
			}
		}
	}

	@Listen("onClick = #btnSubirArchivo")
	public void subirArchivo() throws BiffException, IOException,
			ParseException {

		lineasInvalidas = 0;

		if (mediaAdiestramiento != null) {

			// Pasamos el excel que vamos a leer
			Workbook workbook = Workbook.getWorkbook(mediaAdiestramiento
					.getStreamData());
			// Seleccionamos la hoja que vamos a leer
			Sheet sheet = workbook.getSheet(0);

			String usuario = nombreUsuarioSesion();
			Timestamp fechaAuditoria = new Timestamp(new Date().getTime());
			errores = 0;

			// Metodo para recorrer el archivo excel y verificar que no existan
			// errores

			while (fila < sheet.getRows()) {
				if (isNumeric(sheet.getCell(0, fila).getContents())) {

					errorLinea = 0;

					String ficha = sheet.getCell(0, fila).getContents();
					String nombreEmpleado = sheet.getCell(1, fila)
							.getContents();
					String nombrecurso = sheet.getCell(2, fila).getContents();
					String nombreArea = sheet.getCell(3, fila).getContents();
					String tipoFormacion = sheet.getCell(4, fila).getContents();
					String nombrePeriodo = sheet.getCell(5, fila).getContents();
					String gerencia = sheet.getCell(6, fila).getContents();
					String cargo = sheet.getCell(7, fila).getContents();
					

					// VALIDACION FICHA
					empleado = servicioEmpleado.buscarPorFichaYNombre(ficha,
							nombreEmpleado);

					if (empleado == null) {

						errores = errores + 1;
						errorLinea = errorLinea + 1;
					}

					// VALIDACION NOMBRE_CURSO
					nombreCurso = servicioNombreCurso
							.buscarPorNombre(nombrecurso);

					if (nombreCurso == null) {

						// VALIDACION AREA
						area = servicioArea.buscarPorNombre(nombreArea);

						if (area == null) {

							errores = errores + 1;
							errorLinea = errorLinea + 1;

						}

					} else {

						// VALIDACION AREA
						area = servicioArea.buscarPorNombre(nombreArea);

						if (area == null) {

							errores = errores + 1;
							errorLinea = errorLinea + 1;

						}

					}

					// VALIDACION PERIODO

					periodo = servicioPeriodo.buscarPorNombre(nombrePeriodo);

					if (periodo == null) {

						errores = errores + 1;
						errorLinea = errorLinea + 1;
					}

					if (errorLinea != 0) {
						lineasInvalidas = lineasInvalidas + 1;

					} else {

						lineasValidas = lineasValidas + 1;
					}

					filaEvaluada++;
					fila++;

				} else {

					filaInvalida++;

				}

			}

			if (lineasInvalidas > 0) {

				System.out.println("Entre 1");

				limpiarCampos();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", "consulta");
				map.put("archivo", mediaAdiestramiento.getName());
				map.put("lineasEvaluadas", filaEvaluada);
				map.put("lineasValidas", lineasValidas);
				map.put("lineasInvalidas", lineasInvalidas);
				Sessions.getCurrent().setAttribute("itemsCatalogo", map);
				List<Arbol> arboles = servicioArbol
						.buscarPorArbolPorNombre("Resultado Importacion");
				if (!arboles.isEmpty()) {
					Arbol arbolItem = arboles.get(0);
					cArbol.abrirVentanas(arbolItem, tabBox, contenido, tab,
							tabs);
				}

			} else {

				System.out.println("Entre 2");

				// Copiar archivo excel en el directorio C:\files\
				Files.copy(
						new File("C:\\files\\" + mediaAdiestramiento.getName()),
						mediaAdiestramiento.getStreamData());

				// Insercion de los datos en la base de datos

				fila = 1;
				filaEvaluada = 0;
				filaInvalida = 0;
				lineasEvaluadas = 0;
				lineasValidas = 0;
				lineasInvalidas = 0;
				errores = 0;

				// Metodo para recorrer el archivo excel y verificar que no
				// existan
				// errores

				while (fila < sheet.getRows()) {
					if (isNumeric(sheet.getCell(0, fila).getContents())) {

						errorLinea = 0;

						String ficha = sheet.getCell(0, fila).getContents();
						String nombreEmpleado = sheet.getCell(1, fila)
								.getContents();
						String nombrecurso = sheet.getCell(2, fila)
								.getContents();
						String nombreArea = sheet.getCell(3, fila)
								.getContents();
						String tipoFormacion = sheet.getCell(4, fila)
								.getContents();
						String nombrePeriodo = sheet.getCell(5, fila)
								.getContents();
						String gerencia = sheet.getCell(6, fila).getContents();
						String cargo = sheet.getCell(7, fila).getContents();
						

						// VALIDACION FICHA
						empleado = servicioEmpleado.buscarPorFicha(ficha);

						if (empleado == null) {

							errores = errores + 1;

						}

						// VALIDACION PERIODO

						periodo = servicioPeriodo
								.buscarPorNombre(nombrePeriodo);

						if (periodo == null) {

							errores = errores + 1;

						}

						// VALIDACION NOMBRE_CURSO
						nombreCurso = servicioNombreCurso
								.buscarPorNombre(nombrecurso);

						if (nombreCurso == null) {

							// VALIDACION AREA
							area = servicioArea.buscarPorNombre(nombreArea);

							if (area == null) {

								errores = errores + 1;

							} else {

								System.out.println("Entre 3");

								NombreCurso curso = new NombreCurso(
										idNombreCurso, area, nombrecurso,
										fechaAuditoria, horaAuditoria, usuario);
								servicioNombreCurso.guardar(curso);

								System.out.println(periodo);

								if (periodo != null) {

									System.out.println("Entre 4");

									NombreCurso ultimoCurso = servicioNombreCurso
											.buscarUltimoCurso();

									System.out.println(ultimoCurso);

									Curso configurarCurso = new Curso(idCurso,
											periodo, ultimoCurso, null, null,
											0, null, null, "ACTIVO",
											fechaAuditoria, horaAuditoria,
											usuario);
									servicioCurso.guardar(configurarCurso);

									Curso ultimoCursoRegistrado = servicioCurso
											.buscarUltimoCurso();

									System.out.println(ultimoCursoRegistrado);

									EmpleadoCurso cursosEmpleado = new EmpleadoCurso(
											ultimoCursoRegistrado, empleado,
											"EN TRAMITE");
									servicioEmpleadoCurso
											.guardar(cursosEmpleado);

								}

							}

						} else {

							// VALIDACION AREA
							Area area = servicioArea
									.buscarPorNombre(nombreArea);

							if (area == null) {

								errores = errores + 1;

							}

							Curso configurarCurso = new Curso(idCurso, periodo,
									nombreCurso, null, null, 0, null, null,
									"ACTIVO", fechaAuditoria, horaAuditoria,
									usuario);

							Curso ultimoCursoRegistrado = servicioCurso
									.buscarUltimoCurso();

							EmpleadoCurso cursosEmpleado = new EmpleadoCurso(
									ultimoCursoRegistrado, empleado,
									"EN TRAMITE");
							servicioEmpleadoCurso.guardar(cursosEmpleado);

						}

						if (errorLinea != 0) {
							lineasInvalidas = lineasInvalidas + 1;

						} else {

							lineasValidas = lineasValidas + 1;
						}

						fila++;
						filaEvaluada++;

					} else {

						filaInvalida++;

					}
				}

				limpiarCampos();
				final HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", "consulta");
				map.put("archivo", mediaAdiestramiento.getName());
				map.put("lineasEvaluadas", filaEvaluada);
				map.put("lineasValidas", lineasValidas);
				map.put("lineasInvalidas", lineasInvalidas);
				Sessions.getCurrent().setAttribute("itemsCatalogo", map);
				List<Arbol> arboles = servicioArbol
						.buscarPorArbolPorNombre("Resultado Importacion");
				if (!arboles.isEmpty()) {
					Arbol arbolItem = arboles.get(0);
					cArbol.abrirVentanas(arbolItem, tabBox, contenido, tab,
							tabs);
				}

			}

		} else {

			Messagebox.show("Debe seleccionar un archivo", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);

		}

	}

	private void limpiarCampos() {
		idRow = 0;
		idPeriodo = 0;
		txtArchivoAdiestramiento.setText("");
		txtArchivoAdiestramiento.setPlaceholder("Ningún archivo seleccionado");
		txtArchivoAdiestramiento.setStyle("color:black !important;");
		txtPeriodoGestionarAdiestramiento.setValue("");
		rdgGestionarAdiestramiento.setSelectedItem(null);
		lsbAdiestramientos.setModel(new ListModelList<EmpleadoCurso>());
		gbImportar.setVisible(false);
		gbExportar.setVisible(false);

	}

	private void cerrarVista() {

		cerrarVentana(wdwVGestionarAdiestramiento, "Gestionar DNA", tabs);

	}

	// Metodo para validar si un dato es numerico o no
	private static boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	@Listen("onClick = #btnBuscarPeriodo")
	public void buscarPeriodo() {

		final List<Periodo> listPeriodo = servicioPeriodo.buscarTodos();
		catalogoPeriodo = new Catalogo<Periodo>(divCatalogoPeriodo,
				"Catalogo de Periodos", listPeriodo, true, false, false,
				"Nombre", "Descripción", "Fecha Inicio", "Fecha Fin", "Estado") {

			@Override
			protected List<Periodo> buscar(List<String> valores) {
				List<Periodo> lista = new ArrayList<Periodo>();

				for (Periodo periodo : listPeriodo) {
					if (periodo.getNombre().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& periodo.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& String
									.valueOf(
											formatoFecha.format(periodo
													.getFechaInicio()))
									.toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& String
									.valueOf(
											formatoFecha.format(periodo
													.getFechaFin()))
									.toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& periodo.getEstadoPeriodo().toLowerCase()
									.contains(valores.get(4).toLowerCase())) {
						lista.add(periodo);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Periodo periodo) {
				String[] registros = new String[6];
				registros[0] = periodo.getNombre();
				registros[1] = periodo.getDescripcion();
				registros[2] = formatoFecha.format(periodo.getFechaInicio());
				registros[3] = formatoFecha.format(periodo.getFechaFin());
				registros[4] = periodo.getEstadoPeriodo();

				return registros;
			}
		};
		catalogoPeriodo.setClosable(true);
		catalogoPeriodo.setWidth("80%");
		catalogoPeriodo.setParent(divCatalogoPeriodo);
		catalogoPeriodo.doModal();

	}

	@Listen("onSeleccion = #divCatalogoPeriodo")
	public void seleccionPeriodo() {
		Periodo periodo = catalogoPeriodo.objetoSeleccionadoDelCatalogo();
		idPeriodo = periodo.getId();
		txtPeriodoGestionarAdiestramiento.setValue(periodo.getNombre());
		llenarLista();
		catalogoPeriodo.setParent(null);
	}

	public void llenarLista() {

		if (idPeriodo != 0) {
			Periodo periodoCursos = servicioPeriodo.buscarPeriodo(idPeriodo);
			cursos = servicioCurso.buscarPorPeriodo(periodoCursos);
		
			empleadoCursos = servicioEmpleadoCurso.buscarTodos();
			
			//metodo para buscar todos los DNA de acuerdo al periodo seleccionado
			
			for(int i = 0; i < cursos.size(); i++ ){
				
				
				for(int j = 0; j < empleadoCursos.size() ; j++){
					
					
					if(cursos.get(i).getId() == empleadoCursos.get(j).getCurso().getId()){
						
						if(empleadoCursos.get(j).getEstadoCurso().equals("EN TRAMITE")){
							
							EmpleadoCurso cursosEmpleados = empleadoCursos.get(j);
							cursosPeriodo.add(cursosEmpleados);
							
						}
						
					}
					
					
				}
				
				
			}
			
			lsbAdiestramientos.setModel(new ListModelList<EmpleadoCurso>(cursosPeriodo));
			
			
			
			
			
		}

		

	}

}
