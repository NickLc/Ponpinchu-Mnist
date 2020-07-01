package rnnn01;
import java.util.*;


 public class rna01 {
    static final Random rand = new Random();    
   int ce; //cantidad de neuronas entrada
   int ci; //cantidad de neuronas intermedias
   int cs; // cantidad de nueronas  salida
    
   double xin[][];//={{0,1,0},{0,1,1},{1,0,0},{1,0,1}};
   double xout[][];//={{1},{0},{1},{0}};
   
   double y[];
   double s[];
   double g[];
   double w[];
   int c[];//capas de datos
   
   public rna01(int ce_,int[] ci_,int cs_, double[] w_){
        int ce=ce_;        // capa entrada
        int[] ci=ci_;   //[2,3,2]  1ra capa inter (2nr), 2da capa inter (3nr), 3ra capa inter (2nr)
        int cs=cs_;         // capa salida
        int total_ci = 0;
        for (int i=0; i<ci.length; i++){
            total_ci += ci[i];
        }

        y = new double[total_ci+cs];
        s = new double[total_ci+cs];
        g = new double[total_ci+cs];

        int total_w_inter = ce*ci[0];
        for (int i=0; i<ci.length-1; i++){
            total_w_inter += ci[i]*ci[i+1];
        }
        total_w_inter += ci[ci.length-1]*cs;

        w = new double[total_w_inter];
        c = new int[1+ci.length+1];  //cantidad de capas

        c[0]=ce;
        for (int i=0; i<ci.length;i++){
            c[i+1] = ci[i];
        }
        c[ci.length+1]=cs;
        

        for(int i=0;i<y.length;i++){
            y[i]=0;s[i]=0;g[i]=0; 
        }
        if (w_.length != 0){
            for(int i=0;i<w.length;i++){
                w[i]=w_[i];   
            }  
        }
        else{
            for(int i=0;i<w.length;i++){
                w[i]=getRandom(); 
            }    
        }
        System.out.println("Se creo RNN con las siguientes capas: ");
        System.out.println("capa de entrada: "+ce_+" neuronas");
        System.out.println("capas intermedias: "+total_ci+" neuronas");
        System.out.println("capa de salida: "+cs_+" neuronas");
        
        
   }
   
   public double fun(double d){
        return 1/(1+Math.exp(-d));
   }
   
   public void printxingreso(){
           //visualizar x ingreso     
        for(int i=0;i<xin.length;i++)
            for(int j=0;j<xin[i].length;j++)
                System.out.println("xingreso["+i+","+j+"]="+xin[i][j]);
        System.out.println("                ");
   }
   
   public void printxysalida(){
           //visalizar x de salida
        for(int i=0;i<xout.length;i++)
            for(int j=0;j<xout[i].length;j++)
                System.out.println("xsalida["+i+","+j+"]="+xout[i][j]);
   }
   public void printy(){
        for(int i=0;i<y.length;i++)
                System.out.println("y["+i+"]="+y[i]);   
   } 
   public void printw(){
        for(int i=0;i<w.length;i++)
                System.out.println("w["+i+"]="+w[i]);   
   }
   public void prints(){
        for(int i=0;i<s.length;i++)
                System.out.println("s["+i+"]="+s[i]);   
   } 
   public void printg(){
        for(int i=0;i<g.length;i++)
                System.out.println("g["+i+"]="+g[i]);   
   }
    double getRandom() {
            return (rand.nextDouble() * 2 - 1); // [-1;1[
    }
    
    public void entrenamiento(double[][] in,double[][] sal,int veces){
        xin=in;
        xout=sal;
        
        for(int v=0;v<veces;v++){
            //System.out.println("-------------- INICIO  DEL  ENTRENAMIENTO "+v+" VECES-----------------------");        
            for(int i=0;i<xin.length;i++){
            //    System.out.println("ENTRENADO INPUT: "+i);
                entreno(i);

            }
        }
    }
  
   public void entreno(int cee){     
        int ii;
        double pls; 
        int ce;        
        int yy;
        //-----------------------------------------------------------------------------
        //                      E N T R E N A M I E N T O 
        //-----------------------------------------------------------------------------
              
        //System.out.println("-----------------------------H A L L A N D O -  Y  -------------------------");
        //       capa inicial
        ce = cee;
        ii = 0;     //capa_0 * capa_1
        yy = 0;

        for(int an_next=0; an_next<c[1]; an_next++){  // artificial neural next
            pls=0;
            for(int an_prev=0; an_prev<c[0]; an_prev++){
                pls += w[ii] * xin[ce][an_prev];
                ii++;
            }
            s[yy] = pls;  
            y[yy] = fun(s[yy]); 
            //System.out.println("y_"+yy+": "+ y[yy]);
            yy++;
            
        } 
        int ypp = 0;   
        //      capas intermedias
        for (int cp=1; cp<c.length-1;cp++){
            for(int an_next=0; an_next<c[cp+1]; an_next++){
                pls=0;
                for(int an_prev=0; an_prev<c[cp]; an_prev++){
                    pls += w[ii] * y[an_prev+ypp];
                    ii++;
                }
                s[yy] = pls;  
                y[yy] = fun(s[yy]); 
                //System.out.println("y_"+yy+": "+ y[yy]);
                yy++;
            }
            ypp += c[cp];
        }

        //System.out.println("-----------------------------H A L L A N D O -  G  -------------------------");

        // ultima_capa g
        yy--;
        for(int i=c[c.length-1]-1; i>=0; i--){
            g[yy] = (xout[ce][i]- y[yy]) * y[yy] * (1-y[yy]);
            //System.out.println("g_"+yy+": "+ g[yy]);
            yy--;
        }

        //hallando el cantidad de w's
        int total_w = 0;
        for(int i=1;i<c.length;i++){
            total_w += c[i-1]*c[i];
        }
        ii--;
        
        //capas intermedias g
        for (int cp=c.length-2; cp>=1;cp--){
            pls=0;
            total_w -= c[cp]*c[cp+1]; 
            for(int prev=0; prev<c[cp]; prev++){         
                for(int next=0; next<c[cp+1]; next++){
                    pls += w[total_w + next*c[cp]] * g[c[cp]+next];
                }
                g[yy] = y[yy]*(1-y[yy])*pls;
                //System.out.println("g_"+yy+": "+ g[yy]);
                pls=0;
                yy--;
            } 
        }

        //System.out.println("-----------------------------A C T U A L I Z A N D O -  W -------------------------");

        // primera_capa w

        int gpp = 0;
        ii = 0; //capa_0 *capa_1
        for(int next=0; next<c[1]; next++){
            for(int prev=0; prev<c[0]; prev++){
                w[ii] += g[next] * xin[ce][prev];
                //System.out.println("w_"+ii+": "+ w[ii]);
                ii++;
            }
        }
        gpp += c[1];
        ypp = 0;

        // ultimas_capas w
        for (int cp=2; cp<c.length; cp++){

            for(int next=0; next<c[cp]; next++){
                for(int prev=0; prev<c[cp-1]; prev++){
                    w[ii] += g[gpp] * y[ypp+prev];
                    //System.out.println("w_"+ii+": "+ w[ii]);
                    ii++;
                }
                gpp += 1;
            }
            ypp += c[cp-1];

        }

   }
   
   public double[][] prueba(double[][] pruebas){

        double[][] predicts = new double[pruebas.length][]; 
        double prubs[] = new double[c[0]];
        //System.out.println("----------------------------- T E S T -------------------------");

        for(int i=0;i<pruebas.length;i++){
            for(int j=0;j<pruebas[i].length;j++){
                prubs[j]=pruebas[i][j];
            }
            predicts[i] = usored(prubs);
        }
        return predicts;
   }
           
   public double[] usored(double[] datatest){

    int ii;
    double pls; 
    int yy;

    ii = 0;     //capa_0 * capa_1
    yy = 0;

    for(int an_next=0; an_next<c[1]; an_next++){  // artificial neural next
        pls=0;
        for(int an_prev=0; an_prev<c[0]; an_prev++){
            pls += w[ii] * datatest[an_prev];
            ii++;
        }
        s[yy] = pls;  
        y[yy] = fun(s[yy]); 
        //System.out.println("y_"+yy+": "+ y[yy]);
        yy++;
        
    } 
    int ypp = 0;   
    //      capas intermedias
    for (int cp=1; cp<c.length-1;cp++){
        for(int an_next=0; an_next<c[cp+1]; an_next++){
            pls=0;
            for(int an_prev=0; an_prev<c[cp]; an_prev++){
                pls += w[ii] * y[an_prev+ypp];
                ii++;
            }
            s[yy] = pls;  
            y[yy] = fun(s[yy]); 
            //System.out.println("y_"+yy+": "+ y[yy]);
            yy++;
        }
        ypp += c[cp];
        }
        /*
        System.out.print("prueba: ");
        for(int i=0;i<datatest.length;i++){
            System.out.print("["+datatest[i]+"] ");
        }*/
    yy--;
    int ip = 0;
    double[] predict = new double[c[c.length-1]];

    for(int i=c[c.length-1]-1; i>=0 ;i--){
        predict[ip] = y[yy-i];
        ip++;
    }
    return predict;       
   }
}
