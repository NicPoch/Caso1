package singleClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main 
{
	public static void main(String[] args) throws InterruptedException, FileNotFoundException 
	{
		System.out.println("Start Main");
		
		File file= new File("C:\\Users\\Nicolas\\eclipse-workspace\\Caso1\\excTextFile.txt");
		Scanner myReader = new Scanner(file);
		//lee número de clientes
		int numClientes=Integer.parseInt(myReader.nextLine());
		//lee número de servidores
		int numServidores=Integer.parseInt(myReader.nextLine());
		//el contenido que va a llevar cada mensaje
		String contenido=myReader.nextLine();
		//la respuesta de cada servidor
		String respuesta=myReader.nextLine();
		//el tamaño del buffer
		int bufferSize = Integer.parseInt(myReader.nextLine());
		//arreglo de threads clientes
		ArrayList<Cliente> clientes = new ArrayList<>();
		//arreglo de los threads servidores
		ArrayList<Servidor> servidores= new ArrayList<>();
		//el buffer que se va a usar
		Buffer buffer = new Buffer(bufferSize);
		//Se crean los clientes
		Random r = new Random();
		for(int i=0; i<numClientes;i++)
		{
			int numEnvios = r.nextInt(2-1) +1;
			Cliente c = new Cliente(contenido, buffer,numEnvios);
			clientes.add(c);
		}
		//Se crean los servidores
		for(int i=0;i<numServidores;i++)
		{
			Servidor s = new Servidor(respuesta, buffer);
			servidores.add(s);
		}
		for(Cliente c : clientes)
		{
			c.start();
		}
		for(Servidor s : servidores)
		{
			s.start();
		}
		for(int i=0;i<Math.max(clientes.size(), servidores.size());i++)
		{
			if(i<clientes.size())
			{
				Thread c=clientes.remove(0);
				c.join();
			}
			if(i<servidores.size())
			{
				Thread s =servidores.remove(0);
				s.join();
			}
		}
		System.out.println("End Main");
	}
}
