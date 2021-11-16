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
import model.LlegadaLinea;
import model.ParadaRecorrido;

/**
 * Tiene informaci�n de las l�neas y la hora de inicio de cada recorrido.
 * @author Fran
 *
 */
public class CyclicBehaviourLinea1 extends CyclicBehaviourLinea
{
	public CyclicBehaviourLinea1(Agent agent)
	{
		super(agent);
		
		inicioLinea=new Float[]{1.0f, 2.0f, 150.0f, 200.0f, 300.0f};
		parada=new Integer[]{1,2,3,4,5,6,7,8,9,10};		
		tiempo=new Float[]{2.0f, 3.0f, 2.0f, 7.0f, 1.0f, 4.0f, 3.0f, 2.0f, 1.0f};
		horarioParada=new Hashtable<Integer, Vector<LlegadaLinea>>();
		linea=1;
		calcularHorariosParada();
		
		
		
	}
}
