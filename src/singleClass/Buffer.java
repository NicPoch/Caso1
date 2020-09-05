package singleClass;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Buffer
{
	/**
	 * va a determinar los clientes usando el buffer
	 */
	private AtomicInteger clientesActivos;
	/**
	 * capacidad del buffer
	 */
	private int capacidad;
	/**
	 * el contenedor
	 */
	private ArrayList<Mensaje>buffer;
	/**
	 * objeto para avisar a los mensajes
	 */
	private Object lleno;
	/**
	 * para avisar a los servidores
	 */
	private Object vacio;
	/**
	 * El constructor del buffer
	 * @param capacidad la capacidad del buffer
	 */
	public Buffer(int capacidad) 
	{
		this.capacidad=capacidad;
		buffer= new ArrayList<Mensaje>();
		lleno=new Object();
		vacio=new Object();
		clientesActivos=new AtomicInteger(0);
	}
	public void signIn()
	{
		synchronized (clientesActivos)
		{
			int i=clientesActivos.incrementAndGet();
			if(i==1)
			{
				System.out.println("Buffer Start");
			}
		}
	}
	public void signOut()
	{
		synchronized (clientesActivos)
		{
			int i=clientesActivos.decrementAndGet();
			if(i==0)
			{
				System.out.println("Buffer Stopping");
			}
		}
		synchronized (vacio) 
		{
			vacio.notify();
		}
	}
	public void inputMessage(Mensaje m) throws InterruptedException
	{
		
		if(buffer.size()==capacidad)
		{
			synchronized (lleno)
			{
				lleno.wait();
			}
		}
		synchronized (this)
		{
			buffer.add(m);
		}
		synchronized (vacio) 
		{
			vacio.notify();
		}
		synchronized(m.pendingAns) 
		{
			m.pendingAns.wait();
		}
	}
	
	public Mensaje getMessage() throws InterruptedException, StoppedServerException
	{
		try
		{		
			if(buffer.size()==0)
			{
				synchronized (vacio)
				{
					vacio.wait();
				}
			}		
			Mensaje m;
			synchronized (this)
			{
				m=buffer.remove(0);
			}
			synchronized (lleno) 
			{
				lleno.notify();
			}

			return m;
		}
		catch(IndexOutOfBoundsException e)
		{
			if(!shouldIgo())
			{
				throw new StoppedServerException();
			}
			Thread.yield();
			return getMessage();
		}	
	}
	public boolean shouldIgo()
	{
		synchronized (clientesActivos) 
		{
			return clientesActivos.get()>0;
		}
	}	
	public class StoppedServerException extends Exception
	{
		public StoppedServerException()
		{
			super();
		}
	}
}