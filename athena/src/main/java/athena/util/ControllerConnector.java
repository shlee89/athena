package athena.util;

import org.onosproject.athena.SerializerWrapper;
import org.onosproject.athena.database.AthenaFeatureEventListener;
import org.onosproject.athena.database.CommunicatorOption;
import org.onosproject.athena.database.QueryIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

/**
 * Created by seunghyeon on 4/3/16.
 */
public class ControllerConnector implements Runnable {

    AthenaFeatureEventListener listener = null;
    CommunicatorOption communicatorOption;
    private final Logger log = LoggerFactory.getLogger(getClass());

    //out connection
    private static final String OUT_IP = "127.0.0.1";
//    private static final String OUT_IP = "192.168.0.11";
    private static final String ANY = "0.0.0.0";
    private static final int OUT_PORT = 5001;

    ObjectOutputStream outStream = null;

    //in connection
    private static final int IN_PORT = 5000;

    //receive mode
    public ControllerConnector(AthenaFeatureEventListener listener) {
        this.listener = listener;
    }

    //sender mode
    public ControllerConnector() {
//        connectToController();
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(IN_PORT);
            serverSocket.setSoTimeout(10000);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Socket server = serverSocket.accept();
                ObjectInputStream OI = new ObjectInputStream(server.getInputStream());
                Object obj = OI.readObject();
                if (obj instanceof HashMap) {
                    handleExternalRequest((HashMap) obj);
                }
                ObjectOutputStream out =
                        new ObjectOutputStream(server.getOutputStream());
                HashMap<String, Object> map = new HashMap<>();
                map.put("Response", "ok");
                out.writeObject(map);
                server.close();
            } catch (SocketTimeoutException s) {
                log.debug("Socket timed out!");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleExternalRequest(HashMap<String, Object> event) {
        String val = event.get("queryIdentifier").toString();
        short shortVal = Short.parseShort(val);
        QueryIdentifier queryIdentifier = new QueryIdentifier(shortVal);
        if (listener != null) {
            listener.getFeatureEvent(null,
                    queryIdentifier,
                    event);
        }

    }

    public void addAthenaFeatureEventListener(AthenaFeatureEventListener listener) {
        this.listener = listener;
    }

    public void sendToController(SerializerWrapper serializerWrapper) {
        Socket s = null;
        try {
            s = new Socket(OUT_IP, OUT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(serializerWrapper);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
