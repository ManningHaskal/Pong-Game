import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


public class PongGamePanel extends JPanel implements ActionListener{
	static final int SCREEN_WIDTH = 1400;
	static final int SCREEN_HEIGHT = 750;
	static final int UNIT_SIZE = 30;
	static int DELAY = 20;
	int restart = 0;
	boolean running = false;
	boolean started = false;
	Timer timer;
	Random random;
	int playerOneHeight = (SCREEN_HEIGHT-150)/2;
	int playerOneMove = 0;
	int playerTwoHeight = (SCREEN_HEIGHT-150)/2;
	int playerTwoMove = 0;
	Paddle player1 = new Paddle(50,(SCREEN_HEIGHT-150)/2,20,150, Color.cyan);
	Paddle player2 = new Paddle(SCREEN_WIDTH-70,(SCREEN_HEIGHT-150)/2,20,150, Color.red);
	int diameter = 25;
	Ball ball = new Ball((SCREEN_WIDTH-diameter)/2,(SCREEN_HEIGHT-diameter)/2, diameter, diameter, Color.white);
	int[] score = {0,0,0};//1score,2score,prev win
	int flashing = 0;
	boolean[] cpu = {false, false};



	PongGamePanel()
	{
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();

	}

	public void startGame()
	{
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g)
	{
		if(running)
		{
			flashing++;

			if (started == false)
			{
				g.setColor(Color.cyan);
				g.setFont(new Font ("", Font.BOLD, 100));
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString("Pong",(SCREEN_WIDTH - metrics.stringWidth("Pong"))/2, g.getFont().getSize()+200);

				g.setFont(new Font ("", Font.BOLD, 40));
				metrics = getFontMetrics(g.getFont());

				if(flashing%40<=25)
					g.drawString("Press Space or Return To Start",(SCREEN_WIDTH - metrics.stringWidth("Press Space or Return To Start"))/2, g.getFont().getSize()+400);

				g.setFont(new Font ("", Font.BOLD, 18));
				metrics = getFontMetrics(g.getFont());
				g.drawString("Press \"1\" or \"2\" to enable cpu",(SCREEN_WIDTH - metrics.stringWidth("Press \"1\" or \"2\" to enable cpu"))-10, g.getFont().getSize()+10);
				g.setColor(Color.gray);
				g.drawString("Use W&S and arrow keys to move",(SCREEN_WIDTH - metrics.stringWidth("Use W&S and arrow keys to move"))-10, g.getFont().getSize()+35);

			}
			else
			{



				g.setColor(Color.DARK_GRAY);
				g.setFont(new Font ("", Font.BOLD, 40));
				g.setColor(Color.DARK_GRAY);
				FontMetrics metrics = getFontMetrics(g.getFont());
				metrics = getFontMetrics(g.getFont());
				g.drawString(String.valueOf(score[0]),(SCREEN_WIDTH - metrics.stringWidth(String.valueOf(score[0])))/2-35, g.getFont().getSize()+20);
				g.drawString(String.valueOf(score[1]),(SCREEN_WIDTH - metrics.stringWidth(String.valueOf(score[1])))/2+35, g.getFont().getSize()+20);

				g.fillRect((SCREEN_WIDTH/2)-5, 0, 10, SCREEN_HEIGHT);
				player1.drawPlayer(g);
				if (cpu[0])
					player1.drawCPU(g);
				player2.drawPlayer(g);
				if (cpu[1])
					player2.drawCPU(g);
				ball.draw(g);


				g.setFont(new Font ("", Font.BOLD, 10));
				g.setColor(Color.black);
				g.drawString(String.valueOf(ball.angle),10, 20);
				g.drawString(String.valueOf(ball.speed),10, 40);

				if(score[2]!=0)
				{
					g.setColor(Color.cyan);

				g.setFont(new Font ("", Font.BOLD, 40));
				metrics = getFontMetrics(g.getFont());

				if(flashing%40<=25)
					g.drawString("Press Space or Return To Continue",(SCREEN_WIDTH - metrics.stringWidth("Press Space or Return To Continue"))/2, g.getFont().getSize()+350);
				}

			}
		}
	}


	public void move()
	{
		if(cpu[0])

			if(player1.y >= ball.y+20 && score[2]==0)
				player1.paddleMovement = 2;
			else if(player1.y+150 <= ball.y+5 && score[2]==0)
				player1.paddleMovement = 1;

		if(cpu[1])
			if(player2.y >= ball.y+20 && score[2]==0)
				player2.paddleMovement = 2;
			else if(player2.y+150 <= ball.y+5 && score[2]==0)
				player2.paddleMovement = 1;


		if(player1.paddleMovement == 2 && player1.y > 1)
			player1.move(2);
		else if(player1.paddleMovement == 1 && player1.y < SCREEN_HEIGHT-151)
			player1.move(1);

		if(player2.paddleMovement == 2 && player2.y > 1)
			player2.move(2);
		else if(player2.paddleMovement == 1 && player2.y < SCREEN_HEIGHT-151)
			player2.move(1);

		if(started == true)
		{
			ball.move();
		}

	}



