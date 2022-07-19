package laberintos;

import processing.core.PApplet;

/**
 * Clase que crea un laberinto con Processing.
 * @author Sara
 * @author Baruch
 */
public class Laberinto extends PApplet {
    int alto = 2;            // Altura (en celdas) de la cuadricula.
    int ancho = 2;           // Anchura (en celdas) de la cuadricula.
    int celda = 40;           // Tamanio de cada celda cuadrada (en pixeles).
    ModeloLaberinto modelo;   // El objeto que representa el modelo del laberinto.

    @Override
    public void setup() {
        frameRate(60);
        background(50);
        modelo = new ModeloLaberinto(ancho, alto, celda);
    }
    @Override
    public void settings() {
        size(ancho * celda, (alto * celda));
    }
    
    /**
     * Pintar el mundo del modelo.
     */
    @Override
    public void draw() {
      for (int i = 0; i < alto; i++)
        for (int j = 0; j < ancho; j++){
                  fill(204, 204, 204);
                  stroke(25,25,25);
                  rect(j * modelo.tamanio, i * modelo.tamanio, modelo.tamanio, modelo.tamanio);
                  // En caso de que las paredes de las celdas ya no se encuentren activas, estás se
                  // pintarán del color del fondo.
                  if(!modelo.mundo[i][j].pared_1){
                      stroke(204, 204, 204);
                      line(j * modelo.tamanio, i * modelo.tamanio, ((j + 1) * modelo.tamanio), i * modelo.tamanio);                    
                  }
                  if(!modelo.mundo[i][j].pared_2){
                      stroke(204, 204, 204);
                      line((j * modelo.tamanio) + modelo.tamanio, i * modelo.tamanio, (j + 1) * modelo.tamanio, (((i + 1) * modelo.tamanio)));
                  }
                  if(!modelo.mundo[i][j].pared_3){
                      stroke(204, 204, 204);
                      line(j * modelo.tamanio, (i * modelo.tamanio) + modelo.tamanio, ((j + 1) * modelo.tamanio), ((i + 1) * modelo.tamanio));                    
                  }
                  if(!modelo.mundo[i][j].pared_4){
                      stroke(204, 204, 204);
                      line(j * modelo.tamanio, i * modelo.tamanio, j * modelo.tamanio, ((i + 1) * modelo.tamanio));               
                  }
          }
    }

    /**
     * Clase que representa cada celda de la cuadricula.
     */
    class Celda{
        int celdaX; 
        int celdaY;
        boolean pared_1;
        boolean pared_2;
        boolean pared_3;
        boolean pared_4;
        boolean estado;
        
        /** Constructor de una celda.
          *@param celdaX Coordenada en x
          *@param celdaY Coordenada en y
          *@param estado Estado de la celda. true si no ha sido visitada, false en otro caso.
        */
        Celda(int celdaX, int celdaY, boolean estado){
          this.celdaX = celdaX;
          this.celdaY = celdaY;
          this.estado = estado;
          this.pared_1 = true; // Booleano que representa la pared de arriba
          this.pared_2 = true; // Booleano que representa la pared de la derecha
          this.pared_3 = true; // Booleano que representa la pared de abajo
          this.pared_4 = true; // Booleano que representa la pared de la izquierda
        }
    }  

    /**
     * Clase que modela el laberinto, es decir, crea el mundo del laberinto.
     */
    class ModeloLaberinto{
        int ancho, alto;  // Tamaño de celdas a lo largo y ancho de la cuadrícula.
        int tamanio;  // Tamaño en pixeles de cada celda.
        Celda[][] mundo;  // Mundo de celdas
        int direccion;
        
      /** Constructor del modelo
        @param ancho Cantidad de celdas a lo ancho en la cuadricula.
        @param ancho Cantidad de celdas a lo largo en la cuadricula.
        @param tamanio Tamaño (en pixeles) de cada celda cuadrada que compone la cuadricula.
      */
      ModeloLaberinto(int ancho, int alto, int tamanio){
        this.ancho = ancho;
        this.alto = alto;
        this.tamanio = tamanio;
        mundo = new Celda[alto][ancho];
        for(int i = 0; i < alto; i++)
          for(int j = 0; j < ancho; j++)
            mundo[i][j] = new Celda(j,i, true);
      }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         PApplet.main(new String[] { "laberintos.Laberinto" });
    } 
}