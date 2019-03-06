# Galaxies
A mini-project for CS61B. Focuses on usage of various data structures, understanding skeleton code and how the program functions work together. 
## Table of Contents
- A. Introduction
- B. The Puzzle
- C. Program Design
- D. Instrumentation and Testing
- E. Discussion

## Introduction
This initial programming assignment is intended as an extended finger exercise: a mini-project rather than a full-scale programming project. The intent is to give you a chance to get familiar with Java and the various tools used in the course.

We will be grading solely on whether you manage to get your program to work (according to our tests) and to hand in the assigned pieces. There is a slight stylistic component: the submission and grading machinery require that your program pass a mechanized style check (style61b), which mainly checks for formatting and the presence of comments in the proper places. See the style61b guide for a description of the style it enforces and how to run it yourself.

## The Puzzle
The puzzle game Galaxies is one of Simon Tatham's collection of GUI games (available on a variety of operating-system configurations, including Ubuntu.) He attributes the game to the puzzle-game publisher Nikoli, which published it as Tentai Show (天体ショー tentai shō). In this mini-project, you are given an incomplete Java program that creates these puzzles and allows its user to solve them, and you must supply the missing parts to complete the implementation.

The puzzle itself is quite simple. It is played on a rectangular grid of square cells. A number of *"galactic centers"* (also known as simply *"centers"*), displayed as small circles, are placed about the board. Each galactic center is either in the center of a cell, at the intersection of two grid lines and surrounded by four cells, or in the center of an edge between two cells. No centers appear on the outside edges of the puzzle board. The user who is solving the puzzle must place barriers along the edges of the board in order to divide the cells of the board into contiguous *"galaxies"* (also known as simply *"regions"*), where each galaxy must enclose exactly one galactic center. To successfully solve this puzzle, the user must make galaxies that cover all the cells without overlapping that also satisfy the following criteria:

1. It must be possible to reach any cell in the galaxy from any other cell via a sequence of cells in the same galaxy that are only horizontally or vertically adjacent (not diagonally adjacent).
2. The galactic center must be entirely enclosed in the galaxy.
3. The galaxy must be symmetric around the galactic center: if the galactic center is at coordinates (x,y) and there is a cell in the galaxy at coordinates (x+dx,y+dy), then the cell at coordinates (x−dx,y−dy) must also be in the galaxy.

To demarcate the galaxies, the puzzle solver puts **boundaries** along the edges between cells. Clicking on an edge between two cells toggles a boundary on that edge. The program detects when a boundary completely encloses a properly symmetric galaxy and when the puzzle is completely solved. There must not be stray boundary edges in the middle of a galaxy that are not part of its outer boundary. The perimeter of the game board is a permanent boundary enclosing all the cells as well.

Throughout the project, you will see mention of "marks". These are integer values that will be assigned to cells. If cells are marked with the same value, then they are part of the same set of cells. These sets of cells are used in a few ways, one of which is to denote which cells to display white in the ```GUI```.

## Program Design
The skeleton exhibits a design pattern in common use: the Model-View-Controller Pattern (MVC).

The MVC pattern divides our problem into three parts:

- The *model* represents the subject matter being represented and acted upon—in this case incorporating the state of a board game and the rules by which it may be modified. Our model resides in the Model and Place classes.
- A *view* of the model, which displays the game state to the user. Our view resides in the ```GUI``` and ```BoardWidget ```classes.
- A *controller* for the game, which translates user actions into operations on the model. In our case, it also notifies the view when it may need to check with the model and make the necessary updates. Our controller resides mainly in the Controller class, although it also uses the GUI class to read mouse clicks.
Your job for this project is just to modify and complete the Model class. Don't let that stop you from looking at all the other code in the project. That's actually part of the point of giving you the skeleton. You can learn a great deal about programming by reading other people's programs.

## Discussion
This project is largely an exercise in reading someone else's program and understanding its intent, which is actually a common activity in "real-world" programming as well. While you can, in fact, scrap everything and start from scratch (you are free to change files other than ```Model.java```), you certainly should not do so simply because you don't understand the skeleton provided.

You'll see numerous uses of parts of the Java library we haven't talked about. However, you have seen all these data structures in Python. Where Python has lists, Java has arrays, ```ArrayLists, ArrayDeques,``` and ```LinkedLists``` (among others). Where Python has dictionaries, Java has HashMaps and TreeMaps. Where Python has sets, Java has HashSets and TreeSets. Where Python uses "duck typing"—as when it calls something a sequence because we can apply generic operations like ```len``` and ```map``` to it and iterate over it—Java explicitly identifies supertypes of its various library types, such as ```List, Set,``` and``` Collection```. These and many more library classes are all defined in the online Java library documentation, which should constitute much of your bedtime reading for this semester.

So your first step is to understand what Model is supposed to do and what the comments on each of its methods are supposed to mean. You might ask, "but how can I understand it unless I know how it's being used?" It certainly helps to understand its use, but you should start developing the ability to understand and work on modules in isolation, one aspect of the "separation of concerns" that makes the construction of large systems possible.

Now you have to pick a representation (instance variables, basically) that you think will make it possible, in principle, to implement all the methods. The question here is, "what information does my program need to do X?". At this point, you should be in a position to implement the simple stuff, such as ```xlim, ylim, isCenter, isBoundary, centers, placeCenter, toggleBoundary, mark,``` and ```markAll```.

This leaves the more complex methods, such as unmarkedSymAdjacent, which tells how a galaxy might grow. The method maxUnmarkedGalaxy requires that you add cells to a region so as to keep it a valid galaxy until the region is as large as possible. This is a kind of fixpoint process, in which you repeat an operation until no such repetitions are possible. In this case, we add to the region two as-yet unmarked cells that are symmetric about a center and adjacent to other cells already in the region, mark these cells now that they have been added, and then repeat, expanding to more unmarked cells until no more additions are possible. Feel free to discuss with other students how this might be organized (as usual, share ideas, not code).

This project tries to make good use of abstraction; the simpler functions you write will be instrumental in helping you accomplish more complex goals. Be sure to respect the abstraction barriers to minimize the impact of bugs or logical errors in your code. Further, if you find yourself bogged down by the details of a particular method, consider pausing to break it down into "helper" functions, each performing some coherent piece of the original function (only, as a stylistic matter, do give these new functions descriptive names, generally avoiding the word "helper" in the name.)
