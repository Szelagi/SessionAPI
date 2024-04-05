# SessionAPI

The library facilitates game containerization on a Minecraft server, enabling the creation of isolated environments
within the game with separate logic and state.

<h3>Structure</h3>
We define a container through three main types of components: `Session`, `Board`, and `Controller`.

* **Session:** It forms the foundation of the container, allowing the creation and management of container instances. It
  is responsible for adding new players to the container and removing existing ones.

* **Board:** It manages the game map within the container. All game elements and players reside on the Board, and
  interactions between them take place there. The Board serves as the space where the gameplay unfolds.

* **Controller:** It is a crucial component responsible for implementing the gameplay logic within the container. It
  contains listeners and threads that control the course of the game. The Controller reacts to player actions and
  manages various processes within the container.

Through these three types of components, our library enables the creation of flexible and complex container structures,
providing full control over the gameplay flow and player interaction.

<h3>Encapsulation</h3>
Containers are created with encapsulation in mind, meaning they possess their unique state as well as individual player
states. Additionally, they register running listeners and threads. The library ensures comprehensive management of
container lifecycles. Upon destruction of a container instance, the system automatically terminates and removes all
associated resources, such as running threads, listeners, controllers, generated maps, thus ensuring optimal resource
utilization and preventing memory leaks and abandoned threads.

<h3>Hierarchy</h3>
In the architecture of the library, each component *(Session, Board, Controller)* is associated with an instance of the
Process object.

`Process` object contains references to its parent component and a list of subordinate components created by it. This
establishes a hierarchy of components, forming a tree structure where the root is the Session instance.

Upon termination of a specific component, the tree structure is analyzed, and then all subordinate components assigned
to that resource are deactivated and removed.

<h3>Scalability</h3>
The library ensures high scalability by allowing for the simultaneous launch of multiple instances of the same type of
container. This mechanism relies on generating a separate map for each instance and maintaining a separate, isolated
game state. With this solution, each container instance operates independently of the others, minimizing mutual
interference.

## Installation

1. Download the latest version of the plugin from releases.
2. Include the downloaded plugin as a library in your Java project.
3. Set `depend: [SessionAPI]` in the `plugin.yml` file.
4. Copy the downloaded plugin to the server's plugin folder `/plugins`.

## Tutorial

* [TUTORIAL](TUTORIAL.md)