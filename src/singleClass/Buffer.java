package singleClass;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Buffer
{
	/**
	 * el mutex de acceso para quitar elementos del buffer
	 */
	private AtomicInteger mutexArrayList;
	/**
	 * el objeto para hacer esperar servidores para que quiten mensajes
	 */
	private Object esperaAcceso;
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
		mutexArrayList= new AtomicInteger(0);
		lleno=new Object();
		vacio=new Object();
		esperaAcceso=new Object();
	}
	
	public void inputMessage(Mensaje m) throws InterruptedException
	{
		mutexArrayList.addAndGet(1);
		synchronized (esperaAcceso) 
		{
			esperaAcceso.notify();
		}
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
	
	public Mensaje getMessage() throws InterruptedException
	{
		if(mutexArrayList.get()==0)
		{
			synchronized (esperaAcceso)
			{
				esperaAcceso.wait();
			}
		}
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
			mutexArrayList.getAndDecrement();
			m=buffer.remove(0);
		}
		synchronized (lleno) 
		{
			lleno.notify();
		}
		return m;
	}
}
