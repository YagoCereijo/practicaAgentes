package agent;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import behaviours.CyclicBehaviourRuta;

/**
 * El AgentRuta calcular� la ruta a seguir tomando el origen, destino y hora de salida. 
 * Preguntar� al AgentRecorrido por los recorridos teniendo en cuenta la hora de salida
 * El AgentRecorrido le deber� devolver la matriz de pesos actualizada teniendo en cuenta
 * retrasos de tiempo en funci�n de la salida de los autobuses en cada l�nea.
 * @author Fran
 *
 */
public class AgentRuta extends Agent
{
	protected ParallelBehaviour parallelBehaviour; 
	
	@Override
	protected void setup()
	{
		//Crear servicios proporcionados por el agentes y registrarlos en la plataforma
	    DFAgentDescription dfd = new DFAgentDescription();
	    dfd.setName(getAID());
	    ServiceDescription sd = new ServiceDescription();
	    sd.setName("Ruta");
	    //establezco el tipo del servicio para poder localizarlo cuando haga una busqueda
	    sd.setType("ruta");
	    // Agents that want to use this service need to "know" the ontologia
	    sd.addOntologies("ontologia");
	    // Agents that want to use this service need to "speak" the FIPA-SL language
	    sd.addLanguages(new SLCodec().getName());
	    dfd.addServices(sd);
	    
	    try
	    {
	    	//registro los servicios
	        DFService.register(this, dfd);
	    }
	    catch(FIPAException e)
	    {
	        System.err.println("Agente "+getLocalName()+": "+e.getMessage());
	    }
	    
	    
	    //tratar 5 peticiones a la vez
	    parallelBehaviour=new ParallelBehaviour();
	    ThreadedBehaviourFactory tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new CyclicBehaviourRuta(this)));
	    
	    tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new CyclicBehaviourRuta(this)));
	    
	    tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new CyclicBehaviourRuta(this)));

	    tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new CyclicBehaviourRuta(this)));

	    tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new CyclicBehaviourRuta(this)));
	    
	    addBehaviour(parallelBehaviour);
	}
}
