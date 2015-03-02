package controlador.reportes.evaluacion;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Impresion extends HttpServlet {
	
	 /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

         String par1 = request.getParameter("par1");
         String par2 = request.getParameter("par2");
         String par3 = request.getParameter("par3");
         String par4 = request.getParameter("par4");
         String par5 = request.getParameter("par5");
         String par6 = request.getParameter("par6");
         String par7 = request.getParameter("par7");
         String par8 = request.getParameter("par8");
         String par9 = request.getParameter("par9");
         String par10 = request.getParameter("par10");

        ReporteBase reporte = new ReporteBase();
        ServletOutputStream out;

        byte[] fichero = reporte.getReporte(par1,par2,par3,par4,par5,par6,par7,par8,par9,par10);
        

        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=evaluacion.pdf");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires", 0);
        response.setContentLength(fichero.length);
        out = response.getOutputStream();
        out.write(fichero, 0, fichero.length);
        out.flush();
        out.close();

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>

}
