package controlador.reportes.evaluacion;

import java.awt.Dimension;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;



import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.util.ResourceBundle;

import controlador.maestros.CGenerico;

public class ReporteBase extends CGenerico {
	
	 protected Connection conexion;

	    public ReporteBase() {
	    }

	    public JasperReport cargarReporte(URL url) {
	        JasperReport reporte = null;
	        try {
	            reporte = (JasperReport) JRLoader.loadObject(url);

	        } catch (JRException e) {
	            System.out.println("Error cargando el reporte : " + e.getMessage());
	            System.exit(3);
	        }

	        return reporte;
	    }

	    

	   
	    public byte[] getReporte(String par1, String par2, String par3, String par4, String par5, String par6, String par7, String par8, String par9, String par10) {
	        byte[] fichero = null;

	        conexion = null;
	        try {


	            ClassLoader cl = this.getClass().getClassLoader();
	            InputStream fis = null;
	            
	            String path_banner="";
	            try {
	            	//path_banner=getClass().getResource("/imagenes/banner.jpg").getPath();	
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.toString());
				}
	            
	            
	            
	            Map parameters = new HashMap();

	            parameters.put("evaluacion", par1);
	            parameters.put("evaluador", par2);
	            parameters.put("path_banner", path_banner);
	        
	            fis = (cl.getResourceAsStream("controlador/reportes/evaluacion/jasper/reporteEvaluacion.jasper"));

	           
	            /*String url = "jdbc:sqlserver://172.23.20.72:1433;databaseName=dusa_evaluacion";
	            String user = "client";
	            String password = "123";*/
	            
	            List<String> lista = obtenerPropiedades();
				String user = lista.get(0);
				String password = lista.get(1);
				String url = lista.get(2);

	            try {
	                
	                try {
	                    conexion = java.sql.DriverManager.getConnection(url, user, password);
	                    //conexion=Conexion.getConexion();

	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }

	            JasperPrint jasperPrint = null;

	            try {

	                if (fichero==null)
	                {
	                  fichero = JasperRunManager.runReportToPdf(fis, parameters, conexion);
	                }

	            } catch (JRException ex) {
	            	ex.printStackTrace();
	            	System.out.println(ex.toString());
	            }

	            if (conexion != null) {
	                conexion.close();
	            }

	        } catch (SQLException e) {
	            System.out.println("Error de conexión: " + e.getMessage());
	            System.exit(4);
	        } catch (Exception e) {
	            System.out.println("Error de Reporte: " + e.toString());
	            System.exit(4);
	        }

	    

	        return fichero;

	    }

		@Override
		public void inicializar() throws IOException {
			// TODO Auto-generated method stub
			
		}

}
