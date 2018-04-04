

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class GameSnake {

    final String TITLE_OF_PROGRAM = "Classic game Snake"; // constants
    final String GAME_OVER_MSG = "GAME OVER";
    final int POINT_RADIUS = 20; // in pix
    final int FIELD_HEIGHT = 20; // in point
    final int FIELD_WIDTH = 30;
    final int FIELD_DX = 6;
    final int FIELD_DY = 28;
    final int START_LOCATION = 200;

    final int START_SNAKE_SIZE = 6; //SNAKE
    final int START_SNAKE_X = 10;
    final int START_SNAKE_Y = 10;

    final int SHOW_DELAY = 150;

    final int LEFT = 37; //THE CODES OF THE TABS
    final int RIGHT = 39;
    final int UP = 38;
    final int DOWN = 40;

    final int START_DIR = RIGHT; // COLORS
    final Color DEFAULT_COLOR = Color.BLACK;
    final Color FOOD_COLOR = Color.GREEN;
    final Color POISON_COLOR = Color.RED;

    Snake snake;
    Food food;
    Poison poison;
    JFrame frame; // window
    Canvas canvasPanel;
    Random random = new Random();
    boolean GameOver = false;


    public static void main(String[] args) {
        new GameSnake().go();

    }


    void go(){
        frame = new JFrame(TITLE_OF_PROGRAM + " + " + START_SNAKE_SIZE); //create a new window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //esc = close the window
        frame.setSize(FIELD_WIDTH * POINT_RADIUS + FIELD_DX, FIELD_HEIGHT * POINT_RADIUS + FIELD_DY); //set up the size of the window
        frame.setLocation(START_LOCATION, START_LOCATION); // a starting point
        frame.setResizable(false);//we can't change the size of the window

        canvasPanel = new Canvas(); //the canvas where everything will be drawn
        canvasPanel.setBackground(Color.white);

        frame.getContentPane().add(BorderLayout.CENTER, canvasPanel);

        frame.addKeyListener(new KeyAdapter() { //trasing of the tabs
            public void keyPressed (KeyEvent e) { // kontrol nazhatija klavish
                snake.setDirection(e.getKeyCode());
//                System.out.println(e.getKeyCode()); // code of the tab

//                    System.out.print("Do you want to start a new game?");
//                    if (e.getKeyCode() == 10) {
//                        System.out.print("Let's start!");
//                    }
                    }



        });

        frame.setVisible(true);

        snake = new Snake(START_SNAKE_X, START_SNAKE_Y, START_SNAKE_SIZE, START_DIR);
        food = new Food();
        poison = new Poison();

         while (!GameOver){
            snake.move();
            if (food.isEaten()){
                food.next();
            }
            canvasPanel.repaint();

            if (poison.isEaten_poison()){
                if (!GameOver){
                poison.next();

               }

            }
            try {
                Thread.sleep(SHOW_DELAY); // catch an error
            } catch (InterruptedException e) {e.printStackTrace();}
        }
        if (GameOver){
            frame.addKeyListener(new KeyAdapter() { //trasing of the tabs
                public void keyPressed (KeyEvent e) { // kontrol nazhatija klavish
                    if (e.getKeyCode() == 27){
                        System.exit(0);
                        //System.out.print("Let's start a new game!");


                    }


                }


            });
        }


    }




    class Snake {
        ArrayList<Point> snake = new ArrayList<Point>();
        int direction;

        public Snake(int x, int y, int length, int direction){
            for (int i = 0; i <length; i++){
                Point point = new Point(x-i, y);
                snake.add(point);

            }
            this.direction = direction;
        }

        boolean isInsideSnake(int x, int y) {
            for (Point point : snake) {
                if ((point.getX() == x) && (point.getY() == y)) {
                    return true;
                }
            }
            return false;
        }

        boolean isFood(Point food){
            return (snake.get(0).getX() == food.getX() && snake.get(0).getY() == food.getY());
        }
        boolean isPoison(Point poison){
            return (snake.get(0).getX() == poison.getX() && snake.get(0).getY() == poison.getY());

        }



        void move(){
            int x = snake.get(0).getX(); // the coordinates of a head
            int y = snake.get(0).getY();

            if (direction == LEFT){
                x--;
            }
            if (direction == RIGHT){
                x++;
            }
            if (direction == UP){
                y--;
            }

            if (direction == DOWN){
                y++; //??
            }

            if (x > FIELD_WIDTH -1) { x = 0;} //control of the borders
            if (x < 0) {x = FIELD_WIDTH -1;}
            if (y > FIELD_HEIGHT -1 ) {y = 0;}
            if (y < 0) {y = FIELD_HEIGHT-1;}

           // snake.add(0, new Point(x, y)); //new coordinates of a head (the movement of a snake)
            GameOver = isInsideSnake(x, y); // check for acrooss itselves
            snake.add(0, new Point(x, y));
            if (isFood(food)){ //check if a snake collides with food
                food.eat();
                frame.setTitle(TITLE_OF_PROGRAM + ":" + snake.size());
            } else
            snake.remove(snake.size() -1); //delete a tail

            if (isPoison(poison)){
                poison.eat_poison();
                GameOver = true;
            }

        }

        void setDirection(int direction) {
            if ((direction >= LEFT) && (direction <= DOWN)) {
                if (Math.abs(this.direction - direction) != 2) {
                    this.direction = direction;
                }
            }
        }



        void paint(Graphics g){ //creating an object , g is a variable
            for (Point point: snake){ //beret objekt i perebirajet poelementno i posylajet elementy v point
                point.paint(g);
            }
        }
    }
     class Poison extends Point{
        public Poison(){
            super(-1, -1);
            this.color = Color.red;
        }
        void eat_poison(){
            this.setXY(-1, -1);

        }
        boolean isEaten_poison(){
            return  this.getX() == -1;

        }

         void next(){
             int x, y;
             do {
                 x = random.nextInt(FIELD_WIDTH);
                 y = random.nextInt(FIELD_HEIGHT);
             } while(snake.isInsideSnake(x, y));
             this.setXY(x, y);
         }


     }



    class Food extends Point{ // point is a parent-class

        public Food() { // heritates the variables of Point
            super (-1, -1); // we cant see food
            this.color = FOOD_COLOR;
        }
        void eat(){
            this.setXY(-1, -1);
        }

        boolean isEaten(){
            return this.getX() == -1; // ? when the coordinates are negative it means that the food is eaten
        }

        void next(){
            int x, y;
            do {
                x = random.nextInt(FIELD_WIDTH);
                y = random.nextInt(FIELD_HEIGHT);
            } while(snake.isInsideSnake(x, y));
            this.setXY(x, y);
        }

    }
    class Point {
        int x, y;
        Color color = DEFAULT_COLOR;

        public Point(int x, int y){
            this.setXY(x, y);

        }

        void paint(Graphics g){
            g.setColor(color);
            g.fillOval(x*POINT_RADIUS, y*POINT_RADIUS, POINT_RADIUS, POINT_RADIUS);

        }

        int getX() {return x;}
        int getY() {return y;}

        void setXY(int x, int y){
            this.x = x;
            this.y = y;

        }
    }
    public class Canvas extends JPanel{

        @Override
        public void paint(Graphics g){
            super.paint(g);
            snake.paint(g);
            food.paint(g);
            poison.paint(g);
            if (GameOver) {
                g.setColor(Color.red);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                FontMetrics fm = g.getFontMetrics();
                g.drawString(GAME_OVER_MSG, (FIELD_WIDTH * POINT_RADIUS + FIELD_DX - fm.stringWidth(GAME_OVER_MSG))/2, (FIELD_HEIGHT * POINT_RADIUS + FIELD_DY)/2);


    }

}
    }



}



