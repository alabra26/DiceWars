Code for game DiceWars is in Java.

We want take game data and train a network to predict the likelihood of a nation winning given the starting layout.

The data is organized into 4 matricies as given:
	30x30 matrix that gives country borders; countries that border each other have a 1 in their shared
	5x30 matrix that gives the control of each country by nation
	1x30 matrix that gives the troop count per country
	A 5x1 vector that indicates the winning nation

The data will be unrolled and fed into a Q-network. 

This is an interesting problem because the state space varies so much, but the AI that plays the game is static and very predictable.
We are more interested in figuring out heuristics to predict a decent approximation of a win distribution than we are in getting every prediction right 100%.
