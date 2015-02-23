package componentes;

import org.zkoss.zul.Messagebox;

public class Mensaje {
	public static String claveSYNoEsta = "El Codigo de Producto no Existe.";
	public static String claveRTNoEsta = "El Codigo Definido por el Usuario no Existe.";
	public static String guardado = "Registro Guardado Exitosamente.";
	public static String claveUsada = "La Clave ha sido Usada por Otro Registro.";
	public static String camposVacios = "Debe Llenar Todos los Campos Requeridos.";
	public static String noSeleccionoItem = "No ha seleccionado ningun Item";
	public static String noHayRegistros = "No se Encontraron Registros";
	public static String editarSoloUno = "Solo puede Editar un Item a la vez, "
			+ "Seleccione un (1) solo Item y Repita la Operacion";
	public static String deseaEliminar = "¿Desea Eliminar el Registro?";
	public static String deseaCopiar = "¿Desea Copiar el Registro?, esto creara una nueva evaluacion con la revision actual !";
	public static String eliminado = "Registro Eliminado Exitosamente";
	public static String estaEditando = "No ha culminado la Edicion, ¿Desea Continuar Editando?";
	public static String noSeleccionoRegistro = "No ha seleccionado ningun Registro";
	public static String exportar = "¿Desea exportar los datos de la lista a formato CSV?";
	public static String enUso = "La interfaz esta siendo usada";
	public static String articuloNoExiste = "El Codigo del Articulo no Existe.";
	public static String correoInvalido = "El Correo es Invalido.";
	public static String telefonoInvalido = "El número de Teléfono es invalido.";
	public static String contrasennasInvalidas = "Las contraseñas no coinciden.";
	public static String seleccionarPrograma = "Las contraseñas no coinciden.";
	public static String codigoEmpresa = "La empresa definida por el usuario no existe";
	public static String codigoCargo = "El cargo definido por el usuario no existe";
	public static String codigoUnidad = "La unidad organizativa definida por el usuario no existe";
	public static String codigoPeriodo = "El codigo del periodo definido por el usuario no existe";
	public static String codigoArea = "El codigo del area definido por el usuario no existe";
	public static String codigoClase = "El contenido de la clase definido por el usuario no existe";
	public static String codigoGerencia = "La gerencia definida por el usuario no existe";
	public static String codigoCompetencia = "La competencia definida por el usuario no existe";
	public static String codigoTipoFormacion = "El tipo de formacion definido por el usuario no existe";
	public static String codigoDominio = "El dominio definido por el usuario no existe";
	public static String codigoSupervisor = "El supervisor definido por el usuario no existe";
	public static String codigoEmpleado = "El empleado definido por el usuario no existe";
	public static String codigoCurso = "El curso definido por el usuario no existe";
	public static String nombreCurso = "El nombre del curso definido por el usuario ya existe";
	public static String periodoActivo = "Ya existe un periodo con el estado ACTIVO";
	public static String revisionesActivas = "No puede cambiar el periodo a INACTIVO porque existen revisiones ACTIVAS asociadas a él.";
	public static String revisionActiva = "Ya existe una revision con el estado ACTIVO";
	public static String noSeleccionoCompetencia = "No ha seleccionado ninguna competencia";
	public static String noSeleccionoCurso = "No ha seleccionado ningun curso";
	public static String limiteCurso = "No puede seleccionar mas de 3 cursos";
	public static String noSeleccionoFecha = "No ha seleccionado la fecha de realizacion de una actividad";

	public static String seleccionarEmpresa = "Debe seleccionar una Empresa";
	public static String seleccionarPeriodo = "Debe seleccionar un Periodo";
	public static String seleccionarUnidadOrganizativa = "Debe seleccionar una Unidad Organizativa";
	public static String seleccionarGerencia = "Debe seleccionar una Gerencia";
	public static String seleccionarCompetencia = "Debe seleccionar una Competencia";
	public static String contrasennasNoCoinciden = "Las Contraseñas no Coinciden.";
	public static String tamanioMuyGrande = "El archivo que ha seleccionado excede el tamaño maximo establecido (100 KB)";
	public static String formatoImagenNoValido = "Formato de Imagen no Valido";
	public static String pendiente = "Su evaluación ahora esta en estado Pendiente";
	public static String edicion = "Su evaluación ahora esta en estado En Edición";
	public static String revisada = "Su evaluación ahora esta en estado revisada";
	public static String aprobada = "Su evaluación ahora esta en estado aprobada";
	public static String calibrada = "Su evaluación ahora esta en estado calibrada";
	public static String finalizada = "Su evaluación ahora esta en estado finalizada";
	public static String personalCargo = "No tiene Personal a Cargo";
	public static String yaExistenPendiente = "Ya tiene una evaluación en estado pendiente o superior en este periodo, no puede promover otra evaluación";
	public static String noSePuedeCopiar = "El periodo está inactivo no puede copiar evaluaciones";
	public static String noSePuedeCrear = "El periodo está inactivo no puede crear evaluaciones";
	public static String noSePuedeEliminar = "El periodo está inactivo no puede eliminar evaluaciones";
	public static String reinicioContrasenna = "Se envio un Correo Indicando los datos del Usuario";
	public static String cedulaInvalida = "Formato de Ficha No Valido";
	public static String cedulaNoExiste = "El numero de ficha queingreso no esta asociado a ningun usuario";
	public static String soloUnaEvaluacion = "Solo puede crear una evaluacion por periodo";

	public void mensajeInformacion(String msj) {
		Messagebox.show(msj, "Informacion", Messagebox.OK,
				Messagebox.INFORMATION);
	}

	public void mensajeAlerta(String msj) {
		Messagebox.show(msj, "Alerta", Messagebox.OK, Messagebox.EXCLAMATION);
	}

	public void mensajeError(String msj) {
		Messagebox.show(msj, "Error", Messagebox.OK, Messagebox.ERROR);
	}
}