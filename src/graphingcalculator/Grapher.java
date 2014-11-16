/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphingcalculator;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Octalus
 */
public class Grapher implements Runnable {
    double scale = 1.0;
    Thread t;
    boolean[][] p;
    
    public static double xs;
    public static double ys;
    public static double zoom = 100;
    public static ArrayList<Point> points = new ArrayList<>();
    public static ArrayList<Point> pointsfnd = new ArrayList<>();
    public String equation = "y=x";
    
    public void init() {
        try {
            Display.setParent(MainUI.glContent);
            Display.create();
        
            glMatrixMode(GL_PROJECTION);
            glOrtho(-300, 300, -300, 300, -1, 1);
            glMatrixMode(GL_MODELVIEW);
            glClearColor(1,1,1,1);
        } catch (LWJGLException ex) {
            Logger.getLogger(Grapher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void render() {
        init();
        while(!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            
                points.clear();
                pointsfnd.clear();
                for(double x = -300/zoom-xs; x<300/zoom-xs; x+=2/zoom){
                    for(double y = -300/zoom-ys; y<300/zoom-ys; y+=2/zoom){
                        if(Math.abs(EquationSolver.work(equation, x, y, 0))<1/zoom){
                            boolean c = true;
                            for(Point p: points){
                                if(Math.sqrt((x-p.px)*(x-p.px)+ (y-p.py)*(y-p.py))<2/zoom){
                                    c = false;
                                }
                            }
                            if(c){
                                points.add(new Point(x,y));
                            }
                        }
                    }
                }
                input();
                glTranslated(xs*zoom,ys*zoom,0);
                glScaled(zoom,zoom,zoom);

                axes();

                for(Point p: points){
                    pointsfnd.add(p);
                }

                glBegin(GL_LINES);
                glColor3d(1,0,0);
                for(Point p: points){
                    p.search();
                }
                glEnd();
           
            
            Display.update();
            Display.sync(60);
        }
        destroy();
    }
    
    public void destroy() {
        Display.destroy();
    }
    
    public Grapher() {
        
    }

    @Override
    public void run() {
        render();
    }
    
    public void start () {
        if (t == null) {
            t = new Thread (this, "GLContent");
            t.start ();
        }
    }
    
    public static void input(){
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            xs-=10/zoom;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            xs+=10/zoom;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
            ys-=10/zoom;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
            ys+=10/zoom;
        }
        if(Mouse.isButtonDown(0) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            xs += Mouse.getDX()/zoom;
            ys += Mouse.getDY()/zoom;
        }
        if(Mouse.isButtonDown(0) && Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
            System.out.println(((Mouse.getX()-300)/zoom-xs) + " , " + ((Mouse.getY()-300)/zoom-ys));
        }
        if(Mouse.isButtonDown(1)){
            findpoint(((Mouse.getX()-300)/zoom-xs),((Mouse.getY()-300)/zoom-ys));
        }
        double m = Mouse.getDWheel();
        if(m>0){
            zoom*=1.1;
        }
        if(m<0){
            zoom/=1.1;
        }
    }
    public static void axes(){
        glBegin(GL_LINES);
        
        glColor3d(.75,.75,.75);
        //System.out.println(Math.pow(10,Math.floor(Math.log10(zoom))));
        for(double x = 0; x<300/zoom-xs; x+=10/Math.pow(10,Math.floor(Math.log10(zoom/2)))){
            glVertex3d(x,-300/zoom-ys,0);
            glVertex3d(x,300/zoom-ys,0);
        }
        for(double x = 0; x>-300/zoom-xs; x-=10/Math.pow(10,Math.floor(Math.log10(zoom/2)))){
            glVertex3d(x,-300/zoom-ys,0);
            glVertex3d(x,300/zoom-ys,0);
        }
        for(double x = 0; x<300/zoom-ys; x+=10/Math.pow(10,Math.floor(Math.log10(zoom/2)))){
            glVertex3d(-300/zoom-xs,x,0);
            glVertex3d(300/zoom-xs,x,0);
        }
        for(double x = 0; x>-300/zoom-ys; x-=10/Math.pow(10,Math.floor(Math.log10(zoom/2)))){
            glVertex3d(-300/zoom-xs,x,0);
            glVertex3d(300/zoom-xs,x,0);
        }
        
        glColor3d(0,0,0);
        glVertex3d(-300/zoom-xs,0,0);
        glVertex3d(300/zoom-xs,0,0);
        glVertex3d(0,300/zoom-ys,0);
        glVertex3d(0,-300/zoom-ys,0);
        glEnd();
    }
    public static void findpoint(double x, double y){
        Point pc = null;
        double dmax = Double.MAX_VALUE;
        for(Point p: points){
            double d = Math.sqrt((x-p.px)*(x-p.px)+(y-p.py)*(y-p.py));
            if(d<dmax){
                dmax = d;
                pc = p;
            }
        }
        glBegin(GL_POINTS);
        glColor3d(0,0,1);
        for(int a = -5; a<5; a++){
            for(int b = -5; b<5; b++){
                if(a*a+b*b<10){
                    glVertex3d((pc.px+xs)*zoom+a+.1, (pc.py+ys)*zoom+b+.1,0);
                }
            }
        }
        glEnd();
        //System.out.println(pc.px + " , " + pc.py);
    }
}
