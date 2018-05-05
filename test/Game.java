public class Game
{


	// ----------------------------------------------------------------------
	// Part a: the score message
	private String scoreMessage= "";


	// method to get a message from the score bar
	public String getScoreMessage()
	{
		return scoreMessage;
	} // getScoreMessage


	// method to set a message in the Score bar
	public void setScoreMessage(String message)
	{
		scoreMessage = message;
	} // setScoreMessage

	// changed the message of the method to "Laur"
	public String getAuthor()
	{
		return "Laur";
	} // getAuthor


	// ----------------------------------------------------------------------
	// Part b: constructor and grid accessors

	// this variable stores the size of the grid
	final private int gridSize;
	// creates a "Cell" variable to be used in setting certain parts of the grid.
	final private Cell[][] grid;


	// method to construct the grid
	public Game(int requiredGridSize)
	{
		gridSize = requiredGridSize;
		grid = new Cell[gridSize][gridSize];

		for (int i = 0; i < gridSize; i++)
			for(int j = 0; j < gridSize; j++)
				grid[i][j] = new Cell();
	} // Game


	// this method returns the size of the grid
	public int getGridSize()
	{
		return gridSize;
	} // getGridSize


	// this method returns a certain cell from the grid
	public Cell getGridCell(int x, int y)
	{
		return grid[x][y];
	} // getGridCell


	// ----------------------------------------------------------------------
	// Part c: initial game state

	// Part c-1: setInitialGameState method

	// this variable will store the score at any particular time
	private int score=0;


	// this method creates an initial state of a game
	public void setInitialGameState(int requiredTailX, int requiredTailY,
									int requiredLength, int requiredDirection)
	{
		score=0;
		noOfTrees = 0;

		// two nested 'for' loops used to set every cell in the grid to clear type
		for (int i = 0; i < gridSize; i++)
			for (int j = 0; j < gridSize; j++)
				grid[i][j].setClear();

		// if the trees option will be active a tree will be placed
		if (enableTrees == true)
			placeTree();



		placeSnake(requiredTailX, requiredTailY, requiredLength, requiredDirection);

		placeFood();

	} // setInitialGameState


	// ----------------------------------------------------------------------
	// Part c-2 place food

	// variables used to store the position of the food
	public int foodX = 0;
	public int foodY = 0;


	// method used to change a rqandom CLEAR type cell in the grid to FOOD type
	public void placeFood()
	{
		do
		{
			foodX = (int) (Math.random() * (gridSize - 1) + 0);
			foodY = (int) (Math.random() * (gridSize - 1) + 0);
		}while(grid[foodX][foodY].getType() != Cell.CLEAR);

		grid[foodX][foodY].setFood();


	} // placeFood

	// ----------------------------------------------------------------------



	// Part c-3: place snake

	// variables used to store the position of the tale
	public int tailX =0;
	public int tailY =0;

	// variables used to construct the body of the snake in a required direction
	public int direction = 0;
	public int oppDirection = 0;
	public int snakeLength =0;

	// variables used to set the head of the snake
	public int headX=0, headY=0;

	public void placeSnake(int requiredTailX, int requiredTailY,
						   int requiredLength, int requiredDirection)
	{

		snakeLength = requiredLength;

		tailX = requiredTailX;
		tailY = requiredTailY;

		direction = requiredDirection;
		oppDirection = Direction.opposite(requiredDirection);

		// set the tail of the snake
		grid[tailX][tailY].setSnakeTail(oppDirection,direction);

		// for each case (east, north, west, south) the body of the snake is set up
		// and the position of the head is updated
		if (requiredDirection == Direction.EAST)
		{
			for (int i = 1; i <= snakeLength - 2; i++)
				grid[tailX + i][tailY].setSnakeBody(oppDirection,direction);

			headX = tailX + snakeLength -1;
			headY = tailY;
		} // if

		else if  (requiredDirection == Direction.NORTH)
		{
			for (int j = 1; j <= snakeLength - 2; j++)
				grid[tailX][tailY - j].setSnakeBody(oppDirection,direction);

			headX = tailX;
			headY = tailY - snakeLength+1;
		} // else if

		else if  (requiredDirection == Direction.WEST)
		{
			for (int i =1; i <=  snakeLength - 2; i++)
				grid[tailX -i][tailY].setSnakeBody(oppDirection,direction);

			headX = tailX - snakeLength +1;
			headY = tailY;
		} // else if

		else if  (requiredDirection == Direction.SOUTH)
		{
			for (int j = 1; j <= snakeLength -2 ; j++)
				grid[tailX][tailY + j].setSnakeBody(oppDirection,direction);

			headX = tailX;
			headY = tailY + snakeLength -1;
		} // else if

		// the head of the snake is placed at (length - 1) distance from the tail
		grid[headX][headY].setSnakeHead(oppDirection,direction);

	} // placeSnake


	// ----------------------------------------------------------------------
	// Part d: set snake direction

	// In this part the program takes the required direction from the keyboard
	// and modifies the orientation of the head according to it.
	// Also, if the required direction represents the direction from witch the
	// snake come, it will display a message.

	public void setSnakeDirection(int requiredDirection)
	{

		if (requiredDirection != grid[headX][headY].getSnakeInDirection())
		{
			// the direction and oppDirection are updated to the required ones
			direction = requiredDirection;
			oppDirection = Direction.opposite(direction);

			// direction of the head cell is modified
			grid[headX][headY].setSnakeOutDirection(requiredDirection);

		} // if
		else
		{
			// the expected message is displayed in the score bar
			setScoreMessage("Wrong way, captain!");

		} // else
	} // setSnakeDirection


	// ----------------------------------------------------------------------
	// Part e: snake movement

	// Part e-1: move method


	// the body of the snake is modified so it change position if possible

	int nextHeadX, nextHeadY;

	public void move(int moveValue)
	{




		// check if the snake is dead or not
		if (!grid[headX][headY].isSnakeBloody())
		{

			// initialise two variables to hold the next position of the head
			nextHeadX = headX + Direction.xDelta(direction);
			nextHeadY = headY + Direction.yDelta(direction);

			boolean goodToMove = checkNextCell(nextHeadX, nextHeadY);

			// check if the snake would be dead in that position
			if (goodToMove)
			{

				// if would be not, store the type of the next cell to check for food
				int nextCellType = grid[nextHeadX][nextHeadY].getType();

				// moves the head of the snake at the next position
				moveHead(headX, headY, nextHeadX, nextHeadY);

				if (nextCellType == Cell.FOOD)
				{

					// eat the food if the case and place another one
					eatTheFood(moveValue);
					placeFood();

				} // if
				else
					// if the next cell type is not food then moves the tail
					moveTail();

				headX = nextHeadX;
				headY = nextHeadY;

			} // if (goodToMove)

		} // if (Shake is not bloody)

		moveFood();

	} // move




	// ----------------------------------------------------------------------
	// Part e-2: move the snake head

	// in this part the head of the snake is moved to the next position
	private void moveHead(int headX, int headY, int nextHeadX, int nextHeadY)
	{

		// the head cell is replaced with the corresponding body cell type
		if ((headX != nextHeadX)&&(headY != nextHeadY))
			grid[headX][headY].setSnakeBody(oppDirection, direction);
		else
			grid[headX][headY].setSnakeBody();


		// the next cell is replaced with the corresponding head cell type
		grid [nextHeadX][nextHeadY].setSnakeHead(oppDirection, direction);
	} // moveHead


	// ----------------------------------------------------------------------
	// Part e-3: move the snake tail

	// in this method the cell where the tail is is changed to clear and the cell
	// next to the tail one is changed to tail type
	private void moveTail()
	{
		// gets the snake out direction of the tail to know how to orientate the
		// tail in the next cell
		int tailDirection = grid[tailX][tailY].getSnakeOutDirection();

		// set the initial tail cell to clear type

		if (trailsEnable == true)
		{
			gutterTrails();
			grid[tailX][tailY].setOther(50);
		}
		else
		{
			grid[tailX][tailY].setClear();
			for (int i = 0; i < gridSize; i++)
				for (int j = 0; j < gridSize; j++)
					if (grid[i][j].getType() == Cell.OTHER)
						grid[i][j].setClear();
		}
		int nextTailX = tailX + Direction.xDelta(tailDirection);
		int nextTailY = tailY + Direction.yDelta(tailDirection);
		int oppTailDirection = Direction.opposite(tailDirection);


		tailDirection = grid[nextTailX][nextTailY].getSnakeOutDirection();

		// modify the next cell to be tail type
		grid[nextTailX][nextTailY].setSnakeTail(oppTailDirection, tailDirection);

		tailX = nextTailX;   // tailX - 128
		tailY = nextTailY;   // tailY - 129

	}


	// ----------------------------------------------------------------------
	// Part e-4: check for and deal with crashes

	// in this part the next cell due to the required direction is checked if
	// the snake is good to be moved to. The method will return false if it is
	// not good to move and true otherwise
	private boolean isOutsideGrid(int nextX,int nextY)
	{
		if ((nextX < 0)||(nextX == gridSize)||(nextY < 0)||(nextY == gridSize))
			return true;
		else return false;
	}


	private boolean checkNextCell(int nextHeadX, int nextHeadY)
	{
		// if the next position coordinates are at least one bigger than the size
		// of the grid or smaller than 0 set the snake head to be bloody and
		// prints out a message.

		if(isOutsideGrid(nextHeadX,nextHeadY))
		{
			if (snakeIsDead())
			{
				grid[headX][headY].setSnakeBloody(true);
				setScoreMessage("Trying to escape?");
			}
			return false;
		} // if

		// if the next cell is body part it will change both the head
		// and the body part to bloody type and a message is printed
		else if (grid[nextHeadX][nextHeadY].getType() == Cell.SNAKE_BODY)
		{
			if (snakeIsDead())
			{
				grid[headX][headY].setSnakeBloody(true);
				grid[nextHeadX][nextHeadY].setSnakeBloody(true);
				setScoreMessage("Seems that was not food!");
			}
			return false;
		} // else if


		else if (grid[nextHeadX][nextHeadY].getType() == Cell.SNAKE_TAIL)
		{
			if (snakeIsDead())
			{
				grid[headX][headY].setSnakeBloody(true);
				grid[nextHeadX][nextHeadY].setSnakeBloody(true);
				setScoreMessage("Seems that was not food!");
			}
			return false;
		} // else if

		// if the next cell is TREE type then the head is set bloody
		// and a message is displayed
		else if (grid[nextHeadX][nextHeadY].getType() == Cell.TREE)
		{
			if(snakeIsDead())
			{
				grid[headX][headY].setSnakeBloody(true);
				setScoreMessage("It seems you're not a veggie lover");
			}
			return false;
		}// else if
		else
		{
			resetCountdown();
			return true;
		}

	}
	// ----------------------------------------------------------------------
	// Part e-5: eat the food

	//
	//moveValue * ((snakeLength / (gridSize * gridSize / 36 + 1)) + 1);


	// this method calculates the points obtained after eating the food

	public void eatTheFood(int moveValue)
	{


		if (headX == foodX && headY == foodY) // foodX - 103; foodY - 104
			snakeLength ++;  // - 134

		// calculates the row points obtained and the total score obtained
		// after eating food
		int rowPointsObtained=moveValue*((snakeLength/(gridSize*gridSize/36+1))+1);
		int totalScoreIncrease = rowPointsObtained * noOfTrees; // noOfTrees - 450

		if (enableTrees == true)
		{
			placeTree();
			// calculates the score considering the trees
			score = score + totalScoreIncrease; // score - 71
			// display a representative message
			setScoreMessage("Row points: " + rowPointsObtained + "; No of trees: " +
					+noOfTrees + "; Added score: " + totalScoreIncrease);
		} // if
		else
		{
			// calculates the score without the trees being considerated
			score = score + rowPointsObtained;
			// prints a proper message
			setScoreMessage("Row points: " + rowPointsObtained + "; No of trees: " +
					+noOfTrees + "; Added score: " + rowPointsObtained);
		} // else
	} // eatTheFood


	public int getScore()
	{

		return score;
	} // getScore



	// ----------------------------------------------------------------------
	// Part f: cheat

	// in this method the cheat possibility is introduced. If the snake is bloody
	// it will changed not to be so
	public void cheat()
	{

		// nested two for loops to find if there is a bloody snake cell type and
		// change it if the case
		for (int i = 0; i < gridSize; i++)
			for (int j = 0; j < gridSize; j++)
				if (grid[i][j].isSnakeBloody() == true)
					grid[i][j].setSnakeBloody(false);

		// the score is halved
		score = (int)(score / 2); // 71
		// a representative message is displayed in the score bar
		setScoreMessage("You lost half of your points. " + score +
				" to be precise.");
		resetCountdown();
	} // cheat


	// ---------------------------------------------------------------------------
	// Part g: trees
	int noOfTrees;
	boolean enableTrees = false;


	// this method enables or disables the tree possibility
	public void toggleTrees()
	{

		// if the trees are enabled it uses two nester for loops to clear all the
		// cells that have TREE type
		if (enableTrees == true)
		{
			for (int i = 0; i < gridSize; i++)
				for (int j = 0; j < gridSize; j++)
					if (grid[i][j].getType() == Cell.TREE)
						grid[i][j].setClear();
			// the number of trees is set to 0
			noOfTrees = 0;
			// boolean variable enableTrees is given value false
			enableTrees = false;
		} // if
		else
		{
			// if the trees are not enabled a tree will be placed and the number of
			// trees is set to 1
			placeTree();
			noOfTrees = 1;
			// the trees are enabled
			enableTrees = true;
		} // else
	} // toggleTrees


	int treeX = 0;
	int treeY = 0;

	// this method is used to place a tree in a random clear cell
	public void placeTree()
	{

		// search through the grid till it finds a clear cell
		do
		{
			treeX = (int) (Math.random() * (gridSize - 1) + 0);
			treeY = (int) (Math.random() * (gridSize - 1) + 0);
		}while(grid[treeX][treeY].getType() != Cell.CLEAR);

		// and change it to have a TREE type
		grid[treeX][treeY].setTree();

		// the number of trees is incremented by 1
		noOfTrees ++;

	} // placeTree

	// ----------------------------------------------------------------------
	// Part h: crash countdown

	// this part creates a countdown before the snake will be dead after it gets
	// in a crash situation
	private final int movesToCountBeforeDeath = 5;
	private int movesBeforeDeath = movesToCountBeforeDeath;


	//
	private void resetCountdown()
	{
		if (movesBeforeDeath < movesToCountBeforeDeath)
		{
			// if the player escape before the countdown reaches 0 the countdown will
			// be reset to the initial value
			setScoreMessage("You should play the Lottery. Missed death by "
					+ movesBeforeDeath + " moves" );
		} // if
		movesBeforeDeath = movesToCountBeforeDeath;
	} // resetCountdown

	// this method return true if the countdown reached 0 and false otherwise
	private boolean snakeIsDead()
	{
		movesBeforeDeath --;
		if (movesBeforeDeath > 0)
		{
			setScoreMessage("Hurry up! You only have "+ movesBeforeDeath+
					" moves left to escape");
			return false;
		} // if
		else
		{
			movesBeforeDeath = movesToCountBeforeDeath;
			return true;
		} // else

	} // snakeIsDead




	// ----------------------------------------------------------------------
	// Part i: optional extras

	private int nextFoodX, nextFoodY;
	private boolean trailsEnable = false;
	private boolean foodMovementEnable = false;
	private int foodDirection = direction;

	private void gutterTrails()
	{
		int traceLevel;

		for (int i = 0; i < gridSize; i++)
			for(int j = 0; j < gridSize; j++)
			{
				traceLevel = grid[i][j].getOtherLevel();
				if (grid[i][j].getType() == Cell.OTHER)
					if (traceLevel != 5)
						grid[i][j].setOther(traceLevel-9);
					else
						grid[i][j].setClear();
			}
	}


	private void burnTrees()
	{
		if(!isOutsideGrid(nextHeadX,nextHeadY))
			if (grid[nextHeadX][nextHeadY].getType() == Cell.TREE)
			{
				grid[nextHeadX][nextHeadY].setClear();
				if (noOfTrees != 0)
					noOfTrees --;
			}
	}

	private void moveFood()
	{

		if  ((foodMovementEnable)&&(!grid[headX][headY].isSnakeBloody()))
		{
			nextFoodX = foodX + Direction.xDelta(foodDirection);
			nextFoodY = foodY + Direction.yDelta(foodDirection);
			while ((isOutsideGrid(nextFoodX, nextFoodY)) ||
					(grid[nextFoodX][nextFoodY].getType() != Cell.CLEAR))
			{

				if (foodDirection == 4)
					foodDirection = 1;
				else
					foodDirection++;
				nextFoodX = foodX + Direction.xDelta(foodDirection);
				nextFoodY = foodY + Direction.yDelta(foodDirection);

			}


			if (grid[nextFoodX][nextFoodY].getType() == Cell.CLEAR)
			{
				grid[foodX][foodY].setClear();
				foodX = nextFoodX;
				foodY = nextFoodY;
				grid[foodX][foodY].setFood();
			}

		}
	}




	public String optionalExtras()
	{
		return  "  Press 'g' to activate Gutter trails feature \n" +
				"  Press 'b' to burn a tree in front of you \n" +
				"  Press 'm' to activate Food movement feature";

	} // optionalExtras


	public void optionalExtraInterface(char c)
	{
		if(c == 'g')
			if (trailsEnable)
				trailsEnable = false;
			else
				trailsEnable = true;

		if(c == 'm')
			if (foodMovementEnable)
				foodMovementEnable = false;
			else
				foodMovementEnable = true;

		else if(c == 'b')
			burnTrees();

		else if (c > ' ' && c <= '~')
			setScoreMessage("Key " + new Character(c).toString()
					+ " is unrecognised (try h)");
	} // optionalExtraInterface

} // class Game