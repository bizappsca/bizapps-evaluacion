package controlador.seguridad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import modelo.maestros.Valoracion;
import modelo.seguridad.Arbol;
import modelo.seguridad.Grupo;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import arbol.MArbol;
import arbol.Nodos;

import componentes.Botonera;
import componentes.Catalogo;

import controlador.maestros.CGenerico;

public class CGrupo extends CGenerico {

	@Wire
	private Tree treeGrupo;
	@Wire
	private Textbox txtNombreGrupo;
	@Wire
	private Listbox ltbFuncionalidadesSeleccionados;
	@Wire
	private Div botoneraGrupo;
	@Wire
	private Div catalogoGrupo;
	@Wire
	private Div divGrupo;
	long id = 0;
	TreeModel _model;
	Catalogo<Grupo> catalogo;
	public static List<String> funcionalidades = new ArrayList<String>();

	private static final long serialVersionUID = 3771289676166008495L;

	@Override
	public void inicializar() throws IOException {
		treeGrupo.setModel(getModel());
		treeGrupo.setCheckmark(true);
		treeGrupo.setMultiple(true);
		ltbFuncionalidadesSeleccionados.getItems().clear();
		Botonera botonera = new Botonera() {

			@Override
			public void salir() {
				cerrarVentana(divGrupo, "Grupo");
			}

			@Override
			public void limpiar() {
				metodoLimpiar();
			}

			@Override
			public void guardar() {
				if (validar()) {
					List<Arbol> listaArbol = servicioArbol.listarArbol();
					Set<Arbol> arboles = new HashSet<Arbol>();
					Treechildren treeChildren = treeGrupo.getTreechildren();
					Collection<Treeitem> lista = treeChildren.getItems();
					String nombreGrupo = txtNombreGrupo.getValue();
//					Grupo grupo = servicioGrupo.buscarPorNombre(nombreGrupo);
//					if (id == 0 && grupo == null || id != 0) {
						for (int i = 0; i < listaArbol.size(); i++) {
							for (Iterator<?> iterator = lista.iterator(); iterator
									.hasNext();) {
								Treeitem treeitem = (Treeitem) iterator.next();
								if (listaArbol.get(i).getNombre()
										.equals(treeitem.getLabel())) {
									if (treeitem.isSelected()) {

										Arbol arbol = listaArbol.get(i);
										arboles.add(arbol);
									}
								}
							}
						}
						Boolean estatus = true;
						String nombre = txtNombreGrupo.getValue();
						Grupo grupo1 = new Grupo(id, estatus, nombre, arboles);
						servicioGrupo.guardarGrupo(grupo1);
						Messagebox.show("Registro Guardado Exitosamente",
								"Informacion", Messagebox.OK,
								Messagebox.INFORMATION);
						limpiar();
//					} else {
//						Messagebox.show("Nombre de Grupo no Disponible",
//								"Informacion", Messagebox.OK,
//								Messagebox.INFORMATION);
//						limpiar();
//					}
				}
			}

			@Override
			public void eliminar() {
				if (id != 0) {
					Messagebox.show("¿Esta Seguro de Eliminar el Grupo?",
							"Alerta", Messagebox.OK | Messagebox.CANCEL,
							Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener<Event>() {
								public void onEvent(Event evt)
										throws InterruptedException {
									if (evt.getName().equals("onOK")) {
										Grupo grupo = servicioGrupo.buscarGrupo(id);
										servicioGrupo.eliminar(grupo);
										limpiar();
										Messagebox
												.show("Registro Eliminado Exitosamente",
														"Informacion",
														Messagebox.OK,
														Messagebox.INFORMATION);

									}
								}
							});
				} else
					Messagebox.show("No ha Seleccionado Ningun Registro",
							"Alerta", Messagebox.OK, Messagebox.EXCLAMATION);
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
		botoneraGrupo.appendChild(botonera);
	}

	public TreeModel getModel() {
		if (_model == null) {
			_model = new MArbol(getFooRoot());
		}
		return _model;
	}

	private Nodos getFooRoot() {
		Nodos root = new Nodos(null, 0, "", "");
		Nodos oneLevelNode = new Nodos(null, 0, "", "");
		Nodos twoLevelNode = new Nodos(null, 0, "", "");
		Nodos threeLevelNode = new Nodos(null, 0, "", "");
		Nodos fourLevelNode = new Nodos(null, 0, "", "");
		List<Arbol> listaArbol = servicioArbol.listarArbol();
		ArrayList<Arbol> arbole = new ArrayList<Arbol>();
		List<Arbol> arboles = new ArrayList<Arbol>();
		ArrayList<Long> ids = new ArrayList<Long>();
		for (int k = 0; k < listaArbol.size(); k++) {
			Arbol arbol;
			String nombre = listaArbol.get(k).getNombre();
			arbol = servicioArbol.buscarPorNombreArbol(nombre);
			if (arbol != null)
				ids.add(arbol.getIdArbol());
			arbole.add(arbol);
		}
		Collections.sort(ids);
		for (int t = 0; t < ids.size(); t++) {
			Arbol a;
			a = servicioArbol.buscarPorId(ids.get(t));
			arboles.add(a);
		}
		long temp1, temp2, temp3 = 0;
		for (int i = 0; i < arboles.size(); i++) {
			if (arboles.get(i).getPadre() == 0) {
				oneLevelNode = new Nodos(root, i, arboles.get(i).getNombre(), "");
				root.appendChild(oneLevelNode);
				temp1 = arboles.get(i).getIdArbol();
				arboles.remove(i);
				for (int j = i; j < arboles.size(); j++) {
					if (temp1 == arboles.get(j).getPadre()) {
						twoLevelNode = new Nodos(oneLevelNode, i, arboles
								.get(j).getNombre(), "");
						oneLevelNode.appendChild(twoLevelNode);
						temp2 = arboles.get(j).getIdArbol();
						arboles.remove(j);
						for (int k = j; k < arboles.size(); k++) {
							if (temp2 == arboles.get(k).getPadre()) {
								threeLevelNode = new Nodos(twoLevelNode, i,
										arboles.get(k).getNombre(), "");
								twoLevelNode.appendChild(threeLevelNode);
								temp3 = arboles.get(k).getIdArbol();
								arboles.remove(k);
								for (int z = k; z < arboles.size(); z++) {
									if (temp3 == arboles.get(z).getPadre()) {
										fourLevelNode = new Nodos(
												threeLevelNode, i, arboles.get(
														z).getNombre(),"");
										threeLevelNode
												.appendChild(fourLevelNode);
										arboles.remove(z);
										z = z - 1;
									}
								}
								k = k - 1;
							}
						}
						j = j - 1;
					}
				}
				i = i - 1;
			}
		}
		return root;
	}

	public boolean validarNodoHijo(SelectEvent<Treeitem, String> event) {
		Treeitem itemSeleccionado = event.getReference();
		Arbol arbol = servicioArbol.buscarPorNombreArbol(itemSeleccionado
				.getLabel());
		long padre = arbol.getIdArbol();
		boolean encontrado = false;
		List<Arbol> listaArbol = servicioArbol.listarArbol();
		for (int i = 0; i < listaArbol.size(); i++) {
			if (padre == listaArbol.get(i).getPadre()) {
				if (itemSeleccionado.isSelected()) {
					Messagebox.show("Seleccione las Funcionalidades", "Alerta",
							Messagebox.OK, Messagebox.INFORMATION);
					itemSeleccionado.setSelected(false);
					i = listaArbol.size();
					encontrado = true;
				} else {
					Messagebox.show("Seleccione las Funcionalidades", "Error",
							Messagebox.OK, Messagebox.INFORMATION);
					itemSeleccionado.setSelected(true);
					i = listaArbol.size();
					encontrado = true;
				}
				return encontrado;
			}
		}
		return encontrado;
	}

	public void llenarFuncionalidadesSeleccionadas() {
		Grupo grupo = servicioGrupo.buscarGrupo(id);
		List<Arbol> listaArbol = servicioArbol.buscarporGrupo(grupo);
		int ItemEncontrado = 0;
		for (int i = 0; i < listaArbol.size(); i++) {
			long padre = listaArbol.get(i).getIdArbol();
			ItemEncontrado = 0;
			for (int j = 0; j < listaArbol.size(); j++) {
				long hijo = listaArbol.get(j).getPadre();
				if (padre == hijo) {
					ItemEncontrado = 1;
					j = listaArbol.size();
				}
			}
			if (ItemEncontrado == 0) {
				funcionalidades.add(listaArbol.get(i).getNombre());
			}
		}
		ltbFuncionalidadesSeleccionados.setModel(new ListModelList<String>(
				funcionalidades));
	}

	@Listen("onSelect = #treeGrupo")
	public void selectedNode(SelectEvent<Treeitem, String> event) {
		if (!validarNodoHijo(event)) {
			List<Arbol> listaArbol2 = servicioArbol.listarArbol();
			Treechildren treeChildren = treeGrupo.getTreechildren();
			Collection<Treeitem> listaItems = treeChildren.getItems();
			Treeitem itemSeleccionado = event.getReference();
			List<Long> ids = new ArrayList<Long>();
			for (int o = 0; o < listaArbol2.size(); o++) {
				long id = listaArbol2.get(o).getIdArbol();
				ids.add(id);
			}
			Collections.sort(ids);
			List<Arbol> listaArbol = new ArrayList<Arbol>();
			for (int y = 0; y < ids.size(); y++) {
				listaArbol.add(servicioArbol.buscar(ids.get(y)));
			}
			String nombreItem = String.valueOf(itemSeleccionado.getLabel());
			if (itemSeleccionado.isSelected()) {
				funcionalidades.add(nombreItem);
				ltbFuncionalidadesSeleccionados
						.setModel(new ListModelList<String>(funcionalidades));
			} else {
				List<Listitem> listaFuncionalidadesSeleccionadas = ltbFuncionalidadesSeleccionados
						.getItems();
				for (int i = 0; i < listaFuncionalidadesSeleccionadas.size(); i++) {
					if (listaFuncionalidadesSeleccionadas.get(i).getLabel()
							.equals(nombreItem)) {
						ltbFuncionalidadesSeleccionados
								.removeItemAt(listaFuncionalidadesSeleccionadas
										.get(i).getIndex());
					}
				}
				funcionalidades.remove(nombreItem);
				ltbFuncionalidadesSeleccionados
						.setModel(new ListModelList<String>(funcionalidades));
			}
			Arbol arbolItem = servicioArbol.buscarPorNombreArbol(nombreItem);
			listaArbol.remove((int) (long) arbolItem.getIdArbol() - 1);
			long temp = arbolItem.getPadre();
			long temp2 = 0;
			long temp3 = temp;
			boolean encontradoHermanoHijo = false;
			boolean encontradoHermanoPadre = false;
			for (int i = 0; i < listaArbol.size(); i++) {
				if (temp == listaArbol.get(i).getIdArbol()) {
					for (Iterator<?> iterator = listaItems.iterator(); iterator
							.hasNext();) {
						Treeitem item = (Treeitem) iterator.next();
						if (listaArbol.get(i).getNombre()
								.equals(item.getLabel())) {
							temp2 = listaArbol.get(i).getPadre();
							for (int j = 0; j < listaArbol.size(); j++) {
								if (temp3 == listaArbol.get(j).getPadre()) {
									for (Iterator<?> iterator2 = listaItems
											.iterator(); iterator2.hasNext();) {
										Treeitem item2 = (Treeitem) iterator2
												.next();
										if (listaArbol.get(j).getNombre()
												.equals(item2.getLabel())) {
											if (item2.isSelected()) {
												encontradoHermanoHijo = true;
											}
										}
									}
								}
								if (temp2 == listaArbol.get(j).getPadre()
										&& listaArbol.get(i).getIdArbol() != listaArbol
												.get(j).getIdArbol()) {
									for (Iterator<?> iterator2 = listaItems
											.iterator(); iterator2.hasNext();) {
										Treeitem item2 = (Treeitem) iterator2
												.next();
										if (listaArbol.get(j).getNombre()
												.equals(item2.getLabel())) {
											if (item2.isSelected()) {
												encontradoHermanoPadre = true;
											}
										}
									}
								}
							}
							if (!item.isSelected()) {
								item.setSelected(true);
							} else {
								if (!encontradoHermanoHijo
										&& !encontradoHermanoPadre) {
									item.setSelected(false);
								}
								if (!encontradoHermanoHijo
										&& encontradoHermanoPadre
										&& !itemSeleccionado.isSelected()) {
									item.setSelected(false);
									encontradoHermanoHijo = true;
									encontradoHermanoPadre = false;
								}
							}
						}
					}
					temp = listaArbol.get(i).getPadre();
					i = -1;
				}
			}
		}
	}

	@Listen("onClick = #btnCatalogoGrupo")
	public void buscarItem() {
		metodoLimpiar();
		final List<Grupo> grupos = servicioGrupo.buscarTodos();
		catalogo = new Catalogo<Grupo>(catalogoGrupo, "CatÃ¡logo de Grupos",
				grupos,false,false,false, "Nombre") {
			@Override
			protected String[] crearRegistros(Grupo grupo) {
				String[] registros = new String[1];
				registros[0] = grupo.getNombre();
				return registros;
			}

//			@Override
//			protected List<Grupo> buscar(String valor, String combo) {
//				if (combo.equals("Nombre"))
//					return servicioGrupo.filtroNombre(valor);
//				else
//					return servicioGrupo.buscarTodos();
//			}
			@Override
			protected List<Grupo> buscar(List<String> valores) {
				List<Grupo> lista = new ArrayList<Grupo>();

				for (Grupo grupo : grupos) {
					if (grupo.getNombre().toLowerCase()
									.startsWith(valores.get(0))); {
						lista.add(grupo);
					}
				}
				return lista;

			}

		};
		catalogo.setParent(catalogoGrupo);
		catalogo.doModal();
	}

	@Listen("onSeleccion = #catalogoGrupo")
	public void seleccionGrupo() {
		Grupo grupo = catalogo.objetoSeleccionadoDelCatalogo();
		llenarCampos(grupo);
		catalogo.setParent(null);
	}

	public void llenarCampos(Grupo grupo) {
		txtNombreGrupo.setValue(grupo.getNombre());
		id = grupo.getIdGrupo();
		visualizarFuncionalidades();
	}

	/* Busca si existe un grupo con el mismo nombre escrito */
	@Listen("onChange = #txtNombreGrupo")
	public void buscarPorNombre() {
		Grupo grupo = servicioGrupo.buscarPorNombre(txtNombreGrupo.getValue());
		if (grupo != null)
			llenarCampos(grupo);
	}

	public void metodoLimpiar() {
		txtNombreGrupo.setValue("");
		txtNombreGrupo.setDisabled(false);
		Treechildren treeChildren = treeGrupo.getTreechildren();
		Collection<Treeitem> lista = treeChildren.getItems();
		for (int i = 0; i < treeChildren.getItemCount(); i++) {
			for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
				Treeitem treeitem = (Treeitem) iterator.next();
				treeitem.setSelected(false);
			}
		}
		for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
			Treeitem treeitem = (Treeitem) iterator.next();
			if (treeitem.isOpen()) {
				treeitem.setOpen(false);
				lista = treeGrupo.getTreechildren().getItems();
				iterator = lista.iterator();
			}
		}
		id = 0;
		treeGrupo.setVisible(true);
		funcionalidades.clear();
		ltbFuncionalidadesSeleccionados.setModel(new ListModelList<String>(
				funcionalidades));
	}

