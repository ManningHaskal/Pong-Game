import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Paddle extends Rectangle{

	
	
	int paddleMovement = 0;

	Color color;
	
	Paddle(int paddleX,int paddleY, int paddleWidth, int paddleHeight, Color color)
	{
		super(paddleX, paddleY, paddleWidth, paddleHeight);
		this.color = color;
	}
	
	public void drawPlayer(Graphics g)
	{
	g.setColor(color);	
	g.fillRect(x,y,width,height);
	}
	
	public void drawCPU(Graphics g)
	{
	g.setColor(color.green);	
	g.drawRect(x,y,width,height);
	}
	
	
	
	
	public void move(int ud)
	{
		if(ud==1)
			y += 10;
		if(ud==2)
			y -= 10;
	}


	
	
	
}
