package controlador.transacciones;

import java.awt.Desktop;
import java.io.FileOutputStream;
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

import modelo.beans.BeanCapacitacionRequerida;
import modelo.maestros.Actividad;
import modelo.maestros.ActividadCurso;
import modelo.maestros.Area;
import modelo.maestros.Curso;
import modelo.maestros.Empleado;
import modelo.maestros.EmpleadoCurso;
import modelo.maestros.NombreCurso;
import modelo.maestros.Periodo;
import modelo.maestros.Revision;
import modelo.seguridad.Arbol;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
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
	private Button btnExportarAdiestramientos;
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
	private int idRevision = 0;
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

	Catalogo<Revision> catalogoPeriodo;

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
				titulo = (String) mapa.get("titulo");
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
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(3).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
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
		
		periodo = servicioPeriodo.buscarPeriodoActivo();
		
		if (periodo!=null)
		{
			
		
		

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
					/*String nombreEmpleado = sheet.getCell(1, fila)
							.getContents();*/
					String nombrecurso = sheet.getCell(2, fila).getContents();
					String nombreArea = sheet.getCell(3, fila).getContents();
					String tipoFormacion = sheet.getCell(4, fila).getContents();
					//String nombrePeriodo = sheet.getCell(5, fila).getContents();
					/*String gerencia = sheet.getCell(6, fila).getContents();
					String cargo = sheet.getCell(7, fila).getContents();*/

					// VALIDACION FICHA
					empleado = servicioEmpleado.buscarPorFicha(ficha);

					if (empleado == null) {

						errores = errores + 1;
						errorLinea = errorLinea + 1;
					}

					// VALIDACION NOMBRE_CURSO
					nombreCurso = servicioNombreCurso
							.buscarPorNombre(nombrecurso);

					if (nombreCurso == null) {
						//lineasInvalidas = lineasInvalidas + 1;
						errores = errores + 1;
						errorLinea = errorLinea + 1;
					} 
					
					area = servicioArea.buscarPorNombresTipoFormacion(nombreArea,tipoFormacion);

					if (area == null) {
						//lineasInvalidas = lineasInvalidas + 1;
						errores = errores + 1;
						errorLinea = errorLinea + 1;

					}

					// VALIDACION PERIODO

					/*periodo = servicioPeriodo.buscarPorNombre(nombrePeriodo);

					if (periodo == null) {

						errores = errores + 1;
						errorLinea = errorLinea + 1;
					}

					if (errorLinea != 0) {
						lineasInvalidas = lineasInvalidas + 1;

					} else {

						lineasValidas = lineasValidas + 1;
					}*/

					filaEvaluada++;
					fila++;

				} else {

					filaInvalida++;

				}

			}

			if (lineasInvalidas > 0) {

				

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

				
				// CUANDO NO HAY ERRORES ENTRA POR ACA A INSERTAR

				// Copiar archivo excel en el directorio C:\files\
				/*Files.copy(
						new File("C:\\files\\" + mediaAdiestramiento.getName()),
						mediaAdiestramiento.getStreamData());*/

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
						/*String nombreEmpleado = sheet.getCell(1, fila)
								.getContents();*/
						String nombrecurso = sheet.getCell(2, fila)
								.getContents();
						String nombreArea = sheet.getCell(3, fila)
								.getContents();
						String tipoFormacion = sheet.getCell(4, fila)
								.getContents();
						/*String nombrePeriodo = sheet.getCell(5, fila)
								.getContents();*/
						/*String gerencia = sheet.getCell(6, fila).getContents();
						String cargo = sheet.getCell(7, fila).getContents();*/

						// VALIDACION FICHA
						empleado = servicioEmpleado.buscarPorFicha(ficha);

					
						nombreCurso = servicioNombreCurso
								.buscarPorNombre(nombrecurso);
						
						

						// SI CURSO NO EXISTE LO CREA
						if (nombreCurso == null) {
							
							area = servicioArea.buscarPorNombresTipoFormacion(nombreArea,tipoFormacion);
							
							NombreCurso curso = new NombreCurso(
									idNombreCurso, area, nombrecurso,
									fechaAuditoria, horaAuditoria, usuario);
							servicioNombreCurso.guardar(curso);
							
							NombreCurso ultimoCurso = servicioNombreCurso
									.buscarUltimoCurso();

							Curso configurarCurso = new Curso(idCurso,
									periodo, ultimoCurso, null, null,
									0, null, null, "ACTIVO",
									fechaAuditoria, horaAuditoria,
									usuario);
							servicioCurso.guardar(configurarCurso);

							
							

						} 
						
						Curso ultimoCursoRegistrado = servicioCurso
								.buscarUltimoCurso();

						EmpleadoCurso cursosEmpleado = new EmpleadoCurso(
								ultimoCursoRegistrado, empleado,
								"EN TRAMITE","NO");
						servicioEmpleadoCurso.guardar(cursosEmpleado);
						
						/*Messagebox.show("Se importo correctamente la informacion de los cursos", "Advertencia",
								Messagebox.OK, Messagebox.INFORMATION );*/

						

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
		else {

			Messagebox.show("Debe existir un periodo activo para poder continuar", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);

		}
		

	}

	private void limpiarCampos() {
		idRow = 0;
		idRevision = 0;
		txtArchivoAdiestramiento.setText("");
		txtArchivoAdiestramiento.setPlaceholder("Ningún archivo seleccionado");
		txtArchivoAdiestramiento.setStyle("color:black !important;");
		txtPeriodoGestionarAdiestramiento.setValue("");
		rdgGestionarAdiestramiento.setSelectedItem(null);
		lsbAdiestramientos.setModel(new ListModelList<BeanCapacitacionRequerida>());
		gbImportar.setVisible(false);
		gbExportar.setVisible(false);

	}

	private void cerrarVista() {

		cerrarVentana(wdwVGestionarAdiestramiento, titulo, tabs);

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

		final List<Revision> listPeriodo = servicioRevision.buscarTodas();
		catalogoPeriodo =  new Catalogo<Revision>(divCatalogoPeriodo,
				"Catalogo de Revisiones", listPeriodo, false,false,false,"Periodo",
				"Descripción", "Estado") {

			@Override
			protected List<Revision> buscar(List<String> valores) {
				List<Revision> lista = new ArrayList<Revision>();

				for (Revision revision : listPeriodo) {
					if (revision.getPeriodo().getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& revision.getDescripcion().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& revision.getEstadoRevision().toLowerCase()
									.contains(valores.get(2).toLowerCase())) {
						lista.add(revision);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Revision revision) {
				String[] registros = new String[3];
				registros[0] = revision.getPeriodo().getDescripcion();
				registros[1] = revision.getDescripcion();
				registros[2] = revision.getEstadoRevision();

				return registros;
			}

		};
		
		
		catalogoPeriodo.setClosable(true);
		catalogoPeriodo.setWidth("80%");
		catalogoPeriodo.setParent(divCatalogoPeriodo);
		catalogoPeriodo.setTitle("Catalogo de Revisiones");
		catalogoPeriodo.doModal();

	}

	@Listen("onSeleccion = #divCatalogoPeriodo")
	public void seleccionPeriodo() {
		Revision revision = catalogoPeriodo.objetoSeleccionadoDelCatalogo();
		idRevision = revision.getId();
		txtPeriodoGestionarAdiestramiento.setValue(revision.getDescripcion());
		llenarLista();
		catalogoPeriodo.setParent(null);
	}

	public void llenarLista() {

		if (idRevision != 0) {
			/*Periodo periodoCursos = servicioPeriodo.buscarPeriodo(idPeriodo);
			cursos = servicioCurso.buscarPorPeriodo(periodoCursos);

			empleadoCursos = servicioEmpleadoCurso.buscarTodos();

			for (int i = 0; i < cursos.size(); i++) {

				for (int j = 0; j < empleadoCursos.size(); j++) {

					if (cursos.get(i).getId() == empleadoCursos.get(j)
							.getCurso().getId()) {

						if (empleadoCursos.get(j).getEstadoCurso()
								.equals("EN TRAMITE")) {

							EmpleadoCurso cursosEmpleados = empleadoCursos
									.get(j);
							cursosPeriodo.add(cursosEmpleados);

						}

					}

				}

			}*/
			
			List<BeanCapacitacionRequerida> lista= servicioUtilidad.getListaCapacitacionRequerida(idRevision);

			lsbAdiestramientos.setModel(new ListModelList<BeanCapacitacionRequerida>(lista));

		}

	}
	
	@Listen("onClick = #btnExportarAdiestramientos")
	public void exportar() throws IOException {
		System.out.println(lsbAdiestramientos.getItemCount());
		if (lsbAdiestramientos.getItemCount() != 0) {
			String s = ";";
			final StringBuffer sb = new StringBuffer();

			for (Object head : lsbAdiestramientos.getHeads()) {
				String h = "";
				if (head instanceof Listhead) {
					for (Object header : ((Listhead) head).getChildren()) {
						h += ((Listheader) header).getLabel() + s;
					}
					sb.append(h + "\n");
				}
			}
			for (Object item : lsbAdiestramientos.getItems()) {
				int x=1;
				String i = "";
				for (Object cell : ((Listitem) item).getChildren()) {
					i += ((Listcell) cell).getLabel().trim().replaceAll("[\n\r]","") + s;
				}
				sb.append(i + "\n");
				System.out.println("PASE: " + x);
			}
			
			Messagebox.show(Mensaje.exportar, "Alerta", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								Filedownload.save(sb.toString().getBytes(),
										"text/plain", "CapacitacionRequeridaRevision.csv");
							}
						}
					});
		} else
			msj.mensajeAlerta(Mensaje.noHayRegistros);
	}

	
	public void exportarDatos() throws IOException {
		
		List<BeanCapacitacionRequerida> lista= servicioUtilidad.getListaCapacitacionRequerida(idRevision);

		if (lista.size() != 0) {

			/*Periodo periodoSeleccionado = servicioPeriodo
					.buscarPeriodo(idRevision);*/

			/* La ruta donde se creara el archivo */
			String rutaArchivo = System.getProperty("user.home") + "/DNA" + "_"
					+ lista.get(0).getRevision() + ".xls";
			/* Se crea el objeto de tipo File con la ruta del archivo */
			File archivoXLS = new File(rutaArchivo);
			/* Si el archivo existe se elimina */
			if (archivoXLS.exists())
				archivoXLS.delete();
			/* Se crea el archivo */
			archivoXLS.createNewFile();
			/* Se crea el libro de excel usando el objeto de tipo Workbook */
			HSSFWorkbook libro = new HSSFWorkbook();
			/* Se inicializa el flujo de datos con el archivo xls */
			FileOutputStream archivo = new FileOutputStream(archivoXLS);

			/*
			 * Utilizamos la clase Sheet para crear una nueva hoja de trabajo
			 * dentro del libro que creamos anteriormente
			 */
			HSSFSheet hoja = libro.createSheet("Hoja 1");

			/*
			 * Hacemos un ciclo para inicializar los valores de las filas de
			 * celdas
			 */
			System.out.println(lista.size());
			for (int f = 0; f <= lista.size(); f++) {
				/* La clase Row nos permitira crear las filas */
				Row fila = hoja.createRow(f);

				/* Creamos la celda a partir de la fila actual */
				Cell celda0 = fila.createCell(0);
				Cell celda1 = fila.createCell(1);
				Cell celda2 = fila.createCell(2);
				Cell celda3 = fila.createCell(3);
				Cell celda4 = fila.createCell(4);
				Cell celda5 = fila.createCell(5);
				Cell celda6 = fila.createCell(6);
				Cell celda7 = fila.createCell(7);
				Cell celda8 = fila.createCell(8);
				Cell celda9 = fila.createCell(9);

				/* Si la fila es la numero 0, estableceremos los encabezados */
				if (f == 0) {
					celda0.setCellValue("Ficha");
					celda1.setCellValue("Nombre");
					celda2.setCellValue("Capacitación");
					celda3.setCellValue("Área");
					celda4.setCellValue("Tipo Formación");
					celda5.setCellValue("Urgencia");
					celda6.setCellValue("Gerencia");
					celda7.setCellValue("Cargo");
					celda8.setCellValue("Grado");
					celda9.setCellValue("Revision");
					
				} else {
					/* Si no es la primera fila establecemos un valor */
					celda0.setCellValue(lista.get(f - 1).getFicha());
					celda1.setCellValue(lista.get(f - 1).getNombre());
					celda2.setCellValue(lista.get(f - 1).getCapacitacion());
					celda3.setCellValue(lista.get(f - 1).getArea());
					celda4.setCellValue(lista.get(f - 1).getTipoFormacion());
					celda5.setCellValue(lista.get(f - 1).getUrgencia());
					celda6.setCellValue(lista.get(f - 1).getGerencia());
					celda7.setCellValue(lista.get(f - 1).getCargo());
					celda8.setCellValue(lista.get(f - 1).getGrado());
					celda9.setCellValue(lista.get(f - 1).getRevision());
					
					
				}

			}

			/* Escribimos en el libro */
			libro.write(archivo);
			/* Cerramos el flujo de datos */
			archivo.close();
			/* Y abrimos el archivo con la clase Desktop */
			Desktop.getDesktop().open(archivoXLS);

		} else {

			Messagebox.show("No existen datos para realizar la exportación",
					"Error", Messagebox.OK, Messagebox.ERROR);

		}

	}

}
