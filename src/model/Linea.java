package model;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

public class Linea implements Serializable
{	
	/**
	 * Tiempo que se tarda entre parada y parada. Cada l�nea conoce los tiempos que tardan
	 * sus autobuses y no los del resto;
	 */
	protected Float tiempo[];
	
	/**
	 * hora de inicio de cada recorrido para la l�nea.
	 */
	protected Float inicioLinea[];
	
	/**
	 * Conexiones entre paradas de la l�nea 
	 */
	protected Integer parada[];
	
	protected Integer linea;
	
	/**
	 * Por cada parada guarda las horas de llegada
	 */
	protected Hashtable<Integer, Vector<LlegadaLinea>> horarioParada;


	public Float[] getTiempo()
	{
		return tiempo;
	}

	public void setTiempo(Float[] tiempo)
	{
		this.tiempo = tiempo;
	}

	public Float[] getInicioLinea()
	{
		return inicioLinea;
	}

	public void setInicioLinea(Float[] inicioLinea)
	{
		this.inicioLinea = inicioLinea;
	}

	public Integer[] getParada()
	{
		return parada;
	}

	public void setParada(Integer[] parada)
	{
		this.parada = parada;
	}

	public Integer getLinea()
	{
		return linea;
	}

	public void setLinea(Integer linea)
	{
		this.linea = linea;
	}

	public Hashtable<Integer, Vector<LlegadaLinea>> getHorarioParada()
	{
		return horarioParada;
	}

	public void setHorarioParada(
			Hashtable<Integer, Vector<LlegadaLinea>> horarioParada)
	{
		this.horarioParada = horarioParada;
	}
	
	
}
