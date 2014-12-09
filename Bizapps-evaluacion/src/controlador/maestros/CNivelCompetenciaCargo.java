package controlador.maestros;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import modelo.maestros.Area;
import modelo.maestros.Cargo;
import modelo.maestros.Competencia;
import modelo.maestros.Dominio;
import modelo.maestros.Empleado;
import modelo.maestros.NivelCompetenciaCargo;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Catalogo;
import componentes.Mensaje;

public class CNivelCompetenciaCargo extends CGenerico {

	@Wire
	private Window wdwVNivelCompetenciaCargo;
	@Wire
	private Button btnGuardar;
	@Wire
	private Button btnLimpiar;
	@Wire
	private Button btnSalir;
	@Wire
	private Textbox txtCargoNivelCompetenciaCargo;
	@Wire
	private Button btnBuscarCargo;
	@Wire
	private Listbox lsbCompetencia;
	@Wire
	private Div divCatalogoCargo;
	List<Competencia> competenciasDisponibles = new ArrayList<Competencia>();
	List<NivelCompetenciaCargo> nivelCompetencias = new ArrayList<NivelCompetenciaCargo>();
	ListModelList<Dominio> tipos;
	private int idCargo = 0;

	Mensaje msj = new Mensaje();
	Catalogo<Cargo> catalogoCargo;
	protected List<Cargo> listaGeneral = new ArrayList<Cargo>();

