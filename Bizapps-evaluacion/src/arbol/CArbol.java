package arbol;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import modelo.maestros.Revision;
import modelo.seguridad.Arbol;
import modelo.seguridad.Grupo;
import modelo.seguridad.Usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.image.AImage;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.West;

import controlador.maestros.CGenerico;
import componentes.Mensaje;
import componentes.Validador;

public class CArbol extends CGenerico {

	private static final long serialVersionUID = -5393608637902961029L;
	@Wire
	private Tree arbolMenu;
	@Wire
	private Include contenido;
	@Wire
	private Label etiqueta;
	TreeModel _model;
	List<String> listmenu1 = new ArrayList<String>();
	@Wire
	private Tab tab;
	@Wire
	private Tabbox tabBox;
	@Wire
	private West west;
	@Wire
	private Image imagenes;
	URL url = getClass().getResource("/controlador/maestros/usuario.png");
	@Wire
	private Listbox ltbRoles;
	private  boolean numeroSgt = false;
	private   Tabbox tabBox2;
	private  Include contenido2;
	private  Tab tab2;
	Mensaje msj = new Mensaje();
	@Wire
	private Label lblUsuario;
	HashMap<String, Object> mapGeneral = new HashMap<String, Object>();
	@Override
	public void inicializar() throws IOException {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		Usuario u = servicioUsuario.buscarUsuarioPorNombre(auth.getName());
		List<Grupo> grupos = servicioGrupo.buscarGruposUsuario(u);
		ltbRoles.setModel(new ListModelList<Grupo>(grupos));
		if (u.getImagen() == null) {
			imagenes.setContent(new AImage(url));
		} else {
			try {
				BufferedImage imag;
				imag = ImageIO.read(new ByteArrayInputStream(u.getImagen()));
				imagenes.setContent(imag);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String nombre = u.getNombre();
		String apellido = u.getApellido();
		String cedula = u.getCedula();
		String usuario = "(" + cedula + ")" + "   " + nombre + " " + apellido;   
		lblUsuario.setValue(usuario);
		arbolMenu.setModel(getModel());

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("Vistas");

		if (tabs.size() != 0) {
			tabs.clear();
		}

		if (map != null) {
			if ((String) map.get("vista") != null) {
				contenido.setSrc("/vistas/" + (String) map.get("vista")
						+ ".zul");
			}
		}
		
		Revision revision=servicioRevision.buscarRevisionActiva();
		
		if (revision!=null)
		{
		  Messagebox.show(revision.getMensajeInicio(), "Informacion", Messagebox.OK, Messagebox.INFORMATION);
		}
	}

	/* Permite asignarle los nodos cargados con el metodo getFooRoot() al arbol */
	public TreeModel getModel() {
		if (_model == null) {
			_model = new MArbol(getFooRoot());
		}
		return _model;
	}

	/*
	 * Permite obtener las funcionalidades asociadas al usuario en session y asi
	 * crear un arbol estructurado, segun la distribucion de las mismas
	 */
	private Nodos getFooRoot() {

		Nodos root = new Nodos(null, 0, "", "");
		Nodos oneLevelNode = new Nodos(null, 0, "", "");
		Nodos twoLevelNode = new Nodos(null, 0, "", "");
		Nodos threeLevelNode = new Nodos(null, 0, "", "");
		Nodos fourLevelNode = new Nodos(null, 0, "", "");

		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		System.out.println(auth.getName());
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
				auth.getAuthorities());

		ArrayList<Arbol> arbole = new ArrayList<Arbol>();
		List<Arbol> arboles = new ArrayList<Arbol>();
		ArrayList<Long> ids = new ArrayList<Long>();
		for (int k = 0; k < authorities.size(); k++) {

			Arbol arbol;
			String nombre = authorities.get(k).toString();
			if(Validador.validarNumero(nombre)){
			arbol = servicioArbol.buscar(Long.parseLong(nombre));
			if (arbol != null)
				ids.add(arbol.getIdArbol());
			arbole.add(arbol);
			}
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
								.get(j).getNombre(),"");
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
														z).getNombre(), "");
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

	
	public void booleanoApg (){
		numeroSgt = false;
	}
	/*
	 * Permite seleccionar un elemento del arbol, mostrandolo en forma de
	 * pestaña y su contenido es cargado en un div
	 */
	@Listen("onClick = #arbolMenu")
	public void selectedNode() {
		if(arbolMenu.getSelectedItem()!=null)
		{
		String item = String.valueOf(arbolMenu.getSelectedItem().getValue());
		boolean abrir = true;
		Tab taba = new Tab();
		
//		if (arbolMenu.getSelectedItem().getLevel() > 0) {
			final Arbol arbolItem = servicioArbol.buscarPorNombreArbol(item);
			mapGeneral.put("titulo", arbolItem.getNombre());
			if (!arbolItem.getUrl().equals("inicio")) {
				
				if (String.valueOf(arbolMenu.getSelectedItem().getValue()).equals("Consulta")) 
					west.setOpen(false);
				for (int i = 0; i < tabs.size(); i++) {
					if (tabs.get(i).getLabel().equals(arbolItem.getNombre())) {
						abrir = false;
						taba = tabs.get(i);
					}
				}
				if (abrir) {
				if (arbolItem.getUrl().equals("maestros/VF00021")){
					if (numeroSgt ==false){
						String ruta = "/vistas/" + arbolItem.getUrl() + ".zul";
						contenido = new Include();
						contenido.setSrc(null);
						contenido.setSrc(ruta);
						numeroSgt = true;
						Tab newTab = new Tab(arbolItem.getNombre());
						newTab.setClosable(true);
						newTab.addEventListener(Events.ON_CLOSE,
								new EventListener<Event>() {
									@Override
									public void onEvent(Event arg0) throws Exception {
										for (int i = 0; i < tabs.size(); i++) {
											if (tabs.get(i).getLabel().equals(arbolItem.getNombre())) {
												if (i == (tabs.size() - 1) && tabs.size() > 1) {
													tabs.get(i - 1).setSelected(true);
												}
												
												tabs.get(i).close();
												tabs.remove(i);
											}
										}
									}
								});
						newTab.setSelected(true);
						Tabpanel newTabpanel = new Tabpanel();
						newTabpanel.appendChild(contenido);
						tabBox.getTabs().insertBefore(newTab, tab);
						newTabpanel.setParent(tabBox.getTabpanels());
						tabs.add(newTab);
				}
					else {
						msj.mensajeError(Mensaje.enUso);
					}
				}
				else {
					String ruta = "/vistas/" + arbolItem.getUrl() + ".zul";
					contenido = new Include();
					contenido.setSrc(null);
					contenido.setSrc(ruta);
					Tab newTab = new Tab(arbolItem.getNombre());
					newTab.setClosable(true);
					newTab.addEventListener(Events.ON_CLOSE,
							new EventListener<Event>() {
						@Override
						public void onEvent(Event arg0)
								throws Exception {
							if(arbolItem.getNombre().equals("Consulta"))
								west.setOpen(true);
							for (int i = 0; i < tabs.size(); i++) {
								if (tabs.get(i).getLabel()
										.equals(arbolItem.getNombre())) {
									if (i == (tabs.size() - 1)
											&& tabs.size() > 1) {
										tabs.get(i - 1).setSelected(
												true);
									}
											
											tabs.get(i).close();
											tabs.remove(i);
										}
									}
								}
							});
					newTab.setSelected(true);
					Tabpanel newTabpanel = new Tabpanel();
					newTabpanel.appendChild(contenido);
					tabBox.getTabs().insertBefore(newTab, tab);
					newTabpanel.setParent(tabBox.getTabpanels());
					tabs.add(newTab);
					mapGeneral.put("tabsGenerales", tabs);
					mapGeneral.put("west", west);
					Sessions.getCurrent().setAttribute("mapaGeneral",
							mapGeneral);
					}
				}
				 else {
					taba.setSelected(true);
				}
			} else {
				if (!arbolMenu.getSelectedItem().isOpen())
					arbolMenu.getSelectedItem().setOpen(true);
				else
					arbolMenu.getSelectedItem().setOpen(false);
			}
//		}
		
		}
		tabBox2 = tabBox;
		contenido2 = contenido;
		tab2 = tab;
	}
	
