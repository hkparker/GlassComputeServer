package com.aakask.glasscomputeserver.hkparker;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerRunner {
    public static void run(Class serverClass) {
        try {
            executeInstance((NanoHTTPD) serverClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executeInstance(NanoHTTPD server) {
        try {
            server.start();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
            System.exit(-1);
        }

        System.out.println("Server started...\n");

        try {
			(new LinkedBlockingQueue()).take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
