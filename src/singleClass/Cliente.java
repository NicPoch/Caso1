package singleClass;

import java.util.concurrent.atomic.AtomicInteger;

public class Cliente implements Runnable
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
	
	public Cliente(String contenido,Buffer buffer)
	{
		id=clientIds.getAndIncrement();
		this.contenido=contenido;
		this.buffer=buffer;
	}	

	@Override
	public void run()
	{
		Mensaje m = new Mensaje(contenido, buffer, id);
		m.start();
	}	
	
}
