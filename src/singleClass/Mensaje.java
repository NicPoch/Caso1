package singleClass;

import java.util.concurrent.atomic.AtomicInteger;

public class Mensaje extends Thread 
{
	/**
	 * el generador de identificadores de mensajes
	 */
	public static AtomicInteger messageIds=new AtomicInteger(0);
	/**
	 * el mensaje del cliente
	 */
	private String clientPart;
	/**
	 * ls respuesta del servidor
	 */
	private String serverPart;
	/**
	 * un objeto para generar espera de respuesta
	 */
	public Object pendingAns;
	/**
	 * identificador del mensaje
	 */
	private int id;
	/**
	 * El buffer
	 */
	private Buffer buffer;
	/**
	 * identificador de quien lo mando
	 */
	private int clientId;
	/**
	 * identificador de quien lo respondio
	 */
	private  int serverId;
	/**
	 * El constructor del mensaje
	 * @param clientPart el mensaje del cliente
	 * @param buffer el buffer
	 * @param clientId el identificador del cliente
	 */
	public Mensaje(String clientPart,Buffer buffer, int clientId)
	{
		this.clientPart=clientPart;
		this.clientId=clientId;
		id=messageIds.getAndIncrement();
		pendingAns=new Object();
		this.buffer=buffer;
	}
	/**
	 * Recibe la respuesta del servidor
	 * @param serverPart la respuesta del servidor 
	 * @param serverId el identificador del servidor
	 */
	public void setAns(String serverPart, int serverId)
	{
		this.serverPart=serverPart;
		this.serverId=serverId;
	}
	public String toString()
	{
		return "Message "+id+": (Cliente "+clientId+": "+clientPart+"), (Servidor "+serverId+": "+serverPart+")";
	}
	@Override
	public void run()
	{
		try 
		{
			System.out.println("Se va a enviar el mensaje "+id);
			buffer.inputMessage(this);
			System.out.println(this.toString());
		} catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
