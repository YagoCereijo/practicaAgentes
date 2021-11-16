package model;

import java.io.Serializable;
import java.util.Vector;

public class Recorrido implements Serializable, Cloneable
{
	protected Float tiempo;
	protected Vector<ParadaRecorrido> paradaRecorrido;
	
	public Recorrido()
	{
		paradaRecorrido=new Vector<ParadaRecorrido>();
	}

	public Float getTiempo()
	{
		return tiempo;
	}

	public void setTiempo(Float tiempo)
	{
		this.tiempo = tiempo;
	}

	public Vector<ParadaRecorrido> getParadaRecorrido()
	{
		return paradaRecorrido;
	}

	public void setParadaRecorrido(Vector<ParadaRecorrido> paradaRecorrido)
	{
		this.paradaRecorrido = paradaRecorrido;
	}
	

	
	
}
