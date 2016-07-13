package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Client {

	private Socket connection = null;
	private String request = "", answer = "";
	private boolean busy;
	private final String prefix = "client - ";

	public Client(String request, String hostName, int port) {
		// TODO Auto-generated constructor stub
		this.request = request;
		connection = new Socket();
		InetSocketAddress address = new InetSocketAddress(hostName, port);
		busy = true;
		try {
			connection.connect(address, 7000);
		} catch (SocketTimeoutException ex) {
			System.err.println(prefix + ex);
		} catch (IOException ex) {
			System.err.println(prefix + ex);
		}
	}

    public Client(String akbar, double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	public String getAnswer() {

		if (connection == null || !connection.isConnected()) {
			System.out.println(prefix + "Connection to server failed!");
			return "failed";
		}
		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			outputStream = connection.getOutputStream();
			inputStream = connection.getInputStream();
		} catch (IOException ex) {
			System.err.println(prefix + ex);
		}

		transferData(inputStream, outputStream);

		try {
			inputStream.close();
			outputStream.close();
			connection.close();
		} catch (IOException ex) {
			System.err.println(prefix + ex);
		}

		return answer;

	}

	private void transferData(InputStream inputStream, OutputStream outputStream) {

		int l = 0 ;
		byte[] data = new byte[1024];
		try {
			System.out.println(prefix + "Sending request to server ...");
			outputStream.write(request.getBytes());
			outputStream.flush();
			System.out.println(prefix + "Done...");
		} catch (IOException ex) {
			System.err.println(prefix + ex);
		}

		try {
			int n =0;
			while(inputStream.available()==0 && n <= 20){
				Thread.sleep(300);
				n++;
			}
			System.out.println(prefix + "Receiving answer from server ...");
			l = inputStream.available();
			inputStream.read(data);
			System.out.println(prefix + "Done...");
		} catch (IOException ex) {
			System.err.println(prefix + ex);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		answer = new String(data);
		answer = answer.substring(0, l);
		System.out.println(prefix + "answer compelete");
		busy = false;

	}

	/**
	 * @return the request
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(String request) {
		this.request = request;
	}

	/**
	 * @return the busy
	 */
	public boolean isBusy() {
		return busy;
	}

	/**
	 * @param busy the busy to set
	 */
	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
}
