import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit; // For shutdown await

public class Client {

    private final ExecutorService clientThreadPool; // Declare a thread pool for clients

    public Client(int poolSize) {
        // Initialize the thread pool in the constructor
        this.clientThreadPool = Executors.newFixedThreadPool(poolSize);
    }

    // This method now returns a Runnable that performs a single client connection
    public Runnable createClientTask() {
        return () -> { // Using a lambda expression for brevity instead of new Runnable()
            int port = 8010;
            try {
                InetAddress address = InetAddress.getByName("localhost");
                // System.out.println("Client attempting to connect..."); 
                Socket socket = new Socket(address, port);

                try (
                    PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()))
                ) {
                    toSocket.println("Hello from Client " + socket.getLocalSocketAddress());
                    String line = fromSocket.readLine();
                    System.out.println("Response from Server: " + line + " for " + socket.getLocalSocketAddress());
                } catch (IOException e) {
                    System.err.println("Client Socket I/O error: " + e.getMessage());
                    // e.printStackTrace(); // Uncomment for full stack trace during debugging
                } finally {
                    try {
                        if (socket != null && !socket.isClosed()) {
                            socket.close();
                            // System.out.println("Client connection closed: " + socket.getLocalSocketAddress()); // Optional: for debugging
                        }
                    } catch (IOException e) {
                        System.err.println("Error closing client socket: " + e.getMessage());
                    }
                }
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Client connection error: " + e.getMessage());
            }
        };
    }

    public static void main(String[] args) {
        int numberOfClients = 100; // How many client tasks to submit
        int clientPoolSize = 10;   // How many threads the client's pool should use concurrently

        Client clientApp = new Client(clientPoolSize); // Initialize client with its own thread pool

        System.out.println("Submitting " + numberOfClients + " client tasks to the thread pool...");

        // Submit client tasks to the ExecutorService
        for (int i = 0; i < numberOfClients; i++) {
            clientApp.clientThreadPool.execute(clientApp.createClientTask());
        }

        System.out.println("All client tasks submitted. Shutting down client thread pool.");
        clientApp.clientThreadPool.shutdown(); // Initiate orderly shutdown

        try {
            // Wait for all tasks to complete, or timeout after a certain period
            if (!clientApp.clientThreadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Client thread pool did not terminate in the specified time.");
                clientApp.clientThreadPool.shutdownNow(); // Force shutdown if tasks are stuck
            } else {
                System.out.println("Client thread pool has shut down cleanly.");
            }
        } catch (InterruptedException e) {
            System.err.println("Client thread pool termination interrupted: " + e.getMessage());
            clientApp.clientThreadPool.shutdownNow();
            Thread.currentThread().interrupt(); // Restore interrupt status
        }
        System.out.println("Client application finished.");
    }
}
