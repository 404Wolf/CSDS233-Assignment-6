# CSDS233: Assignment 6
## Wolf Mermelstein

## Implementation Notes
* Vertex and Edge have been changed to be a public subclass that has a private constructor and final fields. This is because it is bad practice to expose instances of private subclasses. These changes, however, still make it so that all instances of Vertex and Edge must go through AirportSystem.
* The `connections` field has been changed to be a method named `getConnections()`, and the field is now a `HashMap`. This allows for O(1) lookups given the ID of a vertex.
* I added an optional root argument for breadth first so that you can choose where to begin the iteration at.
* I've decided to make the source and destination of edges vertex references instead of strings to prevent extra lookups. It is functionally identical.