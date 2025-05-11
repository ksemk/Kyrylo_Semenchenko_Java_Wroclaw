# Kyrylo\_Semenchenko\_Java\_Wroclaw

## Introduction

Hello Ocado team, and thank you for this challenging task, it was fun to work on:)

## Algorithm

At first, I tried using a **greedy algorithm** to find the best set of orders to pay with bonuses. However, I quickly realized that this approach might not yield the optimal discount due to the **knapsack problem** (I was familiar with it thanks to a university course).

As a result, I implemented the `BackPackProblemSolver` class using a **dynamic programming approach**. This class is then used by the other algorithmic classes.

![knapsack](https://github.com/user-attachments/assets/6f97075d-1b4a-4c07-ae78-7a5d71c2e473)

The main algorithm was divided into several steps:

* Paying the full order value with bonus points to gain the "PUNKTY" discount.
* Paying the full order value with a partner bank's method to gain their discount (only if it's better than the "PUNKTY" discount and supported by the order).
* Paying at least 10% of the order with bonus points to receive a 10% discount, and the rest with traditional methods.
* Paying remaining unpaid orders with a card if possible; otherwise, covering the rest using bonus points. Here tryed saving bonus points for fututre purchases, where greater possible discount might occure after paying 10+% or full price with bonuses

In all cases, if a better discount was found, the previously assigned payment amounts were "returned" to the corresponding payment method limits. If using bonus points yielded a better result, the algorithm would prefer it.

I added comments to key parts of the code, including formulas calculations, to make the flow easier to follow.

## Additional Information

* The program was written and compiled using **Java 21** with **Maven** as the build tool.
* Data about orders and payment methods is stored in private fields (using POJOs). While this may not be the fastest approach, it simplifies algorithm design and debugging.
* The initial version of the code was covered by basic **JUnit tests**. However, a math bug was discovered at the last minute, and while a fix was attempted, the core logic changed and the tests were not fully updated.
* For better readability and maintainability, the program can be extended using **Lombok**.
