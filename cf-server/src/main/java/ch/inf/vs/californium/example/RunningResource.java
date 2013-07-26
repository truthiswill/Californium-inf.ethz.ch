package ch.inf.vs.californium.example;

import ch.inf.vs.californium.Server;
import ch.inf.vs.californium.coap.CoAP.ResponseCode;
import ch.inf.vs.californium.network.Exchange;
import ch.inf.vs.californium.resources.ResourceBase;

/**
 * This resource contains two subresources: shutdown and restart. Send a POST
 * request to subresource shutdown to stop the server. Send a POST request to
 * the subresource restart to restart the server.
 * 
 * @author Martin Lanter
 */
public class RunningResource extends ResourceBase {

	private Server server;
	
	private int restartCount;
	
	public RunningResource(String name, Server s) {
		super(name);
		this.server = s;
		
		add(new ResourceBase("shutdown") {
			public void processPOST(Exchange exchange) {
				exchange.respond(ResponseCode.CHANGED);
				sleep(100);
				server.stop();
			}
		});
		
		add(new ResourceBase("restart") {
			public void processPOST(Exchange exchange) {
				restartCount++;
				server.stop();
				sleep(100);
				server.start();
				exchange.respond(ResponseCode.CHANGED, "Restart count: "+restartCount);
			}
		});
	}
	
	private void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
