---
description: >-
  Sender is a feature that controls where a player (or list of players) is being
  send to. You can initiate a send with the API directly and hook to Sender
  Events to control the final destination.
---

# Sender API

### How to send players?

```java
gliderAPI.getSenderAPI().send(cluster, player);
```

To actually send players you just need to specify at least two parameters

* `GCluster`, `GGroup` or `GServer`
* `Player`

You can choose which one and you can obtain them via the [Cluster System API](cluster-system-api.md).

#### Advanced Sender parameters

You can also specify more parameters to control the Sender object during its events, but its optional.

```java
// Sender also has several events that you can force to skip.
List<SkipAbleSenderEvent> skipEvents = List.of(SkipAbleSenderEvent.FIND_PLAYERS);

gliderAPI.getSenderAPI().send(cluster, player, skipEvents);

/* 
You can use the String to parse a parameter that might be useful if you want to
handle specifically Sender objects sent by you.
*/
String senderID = "My Plugin"
// You can also use count, this is primarly intended for queue systems.
int count = 0

gliderAPI.getSenderAPI().send(cluster, player, skipEvents, senderID, count);
```



## Events

These events occur if a Sender object is created. They are fired in a specific order.

### 1. Sender Find Group Event

This event is only called when a `GCluster` is specified. It determines which group the player is supposed to join.. If no one sets the proper `GGroup`, the default one from cluster config is selected.

```
// Some code
```

### 2. Sender Find Players Event

The `Sender` accepts only one `Player` as a parameter, but during this event, you can add additional players to be sent along with the original `Player`. Intended for party or friends systems.

```
// Some code
```

### 3. Sender Find Restricted Servers Event

In this event, you receive a list of all `GServers` that are potential candidates for the Player(s) to be sent to.. If the player should not be sent to the server, remove it.

```
// Some code
```

### 4. Sender Find Available Servers Event

This event focuses on filtering the `GServers` to find those that are currently online and capable of handling the player(s). You can add your own logic to this or rewrite the default one.

```
// Some code
```

### 5. Sender Find Best Server Event

You can assign a score to each `GServer`. The `GServer` with the highest score is then selected for the Player(s) to join.. Mostly intended for Party, Friends or Load balancing systems.

```
// Some code
```

### 6. Sender Proceed Event

You can determine what happens in each case—whether to retry or attempt sending the players to a different destination is up to you.

```
// Some code
```
