//Basic Game Application
//Version 2
// Basic Object, Image, Movement
// Astronaut moves to the right.
// Threaded

//K. Chun 8/2018

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************
// Class Definition Section

public class BasicGameApp implements Runnable {

   //Variable Definition Section
   //Declare the variables used in the program 
   //You can set their initial values too
   
   //Sets the width and height of the program window
	final int WIDTH = 1000;
	final int HEIGHT = 700;

   //Declare the variables needed for the graphics
	public JFrame frame;
	public Canvas canvas;
   public JPanel panel;
   
	public BufferStrategy bufferStrategy;
	public Image astroPic;
	public Image backround;
   //Declare the objects used in the program
   //These are things that are made up of more than one variable type
	private Astronaut astro;
	private Astronaut astro2;
	private Astronaut astro3;
	private Astronaut[] astros;

   // Main method definition
   // This is the code that runs first and automatically
	public static void main(String[] args) {
		BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
		new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method  
	}


   // Constructor Method
   // This has the same name as the class
   // This section is the setup portion of the program
   // Initialize your variables and construct your program objects here.
	public BasicGameApp() {
      
      setUpGraphics();
       astros = new Astronaut[10];
	   for(int x = 0; x< astros.length; x++){
		   astros[x] = new Astronaut((int)(Math.random()*940), (int)(Math.random()*700));
	   }
      //variable and objects
      //create (construct) the objects needed for the game and load up 
		astroPic = Toolkit.getDefaultToolkit().getImage("astronaut.png"); //load the picture
		astro = new Astronaut((int)(Math.random()*940), (int)(Math.random()*700));
		astro2 = new Astronaut((int)(Math.random()*940), (int)(Math.random()*700));
		astro3 = new Astronaut((int)(Math.random()*940), (int)(Math.random()*700));
		backround = Toolkit.getDefaultToolkit().getImage("stars.jpg");


	}// BasicGameApp()
//change
   
//*******************************************************************************
//User Method Section
//
// put your code to do things here.

   // main thread
   // this is the code that plays the game after you set things up
	public void run() {

      //for the moment we will loop things forever.
		while (true) {

         moveThings();  //move all the game objects
         render();  // paint the graphics
         pause(20); // sleep for 10 ms
		}
	}


	public void moveThings() {
      //calls the move( ) code in the objects
		for(int x=0; x< astros.length; x++){
			if(astro.rec.intersects(astros[x].rec)&& astro.isCrashing== false){
				astro.height= 10+astro.height;
				astro.width= 10+astro.width;
			}
		}

		for(int z=0; z< astros.length; z++){
			for(int y = z+1; y<astros.length; y++){
				if(astros[z].rec.intersects(astros[y].rec)&& astros[z].isCrashing == false){
					System.out.println("Crashing astros!");
				}
			}
		}
		astro.wrap();
		astro2.bounce();
		astro3.bounce();
		if(astro.rec.intersects(astro2.rec) && astro.rec.intersects(astro3.rec) && astro.isCrashing == false){
			astro.bounce();
			astro.height = 20;
			astro.width=20;
			astro2.bounce();
			astro2.height=200;
			astro2.width=200;
			astro3.bounce();
			astro3.height = 30;
			astro3.width=30;
			astro.isCrashing = true;
		}
		if(astro.rec.intersects(astro2.rec) == false && astro.rec.intersects(astro3.rec)==false){
			astro.isCrashing = false;
		}


	}


	public void collisions(){
		if(astro.rec.intersects(astro2.rec)){
			astro.dx=-1* astro.dx;
			astro.dy=-1* astro.dy;
			astro2.dx=-1* astro2.dx;
			astro2.dy=-1* astro2.dy;
		}
		if(astro2.rec.intersects(astro3.rec)){
			astro2.height+=15;
			astro2.width+=15;
			astro3.height+=15;
			astro3.width+=15;
		}
		if(astro3.rec.intersects(astro.rec)){
			astro3.dx=-1* astro3.dx;
			astro3.dy=-1* astro3.dy;
			astro.dx=-1* astro.dx;
			astro.dy=-1* astro.dy;
		}
	}
	
   //Pauses or sleeps the computer for the amount specified in milliseconds
   public void pause(int time ){
   		//sleep
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {

			}
   }

   //Graphics setup method
   private void setUpGraphics() {
      frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.
   
      panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
      panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
      panel.setLayout(null);   //set the layout
   
      // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
      // and trap input events (Mouse and Keyboard events)
      canvas = new Canvas();  
      canvas.setBounds(0, 0, WIDTH, HEIGHT);
      canvas.setIgnoreRepaint(true);
   
      panel.add(canvas);  // adds the canvas to the panel.
   
      // frame operations
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
      frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
      frame.setResizable(false);   //makes it so the frame cannot be resized
      frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!
      
      // sets up things so the screen displays images nicely.
      canvas.createBufferStrategy(2);
      bufferStrategy = canvas.getBufferStrategy();
      canvas.requestFocus();
      System.out.println("DONE graphic setup");
   
   }


	//paints things on the screen using bufferStrategy
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		g.drawImage(backround, 0, 0, WIDTH, HEIGHT, null);


		//draw the image of the astronaut
		for(int x = 0; x< astros.length; x++){
			g.drawImage(astroPic, astros[x].xpos, astros[x].ypos, astros[x].width, astros[x].height, null);
		}
		g.drawImage(astroPic, astro.xpos, astro.ypos, astro.width, astro.height, null);
		g.drawImage(astroPic, astro2.xpos, astro2.ypos, astro2.width, astro2.height, null);
		g.drawImage(astroPic, astro3.xpos, astro3.ypos, astro3.width, astro3.height, null);
		g.dispose();

		bufferStrategy.show();
	}
}