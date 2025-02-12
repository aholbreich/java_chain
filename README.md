# Java Chain
Explorational and educational Hobby project.

## Projects Goal

* Explore Blockchain underlying mechanism, problems and ideas
* Having Working and Deployable software
* Create own useful primitives 

# Build & Run
## Gradle

#Build 'fat' jar
```bash

./gradlew clean shadowJar
```
You can start one or several process listening on different ports by providing optional argument

```bash
#Process one
java -jar build/libs/org.holbreich.java_chain-1.0.0.jar 5000

#Process two on another port
java -jar build/libs/org.holbreich.java_chain-1.0.0.jar 5001
```

## Docker

Build with `Docker` or `Podman`

```bash
docker build -t java-chain-app .
```
With containers the networking part becomes trickier. We need to put containe in one network first

```bash
docker network create blockchain-net
```
Then we can run freshly built containers with interfaces in that network.
Run using `-it` if you like to stay attached to input stream and append "Transactions"
```bash
# First interactive
docker run -it --name node1 --network blockchain-net -p 5000:5000 java-chain-app 5000
# Second node on different port.
docker run -it --name node2 --network blockchain-net -p 5001:5000 java-chain-app 5000
```

