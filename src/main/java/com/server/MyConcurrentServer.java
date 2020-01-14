package com.server;

import com.server.exceptions.CommandLineArgumentException;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyConcurrentServer {

    private static final Integer ARGUMENT_SIZE = 2;
    private static final String ARGUMENT_PORT = "-port";
    private static final Integer MAX_THREADS = 100;
    private static final Integer MIN_THREADS = 10;
    private static final Integer IDLE_TIMEOUT = 120;
    private static final Logger LOGGER = Logger.getLogger(MyConcurrentServer.class.getName());
    private static Server server;

    public static void main(String[] args) throws Exception {

        if (argumentsAreWrong(args)){
            throw new CommandLineArgumentException("CommandLineArgument ERROR. Please follow the command: 'java -jar httpserver.jar -port n' (where n is the listen port)");
        }
        int port = Integer.parseInt(args[1]);

        QueuedThreadPool threadPool = new QueuedThreadPool(MAX_THREADS, MIN_THREADS, IDLE_TIMEOUT);

        server = new Server(threadPool);

        try (ServerConnector connector = new ServerConnector(server)){
            connector.setPort(port);
            server.setConnectors(new Connector[] { connector });
        }

        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        servletHandler.addServletWithMapping(HandleRequest.class, "/");
        server.start();
        LOGGER.log(Level.INFO, "Server started. Current port: {0}.", String.valueOf(port));
    }

    public static void stopServer() throws Exception {
        server.stop();
    }

    public static boolean argumentsAreWrong(String[] args) {
        return args.length != ARGUMENT_SIZE || !args[0].equals(ARGUMENT_PORT);
    }
}
