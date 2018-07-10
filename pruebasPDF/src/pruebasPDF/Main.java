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
import javax.swing.JOptionPane;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Main {



	private static final String _01_JPG = "_01.jpg";
	private static final String _02_JPG = "_02.jpg";
	private static final String _03_JPG = "_03.jpg";
	private static final String NOTIFICACIONES_CORREOS_900_VELOCIDAD_PDF = "Notificaciones correos 900 Velocidad.pdf";
	private static final String NOTIFICACIONES_CORREOS_901_VELOCIDAD_PDF = "Notificaciones correos 901 Velocidad.pdf";
	private static final String FOTOS = "\\PDFinput\\fotos\\";
	private static final String FOTOS_REDIMENSIONADAS = "\\PDFinput\\fotos\\redimensionadas\\";
	private static final String PDF_INPUT = "\\PDFinput\\";

	private static final String PDF_OUTPUT = "\\PDFoutput\\";


	private static int numFotos;
	private static String path;
	private static String PDF_ORIGEN;
	private static String[] listadoMatriculas;


	public static void main(String[] args) {

		try {

			path = new File(".").getCanonicalPath();

			obtenerPDForigen(); 
			vaciarCarpetas(PDF_OUTPUT);
			conectarConBBDD();
			verMultasDobles();
			redimensionarImagenes();
			writePDF();
			vaciarCarpetas(FOTOS_REDIMENSIONADAS);
			borrarFotosOrigen();
			borrarPDFOrigen();


		} catch (IOException e) {

			e.printStackTrace();
		}
	}





	private static void verMultasDobles() {
		
		
		for (int i = 0 ; i < numFotos ; i++) {
			for (int j = i+1 ; j < numFotos ; j++) {
				if (listadoMatriculas[i].compareTo(listadoMatriculas[j]) == 0) {
					listadoMatriculas[j]+="_02";
				} 
			}
		}

	}





	private static void obtenerPDForigen() {

		File archivo = new File(path + PDF_INPUT + NOTIFICACIONES_CORREOS_900_VELOCIDAD_PDF);

		if (archivo.exists()) {
			PDF_ORIGEN = NOTIFICACIONES_CORREOS_900_VELOCIDAD_PDF;
		}else {
			//901
			archivo = new File(path + PDF_INPUT + NOTIFICACIONES_CORREOS_901_VELOCIDAD_PDF);
			if (archivo.exists()) {

				PDF_ORIGEN =NOTIFICACIONES_CORREOS_901_VELOCIDAD_PDF;
			} else {

				JOptionPane.showMessageDialog(null, "No se ha encontrado el pdf de origen", 
						"PDF no encontrado", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		}



	}








	private static void writePDF() {

		String src = path + PDF_INPUT +  PDF_ORIGEN;

		//Date hoy = new Date();	
		//DateFormat hourdateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");

		String dest = path + PDF_OUTPUT  + PDF_ORIGEN; // hourdateFormat.format(hoy) + ".pdf\\";

		try {
			manipulatePdf(src, dest);
		} catch (IOException e) {

			e.printStackTrace();
		} catch (DocumentException e) {

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
			Connection conn=DriverManager.getConnection("jdbc:ucanaccess://"+ path + "\\Nuevecita.mdb");
			Statement st = conn.createStatement();


			int i = 0;

			ResultSet rs = st.executeQuery("select * from ListadoMatriculasExportadas");

			while ( rs.next() )
			{
				
				if ('C' == (rs.getObject(1).toString().charAt(0)) && !Character.isLetter(rs.getObject(1).toString().charAt(1))){
					ordenMatriculas[i] = rs.getObject(1).toString().replace("-", "").replace(" ", "");
				} else if (Character.isSpaceChar(rs.getObject(1).toString().charAt(0))) {
					ordenMatriculas[i] = rs.getObject(1).toString().replace("-", "").replace(" ", "");
				} else {
					ordenMatriculas[i] = rs.getObject(1).toString().replace(" ", "");
				}


				i++;
			}

			numFotos = i;


		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

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

			File imagenExiste = new File (path + FOTOS_REDIMENSIONADAS + listadoMatriculas[a] + ".jpg");
			if (imagenExiste.exists()) {

				image = Image.getInstance(path + FOTOS_REDIMENSIONADAS + listadoMatriculas[a] + ".jpg");


				PdfImage stream = new PdfImage(image, "", null);

				stream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
				PdfIndirectObject ref = stamper.getWriter().addToBody(stream);
				image.setDirectReference(ref.getIndirectReference());
				image.setAbsolutePosition(30, 647);
				PdfContentByte over = stamper.getOverContent(i);
				over.addImage(image);



				imageRecorte = Image.getInstance(path + FOTOS_REDIMENSIONADAS + listadoMatriculas[a] + "_recorte.jpg");
				PdfImage streamRecorte = new PdfImage(imageRecorte, "", null);
				streamRecorte.put(new PdfName("ITXT_SpecialId2"), new PdfName("123456788"));
				PdfIndirectObject refRecorte = stamper.getWriter().addToBody(streamRecorte);
				imageRecorte.setDirectReference(refRecorte.getIndirectReference());
				imageRecorte.setAbsolutePosition(30, 797);
				//PdfContentByte overRecorte = stamper.getOverContent(i);
				over.addImage(imageRecorte);

				//	String matDetalle = obtenerDetalle(listadoMatriculas[a]);
				String cola = listadoMatriculas[a].endsWith("_02") ?
						_03_JPG :
							_01_JPG ;
				imageMatricula = Image.getInstance(path + FOTOS_REDIMENSIONADAS + listadoMatriculas[a].replace("_02", "") + cola);  // + matDetalle);
				PdfImage streamMatricula = new PdfImage(imageMatricula, "", null);
				streamMatricula.put(new PdfName("ITXT_SpecialId3"), new PdfName("123456787"));
				PdfIndirectObject refMatricula = stamper.getWriter().addToBody(streamMatricula);
				imageMatricula.setDirectReference(refMatricula.getIndirectReference());
				imageMatricula.setAbsolutePosition(30, 558);
				//PdfContentByte overRecorte = stamper.getOverContent(i);
				over.addImage(imageMatricula);


			}

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
			//comprobamos si existe
			File imgAredimensionar = new File(path + FOTOS + listadoMatriculas[i] + ".jpg");
			if (imgAredimensionar.exists() ) {
				String cola = listadoMatriculas[i].endsWith("_02") ?
						_03_JPG :
							_01_JPG ;

				img = loadImage(path + FOTOS + listadoMatriculas[i] + ".jpg");
				imgResized = resize(img, 279, 181);
				saveImage(imgResized, path + FOTOS_REDIMENSIONADAS + listadoMatriculas[i] + ".jpg" );

				recortar(img, path + FOTOS_REDIMENSIONADAS + listadoMatriculas[i] + ".jpg");


				imgMat = loadImage(path + FOTOS + listadoMatriculas[i].replace("_02", "") + cola);
				imgMatResized = resize(imgMat, 279, 120);
				saveImage(imgMatResized, path + FOTOS_REDIMENSIONADAS + listadoMatriculas[i].replace("_02", "") + cola );
			}
		}
	}


	private static void recortar (BufferedImage bufferedImage, String pathName) {
		BufferedImage recorte =  bufferedImage.getSubimage(0, 0, 1850, 90);
		recorte = resize(recorte, 535, 30);
		saveImage(recorte, pathName.replace(".jpg", "_recorte.jpg"));
	} 


	private static void vaciarCarpetas(String carpeta) {

		File dir = new File(path + carpeta);
		File[] archivos = dir.listFiles();

		for (File archivo : archivos)
			archivo.delete();


	}



	private static void borrarFotosOrigen() {
		int op = JOptionPane.showConfirmDialog(null, "EL PROCESO HA FINALIZADO, ¿Desea borrar las fotos?", "Confirmar borrado fotos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		// 0=yes, 1=no, 2=cancel
		if (op == 0) {
			File dir = new File(path + FOTOS);

			//si se ha generado el archivo podemos borrar las imagenes de entrada
			if (dir != null && dir.list().length > 0 ) {
				File[] archivos = dir.listFiles();

				for (File archivo : archivos)
					archivo.delete();

			}
		}
	}


	private static void borrarPDFOrigen() {
//		int op = JOptionPane.showConfirmDialog(null, "¿Desea borrar el PDF de origen?", "Confirmar borrado PDF Origen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//		// 0=yes, 1=no, 2=cancel
//		if (op == 0) {

		File pdfOrig = new File(path + PDF_INPUT + PDF_ORIGEN);
		pdfOrig.delete();
//		}

	}

	/**Si hay dos multas a la misma matricula en el futuro se puede usar este método para obtener el nombre del archivo detalle de la 
	 * segunda multa. Actualmente en la BBDD solo aparece la misma matricula dos veces y no se puede establecer correspondencia.
	 * Obtiene el nombre del archivo con el detalle de la matricula -NO SE USA
	 * @param matricula
	 * @return
	 */
	private static String  obtenerDetalle(String matricula) {

		String detalle = matricula;


		if(matricula.contains("_")){
			String sub=matricula.substring(matricula.length()-6, matricula.length()-4);

			Integer i = Integer.valueOf(sub);
			i++;
			String res= "0"+Integer.toString(i)  ;

			detalle = matricula.replace(sub , res);
		} else {
			detalle = detalle.replace(".jpg", _01_JPG);
		}



		return detalle;

	}


}




