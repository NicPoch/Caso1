package singleClass;

import java.util.ArrayList;

public class Main 
{
	public static void main(String[] args) 
	{
		int numClientes=Integer.parseInt(args[0]);
		int numServidores=Integer.parseInt(args[1]);
		String contenido=args[2];
		String respuesta=args[3];
		int bufferSize = Integer.parseInt(args[4]);
		ArrayList<Cliente> clientes = new ArrayList<>();
		ArrayList<Servidor> servidores= new ArrayList<>();
		Buffer buffer = new Buffer(bufferSize);
		for(int i=0; i<numClientes;i++)
		{
			Cliente c = new Cliente(contenido, buffer);
			clientes.add(c);
		}
		for(int i=0;i<numServidores;i++)
		{
			Servidor s = new Servidor(respuesta, buffer);
			servidores.add(s);
		}
		for(Servidor s : servidores)
		{
			Thread t = new Thread(s);
			t.start();
		}
		for(Cliente c : clientes)
		{
			Thread t = new Thread(c);
			t.start();
		}
	}
}
