# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Phase 2 - Web Server Design

You can find a sequence diagram for the web server's design [here](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJgwxfDI5jAAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4AREOUaMAAtiiTDZMwkwA0a7jqAO7QHMurG2so88BICIdrAL5FJcL1lTVsnNyNMFMzUHOLV8eT21UeygBxWa02k1O50uYMmtxKrHYXEoT1qIhUTSgmWyYEoAAoMlkcpQMgBHVI5ACUd2KD1EqNk8iUKnUTXsKDAAFVhnivj8UNSEYzFMo1KpUcYmgAxJCcGDcyjCmA6SwwPkLMQ05VY4AAa3lwxgOyQYHiauG-JgwAQOo4qpQAA8cRoEULtMyxZVqmiqKImgqoMKtQi6SovTUHpQmgobShgHaunr0ABRB0qbAEArB+51FEVb2IVAYJqBJzBMZTRbqYDs5ZrZNQbzva221XyXXoOFa9AcUxupmi9Thn1+mBoHwIBBa0OGb3Cj2skA63EB3nDYWCkrzwfi72SmAKDgcA2K7TTur0ufuncNJdx3EKHymvHAJ-xDda7cs3c1feH4+PqaQYhheYb5s8SJvE0hI4iSagTlgCIvMisDgT6UYfNMFoaqsTRHBCr6ml0EAdmguFrDc57UHmBZlMWMDhE4TgVlhsw4TAeHgmshHxMRpHkUctw9qYXi+AE0DsOyMRSnAybSHACgwAAMhAWSFAihblN6kbvG0nS9AMBjqPkaAVuqiybICwIcPCObUahEGvBhnzYb8sIQlZ+x-LZxTIW8qIzk0CCqbKeIqWpZIUmAm60qBs41F+Ypshyq7mQKn7Xt+Eo6NKsrHgGSoquabGLBlA5ZWhgUnoGZ5ajoOr6gGlpIjAnkgkYBQAOTMGcYAgPE2axeis7abm7ycnMb7QEgABeKAcKm6aZuprpDY8aGafRpYAIwsVWqg1kseENk2TQAHIQMwPHTXNBw+TAwlUb6YEJZlSV3nIKCAfEL5vh+-YihVv45QeR4Hn9Z4gcNw5+Rh4WyhkqgIVqsOoaN9lNC5JVLBxFH-DxfGdrjglPTRNSbWATSMcxEysd87Gcfjb6E2RxOUY9ol+P4XgoOgMRxIkPN8+FviISUFMBWNTTNNIyZKcmXTJn0-RGaoJljATJHoKTDmIk57xTJr-Fs12SGQXmEZxUFqmi2FNtPpFajRU9l6veVSVGCg3APm+v1AdoMUyG9Q57iD0hexyhg8cqqpG9rUPPSNjkoU0FipCj5to5bGOYUjO51mnKD3Tpw4U1TTEsXn34F1AqRCZwIneFzWLHv42CyvqSk4jAADiGouuLdHMOj9TSz3CvK-YGoa8zWsrWtFt6ynmFx6zjP3ajkvDUFOJ91WYW7xqjtUi7L1B+7rIwOyYDfX774B2VgOeqHuVyt9hWx7PpF9iU9VxvqABJNAVBrRIGPKvQa58n4hxHqOcck5IEzmHDpf0k1TQ3XmotFAGYTKILGqXIeJYnC7VpvtQ6dZJgnWgOdS6VoppQFmvNU2JRHoJ1dlAhcqhkpgD3moPEgdEoh2Bq-Y8vCB7FEEbuWBGJe4amAnZROMNM5NC7jkMRiNkZm31sgqWHw1hTyrHWZoUwDEoAAdIOsTgIQ7HiCaFATUcKMwhMkUAuoHFuTxhCUxZ0NTrxgD0Yu+CNqEIYhXCY+j+5GJMRqcxljrG2JXK5I6ni1guJAG4pJFCuKTG8b4vG1x-H117J4JuAQOAAHY3BOBQE4GIyZghwFkgANngMuQwvCYBagliPDCeluiT2nmg3ic8Ky5MWIE+yqJUaY0mKvAS2SxnJJJlolCW9E5NA+riXheI4BtN4cfZ2bCz6SO4bfHi-0tzBx-EYEGMo34Q3kDHOhRE54-2KH-PUMAgEgOQOAr+8dVocJ3Do7eY4JxTkBSOMCPTxpDIwQtNM2Dlp4MmcEoslMYA7T2mKchx1GzUJgBdK69DGF3W7A3U+8UgXfg2XsjUeJFkCKudlJouz7yGFMY-ThIL1myMWPItal5vTTNaey-Z8EEBgAZTE6QgdN6VV0eMCJixYlNFqRM9atF0XlxpkqnJMrLEwCKaYTqMg1BLmIMtAAkAANUoEgAAZqgcUpo5AwFNIYGwKAiBOvmlfDUD0nQ5HFC1eMHAXXxEMCAa0-l9DusjTALE5InZ+pABAYKsBIDxo5RqFwmBTWcjSEpVqch4hWv6Jy01LAuhdFaDAAAshyeIEAOBWtaJyLo+bWBxg4JQVQVrroMJmnkAoTQAA8q8qhdoAEItssFavV2AEDAEsJQOA6boCHAAOosAAYrfo06lIKDgAAaX+KY1VMBtrhGCIEI1XayA+GBViGwBQDBWuaNTPo4xbimqlNCHwWJE1qGwG+lAH7SxOG-WsMhtY8WnWVPGYDyacirF-TAf9FxAOGBfaBtA77mg7Wg5MWDSyqHvB8HCodTD71-oA0B3DYGINlmI6Rih5Gmgth7W2JMrN0OYZSAxkDTHmgAFYmKsZxXB+s+L3h4l7aoC1OCCgwAgA6h6snKRocbmJfwq7JwQB2PzJACQwD6eCkZgAUhAWUfLDD+DSbqLpQ81m9PbQZCtgzV4ViXSutdG6oCbAvdIDVi8RWG3+WvFJAJl2rqgOujNWTz0GrVRvTOazRwACsbNoG2dZ2U4qUOHIUewk5V8ORnIecAJlF9rn7juQBKrTyIF1Qal84BoC-kvO-pCyRPK4HgpRYohVOcJqDtJVg5T89XQlzReYIhJDKxSbI7JmhxL0HUbJQiVhJXjlXO4ds4LNXoF1duXlOzTzgtvO1P-C7HAIBqDQN1QNOJIF9ZG7y+BELAVII+xhad8YDmTeWmMPEAD40dwh+KVQzaJzHh0FHTFTg706CfNmmAXrohJp8EgF9UPA4JzJqUbVSPdUkeW+x1biHjzY6dsw4oO2JHMo+6OXhQZWu3YS9AK0sZ4w8dIpCobQrs6jwPLzhMvHge4MhbNrV82kfllIRT+DBKuN8-dbx+nD0KVHKpWV7LBX6Wr0srF-zGagsyuO9yl+oNjy+bi+6iA6PTFWnFGmjNlKlH61TrXMQKz-J-YNpMKuYoa51x1gQkn1NK5Purj78PHNSncygCu4zpmvCp8QHGWAwBsBLsICOtAnSNIuZhdLWW8tFbK2MBH4VyiPhpe0dIlAGzuB4AUHn5A0blr8M91eWrrfs+3yt8Cm3We8A84QM84ZpFxFQr1-tmAIA29QDEb3gG1vhGlGz5P-1VY+9uxO4PvAAZVDr8ubVll2+J8xuKlAOfv26-e-db7jOTey+51j6H+PRda9y-otHrTCHuoGHr-o9EAA).