//this code uses the LEAP motion controller to detect vectors in the hand and move the mouse according to various gestures such as a circle
//made with the index finger on the right hand (which translate to scrolling), and closing the left hand to signify a click.


//Created by Omar Harb


import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;
import com.leapmotion.leap.*;

public class MyMouse 
{

	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static double width = screenSize.getWidth();
	static double height = screenSize.getHeight();

	static Controller con;
	static Frame frame;
	static HandList hands;
	static Hand right;
	static FingerList fingers;
	static Finger front;
	static Vector fingerDir;
	static float xAtDir, yAtDir, x ,y, lastXAtDir, lastYAtDir;
	static boolean pressed = false;

	
	static int counter = 0;
	
	public static void main(String[] args) throws AWTException
	{
		Robot robot = new Robot();
		con = new Controller();
	    con.enableGesture(Gesture.Type.TYPE_CIRCLE);
	    con.setPolicyFlags(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES);
		
		while(true)
		{
			frame = con.frame();
			hands = frame.hands();
			right = hands.rightmost();
			fingers = right.fingers();
			front = fingers.frontmost();
			fingerDir = front.direction();
			lastXAtDir = xAtDir;
			lastYAtDir = yAtDir;
			xAtDir = fingerDir.getX();
			yAtDir = fingerDir.getY();
			boolean triggerReady;
			
			if(xAtDir == 0 && yAtDir == 0)
				continue;
			
//			if(fingers.count() <= 2)
//			{
//				Vector leftDir = fingers.leftmost().direction();
//				Vector rightDir = fingers.frontmost().direction();
//				double dot = leftDir.dot(rightDir);
//				double leftMag = leftDir.magnitude();
//				double rightMag = rightDir.magnitude();
//				double angle = Math.acos(dot/(leftMag*rightMag));
//				if(angle>.9)
//				{
//					triggerReady = true;
//				}
//				if(angle<.5 || fingers.count() == 1)
//				{
//					robot.mousePress(InputEvent.BUTTON1_MASK);
//					robot.mouseRelease(InputEvent.BUTTON1_MASK);
//					triggerReady = false;
//				}
//			}
			
			if(Math.abs(lastXAtDir - xAtDir) < .00075)
			{
				//System.out.println("SO SMALL: " + lastXAtDir + "\t\t"  + xAtDir);
			}
			else			
				x = 1500* xAtDir + (float) (.5* width);
			
			if(Math.abs(lastYAtDir - yAtDir) < .00075)
			{
				//System.out.println("SO SMALL");
			}
			else			
				y = -1500* yAtDir + (float) (.5* height);
			
			GestureList gestures = frame.gestures();
			
			
			//System.out.println(xAtDir);
			
			robot.mouseMove((int) x, (int) y);
			
			
			Vector tipPos = front.stabilizedTipPosition();
		//	System.out.println(tipPos);
			//System.out.println(counter++);
			float tipZ = tipPos.getZ();
//			
			if(hands.leftmost().fingers().isEmpty() && pressed == false)
			{
				robot.mousePress(InputEvent.BUTTON1_MASK);
			    pressed = true;
				//System.out.println("closed");
			    /*try {
				    Thread.sleep(750);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}*/
				//press key when hand is closed, then dont release click until hand is opened
			    
			}
			else if(hands.leftmost().fingers().count()>1)
			{
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
				pressed = false;
			}
				
			if(gestures.count() > 0)
			{
				CircleGesture cg = new CircleGesture( gestures.get(0) );
				boolean clockwiseness;				
				if(cg.pointable().direction().angleTo(cg.normal()) <= Math.PI/2)
				{		            clockwiseness = false;
		         
					clockwiseness = true;		
					robot.mouseWheel(1);
					robot.delay(45);
						
				}
				else
				{
		            robot.mouseWheel(-1);
		            robot.delay(45);
				}	
				Runtime.getRuntime().gc();
			
			}
		}
	}
}
