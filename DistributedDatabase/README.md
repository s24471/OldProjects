# 🗄️ Distributed Database System
A lightweight distributed key-value database implemented in Java as a project for my uni classes.

# How to Use:
### 1. Start database nodes

```sh
java DatabaseNode -tcpport <port> -record <key>:<value> -connect <node_ip>:<node_port>
```
- -tcpport \<port> → Port on which the node will run
- -record \<key>:\<value> → Initial key-value record for this node
- -connect \<node_ip>:\<node_port> → Connects this node to another database node


### 2. Start the database client
```sh
java DatabaseClient -gateway <address>:<port> -operation <command>
```
- -gateway \<address>:\<port> → The address of the database node acting as a gateway
- -operation \<command> → The operation to perform (e.g., set-value, get-value, find-key, get-max, get-min)

### 3. Example Commands:

```sh
java DatabaseClient -gateway 127.0.0.1:5000 -operation "set-value 1:42"
java DatabaseClient -gateway 127.0.0.1:5000 -operation "get-value 1"
java DatabaseClient -gateway 127.0.0.1:5000 -operation "find-key 1"
```
# Features:
- Distributed Architecture – Nodes communicate with each other to store and retrieve values.
- Key-Value Storage – Each node stores a key-value pair and can distribute queries across the network.
- Client-Server Model – Clients can interact with the database through a gateway node.
- Fault Tolerance – Handles multiple nodes and communication failures gracefully.
- Parallel Execution – Uses ExecutorService for handling multiple connections efficiently.