	public void abrirVentanas(final Arbol arbolItem, Tabbox tabBox3,
			Include contenido3, Tab tab3, List<Tab> tabss) {
		boolean abrir = true;
		contenido2 = contenido3;
		tabBox2 = tabBox3;
		tab2 = tab3;
		tabs = tabss;
		Tab taba = new Tab();
		
			if (!arbolItem.getUrl().equals("inicio")) {
				for (int i = 0; i < tabs.size(); i++) {
					if (tabs.get(i).getLabel().equals(arbolItem.getNombre())) {
						abrir = false;
						taba = tabs.get(i);
					}
				}
				if (abrir) {
					String ruta = "/vistas/" + arbolItem.getUrl() + ".zul";
					contenido2 = new Include();
					contenido2.setSrc(null);
					contenido2.setSrc(ruta);

					Tab newTab = new Tab(arbolItem.getNombre());
					newTab.setClosable(true);
					newTab.addEventListener(Events.ON_CLOSE,
							new EventListener<Event>() {
								@Override
								public void onEvent(Event arg0) throws Exception {
									for (int i = 0; i < tabs.size(); i++) {
										if (tabs.get(i).getLabel()
												.equals(arbolItem.getNombre())) {
											if (i == (tabs.size() - 1)
													&& tabs.size() > 1) {
												tabs.get(i - 1).setSelected(true);
											}

											tabs.get(i).close();
											tabs.remove(i);
										}
									}
								}
							});
					newTab.setSelected(true);
					Tabpanel newTabpanel = new Tabpanel();
					newTabpanel.appendChild(contenido2);
					tabBox2.getTabs().insertBefore(newTab,tab2);
					newTabpanel.setParent(tabBox2.getTabpanels());
					tabs.add(newTab);
					mapGeneral.put("tabsGenerales", tabs);
					Sessions.getCurrent().setAttribute("mapaGeneral", mapGeneral);
			
				} else {
					taba.setSelected(true);
				}
			}
	}

