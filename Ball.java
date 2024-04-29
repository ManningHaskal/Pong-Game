import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ball extends Rectangle
{
	int speed = 8;
	int Vx;
	int Vy;
	int X;
	int Y;
	int angle = 0;
	Color color;




	Ball(int x, int y, int width, int height, Color color)
	{
		super(x,y,width,height);
		this.color = color;
	}


	public void draw(Graphics g)
	{
		g.setColor(color);	
		g.fillOval(x,y,width,height);
	}


	public void move()
	{

		angle%=360;

		Vx = (int)(speed* Math.cos(Math.toRadians(angle)));
		Vy = (int)(speed* Math.sin(Math.toRadians(angle)));
		x+=Vx;
		y-=Vy;
	}

	public void startAngle(int score)
	{
		// if no score
		if(score==0)
		{
			angle = (int) (Math.random()*360);
			if (angle>45 && angle<135)
				angle+=90;
			else if(angle>225 && angle < 315)
				angle+=90;
		}

		else if(score==1)
			angle = (int) (Math.random()*90)+135;

		else
			angle = (int) (Math.random()*90)+315;



	}	
}
