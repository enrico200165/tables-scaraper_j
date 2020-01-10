package com.enrico200165.weblistscraper.tools;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Invocation.Builder;

import org.apache.log4j.Logger;

/* importante per capire qualcosa di questa classe:
 * l'invocation builder viene generato dal target, quindi è disponibile poco prima dell'invocazione,
 * non ha senso memorizzarlo, IN MODO STABILE, QUI, c'è un attributo builder per uso temporaneo
 * non appena lo otteniamo impostiamo l'attributo con onSet()
 * 
 * le funzionalità di questa classe di fatto sono nel metodo onSet() che configura il builder
 * che stiamo ricevendo proprio perchè lo configuriamo
 */
public abstract class InvocationBuilderWrapper {

	public InvocationBuilderWrapper() {
		headers = new HashMap<String, String>();
	}

		
	public void setOauthToken(String oauthTokenPar) {		
		oauthToken = oauthTokenPar;
	}

	
	
	public Builder it() {
		return this.bld;
	}

	public InvocationBuilderWrapper setBuilder(Builder b) {
		this.bld = b;
		onSet();
		return this;
	}

	public boolean setHeader(String header, String value) {
		this.bld.header(header, value);
		return true;
	}

	protected abstract InvocationBuilderWrapper adjustHeader(String headerName);

	abstract public InvocationBuilderWrapper onSet();

	protected Builder bld;
	protected String oauthToken;
	
	protected Map<String, String> headers;

	private static org.apache.log4j.Logger log = Logger.getLogger(InvocationBuilderWrapper.class);
}
