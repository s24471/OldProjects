# 🔲Cellular Automaton Simulator
This project is a customizable cellular automaton simulator, allowing users to define rules for cell evolution, interact with the grid, and observe pattern evolution over time.

![alt text](<GameOfLife.gif>)
# Features
- Custom Rule Input – Users define survival and birth conditions in a /-separated format.
- Interactive Grid – Click on cells to toggle their state before running the simulation.
- Dynamic Board Size – Choose any grid dimensions.
- Adjustable Speed – Modify simulation speed via UI controls.
- Pause/Resume & Restart – Control simulation execution.

# How It Works
The user enters rules in the format B/S, where:

- B = number of neighbors required for a dead cell to become alive.
- S = number of neighbors for a live cell to survive.

Example: 23/3 (Conway's Game of Life).

The user specifies grid width and height.

The board initializes, allowing manual activation of cells before simulation starts.

The simulation progresses based on defined rules, updating cells in each generation.