	public void checkCollisions()
	{
		if(!running)
			timer.stop();

		//ball bouncing off roof and floor
		if(ball.y <= 1 && ball.angle < 90)
			ball.angle=360-ball.angle;
		else if(ball.y <= 1 && ball.angle > 90 && ball.angle < 180)
			ball.angle=360-ball.angle;
		else if(ball.y >= SCREEN_HEIGHT-diameter && ball.angle > 270)
			ball.angle=360-ball.angle;
		else if(ball.y >= SCREEN_HEIGHT-diameter && ball.angle < 270 && ball.angle > 180)
			ball.angle=360-ball.angle;


		//ball off player 1
		if(ball.intersects(player1) && ball.angle > 90 && ball.angle < 270)
		{

			if((ball.angle > 90 && ball.angle <= 180 && player1.paddleMovement == 2) || (ball.angle < 270 && ball.angle >= 180 && player1.paddleMovement == 1))
			{
				ball.speed+=1;
				if (ball.angle <= 180)
					ball.angle-=Math.random()*8;
				else
					ball.angle+=Math.random()*8;
			}
			else if((ball.angle > 90 && ball.angle <= 180 && player1.paddleMovement == 1) || (ball.angle < 270 && ball.angle > 180 && player1.paddleMovement == 2))
			{
				if(ball.speed > 7)
					ball.speed-=1;
				if (ball.angle <= 180)
					ball.angle+=Math.random()*5;
				else
					ball.angle-=Math.random()*5;
			}
			ball.angle = 540-ball.angle;
			ball.angle%=360;
		}

		//ball off player 2
		if(ball.intersects(player2) && (ball.angle > 270 || ball.angle < 90))
		{
			if((ball.angle > 270 && player2.paddleMovement == 1) || (ball.angle < 90 && player2.paddleMovement == 2))
			{
				ball.speed+=1;
				if (ball.angle >= 270)
					ball.angle-=Math.random()*8;
				else
					ball.angle+=Math.random()*8;
			}
			else if((ball.angle > 270 && player2.paddleMovement == 2) || (ball.angle < 90 && player2.paddleMovement == 1))
			{
				if(ball.speed > 7)
					ball.speed-=1;
				if (ball.angle >= 270)
					ball.angle+=Math.random()*5;
				else
					ball.angle-=Math.random()*10;
			}
			ball.angle = 540-ball.angle;
			ball.angle%=360;
		}


		//scoring
		if(ball.x <= 0 && score[2] == 0)
		{
			score[1]++;
			score[2] = 2;
		}
		else if(ball.x >= SCREEN_WIDTH && score[2] == 0)
		{
			score[0]++;
			score[2] = 1;
		}

	}


	public void restart()
	{
		if (started == false)
		{
			started = true;
			ball.speed = 15;
			ball.startAngle(score[2]);
		}
		if(score[2]!=0)
		{
			ball.speed = 15;
			ball.x = (SCREEN_WIDTH-diameter)/2;
			ball.y = (SCREEN_HEIGHT-diameter)/2;
			ball.startAngle(score[2]);
			player1.y = (SCREEN_HEIGHT-150)/2;
			player2.y = (SCREEN_HEIGHT-150)/2;
			score[2] = 0;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (running)
		{
			move();
			checkCollisions();

		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			switch(e.getKeyCode()) 
			{
			case KeyEvent.VK_ENTER:
				restart();
				break;
			case KeyEvent.VK_SPACE:
				restart();
				break;
			case KeyEvent.VK_S:
				player1.paddleMovement=1;
				break;
			case KeyEvent.VK_W:
				player1.paddleMovement=2;
				break;
			case KeyEvent.VK_DOWN:
				player2.paddleMovement=1;
				break;
			case KeyEvent.VK_UP:
				player2.paddleMovement=2;
				break;
			case KeyEvent.VK_1:
				cpu[0] = !cpu[0];
				break;
			case KeyEvent.VK_2:
				cpu[1] = !cpu[1];
				break;

			}
		}

		public void keyReleased(KeyEvent e)
		{
			switch(e.getKeyCode()) 
			{
			case KeyEvent.VK_S:
				player1.paddleMovement=0;
			case KeyEvent.VK_W:
				player1.paddleMovement=0;
				break;
			case KeyEvent.VK_UP:
				player2.paddleMovement=0;
			case KeyEvent.VK_DOWN:
				player2.paddleMovement=0;
				break;

			}
		}
	}
}