	public void visualizarFuncionalidades() {
		llenarFuncionalidadesSeleccionadas();
		treeGrupo.setVisible(true);
		Treechildren treeChildren = treeGrupo.getTreechildren();
		Collection<Treeitem> lista = treeChildren.getItems();
		for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
			Treeitem treeitem = (Treeitem) iterator.next();
			if (!treeitem.isOpen()) {
				treeitem.setOpen(true);
				lista = treeChildren.getItems();
				iterator = lista.iterator();
			}
		}
		Grupo grupo = servicioGrupo.buscarGrupo(id);
		List<Arbol> listaArbol = servicioArbol.buscarporGrupo(grupo);
		for (int i = 0; i < listaArbol.size(); i++) {
			for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
				Treeitem treeitem = (Treeitem) iterator.next();
				if (listaArbol.get(i).getNombre().equals(treeitem.getLabel())) {
					treeitem.setSelected(true);
				}
			}
		}
		for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
			Treeitem treeitem = (Treeitem) iterator.next();
			if (treeitem.isOpen()) {
				treeitem.setOpen(false);
				lista = treeGrupo.getTreechildren().getItems();
				iterator = lista.iterator();
			}
		}
	}

	private boolean validar() {
		if (txtNombreGrupo.getValue().equals("")) {
			Messagebox.show("Debe Llenar Todos los Campos", "Alerta",
					Messagebox.OK, Messagebox.EXCLAMATION);
			return false;
		}
		return true;
	}
}
