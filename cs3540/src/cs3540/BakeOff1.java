package cs3540;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Collections;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class BakeOff1 extends PApplet {
	// when in doubt, consult the Processsing reference:
	// https://processing.org/reference/
	// The argument passed to main must match the class name
	public static void main(String[] args) {
		// Tell processing what class we want to run.
		PApplet.main("cs3540.BakeOff1");
	}

	int margin = 200; // set the margin around the squares
	final int padding = 50; // padding between buttons and also their width/height
	final int buttonSize = 40; // padding between buttons and also their width/height
	ArrayList<Integer> trials = new ArrayList<Integer>(); // contains the order of buttons that activate in the test
	int trialNum = 0; // the current trial number (indexes into trials array above)
	int startTime = 0; // time starts when the first click is captured
	int finishTime = 0; // records the time of the final click
	int hits = 0; // number of successful clicks
	int misses = 0; // number of missed clicks
	Robot robot; // initialized in setup

	int numRepeats = 1; // sets the number of times each button repeats in the test
	PImage arrowCursor;
	/**
	 * https://processing.org/reference/settings_.html#:~:text=The%20settings()%20method%20runs,commands%20in%20the%20Processing%20API.
	 */
	public void settings() {
		size(700, 700);
	}

	/**
	 * // https://processing.org/reference/setup_.html
	 */
	public void setup() {
	    size(700, 700);
	    
	    // Load the custom arrow cursor image
	    arrowCursor = loadImage("res/arrow.png"); // Ensure "arrow.png" is in the "res" folder and is smaller (e.g., 64x64)

	    noStroke();
	    textFont(createFont("Arial", 16));
	    textAlign(CENTER);
	    frameRate(60);
	    ellipseMode(CENTER);

	    try {
	        robot = new Robot();
	    } catch (AWTException e) {
	        e.printStackTrace();
	    }

	    // Shuffle the trials array (leave your existing code here)
	    for (int i = 0; i < 16; i++)
	        for (int k = 0; k < numRepeats; k++)
	            trials.add(i);

	    Collections.shuffle(trials); // randomize the order of buttons
	    surface.setLocation(0, 0); // Position the window at the top-left corner
	}

	public void draw() {
	    background(0); // Set background to black

	    if (trialNum >= trials.size()) {
	        // End-of-game code (leave as is)
	        return;
	    }

	    fill(255); // Set fill color to white
	    text((trialNum + 1) + " of " + trials.size(), 40, 20); // Display the current trial number

	    // Draw all buttons
	    for (int i = 0; i < 16; i++) {
	        drawButton(i);
	    }

	    // Get the current target button location
	    Rectangle targetButton = getButtonLocation(trials.get(trialNum));
	    float targetX = targetButton.x + targetButton.width / 2;
	    float targetY = targetButton.y + targetButton.height / 2;

	    // Calculate the angle between the mouse and the target button
	    float angle = atan2(targetY - mouseY, targetX - mouseX);

	    // Set up a PGraphics object to rotate the cursor image dynamically
	    PGraphics rotatedArrow = createGraphics(arrowCursor.width, arrowCursor.height);
	    rotatedArrow.beginDraw();
	    rotatedArrow.translate(arrowCursor.width / 2, arrowCursor.height / 2);
	    rotatedArrow.rotate(angle);
	    rotatedArrow.image(arrowCursor, -arrowCursor.width / 2, -arrowCursor.height / 2);
	    rotatedArrow.endDraw();

	    // Ensure that the hotSpot is within the bounds of the cursor image (e.g., 64x64)
	    int hotSpotX = arrowCursor.width / 2;
	    int hotSpotY = arrowCursor.height / 2;
	    
	    // Set the custom cursor with valid hotspot coordinates
	    cursor(rotatedArrow.get(), hotSpotX, hotSpotY); 

	    fill(255, 0, 0, 200); // Optional: draw user cursor as a red circle
	    ellipse(mouseX, mouseY, 20, 20);
	}



	public void mousePressed() // test to see if hit was in target!
	{
		if (trialNum >= trials.size()) // check if task is done
			return;

		if (trialNum == 0) // check if first click, if so, record start time
			startTime = millis();

		if (trialNum == trials.size() - 1) // check if final click
		{
			finishTime = millis();
			// write to terminal some output:
			System.out.println("we're all done!");
		}

		Rectangle bounds = getButtonLocation(trials.get(trialNum));

		// check to see if cursor was inside button
		if ((mouseX > bounds.x && mouseX < bounds.x + bounds.width)
				&& (mouseY > bounds.y && mouseY < bounds.y + bounds.height)) // test to see if hit was within bounds
		{
			System.out.println("HIT! " + trialNum + " " + (millis() - startTime)); // success
			hits++;
		} else {
			System.out.println("MISSED! " + trialNum + " " + (millis() - startTime)); // fail
			misses++;
		}

		trialNum++; // Increment trial number

		// in this example design, I move the cursor back to the middle after each click
		// Note. When running from eclipse the robot class affects the whole screen not
		// just the GUI, so the mouse may move outside of the GUI.
		// robot.mouseMove(width/2, (height)/2); //on click, move cursor to roughly
		// center of window!
	}

	// probably shouldn't have to edit this method
	public Rectangle getButtonLocation(int i) // for a given button ID, what is its location and size
	{
		int x = (i % 4) * (padding + buttonSize) + margin;
		int y = (i / 4) * (padding + buttonSize) + margin;

		return new Rectangle(x, y, buttonSize, buttonSize);
	}

	// you can edit this method to change how buttons appear
	public void drawButton(int i) {
		Rectangle bounds = getButtonLocation(i);

		if (trials.get(trialNum) == i) // see if current button is the target
			fill(0, 255, 255); // if so, fill cyan
		else
			fill(200); // if not, fill gray

		rect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public void mouseMoved() {
		// can do stuff everytime the mouse is moved (i.e., not clicked)
		// https://processing.org/reference/mouseMoved_.html
	}

	public void mouseDragged() {
		// can do stuff everytime the mouse is dragged
		// https://processing.org/reference/mouseDragged_.html
	}

	public void keyPressed() {
		// can use the keyboard if you wish
		// https://processing.org/reference/keyTyped_.html
		// https://processing.org/reference/keyCode.html
	}
}
