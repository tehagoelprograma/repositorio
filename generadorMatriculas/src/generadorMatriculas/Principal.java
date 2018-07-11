package generadorMatriculas;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Principal {

	public static void main(String[] args) {
		String chorro = "";
		Character[] letras = {'B','C','D','F','G','H','J','K','L','M','N','P','R','S','T','V','W','X','Y','Z'};
		String ulti="";
		for (int i = 0 ; i < 20; i++ ) {
			for (int j = 0 ; j < 20; j++ ) {
				for (int k = 0 ; k < 20; k++ ) {


					chorro += "1884"+letras[i].toString() + letras[j]+ letras[k] +"\n";
				
					ulti="1884"+letras[i].toString() + letras[j]+ letras[k] +"\n";
				}
			}
		}


		System.out.print(ulti);
		escribirEnFichero(chorro);
	}

	private static void escribirEnFichero(String chorro) {
		String sFichero = "fichero.txt";
		File fichero = new File(sFichero);

	
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(sFichero));

			
					bw.write(chorro);


			} catch (IOException e) {
				
				e.printStackTrace();
			}
		

	}




}
