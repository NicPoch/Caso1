package singleClass;

import java.util.ArrayList;
import java.util.Random;

public class Main 
{
	public static void main(String[] args) throws InterruptedException 
	{
		System.out.println("Start Main");
		//lee número de clientes
		int numClientes=Integer.parseInt(args[0]);
		//lee número de servidores
		int numServidores=Integer.parseInt(args[1]);
		//el contenido que va a llevar cada mensaje
		String contenido=args[2];
		//la respuesta de cada servidor
		String respuesta=args[3];
		//el tamaño del buffer
		int bufferSize = Integer.parseInt(args[4]);
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
