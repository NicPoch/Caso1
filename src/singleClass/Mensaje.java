package singleClass;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.Date;

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
	 * el momento que fue creado el mensaje
	 */
	private Date create;
	/**
	 * el momento que fue respondido
	 */
	private Date answered;
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
		this.create=new Date();
	}
	/**
	 * Recibe la respuesta del servidor
	 * @param serverPart la respuesta del servidor 
	 * @param serverId el identificador del servidor
	 */
	public void setAns(String serverPart, int serverId)
	{
		this.answered=new Date();
		this.serverPart=serverPart;
		this.serverId=serverId;
	}
	public String toString()
	{
		@SuppressWarnings("deprecation")
		String cl="Client: "+clientId+", "+clientPart+", "+create.getHours()+":"+create.getMinutes()+":"+create.getSeconds();
		@SuppressWarnings("deprecation")
		String sv="Server: "+serverId+", "+serverPart+", "+answered.getHours()+":"+answered.getMinutes()+":"+answered.getSeconds();;
		return "Message "+id+": ("+cl+"), ("+sv+")";
	}
	@Override
	public void run()
	{
		try 
		{
			buffer.inputMessage(this);
			System.out.println(this.toString());
		} catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
