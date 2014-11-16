/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package graphingcalculator;

import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Max
 */
public class Point {
    double px;
    double py;
    double maxd = 100;
    Point pc = null;
    Point pc2 = null;
    public Point(double x, double y){
        px = x;
        py = y;
    }
    public void render(){
        glVertex3d(px+.1,py+.1,0);
        
    }
    public void search(){
        double dmost = maxd/Grapher.zoom;
        for(Point x: Grapher.points){
            
            double d = Math.sqrt((px-x.px)*(px-x.px)+(py-x.py)*(py-x.py));
            if(d<dmost && Grapher.pointsfnd.contains(x) && x!=this){
                pc = x;
                
                dmost = d;
            }
        }
        if(pc != null){
            glVertex3d(px,py,0);
            glVertex3d(pc.px,pc.py,0);
           
            Grapher.pointsfnd.remove(this);
        }
        
    }
}