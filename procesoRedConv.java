package rnnn01;

import java.io.IOException;

public class procesoRedConv {

    public String inputFile;

    public procesoRedConv(String inputFile){
        this.inputFile = inputFile;
    }

    public double[] empezarProceso() throws IOException {
        
        String outputFile = "E:/imagenOut.jpg";
        
        redConvolucion rnc = new redConvolucion();
        boolean verbose = false;
        rnc.converImageToGray(this.inputFile, outputFile);

        if (verbose == true){ 
            System.out.println("Conversion a grises finalizada");
        }
        double[][] matrixImage = rnc.getMatrixImage(outputFile);

        // Definiendo 6 kernels
        int[][][] kernels_1 = {
                        {{-1,2,1,1,1}, {2,-4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn1
                        {{0,-1,0,0,1}, {2,4,2,-1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn2
                        {{1,0,-1,0,2}, {2,4,-2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn3
                        {{1,2,0,-1,3}, {2,-4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn4
                        {{1,-1,0,1,0}, {2,-4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn5
                        {{1,0,1,-3,1}, {2,-4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}}; //kn6

        double[][][] layer_1_convol = new double[6][][];
        
        for(int i = 0; i<layer_1_convol.length; i++){
            layer_1_convol[i] = rnc.convolution(matrixImage, kernels_1[i]);
        }

        double[][][] layer_2_maxpool = new double[6][][];
        for(int i = 0; i<layer_2_maxpool.length; i++){
            layer_2_maxpool[i] = rnc.maxPooling(layer_1_convol[i], 2, 2);
        }
        
        int[][][] kernels_2 = {
            {{1,2,1,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn1
            {{2,2,1,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn2
            {{3,2,1,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn3
            {{-1,2,1,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn4
            {{-1,2,0,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn5
            {{2,2,0,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn6
            {{0,2,1,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn7
            {{1,2,0,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn8
            {{1,2,1,0,-1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn9
            {{1,0,1,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn10
            {{-1,3,1,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn11
            {{1,2,1,-1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn12
            {{1,-2,1,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn13
            {{1,2,0,-1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn14
            {{1,2,-1,1,0}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}}, //kn15
            {{1,1,-1,1,1}, {2,4,2,1,1}, {1,2,1,1,1}, {1,2,1,1,1}, {1,2,1,1,1}} //kn16
            }; 

        int[][] merges = {{0,1,2},{1,2,3},{2,3,4},{3,4,5},{0,4,5},{0,1,5},{0,1,2,3},
                        {1,2,3,4},{2,3,4,5},{0,3,4,5},{0,1,4,5},{0,1,2,5},{0,1,3,4},{1,2,4,5},
                        {0,2,3,5},{0,1,2,3,4,5}};

        
        double[][][] layer_3_convol = new double[16][][];
        for(int i = 0; i<layer_3_convol.length; i++){
            layer_3_convol[i] = mergeConv(layer_2_maxpool, merges[i], kernels_2[i]);
        }

        double[][][] layer_4_maxpool = new double[16][][];
        for(int i = 0; i<layer_4_maxpool.length; i++){
            layer_4_maxpool[i] = rnc.maxPooling(layer_3_convol[i], 2, 2);
        }

        double[][] layer_5_sum_max_pool = layer_4_maxpool[0];
        for (int i = 1; i < layer_4_maxpool.length; i++) {
            layer_5_sum_max_pool = sumMatrix(layer_5_sum_max_pool, layer_4_maxpool[i]);
        }
        
        double[] layer_flattening = rnc.Flattening(layer_5_sum_max_pool);

        return layer_flattening;
    }

    public static double[][] mergeConv(double[][][] input, int[] index_merge, int[][] kernel) {
        double[][] outMerge = input[index_merge[0]];
        for (int i = 1; i < index_merge.length; i++) {
            outMerge = sumMatrix(outMerge, input[index_merge[i]]);
        }
        redConvolucion rnc = new redConvolucion();
        outMerge = rnc.convolution(outMerge, kernel);
        return outMerge;
    }

    private static double[][] sumMatrix(double[][] A, double[][] B) {
        double[][] C = new double[A.length][A[0].length];
        for(int j=0; j<A.length; j++)
            for(int i=0; i<A[j].length; i++)
                C[j][i] = A[j][i] + B[j][i];

        return C;
    }

    public static void main(String[] args) throws IOException {
        String inputFile = "E:/img_3.jpg";//"E:/testDBnumber/img_0.jpg";
        procesoRedConv prcRC = new procesoRedConv(inputFile);
        double[] flattening = prcRC.empezarProceso();
        
    }
}
