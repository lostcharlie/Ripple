package ripple.test;

import ripple.client.RippleClient;
import ripple.server.RippleServer;
import ripple.server.core.NodeMetadata;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhen Tang
 */
public class TestSubscribe {
    private static final int SERVER_COUNT = 10;
    private static final int CLIENTS_PER_SERVER = 3;
    private static final String DATABASE_PATH = "D:\\ripple-test-dir";

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(DATABASE_PATH));

            List<RippleServer> serverList = new ArrayList<>();
            List<RippleClient> clientList = new ArrayList<>();
            List<NodeMetadata> nodeList = new ArrayList<>();

            int branch = 2;
            int i = 0;
            for (i = 0; i < SERVER_COUNT; i++) {
                int serverId = i + 1;
                String storageLocation = DATABASE_PATH + "\\server-" + serverId + ".db";
                RippleServer rippleServer = RippleServer.treeProtocol(serverId, storageLocation, branch);
                rippleServer.start();
                serverList.add(rippleServer);
                System.out.println("Node " + rippleServer.getId() + ": " + rippleServer.getAddress() + ":" + rippleServer.getPort());
                nodeList.add(new NodeMetadata(serverList.get(i).getId(), serverList.get(i).getAddress(), serverList.get(i).getPort()));
            }
            for (i = 0; i < SERVER_COUNT; i++) {
                serverList.get(i).initCluster(nodeList);
            }

            String applicationName = "testApp";
            String key = "test";
            String value = "test";

            int j = 0;
            for (i = 0; i < SERVER_COUNT; i++) {
                for (j = 0; j < CLIENTS_PER_SERVER; j++) {
                    RippleServer rippleServer = serverList.get(i);
                    String serverAddress = rippleServer.getAddress();
                    int serverPort = rippleServer.getPort();
                    String storageLocation = DATABASE_PATH + "\\server-" + rippleServer.getId() + "-client-" + (j + 1) + ".db";
                    RippleClient rippleClient = new RippleClient(serverAddress, serverPort, storageLocation);
                    rippleClient.start();
                    clientList.add(rippleClient);
                    System.out.println("Client " + (j + 1) + " for Server " + rippleServer.getId() + ":"
                            + rippleClient.getAddress() + ":" + rippleClient.getPort());
                }
            }

            for (RippleClient rippleClient : clientList) {
                rippleClient.subscribe(applicationName, key);
            }

            System.out.println("Press any key to stop.");
            System.in.read();

            for (RippleClient rippleClient : clientList) {
                rippleClient.stop();
            }

            for (RippleServer rippleServer : serverList) {
                rippleServer.stop();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
