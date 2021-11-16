package behaviours;

import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.Linea;
import model.LlegadaLinea;
import model.ParadaRecorrido;

/**
 * Tiene informaci�n de las l�neas y la hora de inicio de cada recorrido.
 * @author Fran
 *
 */
public class CyclicBehaviourLinea extends CyclicBehaviour
{
	protected final static int NUM_PARADAS=13;
	
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
	
	
	
	public CyclicBehaviourLinea(Agent agent)
	{
		super(agent);
	}

	
	
	public Hashtable<Integer, Vector<LlegadaLinea>> getHorarioParada()
	{
		return horarioParada;
	}
	public Float[] getTiempo()
	{
		return tiempo;
	}
	public void setTiempo(Float[] tiempo)
	{
		this.tiempo = tiempo;
	}
	public Integer[] getParada()
	{
		return parada;
	}
	public void setParada(Integer[] parada)
	{
		this.parada = parada;
	}


	@Override
	public void action()
	{
		// TODO Auto-generated method stub
        ACLMessage msg=this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
        try
		{
        	Linea linea=new Linea();
        	linea.setInicioLinea(inicioLinea);
        	linea.setParada(parada);
        	linea.setTiempo(tiempo);
        	linea.setLinea(this.linea);
        	linea.setHorarioParada(horarioParada);
        	
           	ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
       		aclMessage.addReceiver(msg.getSender());
            aclMessage.setOntology("ontologia"+this.linea);
            //el lenguaje que se define para el servicio
            aclMessage.setLanguage(new SLCodec().getName());
            //el mensaje se transmita en XML
            aclMessage.setEnvelope(new Envelope());
			//cambio la codificacion de la carta
			aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
            //aclMessage.getEnvelope().setAclRepresentation(FIPANames.ACLCodec.XML); 
    		aclMessage.setContentObject((Serializable)linea);
    		this.myAgent.send(aclMessage);  
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * En cada parada calcular� las horas por las que pasa un bus y almacenar� la hora y la l�nea del bus que pasa por esa parada
	 */
	public void calcularHorariosParada()
	{
    	Float tiempoTemp;
    	for(int l=0;l<inicioLinea.length;l++)
    	{
    		tiempoTemp=0f;
    		for(int i=0;i<parada.length;i++)
    		{
    			if(i==0)
    				tiempoTemp=inicioLinea[l];
    			else
    				tiempoTemp+=tiempo[i-1];
    			
    			if(horarioParada.get(parada[i])==null)
    			{
    				Vector<LlegadaLinea> vectorLinea=new Vector<LlegadaLinea>();
    				vectorLinea.add(new LlegadaLinea(tiempoTemp, linea));
    				horarioParada.put(parada[i], vectorLinea);
    			}
    			else
    				horarioParada.get(parada[i]).add(new LlegadaLinea(tiempoTemp, linea));
    				
    		}
    	}
	}

}
