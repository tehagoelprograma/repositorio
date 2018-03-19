package pruebasPDF;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Main {



	private static final String FOTOS = "\\PDFinput\\fotos\\";
	private static final String FOTOS_REDIMENSIONADAS = "\\PDFinput\\fotos\\redimensionadas\\";
	private static final String PDF_INPUT = "\\PDFinput";
	private static final String PDF_ORIGEN = PDF_INPUT+"\\Notificaciones correos 900 Velocidad.pdf";
	private static final String PDF_OUTPUT = "\\PDFoutput";

	private static int numFotos;
	private static String path;
	private static String[] listadoMatriculas;


	public static void main(String[] args) {

		try {

			path = new File(".").getCanonicalPath();


			conectarConBBDD();
			redimensionarImagenes();
			writePDF();


		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}


	private static void writePDF() {

		String src = path + PDF_ORIGEN;

		Date hoy = new Date();	
		DateFormat hourdateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");

		String dest = path + PDF_OUTPUT + "\\estampado_" +  hourdateFormat.format(hoy) + ".pdf\\";

		try {
			manipulatePdf(src, dest);
		} catch (IOException | DocumentException e) {
			
			e.printStackTrace();
		}




	}

	/**
	 * Método que obtiene el listado (ordenado) de matriculas 
	 * @param imgs
	 * @return
	 */
	private static void conectarConBBDD() {

		String[] ordenMatriculas = new String[500];

		try {

			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://c:\\bbdd\\Nuevecita.mdb");
			Statement st = conn.createStatement();


			int i = 0;

			ResultSet rs = st.executeQuery("select * from ListadoMatriculasExportadas");

			while ( rs.next() )
			{
				ordenMatriculas[i] = rs.getObject(1).toString().replace("  -", "").trim();
				i++;
			}

			numFotos = i;


		} catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}

		listadoMatriculas = ordenMatriculas;
	}


	public static void manipulatePdf(String src, String dest) throws IOException, DocumentException {

		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		Image  image, imageRecorte, imageMatricula;


		int i = 1;

		for (int a = 0 ; a < numFotos ; a++) {

			image = Image.getInstance(path + FOTOS_REDIMENSIONADAS + listadoMatriculas[a] + ".jpg");
			PdfImage stream = new PdfImage(image, "", null);

			stream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
			PdfIndirectObject ref = stamper.getWriter().addToBody(stream);
			image.setDirectReference(ref.getIndirectReference());
			image.setAbsolutePosition(30, 650);
			PdfContentByte over = stamper.getOverContent(i);
			over.addImage(image);
			
			
			
			imageRecorte = Image.getInstance(path + FOTOS_REDIMENSIONADAS + listadoMatriculas[a] + "_recorte.jpg");
			PdfImage streamRecorte = new PdfImage(imageRecorte, "", null);
			streamRecorte.put(new PdfName("ITXT_SpecialId2"), new PdfName("123456788"));
			PdfIndirectObject refRecorte = stamper.getWriter().addToBody(streamRecorte);
			imageRecorte.setDirectReference(refRecorte.getIndirectReference());
			imageRecorte.setAbsolutePosition(30, 810);
			//PdfContentByte overRecorte = stamper.getOverContent(i);
			over.addImage(imageRecorte);
			
			imageMatricula = Image.getInstance(path + FOTOS_REDIMENSIONADAS + listadoMatriculas[a] + " - copia.jpg");
			PdfImage streamMatricula = new PdfImage(imageMatricula, "", null);
			streamMatricula.put(new PdfName("ITXT_SpecialId3"), new PdfName("123456787"));
			PdfIndirectObject refMatricula = stamper.getWriter().addToBody(streamMatricula);
			imageMatricula.setDirectReference(refMatricula.getIndirectReference());
			imageMatricula.setAbsolutePosition(30, 560);
			//PdfContentByte overRecorte = stamper.getOverContent(i);
			over.addImage(imageMatricula);
			
			
			

			i++;
			i++; // para que se salte una pagina
	
		}
		stamper.close();
		reader.close();
	}




	/*
    Este método se utiliza para cargar la imagen de disco
	 */
	public static BufferedImage loadImage(String pathName) {
		BufferedImage bimage = null;
		try {
			bimage = ImageIO.read(new File(pathName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bimage;
	}

	/*
    Este método se utiliza para redimensionar la imagen
	 */
	public static BufferedImage resize(BufferedImage bufferedImage, int newW, int newH) {
		int w = bufferedImage.getWidth();
		int h = bufferedImage.getHeight();

		//int newW = 279;//(int) (w * 0.90);
		//int newH  =181;//(int) (h * 0.90);
		BufferedImage bufim = new BufferedImage(newW, newH, bufferedImage.getType());
		Graphics2D g = bufim.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		g.drawImage(bufferedImage, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();

		return bufim;
	}



	/*
    Este método se utiliza para almacenar la imagen en disco
	 */
	public static void saveImage(BufferedImage bufferedImage, String pathName) {
		try {
			String format = (pathName.endsWith(".png")) ? "png" : "jpg";
			File file =new File(pathName);
			file.getParentFile().mkdirs();
			ImageIO.write(bufferedImage, format, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void redimensionarImagenes() {
		File f = new File(path + FOTOS_REDIMENSIONADAS );
		
		
		if (f.exists() ) f.delete(); else f.mkdir();
		
		
		BufferedImage img, imgResized, imgMat, imgMatResized;
		for (int i=0; i<numFotos ; i++) {

			img = loadImage(path + FOTOS + listadoMatriculas[i] + ".jpg");
			imgResized = resize(img, 279, 181);
			saveImage(imgResized, path + FOTOS_REDIMENSIONADAS + listadoMatriculas[i] + ".jpg" );
			
			recortar(img, path + FOTOS_REDIMENSIONADAS + listadoMatriculas[i] + ".jpg");
			
			
			imgMat = loadImage(path + FOTOS + listadoMatriculas[i] + " - copia.jpg");
			imgMatResized = resize(imgMat, 279, 120);
			saveImage(imgMatResized, path + FOTOS_REDIMENSIONADAS + listadoMatriculas[i] +  " - copia.jpg" );
		}
	}
	
	
	private static void recortar (BufferedImage bufferedImage, String pathName) {
		BufferedImage recorte =  bufferedImage.getSubimage(0, 0, 1700, 90);
		recorte = resize(recorte, 530, 30);
		saveImage(recorte, pathName.replace(".jpg", "_recorte.jpg"));
	} 

}




