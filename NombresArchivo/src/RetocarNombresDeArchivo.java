import java.io.File;
import java.io.IOException;

public class RetocarNombresDeArchivo {

	private static String path;

	public static void main(String[] args) {

		try {
			path = new File(".").getCanonicalPath();



			File archivo = new File (path + "\\FOTOS");
			String[] ficheros = archivo.list(); 

			if (ficheros == null)
				System.out.println("No hay ficheros en el directorio especificado");
			else { 
				for (int x=0;x<ficheros.length;x++) {
					//if (!Character.isDigit(ficheros[x].charAt(0))) {
						String term = ficheros[x].substring(ficheros[x].length()-7, ficheros[x].length()-4).trim();
						int num = Integer.parseInt(term);
						File f = new File(path + "\\FOTOS\\" + ficheros[x].substring(0, ficheros[x].length()-7) + num + ".jpg" );
						new File(path + "\\FOTOS\\" + ficheros[x]).renameTo(f );
					//}
				}
			}


//			if (ficheros == null)
//				System.out.println("No hay ficheros en el directorio especificado");
//			else { 
//				for (int x=0;x<ficheros.length;x++) {
//
//					System.out.println(ficheros[x]);
//
//				}
//			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}


