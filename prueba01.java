package rnnn01;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class prueba01 {
    public static void main(String[] args) throws IOException {
        
        String dirInputFiles = "E:/UNI/9-CICLO/Topicos Grafica - DeepLearning/db_number_mnist/trainingSet/trainingSet/"; 
        int imgs_per_num = 100;   //max 4000
        System.out.println("Leyendo las imagenes para entrenar");
        double [][][] input_output = getInputsOutputs(dirInputFiles, imgs_per_num);

        double[][] inputs = input_output[0];
        double[][] outputs = input_output[1];
        /*
        for(int i=0; i<inputs.length; i++){
            printArray(inputs[i]);
            printArray(outputs[i]);
        }
        System.out.println();
        */
        int[] capas_inter = {20};
        double[] w_ = {};
        int cant_veces = 1000;
        rna01 rn = new rna01(inputs[0].length, capas_inter, outputs[0].length, w_);
        System.out.println("Entrenando las neuronas");
        rn.entrenamiento(inputs, outputs, cant_veces);

        // Datos para Evaluar
        
        System.out.println("Leyendo las imagenes para test");
        String dirFilesTest = "E:/UNI/9-CICLO/Topicos Grafica - DeepLearning/db_number_mnist/testSet/testSet";
        int max_num_imgs_test = 20;  // max 28,000
        double [][] tests = getTests(dirFilesTest, max_num_imgs_test);
        
        System.out.println("Realizar test");
        double[][] predict = rn.prueba(tests);
        //printMatrix("Prediccion", predict);
        
        System.out.println("Interpretar resultados");
        double[] result = interpretPredict(predict);
        printArray(result);

        double[] trueResult = {2,0,9,0,3,7,0,3,0,3,5,2,4,0,4,3,3,1,9,0};
        printArray(trueResult);
        // ---------------------------------------------------------------------
        /*
         * double ingreso[][]={ {0,0}, {0,1}, {1,0}, {1,1}}; double
         * salida[][]={{0},{1},{1},{0}}; double evaluar[][]={ {1,1}, {1,0}, {0,1},
         * {0,0}}; int[] capas_inter = { 20 }; double[] w_ = {};//{2,-2,1,3,3,-2}; rna01
         * rn = new rna01(ingreso[0].length, capas_inter, salida[0].length, w_); int
         * cant_veces = 5000; rn.entrenamiento(ingreso, salida, cant_veces);
         * 
         * double[][] predict = rn.prueba(evaluar); printMatrix("Predict", predict);
         */
    }
    
    public static double[] interpretPredict(double[][] predicts) {
        double[] endResult = new double[predicts.length];
        double max_value;
        int max_index;
        for(int i=0; i<predicts.length; i++){
            max_value = 0.0; max_index = 0;
            for(int num=0; num<predicts[i].length; num++){
                if(predicts[i][num] > max_value){
                    max_value = predicts[i][num];
                    max_index = num;
                }
            }
            endResult[i] = max_index;
        }

        return endResult;

    }
    public static void printMatrix(String name, double[][] input){
        for(int i=0; i<input.length; i++){
            System.out.print(name+" ["+i+"] : ");
            printArray(input[i]);
        }
        System.out.println();
    }

    public static void printArray(double[] input){
        System.out.print("[ ");
        for(int i=0; i<input.length; i++)
            System.out.print("["+input[i]+"] ");
        System.out.println("] ");
    }

    public static double[][] getTests(String dirFiles, int max_num_imgs) throws IOException {

        final File folder = new File(dirFiles);  //"C:\\projects"
        List<String> result = new ArrayList<>();
        search(".*\\.jpg", folder, result, max_num_imgs);
        double[][] tests = new double[result.size()][];
        // proceso para cada archivo
        
        int k=0;
        for(int i=0; i<result.size(); i++){
                //System.out.println(result.get(i));
                procesoRedConv prcRC = new procesoRedConv(result.get(i));
                tests[k] = prcRC.empezarProceso();
                k++;
        }
        
        return tests;
    }

    public static double[][][] getInputsOutputs(String dirFiles, int max_num_imgs) throws IOException {

        final File folder = new File(dirFiles);  //"C:\\projects"
        List<String> result = new ArrayList<>();
        search(".*\\.jpg", folder, result, max_num_imgs);
        double [][][] input_output = new double [2][][];
        double[][] inputs = new double[result.size()][];
        double[][] outputs = new double[result.size()][10];

        // proceso para cada archivo
         
        int index = 0;
        int k=0;
        for(int i=0; i<max_num_imgs; i++)
            for(int num=0; num<10; num++){
                index = num*max_num_imgs+i;
                //System.out.println(result.get(index));
                procesoRedConv prcRC = new procesoRedConv(result.get(index));
                inputs[k] = prcRC.empezarProceso();
                outputs[k] = getOutputSimple(num);
                k++;
            }
        
        input_output[0] = inputs;
        input_output[1] = outputs;

        return input_output;
    }

    public static double[] getOutputSimple(int num){
        double[] outputsimple ={0,0,0,0,0,0,0,0,0,0};
        outputsimple[num] = 1.0;
        return outputsimple;
    }

    public static void search(final String pattern, final File folder, List<String> result, int max_num_imgs) {
        int num_imgs = 0;
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result, max_num_imgs);
            }
            
            if (num_imgs >= max_num_imgs) break;

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                    num_imgs++;
                }
            }

        }
    }
}
