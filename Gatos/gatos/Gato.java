/*
 * No redistribuir.
 */
package gatos;

import java.util.LinkedList;

/**
 * Clase para representar un estado del juego del gato. Cada estado sabe cómo
 * generar a sus sucesores.
 *
 * @author Vero
 */
public class Gato {

    public static final int MARCA1 = 1;             // Número usado en el tablero del gato para marcar al primer jugador.
    public static final int MARCA2 = 4;             // Se usan int en lugar de short porque coincide con el tamaÃ±o de la palabra, el código se ejecuta ligeramente más rápido.

    int[][] tablero = new int[3][3];     // Tablero del juego
    Gato padre;                          // Quién generó este estado.
    LinkedList<Gato> sucesores;          // Posibles jugadas desde este estado.
    boolean jugador1 = false;            // Jugador que tiró en este tablero.
    boolean hayGanador = false;          // Indica si la última tirada produjo un ganador.
    int tiradas = 0;                     // Número de casillas ocupadas.

    /**
     * Constructor del estado inicial.
     */
    Gato() {}

    /**
     * Constructor que copia el tablero de otro gato y el número de tiradas
     */
    Gato(Gato g) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = g.tablero[i][j];
            }
        }
        tiradas = g.tiradas;
    }

    /**
     * Indica si este estado tiene sucesores expandidos.
     */
    int getNumHijos() {
        if (sucesores != null) {
            return sucesores.size();
        } else {
            return 0;
        }
    }

    /* Función auxiliar.
     * Dada la última posición en la que se tiró y la marca del jugador
     * calcula si esta jugada produjo un ganador y actualiza el atributo correspondiente.
     * 
     * Esta función debe ser lo más eficiente posible para que la generación del árbol no sea demasiado lenta.
     */
    private void hayGanador(int ren, int col, int marca) {
        // Horizontal
        if (tablero[ren][(col + 1) % 3] == marca && tablero[ren][(col + 2) % 3] == marca) {
            hayGanador = true;
            return;
        }
        // Vertical
        if (tablero[(ren + 1) % 3][col] == marca && tablero[(ren + 2) % 3][col] == marca) {
            hayGanador = true;
            return;
        }
        // Diagonal
        if ((ren != 1 && col == 1) || (ren == 1 && col != 1)) {
			hayGanador = false;  // No debiera ser necesaria.
            return; // No pueden hacer diagonal
        }      // Centro y esquinas
        if (col == 1 && ren == 1) {
            // Diagonal \
            if (tablero[0][0] == marca && tablero[2][2] == marca) {
                hayGanador = true;
                return;
            }
            if (tablero[2][0] == marca && tablero[0][2] == marca) {
                hayGanador = true;
                return;
            }
        } else if (ren == col) {
            // Diagonal \
            if (tablero[(ren + 1) % 3][(col + 1) % 3] == marca && tablero[(ren + 2) % 3][(col + 2) % 3] == marca) {
                hayGanador = true;
                return;
            }
        } else {
            // Diagonal /
            if (tablero[(ren + 2) % 3][(col + 1) % 3] == marca && tablero[(ren + 1) % 3][(col + 2) % 3] == marca) {
                hayGanador = true;
                return;
            }
        }
    }

    /* Función auxiliar.
     * Coloca la marca del jugador en turno para este estado en las coordenadas indicadas.
     * Asume que la casilla está libre.
     * Coloca la marca correspondiente, verifica y asigna la variable si hay un ganador.
     */
    private void tiraEn(int ren, int col) {
        tiradas++;
        int marca = (jugador1) ? MARCA1 : MARCA2;
        tablero[ren][col] = marca;
        hayGanador(ren, col, marca);
    }

    /**
     * ------- *** ------- *** -------
     * Este es el método que se deja como práctica.
     * ------- *** ------- *** -------
     * Crea la lista sucesores y 
     * agrega a todos los estados que surjen de tiradas válidas. Se consideran
     * tiradas válidas a aquellas en una casilla libre. Además, se optimiza el
     * proceso no agregando estados con jugadas simétricas. Los estados nuevos
     * tendrán una tirada más y el jugador en turno será el jugador
     * contrario.
     */
    LinkedList<Gato> generaSucesores() { 
                       //Un validador en defecto true para agregar al primer hijo
        if (hayGanador || tiradas == 9) {
            return null; // Es un estado meta.
        }
        else{       
            //Generamos todas las posibles jugadas
            this.sucesores = new LinkedList<>();         //Se inicializa la lista de sucesores de este punto del gato
            for(int i = 0 ; i < 3 ; i++){
                for(int j = 0 ; j < 3; j++){
                    if(this.tablero[i][j] == 0) {
                        Gato gatoHijo = new Gato(this);  //Se crea un gato hijo
                        gatoHijo.padre = this;           //Se le asigna un padre 
                        gatoHijo.jugador1 = !jugador1;   //Se le asigna el jugador contrario
                        gatoHijo.tiraEn(i,j);
                        boolean validador = true; 
                        //Se rellena una posible jugada
                        for(Gato hijo : this.sucesores){ //Revisa si el gato que se quiere agregar es simetro a algún otro gato de la lista
                            if(hijo.equals(gatoHijo)){
                               validador = false;         //Se encontró una simetría, por lo tanto no es un hijo nuevo.             
                               break;
                              }
                        }
                        if(validador)
                           this.sucesores.add(gatoHijo);
                    }
                }                
            }      
        }
        return sucesores;
    }

    // ------- *** ------- *** -------
    // Serie de funciones que revisan la equivalencia de estados considerando las simetrías de un cuadrado.
    // ------- *** ------- *** -------
    // http://en.wikipedia.org/wiki/Examples_of_groups#The_symmetry_group_of_a_square_-_dihedral_group_of_order_8
    // ba es reflexion sobre / y ba3 reflexion sobre \.
    
    /**
     * Revisa si ambos gatos son exactamente el mismo.
     */
    boolean esIgual(Gato otro) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] != otro.tablero[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Al reflejar el gato sobre la diagonal \ son iguales (ie traspuesta)
     */
    boolean esSimetricoDiagonalInvertida(Gato otro) {
        for (int i = 0; i < 3; i++)                      //Checar si [A B C]   [A D G]
           for (int j = 0; j < 3; j++)                   //          [D E F] = [B E H]
               if (tablero[i][j] != otro.tablero[j][i])  //          [G H I]   [C F I]
                   return false;
       return true;
    }

    /**
     * Al reflejar el gato sobre la diagonal / son iguales (ie traspuesta)
     */
    boolean esSimetricoDiagonal(Gato otro) {
        for (int i = 0; i < 3; i++)                        //Checar si [A B C]   [I F C]
          for (int j = 0; j < 3; j++)                      //          [D E F] = [H E B]
              if (tablero[i][j] != otro.tablero[2-j][2-i]) //          [G H I]   [G D A]
                  return false;
      return true;      
    }

    /**
     * Al reflejar el otro gato sobre la vertical son iguales
     */
    boolean esSimetricoVerticalmente(Gato otro) {
        for (int i = 0; i < 3; i++)                           //Checar si [A B C]   [C B A]
            for (int j = 0; j < 3; j++)                       //          [D E F] = [F E D]
                if (tablero[i][j] != otro.tablero[2 - i][j])  //          [G H I]   [I H G]
                   return false;
       return true;
    }

    /**
     * Al reflejar el otro gato sobre la horizontal son iguales
     */
    boolean esSimetricoHorizontalmente(Gato otro) {
        for (int i = 0; i < 3; i++)                           //Checar si [A B C]   [G H I]
            for (int j = 0; j < 3; j++)                       //          [D E F] = [D E F]
                if (tablero[i][j] != otro.tablero[i][2 - j])  //          [G H I]   [A B C]
                   return false;
       return true;
    }

    /**
     * Rota el otro tablero 90Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico90(Gato otro) {
        Gato aux = this;
        aux = rotarGato(aux,1);
        return aux.esIgual(otro);
    }

    /**
     * Rota el otro tablero 180Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico180(Gato otro) {
        Gato aux = this;
        aux = rotarGato(aux,2);
        return aux.esIgual(otro);
    }

    /**
     * Rota el otro tablero 270Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico270(Gato otro) {
        Gato aux = this;
        aux = rotarGato(aux,3);
        return aux.esIgual(otro);       
    }
    
    /**
     * Rota el gato n veces.
     * @param gato el gato a rotar
     * @param n el número de veces que se quiere rotar
     * @return el gato rotado
     */
    Gato rotarGato(Gato gato, int n){
        Gato gatoRotado = new Gato(gato);        
        for(int i = 0; i < n; i++){        
            int p1 = gatoRotado.tablero[0][0];
            int p2 = gatoRotado.tablero[0][1];
            int p3 = gatoRotado.tablero[0][2];
            int p4 = gatoRotado.tablero[1][0];
            int p5 = gatoRotado.tablero[1][2];
            int p6 = gatoRotado.tablero[2][0];
            int p7 = gatoRotado.tablero[2][1];
            int p8 = gatoRotado.tablero[2][2];
            
            gatoRotado.tablero[0][0] = p6;
            gatoRotado.tablero[0][1] = p4;
            gatoRotado.tablero[0][2] = p1;
            gatoRotado.tablero[1][0] = p7;
            gatoRotado.tablero[1][2] = p2;
            gatoRotado.tablero[2][0] = p8;
            gatoRotado.tablero[2][1] = p5;
            gatoRotado.tablero[2][2] = p3;
        }
        return gatoRotado;
    }
    

    /**
     * Indica si dos estados del juego del gato son iguales, considerando
     * simetrías, de este modo el problema se vuelve manejable.
     */
    @Override
    public boolean equals(Object o) {
        Gato otro = (Gato) o;        
        if (esIgual(otro)) {
            return true;
        }

        if (esSimetricoDiagonalInvertida(otro)) {
            return true;
        }
        if (esSimetricoDiagonal(otro)) {
            return true;
        }
        if (esSimetricoVerticalmente(otro)) {
            return true;
        }
        if (esSimetricoHorizontalmente(otro)) {
            return true;
        }
        if (esSimetrico90(otro)) {
            return true;
        }
        if (esSimetrico180(otro)) {
            return true;
        }
        if (esSimetrico270(otro)) {
            return true; // No redujo el diámetro máximo al agregarlo
        }
        return false;
    }

    /**
     * Devuelve una representación con caracteres de este estado. Se puede usar
     * como auxiliar al probar segmentos del código.
     */
    @Override
    public String toString() {
        char simbolo = jugador1 ? 'o' : 'x';
        String gs = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gs += tablero[i][j] + " ";
            }
            gs += '\n';
        }
        return gs;
    }
}
