public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner (System.in);
        int opcion;

        do{
            System.out.print("MENU");
            System.out.print("\n1.Introduce nuevo alumno");
            System.out.print("\n2.Mostrar informacion en funcion de una nota");
            System.out.print("\n3.Salir\n");
            opcion=sc.nextInt();


            switch(opcion){
                case 1:
                    introducir();
                    break;
                case 2:
                    mostrar();
                    break;
                case 3:
                    salir();
                    break;
                default:
                    System.out.print("Numero incorrecto vuelva a escribir uno");
            }
        }while(opcion != 3);
        sc.close();
    }
}
//soy hugo