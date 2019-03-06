# Amazons
## Table of Contents
- A. Introduction
- B. Game Basics
- C. Notation
- D. Commands
- E. Game Output
- F. Task

## Introduction
The game of *Amazons* is a simple but rather interesting board game, usually for two players. It was invented in 1988 by Walter Zamkauskas of Argentina, and originally called *El Juego de las Amazonas* (now a trademark of Ediciones de Mente). The board is a 10 by 10 chessboard. Each player gets four "amazons" (represented as chess queens), white for the player who moves first, and black for the opponent. 

## Game of Amazons Basics

Each move consists of two parts: 
- First, an amazon of the appropriate color makes a chess queen move—any non-zero number of squares in a straight line horizontally, vertically, or diagonally with the restriction that the piece may not move onto or through an occupied square. Pieces are not captured in this game; the board keeps getting fuller. 
- Next, the piece that moved "throws a spear" from her final position. Spears move exactly like pieces, and are subject to the same restrictions. The spear sticks permanently in the (previously empty) square in which it lands. That square is counted as being occupied for the rest of the game (so no piece or spear may move through it). 

A player who has no legal move loses. For example, after black moves a7-a6(a7) in Figure 2, he will have used up all his moves, and white (who still has plenty of room) will win after his next move. Draws are impossible.

## Notation
A square is denoted by a column letter followed by a row number (as in e4). Columns are enumerated from left to right with letters a through j. Rows are enumerated from the bottom to the top with numbers 1 through 10. An entire move then consists of the starting and ending position of the piece that is moved, followed, in parentheses, by the position to which the spear is thrown. Thus, d10-c9(h4) means "Move from d10 to c9 and then throw a spear to h4."

## Commands
When running from the command line, the program will accept the following commands, which may be preceded by whitespace.

- **new**: End any game in progress, clear the board to its initial position, and set the current player to white.
- A move, either in the format described in Notation or (for convenience) with blanks replacing punctuation, as in g1 c5 e7.
- **seed** *N*: If the AIs are using random numbers for move selection, this command seeds their random-number generator with the integer N. Given the same seed and the same opposing moves, an AI should always make the same moves. This feature makes games reproducible.
- **auto** *C*: Make the C player an automated player. Here, C is "black" or "white", case-insensitive.
- **manual** *C*: Make the C player a human player (entering moves as manual commands).
- **dump**: Print the current state of the board in exactly the following format:
```
===
   - - - - - - B - - -
   - - B - - - - - - -
   - - - - - - - - - -
   B - - W - - S - - B
   - - - - - - - - - -
   - - - - - - - - - -
   W - - - - - - S - W
   - - - - - - - - - -
   - - - - - - - - - -
   - - - - - - W - - -
===
```
Here, B denotes a black queen, ```W``` a white queen, and ```S``` a spear. You must not use the === lines for any other output).

**quit**: Exit the program.
Feel free any other commands you think might be nice.

## Output
When an AI plays, it should print out each move that it makes using exactly the format
```
 * a1-c3(c6)
 ```
(with asterisk shown). Do not print these lines out for a manual player's moves.

When one side wins, the program should print out one of
```
 * White wins.
 * Black wins.
 ```
(also with periods) as appropriate. Do not use the * character in any other output you produce.

You may prompt a manual player for input using the form
```
...>
```
where "..." may be any text. The grading scripts will discard any text from the beginning of a line up to a > character.

## Your Task
Your job is to write a program to play Amazons. 

The AI in your program should be capable of finding a win that is within 10 moves. The branching factor for Amazons is quite high at the beginning of a game, but rapidly declines thereafter. Therefore we suggest that you choose a maximum search depth for a move that depends on how full the board is. Experiment a bit to see what works. The autograder will allow 3 minutes for a fully automated game.
