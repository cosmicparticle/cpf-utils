package cho.carbon.hc.copframe.utils.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;

import org.apache.log4j.Logger;

public class StaticRmiSocketFactory implements RMIClientSocketFactory, RMIServerSocketFactory, Serializable {

	Logger logger = Logger.getLogger(StaticRmiSocketFactory.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 5009149455880098225L;
	private String staticIp;
	private int staticPort;

	public StaticRmiSocketFactory(String ip, int port) {
		this.staticIp = ip;
		this.staticPort = port;
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException {
		logger.info("create client socket " + this.staticIp + ":" + this.staticPort);
		return new Socket(this.staticIp, this.staticPort);
	}

	@Override
	public ServerSocket createServerSocket(int port) throws IOException {
		logger.info("create server socket " + this.staticIp + ":" + this.staticPort);
		return new ServerSocket(this.staticPort, 0, InetAddress.getByName(this.staticIp));
	}

}