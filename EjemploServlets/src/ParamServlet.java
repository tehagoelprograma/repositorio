import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig(location="D:/uploads")
public class ParamServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// Obtenemos un objeto Print Writer para enviar respuesta
				
		 res.setContentType("text/html;charset=UTF-8");
	        Collection<Part> parts = req.getParts();
	        for(Part part : parts) {
	        	String s = getFileName(part);
	                part.write(s);
	        }
		
		
		
		PrintWriter pw = res.getWriter();
		pw.println("<HTML><HEAD><TITLE>Leyendo parámetros</TITLE></HEAD>");
		pw.println("<BODY BGCOLOR=\"#CCBBAA\">");
		pw.println("<H2>Leyendo parámetros desde un formulario html</H2><P>");
		pw.println("<UL>\n");
		/*pw.println("Te llamas " + req.getParameter("NOM") + "<BR>");
		pw.println("y tienes "  + req.getParameter("EDA") + " años<BR>");*/
		pw.println("</BODY></HTML>");
		pw.close();
	}
	
	
	
	
	
	 public String getFileName(Part part) {
	        String contentHeader = part.getHeader("content-disposition");
	        String[] subHeaders = contentHeader.split(";");
	        for(String current : subHeaders) {
	            if(current.trim().startsWith("filename")) {
	                int pos = current.indexOf('=');
	                String fileName = current.substring(pos+1);
	                return fileName.replace("\"", "");
	            }
	        }
	        return null;
	    }
} 