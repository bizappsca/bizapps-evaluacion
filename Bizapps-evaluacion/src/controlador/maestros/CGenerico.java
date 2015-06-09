package controlador.maestros;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import modelo.seguridad.Usuario;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import security.modelo.Grupo;
import security.modelo.UsuarioSeguridad;
import security.servicio.SArbol;
import security.servicio.SGrupo;
import security.servicio.SUsuarioSeguridad;
import servicio.maestros.SActividad;
import servicio.maestros.SArea;
import servicio.maestros.SCargo;
import servicio.maestros.SClase;
import servicio.maestros.SCompetencia;
import servicio.maestros.SConfiguracionGeneral;
import servicio.maestros.SCurso;
import servicio.maestros.SDistribucion;
import servicio.maestros.SDominio;
import servicio.maestros.SEmpleado;
import servicio.maestros.SEmpresa;
import servicio.maestros.SFechaValidezEstado;
import servicio.maestros.SGerencia;
import servicio.maestros.SItemEvaluacion;
import servicio.maestros.SMedicion;
import servicio.maestros.SNombreCurso;
import servicio.maestros.SParametro;
import servicio.maestros.SPerfilCargo;
import servicio.maestros.SPeriodo;
import servicio.maestros.SPerspectiva;
import servicio.maestros.SResultadoInterfaz;
import servicio.maestros.SRevision;
import servicio.maestros.STipoFormacion;
import servicio.maestros.SUnidadMedida;
import servicio.maestros.SUnidadOrganizativa;
import servicio.maestros.SUrgencia;
import servicio.maestros.SValoracion;
import servicio.reportes.SReporte;
import servicio.seguridad.SUsuario;
import servicio.transacciones.SActividadCurso;
import servicio.transacciones.SBitacora;
import servicio.transacciones.SConductaCompetencia;
import servicio.transacciones.SEmpleadoClase;
import servicio.transacciones.SEmpleadoCurso;
import servicio.transacciones.SEmpleadoItem;
import servicio.transacciones.SEmpleadoParametro;
import servicio.transacciones.SEvaluacion;
import servicio.transacciones.SEvaluacionCapacitacion;
import servicio.transacciones.SEvaluacionCompetencia;
import servicio.transacciones.SEvaluacionConducta;
import servicio.transacciones.SEvaluacionIndicador;
import servicio.transacciones.SEvaluacionObjetivo;
import servicio.transacciones.SNivelCompetenciaCargo;
import servicio.transacciones.SUtilidad;
import componentes.Mensaje;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public abstract class CGenerico extends SelectorComposer<Component> {

	private static final long serialVersionUID = -2264423023637489596L;

	@WireVariable("SArbol")
	protected SArbol servicioArbol;
	@WireVariable("SGrupo")
	protected SGrupo servicioGrupo;
	@WireVariable("SUsuarioSeguridad")
	protected SUsuarioSeguridad servicioUsuarioSeguridad;
	@WireVariable("SUsuario")
	protected SUsuario servicioUsuario;
	@WireVariable("SEmpleado")
	protected SEmpleado servicioEmpleado;
	@WireVariable("SPerspectiva")
	protected SPerspectiva servicioPerspectiva;
	@WireVariable("SClase")
	protected SClase servicioClase;
	@WireVariable("SEvaluacion")
	protected SEvaluacion servicioEvaluacion;
	@WireVariable("SEmpleadoItem")
	protected SEmpleadoItem servicioEmpleadoItem;
	@WireVariable("SEmpleadoParametro")
	protected SEmpleadoParametro servicioEmpleadoParametro;
	@WireVariable("SParametro")
	protected SParametro servicioParametro;
	@WireVariable("SEmpleadoClase")
	protected SEmpleadoClase servicioEmpleadoClase;
	@WireVariable("SCompetencia")
	protected SCompetencia servicioCompetencia;
	@WireVariable("SEmpleadoCurso")
	protected SEmpleadoCurso servicioEmpleadoCurso;
	@WireVariable("SDistribucion")
	protected SDistribucion servicioDistribucion;
	@WireVariable("SPerfilCargo")
	protected SPerfilCargo servicioPerfilCargo;
	@WireVariable("SNivelCompetenciaCargo")
	protected SNivelCompetenciaCargo servicioNivelCompetenciaCargo;
	@WireVariable("SItemEvaluacion")
	protected SItemEvaluacion servicioItemEvaluacion;
	@WireVariable("SCargo")
	protected SCargo servicioCargo;
	@WireVariable("SCurso")
	protected SCurso servicioCurso;
	@WireVariable("SActividadCurso")
	protected SActividadCurso servicioActividadCurso;
	@WireVariable("SEmpresa")
	protected SEmpresa servicioEmpresa;
	@WireVariable("SGerencia")
	protected SGerencia servicioGerencia;
	@WireVariable("SUnidadOrganizativa")
	protected SUnidadOrganizativa servicioUnidadOrganizativa;
	@WireVariable("SRevision")
	protected SRevision servicioRevision;
	@WireVariable("SPeriodo")
	protected SPeriodo servicioPeriodo;
	@WireVariable("STipoFormacion")
	protected STipoFormacion servicioTipoFormacion;
	@WireVariable("SArea")
	protected SArea servicioArea;
	@WireVariable("SActividad")
	protected SActividad servicioActividad;
	@WireVariable("SDominio")
	protected SDominio servicioDominio;
	@WireVariable("SMedicion")
	protected SMedicion servicioMedicion;
	@WireVariable("SUnidadMedida")
	protected SUnidadMedida servicioUnidadMedida;
	@WireVariable("SUrgencia")
	protected SUrgencia servicioUrgencia;
	@WireVariable("SValoracion")
	protected SValoracion servicioValoracion;
	@WireVariable("SEvaluacionObjetivo")
	protected SEvaluacionObjetivo servicioEvaluacionObjetivo;
	@WireVariable("SEvaluacionIndicador")
	protected SEvaluacionIndicador servicioEvaluacionIndicador;
	@WireVariable("SConductaCompetencia")
	protected SConductaCompetencia servicioConductaCompetencia;
	@WireVariable("SEvaluacionConducta")
	protected SEvaluacionConducta servicioEvaluacionConducta;
	@WireVariable("SEvaluacionCompetencia")
	protected SEvaluacionCompetencia servicioEvaluacionCompetencia;
	@WireVariable("SBitacora")
	protected SBitacora servicioBitacora;
	@WireVariable("SReporte")
	protected SReporte servicioReporte;
	@WireVariable("SUtilidad")
	protected SUtilidad servicioUtilidad;
	@WireVariable("SEvaluacionCapacitacion")
	protected SEvaluacionCapacitacion servicioEvaluacionCapacitacion;
	@WireVariable("SConfiguracionGeneral")
	protected SConfiguracionGeneral servicioConfiguracionGeneral;
	@WireVariable("SNombreCurso")
	protected SNombreCurso servicioNombreCurso;
	@WireVariable("SFechaValidezEstado")
	protected SFechaValidezEstado servicioFechaValidezEstado;
	@WireVariable("SResultadoInterfaz")
	protected SResultadoInterfaz servicioErrorInterfaz;

	public Tabbox tabBox;
	public Include contenido;
	protected SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
	public Mensaje msj = new Mensaje();
	public List<Tab> tabs = new ArrayList<Tab>();
	protected DateFormat df = new SimpleDateFormat("HH:mm:ss");
	public final Calendar calendario = Calendar.getInstance();
	public String horaAuditoria = String.valueOf(calendario
			.get(Calendar.HOUR_OF_DAY))
			+ ":"
			+ String.valueOf(calendario.get(Calendar.MINUTE))
			+ ":"
			+ String.valueOf(calendario.get(Calendar.SECOND));
	public java.util.Date fecha = new Date();
	public Timestamp fechaHora = new Timestamp(fecha.getTime());
	public String titulo = "";

	public Tab tab;
	/* Titulos de Mensaje */
	public String informacion = "INFORMACION";
	public String alerta = "ALERTA";

	private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
			"/META-INF/ConfiguracionAplicacion.xml");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		inicializar();
	}

	public static SEmpleado getServicioEmpleado() {
		return applicationContext.getBean(SEmpleado.class);
	}

	public static SEmpleadoCurso getServicioEmpleadoCurso() {
		return applicationContext.getBean(SEmpleadoCurso.class);
	}

	public abstract void inicializar() throws IOException;

	public void cerrarVentana(Div div, String id) {
		div.setVisible(false);
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getLabel().equals(id)) {
				if (i == (tabs.size() - 1) && tabs.size() > 1) {
					tabs.get(i - 1).setSelected(true);
				}
				tabs.get(i).onClose();
				tabs.remove(i);
			}
		}
	}

	public void cerrarVentana1(Window window, String id) {
		window.setVisible(false);
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getLabel().equals(id)) {
				if (i == (tabs.size() - 1) && tabs.size() > 1) {
					tabs.get(i - 1).setSelected(true);
				}
				tabs.get(i).onClose();
				tabs.remove(i);
			}
		}
	}

	public void cerrarWindow(Window win, String id) {
		win.setVisible(false);
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getLabel().equals(id)) {
				if (i == (tabs.size() - 1) && tabs.size() > 1) {
					tabs.get(i - 1).setSelected(true);
				}
				tabs.get(i).onClose();
				tabs.remove(i);
			}
		}
	}

	public void cerrarVentana(Div div, String id, List<Tab> tabs2) {
		div.setVisible(false);
		tabs = tabs2;
		System.out.println(tabs.size());
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getLabel().equals(id)) {
				if (i == (tabs.size() - 1) && tabs.size() > 1) {
					tabs.get(i - 1).setSelected(true);
				}
				tabs.get(i).onClose();
				tabs.remove(i);
			}
		}
	}

	public void cerrarVentana(Window div, String id, List<Tab> tabs2) {
		div.setVisible(false);
		tabs = tabs2;
		System.out.println(tabs.size());
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getLabel().equals(id)) {
				if (i == (tabs.size() - 1) && tabs.size() > 1) {
					tabs.get(i - 1).setSelected(true);
				}
				tabs.get(i).onClose();
				tabs.remove(i);
			}
		}
	}

	public String nombreUsuarioSesion() {
		Authentication sesion = SecurityContextHolder.getContext()
				.getAuthentication();
		return sesion.getName();
	}

	public boolean enviarEmailNotificacion(String correo, String mensajes) {
		try {

			String cc = "NOTIFICACION DE SISTEMA DE EVALUACION DE DESEMPE�O";
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "172.23.20.66");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", "2525");
			props.setProperty("mail.smtp.auth", "true");

			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getInstance(props, auth);
			String remitente = "cdusa@dusa.com.ve";
			String destino = correo;
			String mensaje = mensajes;
			String destinos[] = destino.split(",");
			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(remitente));

			Address[] receptores = new Address[destinos.length];
			int j = 0;
			while (j < destinos.length) {
				receptores[j] = new InternetAddress(destinos[j]);
				j++;
			}

			message.addRecipients(Message.RecipientType.TO, receptores);
			message.setSubject(cc);
			message.setText(mensaje);

			Transport.send(message);

			return true;
		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication("cdusa", "cartucho");
		}
	}

	public String damePath() {
		return Executions.getCurrent().getContextPath() + "/";
	}

	public List<String> obtenerPropiedades() {
		List<String> arreglo = new ArrayList<String>();
		DriverManagerDataSource ds = (DriverManagerDataSource) applicationContext
				.getBean("dataSource");
		arreglo.add(ds.getUsername());
		arreglo.add(ds.getPassword());
		arreglo.add(ds.getUrl());
		return arreglo;
	}

	public void cerrarVentana2(Window window, String id, List<Tab> tabs2) {
		window.setVisible(false);
		tabs = tabs2;
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getLabel().equals(id)) {
				if (i == (tabs.size() - 1) && tabs.size() > 1) {
					tabs.get(i - 1).setSelected(true);
				}
				tabs.get(i).onClose();
				tabs.remove(i);
			}
		}
	}

	public void guardarDatosSeguridad(Usuario usuarioLogica,
			Set<Grupo> gruposUsuario) {
		UsuarioSeguridad usuario = new UsuarioSeguridad(
				usuarioLogica.getLogin(), usuarioLogica.getEmail(),
				usuarioLogica.getPassword(), usuarioLogica.getImagen(), true,
				usuarioLogica.getNombre(),
				usuarioLogica.getApellido(), fechaHora, horaAuditoria,
				nombreUsuarioSesion(), gruposUsuario);
		servicioUsuarioSeguridad.guardar(usuario);
	}

	public void inhabilitarSeguridad(List<Usuario> list) {
		for (int i = 0; i < list.size(); i++) {
			UsuarioSeguridad usuario = servicioUsuarioSeguridad
					.buscarPorLogin(list.get(i).getLogin());
			usuario.setEstado(false);
			servicioUsuarioSeguridad.guardar(usuario);
		}
	}
}