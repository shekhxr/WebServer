# WebServer

This repository contains various examples demonstrating client-server communication using Java sockets. The project is structured into different modules, showcasing single-threaded, multi-threaded (manual threading), and multi-threaded (ExecutorService) approaches to handling network connections.

## Project Structure

Based on the repository's structure shown in the screenshot, the projects are organized as follows:

* `MultiThreadedWebServer/`: Likely contains the multi-threaded server and client implementations using manual threading or ExecutorService.
* `SingleThreadedWebServer/`: Contains the single-threaded server and client implementations.
* `ThreadPoolWebServer/`: Specifically dedicated to the multi-threaded server and client implementations utilizing Java's `ExecutorService`.
* `README.md`: This file.

## Project 1: Single-Threaded Client-Server

This is a fundamental implementation where the server handles one client connection at a time.

### Files

* `SingleThreadedWebServer/Server.java`: The server-side logic for the single-threaded example.
* `SingleThreadedWebServer/Client.java`: The client-side logic for the single-threaded example.

### Features

* **Server (`Server.java`)**:
    * Listens for incoming client connections on port `8010`.
    * Sets a socket timeout of 20 seconds.
    * Prints "Server is listening on port: 8010" to the console.
    * Accepts a single client connection per loop iteration.
    * Prints the remote socket address of the connected client.
    * Sends a "Hello World from the server" message to the connected client.
* **Client (`Client.java`)**:
    * Connects to a server running on `localhost`.
    * Attempts to connect to port `8090`.
    * Sends a "Hello World from socket" message, including its local socket address, to the server.
    * Receives a response from the server.
    * Closes its `PrintWriter`, `BufferedReader`, and `Socket` after communication.

### How to Run (Single-Threaded)

#### Prerequisites

* Java Development Kit (JDK) installed on your system.

#### Running the Server

1.  Navigate to the `SingleThreadedWebServer/` directory.
2.  Compile the `Server.java` file:
    ```bash
    javac Server.java
    ```
3.  Run the compiled server class:
    ```bash
    java Server
    ```
    The server will indicate it's listening on port `8010`.

#### Running the Client

1.  Navigate to the `SingleThreadedWebServer/` directory.
2.  Compile the `Client.java` file:
    ```bash
    javac Client.java
    ```
3.  Run the compiled client class:
    ```bash
    java Client
    ```
    **Note on Port Mismatch:** The server listens on `8010`, while this client attempts to connect to `8090`. For successful communication, you will need to adjust the `port` variable in `SingleThreadedWebServer/Client.java` to `8010`.

## Project 2: Multi-threaded Server with Multi-Client Spawner (Manual Threading)

This example features a server that handles multiple concurrent client connections by spawning a new `Thread` for each client, and a client program designed to launch multiple client threads.

### Files

* `MultiThreadedWebServer/Server.java` (or similar, if specific to this version): The multi-threaded server using manual thread creation.
* `MultiThreadedWebServer/Client.java` (or similar, if specific to this version): The client that spawns multiple threads.

### Features

* **Server (`Server.java`)**:
    * Listens for client connections on port `8010`.
    * Sets a socket timeout of 70 seconds.
    * Continuously accepts client connections.
    * For each accepted client, it creates and starts a new `Thread` to handle communication using a `Consumer<Socket>`.
    * Sends a "Hello from server" message including the client's `InetAddress`.
* **Client (`Client.java`)**:
    * Connects to a server on `localhost` on port `8010`.
    * Sends a "Hello from Client" message along with its local socket address.
    * Receives a response from the server and prints it.
    * The `main` method spawns 100 client threads, each executing the client's connection logic.

### How to Run (Multi-threaded - Manual Threading)

#### Prerequisites

* Java Development Kit (JDK) installed on your system.

#### Running the Server

1.  Navigate to the `MultiThreadedWebServer/` directory (or wherever this `Server.java` is located).
2.  Compile the `Server.java` file:
    ```bash
    javac Server.java
    ```
3.  Run the compiled server class:
    ```bash
    java Server
    ```
    The server will indicate it's listening on port `8010`.

#### Running the Client

1.  Navigate to the `MultiThreadedWebServer/` directory (or wherever this `Client.java` is located).
2.  Compile the `Client.java` file:
    ```bash
    javac Client.java
    ```
3.  Run the compiled client class:
    ```bash
    java Client
    ```
    This will start 100 client threads, each attempting to connect to the server, send a message, and receive a response. Responses will be printed to the console.

## Project 3: Multi-threaded Client-Server (with ExecutorService)

This version leverages Java's `ExecutorService` for managing threads, offering a more robust and scalable approach to handling concurrent connections.

### Files

* `ThreadPoolWebServer/Server.java` (or similar): The multi-threaded server utilizing `ExecutorService`.
* `ThreadPoolWebServer/Client.java` (or similar): The multi-threaded client utilizing `ExecutorService`.

### Features

* **Server (`Server.java`)**:
    * Utilizes an `ExecutorService` with a fixed-size thread pool (default size 10) to manage client connections.
    * Listens for client connections on port `8010`.
    * Sets a socket timeout of 70 seconds.
    * Accepts client connections and submits each `Socket` to the thread pool for handling.
    * The `handleClient` method sends "Hello from server" along with the client's `InetAddress`.
    * Shuts down the thread pool when the server exits.
* **Client (`Client.java`)**:
    * Initializes its own `ExecutorService` with a configurable pool size (default 10).
    * Connects to a server on `localhost` on port `8010`.
    * Sends a "Hello from Client" message along with its local socket address.
    * Receives a response from the server and prints it.
    * The `main` method submits a specified number of client tasks (default 100) to its `ExecutorService`.
    * Initiates an orderly shutdown of its thread pool and waits for tasks to complete or times out.

### How to Run (Multi-threaded - ExecutorService)

#### Prerequisites

* Java
