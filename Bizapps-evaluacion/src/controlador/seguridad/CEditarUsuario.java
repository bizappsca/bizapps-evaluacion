package controlador.seguridad;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import modelo.seguridad.Usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;


import componentes.Botonera;
import componentes.Mensaje;
import componentes.Validador;

import controlador.maestros.CGenerico;

public class CEditarUsuario extends CGenerico {

	@Wire
	private Textbox txtNombreUsuarioEditar;
	@Wire
	private Textbox txtClaveUsuarioNueva;
	@Wire
	private Textbox txtClaveUsuarioConfirmar;
	@Wire
	private Image imgUsuario;
	@Wire
	private Fileupload fudImagenUsuario;
	@Wire
	private Div botoneraEditarUsuario;
	@Wire
	private Div divEditarUsuario;
	ArrayList<Boolean> valorCorreo = new ArrayList<Boolean>();
	private String mensaje = "Su solicitud ha sido exitosamente procesada, le enviamos su usuario y contraseña,";
	private String id = "";
	private Media media;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	URL url = getClass().getResource("/controlador/maestros/usuario.png");
	private static final long serialVersionUID = 2439502647179786175L;

	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> mapa = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (mapa != null) {
			if (mapa.get("tabsGenerales") != null) {
				tabs = (List<Tab>) mapa.get("tabsGenerales");
				mapa.clear();
				mapa = null;
			}
		}
		Usuario usuario = servicioUsuario
				.buscarUsuarioPorNombre(nombreUsuarioSesion());
		id = usuario.getCedula();
		txtNombreUsuarioEditar.setValue(usuario.getLogin());
		if (usuario.getImagen() == null) {
			imgUsuario.setContent(new AImage(url));
		} else {
			try {
				BufferedImage imag;
				imag = ImageIO.read(new ByteArrayInputStream(usuario
						.getImagen()));
				imgUsuario.setContent(imag);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Botonera botonera = new Botonera() {

			@Override
			public void salir() {
			cerrarVentana(divEditarUsuario, "Editar Usuario", tabs);
			}

			@Override
			public void limpiar() {
				Usuario usuario = servicioUsuario
						.buscarUsuarioPorNombre(nombreUsuarioSesion());
				id = usuario.getCedula();
				txtNombreUsuarioEditar.setValue(usuario.getLogin());
				txtClaveUsuarioConfirmar.setValue("");
				txtClaveUsuarioNueva.setValue("");
				if (usuario.getImagen() == null) {
					try {
						imgUsuario.setContent(new AImage(url));
					} catch (IOException e) {

						e.printStackTrace();
					}
				} else {
					try {
						BufferedImage imag;
						imag = ImageIO.read(new ByteArrayInputStream(usuario
								.getImagen()));
						imgUsuario.setContent(imag);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void guardar() {
				if (validar()) {
					if (txtClaveUsuarioNueva.getValue().equals(
							txtClaveUsuarioConfirmar.getValue())) {
						Usuario usuario = servicioUsuario
								.buscarUsuarioPorId(id);
						byte[] imagenUsuario = null;
						imagenUsuario = imgUsuario.getContent().getByteData();
						String password = txtClaveUsuarioConfirmar.getValue();
						usuario.setPassword(password);
						usuario.setImagen(imagenUsuario);
						servicioUsuario.guardar(usuario);
						Messagebox.show("Los Datos fueron modificados Satisfactoriamente" , "Informacion",
								Messagebox.OK, Messagebox.INFORMATION);
						limpiar();
					} else {
						msj.mensajeError(Mensaje.contrasennasNoCoinciden);
					}
				}
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
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botoneraEditarUsuario.appendChild(botonera);
	}

	protected boolean validar() {
		if (txtClaveUsuarioConfirmar.getValue().equals("")
				|| txtClaveUsuarioNueva.getValue().equals("")) {
			msj.mensajeError(Mensaje.camposVacios);
			return false;
		} else
			return true;
	}

	@Listen("onUpload = #fudImagenUsuario")
	public void processMedia(UploadEvent event) throws IOException {
		media = event.getMedia();
		imgUsuario.setContent(new AImage(url));
		if (Validador.validarTipoImagen(media)) {
			if (Validador.validarTamannoImagen(media)) {
				imgUsuario.setHeight("150px");
				imgUsuario.setWidth("150px");
				imgUsuario.setContent((org.zkoss.image.Image) media);
				imgUsuario.setVisible(true);
			} else {
				msj.mensajeError(Mensaje.tamanioMuyGrande);
				imgUsuario.setContent(new AImage(url));
			}
		} else {
			msj.mensajeError(Mensaje.formatoImagenNoValido);
			imgUsuario.setContent(new AImage(url));
		}
	}

	/**
	 * Metodo que permite confirmar que se logro enviar efectivamente el correo
	 * electronico
	 * 
	 * @param valor2
	 *            arreglo de valores booleanos
	 */
	private int confirmacion(ArrayList<Boolean> valor2) {
		// TODO Auto-generated method stub
		for (int w = 0; w < valor2.size(); w++) {
			if (valor2.get(w).equals(false)) {
				return Messagebox.show("Correo electronico no enviado",
						"Error", Messagebox.OK, Messagebox.ERROR);
			}
		}
		return Messagebox.show("Su contraseña ha sido cambiada exitosamente, " +
				"hemos enviado a su correo electronico la información de su cuenta", "Informacion",
				Messagebox.OK, Messagebox.INFORMATION);
	}





}
