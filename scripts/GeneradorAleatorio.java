import java.util.Random;

public class GeneradorAleatorio {
    public static void main(String[] args) {
        // Verificar que se pasen los argumentos requeridos
        if (args.length < 2) {
            System.out.println("Argumentos no validos");
            return;
        }

        try {
            // Convertir los argumentos en números enteros
            int numeroMinimo = Integer.parseInt(args[0]);
            int numeroMaximo = Integer.parseInt(args[1]);

            // Verificar que el rango es válido
            if (numeroMinimo > numeroMaximo) {
                System.out.println("El numeroMinimo no puede ser mayor que numeroMaximo.");
                return;
            }

            // Generar un número aleatorio dentro del rango
            Random random = new Random();
            int numeroAleatorio = random.nextInt((numeroMaximo - numeroMinimo) + 1) + numeroMinimo;

            // Imprimir el número aleatorio
            System.out.print(numeroAleatorio);
        } catch (NumberFormatException e) {
            System.out.println("Ambos argumentos deben ser números enteros.");
        }
    }
}
