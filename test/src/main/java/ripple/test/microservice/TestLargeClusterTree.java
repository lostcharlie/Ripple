package ripple.test.microservice;

import ripple.client.RippleClient;
import ripple.common.entity.Message;
import ripple.server.RippleServer;
import ripple.server.core.NodeMetadata;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Zhen Tang
 */
public class TestLargeClusterTree {
    private static final int SERVER_COUNT = 100;
    private static final int ROUND = 10;
    private static final String DATABASE_PATH = "D:\\ripple-test-dir";

    public static void main(String[] args) {
        try {
            System.setProperty("ripple.debug", "true");
            System.setProperty("ripple.networkLatency", "50");
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn");
            List<Long> subscribeTime = new ArrayList<>();
            List<Long> unsubscribeTime = new ArrayList<>();
            List<Long> deliveryTime = new ArrayList<>();

            int currentRound = 0;
            for (currentRound = 0; currentRound < ROUND; currentRound++) {
                System.out.println("[" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) + "] "
                        + "Round " + (currentRound + 1) + " / " + ROUND);
                String suffix = UUID.randomUUID().toString();
                String databasePath = DATABASE_PATH + "\\" + suffix;

                Files.createDirectories(Paths.get(databasePath));

                List<RippleServer> serverList = new ArrayList<>();
                List<OperatorService> operatorServiceList = new ArrayList<>();
                List<RippleClient> clientList = new ArrayList<>();
                List<NodeMetadata> nodeList = new ArrayList<>();

                System.out.println("[" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) + "] "
                        + "Calling System.gc().");
                System.gc();

                int branch = 4;
                int i = 0;
                for (i = 0; i < SERVER_COUNT; i++) {
                    int serverId = i + 1;
                    String storageLocation = databasePath + "\\server-" + serverId + ".db";
                    RippleServer rippleServer = RippleServer.treeProtocol(serverId, storageLocation, branch);
                    rippleServer.start();
                    serverList.add(rippleServer);
                    System.out.println("[" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) + "] "
                            + "Node " + rippleServer.getId() + ": " + rippleServer.getAddress() + ":" + rippleServer.getPort());
                    nodeList.add(new NodeMetadata(serverList.get(i).getId(), serverList.get(i).getAddress(), serverList.get(i).getPort()));
                }
                for (i = 0; i < SERVER_COUNT; i++) {
                    serverList.get(i).initCluster(nodeList);
                }

                for (i = 0; i < SERVER_COUNT; i++) {
                    RippleServer rippleServer = serverList.get(i);
                    String serverAddress = rippleServer.getAddress();
                    int serverPort = rippleServer.getPort();

                    OperatorService operator = new OperatorService(serverAddress, serverPort
                            , databasePath + "\\server-" + rippleServer.getId() + "-operator-service.db");
                    operator.start();
                    operatorServiceList.add(operator);
                    clientList.add(operator.getClient());
                    System.out.println("[" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) + "] "
                            + "[Operator Service] " + operator.getAddress() + ":" + operator.getPort()
                            + ", Client = " + operator.getClient().getAddress() + ":" + operator.getClient().getPort());
                }

                String applicationName = "testApp";
                String key = "function";
                String value = "add";

                Date subscribeStartDate = new Date(System.currentTimeMillis());
                for (RippleClient rippleClient : clientList) {
                    rippleClient.subscribe(applicationName, key);
                }
                Date subscribeEndDate = new Date(System.currentTimeMillis());
                subscribeTime.add(subscribeEndDate.getTime() - subscribeStartDate.getTime());
                System.out.println("[" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) + "] "
                        + "Subscribe completed in " + (subscribeEndDate.getTime() - subscribeStartDate.getTime()) + " ms. (" + clientList.size() + " clients)");

                Date startDate = new Date(System.currentTimeMillis());
                System.out.println("[" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) + "] "
                        + "Start update delivery");
                serverList.get(0).getNode().put(applicationName, key, value);
                Message message = serverList.get(0).getNode().getStorage().getMessageService()
                        .findMessages(applicationName, key).get(0);
                long count = serverList.get(0).getNode().getStorage().getAckService()
                        .getAck(message.getUuid()).getAckNodes().size();
                while (count != SERVER_COUNT) {
                    System.out.println("[" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) + "] "
                            + "Update sent to " + count + " server nodes. Wait for 100 ms.");
                    Thread.sleep(100);
                    count = serverList.get(0).getNode().getStorage().getAckService()
                            .getAck(message.getUuid()).getAckNodes().size();
                }
                Date endDate = new Date(System.currentTimeMillis());
                deliveryTime.add(endDate.getTime() - startDate.getTime());
                System.out.println("[" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) + "] "
                        + "Delivery completed in " + (endDate.getTime() - startDate.getTime()) + " ms.");

                Date unsubscribeStartDate = new Date(System.currentTimeMillis());
                for (RippleClient rippleClient : clientList) {
                    rippleClient.unsubscribe(applicationName, key);
                }
                Date unsubscribeEndDate = new Date(System.currentTimeMillis());
                unsubscribeTime.add(unsubscribeEndDate.getTime() - unsubscribeStartDate.getTime());
                System.out.println("[" + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())) + "] "
                        + "Unsubscribe completed in " + (unsubscribeEndDate.getTime() - unsubscribeStartDate.getTime()) + " ms. (" + clientList.size() + " clients)");

                for (OperatorService operatorService : operatorServiceList) {
                    operatorService.stop();
                }

                for (RippleServer rippleServer : serverList) {
                    rippleServer.stop();
                }
            }

            System.out.println("Summary:");
            System.out.println("Subscribe time for " + SERVER_COUNT + " nodes: ");
            int i;
            for (i = 0; i < ROUND; i++) {
                System.out.println("Round " + (i + 1) + ": " + subscribeTime.get(i) + " ms");
            }
            System.out.println("Unsubscribe time for " + SERVER_COUNT + " nodes: ");
            for (i = 0; i < ROUND; i++) {
                System.out.println("Round " + (i + 1) + ": " + unsubscribeTime.get(i) + " ms");
            }
            System.out.println("Delivery time: ");
            for (i = 0; i < ROUND; i++) {
                System.out.println("Round " + (i + 1) + ": " + deliveryTime.get(i) + " ms");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
