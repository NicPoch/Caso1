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
	 * va a determinar los servidores usando el buffer
	 */
	private AtomicInteger servidores;
	/**
	 * mensajes que pasaron por el buffer
	 */
	private AtomicInteger delivered;
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
	
	private Boolean activateRemover;
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
		servidores=new AtomicInteger(0);
		delivered=new AtomicInteger(0);
		activateRemover=true;
	}
	public void signIn()
	{
		synchronized (clientesActivos)
		{
			int i=clientesActivos.incrementAndGet();
			if(i==1)
			{
				synchronized (activateRemover) 
				{
					activateRemover=false;
				}
				System.out.println("Buffer Start");
			}
		}
	}
	public void signOut(int id)
	{
		
		synchronized (clientesActivos)
		{
			int i=clientesActivos.decrementAndGet();
			System.out.println("Quedan "+i+" se pi "+id);
			synchronized (buffer) 
			{
				System.out.println("Quedan buffer"+buffer.size());
			}
			if(i<=0)
			{
				System.out.println("Buffer Stopping");
				synchronized (activateRemover) 
				{
					activateRemover=true;
				}
			}
		}
		synchronized (activateRemover) 
		{
			if(activateRemover)
			{
				remover();
			}
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
			synchronized (vacio) 
			{
				vacio.notifyAll();
			}
		}		
		synchronized(m.pendingAns) 
		{
			m.pendingAns.wait();
		}
		synchronized (delivered) 
		{
			delivered.incrementAndGet();
		}
	}
	public int getDelivered()
	{
		return delivered.get();
	}
	public Mensaje getMessage() throws InterruptedException, StoppedServerException
	{
		
		try
		{	
			synchronized (servidores) 
			{
				System.out.println("Servidores "+servidores.incrementAndGet());	
				
			}
			synchronized (buffer) 
			{
				
			}
			int sizeBuff;
			synchronized (this)
			{
				sizeBuff=buffer.size();
			}
			if(sizeBuff==0)
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
			synchronized (servidores) 
			{
				System.out.println("Servidores "+servidores.decrementAndGet());		
				return m;
			}
			
		}
		catch(IndexOutOfBoundsException e)
		{
			synchronized (servidores) 
			{
				System.out.println("Servidores "+servidores.decrementAndGet());				
			}
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
	
	@SuppressWarnings("serial")
	public class StoppedServerException extends Exception
	{
		public StoppedServerException()
		{
			super();
		}
	}
	
	private void remover()
	{		
		System.out.println("REMOVE");
		try 
		{
			Thread.sleep(100);
			synchronized (vacio)
			{
				vacio.notifyAll();
			}
			boolean go=false;
			synchronized (servidores) 
			{
				if(servidores.get()>0)
				{
					go=true;
				}
			}
			if(go)
			{
				remover();
			}
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}		
	}
}