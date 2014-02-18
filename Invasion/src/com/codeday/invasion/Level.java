package com.codeday.invasion;

import java.util.ArrayList;

public class Level
{
	private ArrayList<Point> asteroids = new ArrayList<Point>();
	private int asteroidHealth;
	private ArrayList<Point> enemies = new ArrayList<Point>();
	
	public Level(String line)
	{
		String[] parts = line.split(";");
		asteroidHealth = Integer.parseInt(parts[0]);
		String[] points = parts[1].split(":");
		for (String point : points)
		{
			String[] temp = point.split(",");
			asteroids.add(new Point(Double.parseDouble(temp[0]), Double.parseDouble(temp[1])));
		}
		if (parts.length > 2)
		{
			points = parts[2].split(":");
			for (String point : points)
			{
				String[] temp = point.split(",");
				if (temp.length > 1)
					enemies.add(new Point(Double.parseDouble(temp[0]), Double.parseDouble(temp[1])));
			}
		}
	}
	
	public void buildLevel(World world)
	{
		for (Point point : asteroids)
		{
			Asteroid.MAX_HEALTH = asteroidHealth;
			Asteroid a = new Asteroid(world.getAtlas().findRegion("meteorBig"), world, false);
			a.setPosition((float)point.getX() * world.getWidth() - a.getWidth() / 2, 
					(float)point.getY() * world.getHeight() - a.getHeight() / 2);
			world.addAsteroid(a);
		}
		for (Point point : enemies)
		{
			Enemy e = new Enemy(world.getAtlas().findRegion("enemyUFO"), world, false);
			e.setPosition((float)point.getX() * world.getWidth() - e.getWidth() / 2, 
						  (float)point.getY() * world.getHeight() - e.getHeight() / 2);
			world.addEnemy(e);
		}
	}
}
