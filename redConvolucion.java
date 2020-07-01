package rnnn01;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

//public class ReadAndWriteImage
public class redConvolucion {
    public redConvolucion(){

    }
    public void printArray(double[] Arr){
        for(int i=0;i<Arr.length;i++){
            System.out.print(Arr[i]+" ");
        }
    }
    
    public void printMatrix(double[][] M){
        for(int j=0; j<M.length; j++){
            for(int i=0; i<M[j].length; i++){
                System.out.print(M[j][i] + " ");
            }
            System.out.println();
        }
    }

    public double[] Flattening(double[][] max_pool){
        double[] flatening = new double[max_pool.length*max_pool[0].length];
        int count = 0;
        for(int j=0; j<max_pool.length;j++){
            for(int i=0; i<max_pool[0].length; i++){
                flatening[count] = max_pool[j][i];
                count++;
            }
        }  
        return flatening;
    }

    public double[][] maxPooling(double[][] convol_ker, int dim_row, int dim_col){
        int mp_col = (int) Math.ceil(convol_ker.length/dim_col);
        int mp_row = (int) Math.ceil(convol_ker[0].length/dim_row);
        double[][] maxPooling = new double [mp_col][mp_row]; 
        double max_aux = 0;

        int y = 0;
        int x;
        for(int j_max_pul=0; j_max_pul<mp_col; j_max_pul++){
            x = 0;
            for(int i_max_pul=0; i_max_pul<mp_row; i_max_pul++){
                // Stop with board
                if(y+dim_col>convol_ker.length){
                    dim_col = convol_ker.length-y;
                }
                if(x+dim_row>convol_ker[y].length){
                    dim_row = convol_ker[y].length-x;
                }
                // Sum
                for(int j=0; j<dim_col; j++){
                    for(int i=0; i<dim_row; i++){
                        if(convol_ker[y+j][x+i]>max_aux){
                            max_aux = convol_ker[y+j][x+i];
                        }
                    }
                }
                //System.out.println(j_max_pul+"," +i_max_pul+","+ max_aux);
                maxPooling[j_max_pul][i_max_pul] = max_aux;
                max_aux = 0;
                x=x+dim_row;
            }
            y=y+dim_col;
        }
        return maxPooling;

    }

    public double[][] convolution(double[][] input, int[][] kernel){
        
        int hi = input.length;
        int wi = input[0].length;
        int hk = kernel.length;
        double r, sum;
        double[][] output = new double[hi-hk][wi-hk];
        for(int y=0; y<hi-hk; y++){
            for(int x=0; x<wi-hk; x++){
                sum = 0;
                for(int j=0; j<hk; j++){
                    for(int i=0; i<hk; i++){
                        r = input[j+y][i+x];
                        sum += kernel[j][i] * r;
                    }
                }
                output[y][x] = sum;     
            }
        }
        return output;
    }
    
    public double[][] getMatrixImage(String inputFileGray){
        try
        {
            File file = new File(inputFileGray);
            BufferedImage image = null;
            image = ImageIO.read(file);
            int hi = image.getHeight();
            int wi = image.getWidth();
            int r;
            double[][] pixeles = new double[hi][wi];
            for(int y=0; y<hi; y++){
                for(int x=0; x<wi; x++){
                    r = getComponentRGB(image.getRGB(x, y), "red")/255;
                    pixeles[y][x] = r;
                }
            }
            return pixeles;
  
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    public void converImageToGray(String inputFile, String outputFile) throws IOException {
        try
        {
            File file = new File(inputFile);
            BufferedImage image = null;
            image = ImageIO.read(file);
            int h = image.getHeight();
            int w = image.getWidth();
            String type_img = inputFile.substring(inputFile.length()-3,inputFile.length());
            int r,g,b;
        
            for(int y=0;y<h;y++){
                for(int x=0; x<w;x++){
                    r = getComponentRGB(image.getRGB(x, y), "red");
                    g = getComponentRGB(image.getRGB(x, y), "green");
                    b = getComponentRGB(image.getRGB(x, y), "blue");
                    int grayLevel = (r + g + b) / 3;
                    int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                    image.setRGB(x, y, gray);
                }
            }
            ImageIO.write(image, type_img, new File(outputFile));    
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public int getComponentRGB(int color, String typeComponent){
        int nro_component = 0;
        if(typeComponent == "red"){nro_component = (color & 0xff0000) >> 16;}
        if(typeComponent == "green"){nro_component = (color & 0xff00) >> 8;}
        if(typeComponent == "blue"){nro_component = color & 0xff;}
        if(typeComponent == "alpha"){nro_component = (color & 0xff000000) >>> 24;}
        return nro_component;
    }
}