	public ListModelList<Dominio> getTipos() {
		tipos = new ListModelList<Dominio>(
				servicioDominio.buscarPorTipo("REQUERIDO"));
		return tipos;
	}

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				titulo = (String) map.get("titulo");
				map.clear();
				map = null;
			}
		}
		txtCargoNivelCompetenciaCargo.setFocus(true);

	}

	@Listen("onClick = #btnBuscarCargo")
	public void mostrarCatalogoCargo() {
		listaGeneral = servicioCargo.buscarTodos();
		catalogoCargo = new Catalogo<Cargo>(divCatalogoCargo,
				"Catalogo de Cargos", listaGeneral,true,false,false, "Descripción", "Nómina",
				"Cargo Auxiliar", "Empresa Auxiliar") {

			@Override
			protected List<Cargo> buscar(List<String> valores) {
				List<Cargo> lista = new ArrayList<Cargo>();

				for (Cargo cargo : listaGeneral) {
					if (cargo.getDescripcion().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& cargo.getNomina().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& cargo.getIdCargoAuxiliar().toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& cargo.getIdEmpresaAuxiliar().toLowerCase()
									.contains(valores.get(3).toLowerCase())) {
						lista.add(cargo);
					}
				}
				return lista;

			}

			@Override
			protected String[] crearRegistros(Cargo cargo) {
				String[] registros = new String[4];
				registros[0] = cargo.getDescripcion();
				registros[1] = cargo.getNomina();
				registros[2] = cargo.getIdCargoAuxiliar();
				registros[3] = cargo.getIdEmpresaAuxiliar();

				return registros;
			}

		};

		catalogoCargo.setClosable(true);
		catalogoCargo.setWidth("80%");
		catalogoCargo.setParent(divCatalogoCargo);
		catalogoCargo.doModal();
	}

	@Listen("onSeleccion = #divCatalogoCargo")
	public void seleccionCargo() {
		Cargo cargo = catalogoCargo.objetoSeleccionadoDelCatalogo();
		idCargo = cargo.getId();
		txtCargoNivelCompetenciaCargo.setValue(cargo.getDescripcion());
		idCargo = cargo.getId();
		catalogoCargo.setParent(null);
		llenarLista();
	}

	@Listen("onChange = #txtCargoNivelCompetenciaCargo")
	public void buscarCargo() {
		List<Cargo> cargos = servicioCargo
				.buscarPorNombres(txtCargoNivelCompetenciaCargo.getValue());

		if (cargos.size() == 0) {
			msj.mensajeAlerta(Mensaje.codigoCargo);
			txtCargoNivelCompetenciaCargo.setFocus(true);
		} else {
			idCargo = cargos.get(0).getId();
			llenarLista();
		}

	}

	@Listen("onClick = #btnLimpiar")
	public void limpiarCampos() {

		idCargo = 0;
		txtCargoNivelCompetenciaCargo.setValue("");
		lsbCompetencia.setModel(new ListModelList<Competencia>());
		txtCargoNivelCompetenciaCargo.setFocus(true);
	}

	public boolean camposLLenos() {
		if (txtCargoNivelCompetenciaCargo.getText().compareTo("") == 0) {
			return false;
		} else
			return true;
	}

	protected boolean validar() {

		if (!camposLLenos()) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else
			return true;

	}

	@Listen("onClick = #btnSalir")
	public void salir() {

		cerrarVentana2(wdwVNivelCompetenciaCargo, titulo,tabs);
	}

	public void llenarLista() {

		competenciasDisponibles = servicioCompetencia.buscarTodas();
		lsbCompetencia.setModel(new ListModelList<Competencia>(
				competenciasDisponibles));
		lsbCompetencia.setMultiple(false);
		lsbCompetencia.setCheckmark(false);
		lsbCompetencia.setMultiple(true);
		lsbCompetencia.setCheckmark(true);
		lsbCompetencia.renderAll();
		Cargo cargo = servicioCargo.buscarCargo(idCargo);
		nivelCompetencias = servicioNivelCompetenciaCargo.buscar(cargo);

		System.out.println("Total Competencias" + " "
				+ lsbCompetencia.getItems().size());
		System.out.println("Competencias del cargo" + " "
				+ nivelCompetencias.size());

		if (nivelCompetencias.size() != 0) {

			for (int i = 0; i < lsbCompetencia.getItems().size(); i++) {

				Listitem listItem = lsbCompetencia.getItemAtIndex(i);
				for (int j = 0; j < nivelCompetencias.size(); j++) {
					if (competenciasDisponibles.get(i).getId() == nivelCompetencias
							.get(j).getCompetencia().getId()) {

						listItem.setSelected(true);
						if (nivelCompetencias.get(j).getDominio() != null) {
							String descripcionDominio = nivelCompetencias
									.get(j).getDominio()
									.getDescripcionDominio();
							System.out.println(descripcionDominio);
							((Combobox) ((listItem.getChildren().get(4)))
									.getFirstChild())
									.setValue((descripcionDominio));
						}

					}

				}

			}

		}

	}

	public List<Competencia> obtenerSeleccionados() {
		List<Competencia> valores = new ArrayList<Competencia>();
		boolean entro = false;
		if (lsbCompetencia.getItemCount() != 0) {
			final List<Listitem> list1 = lsbCompetencia.getItems();
			for (int i = 0; i < list1.size(); i++) {
				if (list1.get(i).isSelected()) {
					Competencia competencia = list1.get(i).getValue();
					entro = true;
					valores.add(competencia);
				}
			}
			if (!entro) {
				valores.clear();
				return valores;
			}
			return valores;
		} else
			return null;
	}

	@Listen("onClick = #btnGuardar")
	public void guardar() {

		boolean errorCampo = false;
		if (validar()) {

			Cargo cargoEmpleado = servicioCargo.buscarCargo(idCargo);

			if (cargoEmpleado != null) {

				if (obtenerSeleccionados().size() != 0) {

					if (lsbCompetencia.getItemCount() != 0) {
						for (int i = 0; i < lsbCompetencia.getItemCount(); i++) {

							if (lsbCompetencia.getItems().get(i).isSelected()) {

								Listitem listItem = lsbCompetencia
										.getItemAtIndex(i);

								if (((Combobox) ((listItem.getChildren().get(4)))
										.getFirstChild()).getValue().equals("")) {

									errorCampo = true;
								}

							}

						}

						System.out.println(errorCampo);
						if (errorCampo == true) {
							Messagebox
									.show("Debe ingresar el dominio de las competencias seleccionadas",
											"Advertencia", Messagebox.OK,
											Messagebox.EXCLAMATION);
						} else {

							for (int i = 0; i < lsbCompetencia.getItemCount(); i++) {

								if (lsbCompetencia.getItems().get(i)
										.isSelected()) {

									Listitem listItem = lsbCompetencia
											.getItemAtIndex(i);

									int codigoCompetencia = ((Intbox) ((listItem
											.getChildren().get(0)))
											.getFirstChild()).getValue();
									String tipoDominio = ((Combobox) ((listItem
											.getChildren().get(4)))
											.getFirstChild()).getValue();

									Cargo cargo = servicioCargo
											.buscarCargo(idCargo);
									Competencia competencia = servicioCompetencia
											.buscarCompetencia(codigoCompetencia);
									// Dominio dominio = servicioDominio
									// .buscarPorNombre(tipoDominio);

									Dominio dominio = servicioDominio
											.buscarPorNombreTipo(tipoDominio,
													"REQUERIDO");

									NivelCompetenciaCargo nivel = new NivelCompetenciaCargo(
											competencia, cargo, dominio);
									servicioNivelCompetenciaCargo
											.guardar(nivel);

								}

							}

							msj.mensajeInformacion(Mensaje.guardado);
							limpiarCampos();
						}

					}

				} else {

					msj.mensajeAlerta(Mensaje.noSeleccionoCompetencia);
					txtCargoNivelCompetenciaCargo.setFocus(true);
				}

			} else {

				msj.mensajeAlerta(Mensaje.codigoCargo);
				txtCargoNivelCompetenciaCargo.setFocus(true);

			}

		}

	}

}
