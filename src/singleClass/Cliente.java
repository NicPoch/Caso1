package singleClass;

import java.util.concurrent.atomic.AtomicInteger;

public class Cliente extends Thread
{
	/**
	 * el generador de identificadores de clientes
	 */
	public static AtomicInteger clientIds=new AtomicInteger(0);
	/**
	 * identificador del cliente
	 */
	private int id;
	/**
	 * lo que va a enviar el cliente
	 */
	private String contenido;
	/**
	 * el buffer
	 */
	private Buffer buffer;
	/**
	 * número de mensajes que tiene que enviar
	 */
	private int numEnvios;
	
	public Cliente(String contenido,Buffer buffer,int numEnvios)
	{
		id=clientIds.getAndIncrement();
		this.contenido=contenido;
		this.buffer=buffer;
		this.numEnvios=numEnvios;
	}	

	@Override
	public void run()
	{
		System.out.println("Cliente "+id+" sign in!");
		buffer.signIn();
		for(int i=0;i<numEnvios;i++)
		{
			Mensaje m = new Mensaje(contenido, buffer, id);
			m.start();
			try {
				m.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Cliente "+id+" sign out!");
		buffer.signOut();
	}	
	
}
