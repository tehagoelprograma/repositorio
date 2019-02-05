package aTexto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Principal {

	private static String path;

	public static void main(String[] args) {



		try {
			path = new File(".").getCanonicalPath();

			aperturaReader();
		} catch (IOException e) {

			e.printStackTrace();
		}


		aperturaReader();






	}





	private static void  aperturaReader() {
		FileReader fr = null;
		BufferedReader br= null;
		FileWriter fsalida = null;
		PrintWriter pw = null;
		File fentrada;


		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).


			fentrada = obtenerFicheroTexto();

			fsalida = new FileWriter(fentrada +"_arreglado" );
			pw = new PrintWriter(fsalida);

			String  lineanueva, linea = "";

			FileReader f = new FileReader(fentrada);
			BufferedReader b = new BufferedReader(f);
			while((linea = b.readLine())!=null) {

				lineanueva = cambiarObservaciones(linea);
				lineanueva = cambiarCausa_situacion(lineanueva);
				lineanueva = arreglarMatriculasViejas(lineanueva);
				pw.println(lineanueva);

			}
			b.close();
			pw.close();





		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close(); 

				}   
				if (null != br) {
					pw.close();
				}
			}catch (Exception e2){ 
				e2.printStackTrace();
			}

		}
	}

	
	
	private static String arreglarMatriculasViejas(String linea) {
		String linea1;
		String linea2;
		String lineanueva;
		String matriculaVieja;
		String matriculaNueva;
		
		linea1=linea.substring(0, 523);

		matriculaVieja = linea.substring(523, 538);
		matriculaNueva= matriculaVieja.replace("-", "");

		linea2=linea.substring(538, linea.length());

		lineanueva=linea1 + rellenarAlfanumerico(matriculaNueva, 15) + linea2;
		return lineanueva;
	}

	private static String cambiarCausa_situacion(String linea) {
		String linea1;
		String linea2;
		String lineanueva;
		//String causa_situacion;
		String nuevaCausa_situacion;
		linea1=linea.substring(0, 789);

		//causa_situacion = linea.substring(789, 889);
		
		
		nuevaCausa_situacion= "Infracción recogida a través de medios de captación de imagenes";

		linea2=linea.substring(889, linea.length());

		lineanueva=linea1 + rellenarAlfanumerico(nuevaCausa_situacion, 100) + linea2;
		return lineanueva;
	}


	private static String cambiarObservaciones(String linea) {
		String linea1;
		String linea2;
		String lineanueva;
		String observaciones;
		String nuevaObservaciones;
		linea1=linea.substring(0, 344);

		observaciones = linea.substring(344, 494);
		int vel = Integer.parseInt(observaciones.replace(" ", ""));
		
		nuevaObservaciones= "Captado a una velocidad de " + vel + "km/h utilizando el modelo Jenoptik-Robot/MULTARADAR C/TCV";

		linea2=linea.substring(494, linea.length());

		lineanueva=linea1 + rellenarAlfanumerico(nuevaObservaciones, 150) + linea2;
		return lineanueva;
	}


	private static String rellenarAlfanumerico(String literal, int tamano) {
		return String.format("%-"+ tamano +  "s", literal);
	}




	private static File obtenerFicheroTexto() {

		File archivo = new File (path);
		String[] ficheros = archivo.list(); 

		if (ficheros == null)
			System.out.println("No hay ficheros en el directorio especificado");
		else { 
			for (int x=0;x<ficheros.length;x++) {
				
				if (ficheros[x].startsWith("Export")) {
					
					return new File(path + "\\" + ficheros[x]);
				}
			}
		}
		return null;
	}
	
	
}



