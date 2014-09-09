package controlador.seguridad;

import java.io.IOException;

import modelo.seguridad.Usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import componentes.Botonera;
import componentes.Validador;

import controlador.maestros.CGenerico;
import controlador.reportes.evaluacion.ReporteBase;

public class CReinicioPassword extends CGenerico {

	@Wire
	private Textbox txtCorreoUsuario;
	@Wire
	private Textbox txtCedulaUsuario;
	@Wire
	private Label lblNombreUsuario;
	@Wire
	private Div botoneraReinicio;
	@Wire
	private Window wdwRecordar;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private static final long serialVersionUID = 6988038390488496987L;

	@Override
	public void inicializar() throws IOException {
		// TODO Auto-generated method stub
		Botonera botonera = new Botonera() {

			@Override
			public void salir() {
				wdwRecordar.onClose();
			}

			@Override
			public void limpiar() {
				txtCorreoUsuario.setValue("");
				txtCedulaUsuario.setValue("");
			}

			@Override
			public void guardar() {
				String password = KeyGenerators.string().generateKey();
				String correo;
				if (validar()) {
					Usuario usuario = servicioUsuario
							.buscarPorCedula(txtCedulaUsuario.getValue());
					if (usuario != null) {
						if (usuario.getEmail() != null) {
							correo = usuario.getEmail();
						} else {
							correo = txtCorreoUsuario.getValue();
						}
						usuario.setPassword(password);
						servicioUsuario.guardar(usuario);
						EmailHelper.sendCommentEmail(correo, "Reinicio de Contraseña");
						
//						enviarEmailNotificacion(
//								correo,
//								"Ha Solicitado Reiniciar su Password, sus nuevos datos para el inicio de sesion son: "
//										+ " Usuario: "
//										+ usuario.getLogin()
//										+ "  " + " Password: " + password);
						limpiar();
						salir();
					} else {
						Messagebox
								.show("El Numero de Cedula que Ingreso No esta asociado a Ningun Usuario",
										"Informacion", Messagebox.OK,
										Messagebox.INFORMATION);
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
		};
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botoneraReinicio.appendChild(botonera);
	}

	protected boolean validar() {
		if (txtCedulaUsuario.getText().compareTo("") == 0
				|| txtCorreoUsuario.getText().compareTo("") == 0) {
			Messagebox.show("Debe Llenar Todos los Campos", "Informacion",
					Messagebox.OK, Messagebox.INFORMATION);
			return false;
		} else
			return true;
	}

	/*
	 * Metodo que permite validar el correo electronico
	 */
	@Listen("onChange = #txtCorreoUsuario")
	public void validarCorreo() throws IOException {
		if (Validador.validarCorreo(txtCorreoUsuario.getValue()) == false) {
			Messagebox.show("Correo Invalido", "Informacion", Messagebox.OK,
					Messagebox.INFORMATION);
		}
	}

	/* Valida la cedula */
	@Listen("onChange = #txtCedulaUsuario")
	public void validarCedula() {
		if (!Validador.validarNumero(txtCedulaUsuario.getValue())) {
			Messagebox.show("Cedula Invalida", "Informacion", Messagebox.OK,
					Messagebox.INFORMATION);
		}
	}
}
