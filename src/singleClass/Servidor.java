package singleClass;

import java.util.concurrent.atomic.AtomicInteger;

import singleClass.Buffer.StoppedServerException;

public class Servidor extends Thread
{
	/**
	 * el generador de identificadores para los servidores
	 */
	public static AtomicInteger serverIds=new AtomicInteger(0);
	/**
	 * el identificador del servidor
	 */
	private int id;
	/**
	 * la respuesta por defecto
	 */
	private String respuesta;
	/**
	 * el mensaje almacenado
	 */
	private Mensaje mensaje;
	/**
	 * el acceso al buffer
	 */
	private Buffer buffer;
	
	private int messageAns;
	/**
	 * el constructor de servidores
	 * @param id el identificador del servidor
	 * @param respuesta la respuesta que va dar el servidor
	 * @throws InterruptedException 
	 */
	public Servidor(String respuesta,Buffer buffer) 
	{
		this.id=serverIds.getAndIncrement();
		this.respuesta=respuesta;
		this.buffer=buffer;
		mensaje=null;
		messageAns=0;
	}
	public void ansMesage() throws InterruptedException, StoppedServerException
	{
		mensaje=buffer.getMessage();
		messageAns++;
		mensaje.setAns(respuesta, id);
		synchronized (mensaje.pendingAns) 
		{
			mensaje.pendingAns.notify();
		}		
	}
	@Override
	public void run() 
	{
		System.out.println("Server "+id+" start!");
		boolean go=true;
		while(go)
		{
			try
			{
				while(go)
				{
					if(buffer.shouldIgo())
					{
						System.out.println("Server "+id+" answers: "+messageAns);
						ansMesage();
					}
					else
					{
						go=false;
					}
					
				}
			}
			catch (StoppedServerException e)
			{
				go=false;
			}
			catch(Exception e)
			{
				System.out.println("Server "+id+" stopped!");
				e.printStackTrace();
			}
		}
		System.out.println("No more clients server "+id+" stopping");
	}
}