	/* Metodo que permite abrir la ventana de editar usuario en una pestaña */
	@Listen("onClick = #lblEditarCuenta")
	public void abrirEditarCuenta() {
		boolean abrir = true;
		Tab taba = new Tab();
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getLabel().equals("Editar Usuario")) {
				abrir = false;
				taba = tabs.get(i);
			}
		}
		if (abrir) {
			String ruta = "/vistas/seguridad/VEditarUsuario.zul";
			contenido = new Include();
			contenido.setSrc(null);
			contenido.setSrc(ruta);
			Tab newTab = new Tab("Editar Usuario");
			newTab.setClosable(true);
			newTab.addEventListener(Events.ON_CLOSE,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event arg0) throws Exception {
							for (int i = 0; i < tabs.size(); i++) {
								if (tabs.get(i).getLabel()
										.equals("Editar Usuario")) {
									if (i == (tabs.size() - 1)
											&& tabs.size() > 1) {
										tabs.get(i - 1).setSelected(true);
									}

									tabs.get(i).close();
									tabs.remove(i);
								}
							}
						}
					});
			newTab.setSelected(true);
			Tabpanel newTabpanel = new Tabpanel();
			newTabpanel.appendChild(contenido);
			tabBox.getTabs().insertBefore(newTab, tab);
			newTabpanel.setParent(tabBox.getTabpanels());
			tabs.add(newTab);
			mapGeneral.put("tabsGenerales", tabs);
			Sessions.getCurrent().setAttribute("mapaGeneral", mapGeneral);
		} else
			taba.setSelected(true);
	}
}